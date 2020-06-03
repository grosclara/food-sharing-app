from django.shortcuts import render
from rest_framework import viewsets, filters
from rest_framework.decorators import action
from .models import Product, Order, User
from .serializers import ProductSerializer, OrderSerializer, UserSerializer, CustomRegisterSerializer
from rest_framework.permissions import IsAuthenticated
from rest_auth.registration.views import RegisterView
from rest_auth.views import LogoutView, UserDetailsView
from rest_auth.serializers import UserDetailsSerializer
from django.contrib.auth import authenticate
from rest_framework.authtoken.models import Token
from rest_framework.decorators import api_view, permission_classes
from rest_framework.permissions import AllowAny
from rest_framework import status
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import mixins
from django.conf import settings
from django.core.mail import send_mail
from django.http import QueryDict
from .constants import AVAILABLE, COLLECTED, DELIVERED


class ProductViewSet(mixins.CreateModelMixin,
                    mixins.RetrieveModelMixin,
                    mixins.ListModelMixin,
                    mixins.DestroyModelMixin,
                    viewsets.GenericViewSet):

    """
    A viewset that provides retrieve, create, list and destroy actions.
    To use it, override the class and set the .queryset and .serializer_class attributes.

    In this viewset, the serializer is the ``ProductSerializer`` and the queryset retrieves instances from the model :model:`api.Product`.\n
    The .queryset attribute returns all products that have the same location as the campus of the authenticated user.
    Optionally restricts the queryset by filtering against query parameters in the URL such as supplier, category or status.

    **Retrieve**    
        Return an existing model instance in a response where the ``pk`` is passed as a path variable in the url.\n
        If the product location is different from the authenticated user campus, the product will not be retrieved.\n
        If an object can be retrieved this returns a 200 OK response, with a serialized representation of the object as the body of the response.\n
        Otherwise it will return a 404 Not Found.

    **List**
        List a queryset.\n
        If the queryset is populated, this returns a 200 OK response, with a serialized representation of the queryset as the body of the response.\n
        The response data is paginated with the PageNumberPagination module which displays a page of 12 items.

    **Create**
        The authenticated user can create and save a new product model instance of which he will be the supplier.\n
        If an object is created this returns a 201 Created response, with a serialized representation of the object as the body of the response. 
        If the representation contains a key named url, then the Location header of the response will be populated with that value.\n
        If the request data provided for creating the object was invalid, a 400 Bad Request response will be returned, 
        with the error details as the body of the response.

    **Destroy**
        Deletion of an existing model instance.\n
        The authenticated user can only try to delete the products he created and they must be available 
        (cannot delete a product already ordered by someone else), otherwise it will return a 400 Bad Request. \n
        If an object is deleted this returns a 200 Ok response, with a serialized representation of the object as the body of the response, 
        otherwise it will return a 404 Not Found.
    """
    
    # Specify the returned serializer of the get_serializer() method
    serializer_class = ProductSerializer
    
    def get_queryset(self):

        """
        Queryset that retrieves instances from the Product model used for list and retrieve methods.

        Return queryset of all products that have the same location as the campus of the authenticated user ordered by their updated_date.
        Optionally restricts the queryset by filtering against query parameters in the URL such as supplier, category or status.
        """

        # Filter by product location regarding the campus of the authenticated user
        queryset = Product.objects.filter(campus=self.request.user.campus)

        # Get optional query parameters passed in the url
        status = self.request.query_params.get('status', None)
        category = self.request.query_params.get('category',None)
        shared = self.request.query_params.get('shared',None)    
        # If any query parameters is not None, filter against it
        if status is not None:
            queryset = queryset.filter(status=status)
        if category is not None:
            queryset = queryset.filter(category=category)
        if shared is not None :
            if int(shared) == 1:
                queryset = queryset.filter(supplier=self.request.user)
                
        # Order by expiration_date (in ascendent order)
        return queryset.order_by('-updated_at')

    def create(self, request, *args, **kwargs):
        
        # QueryDict that will be used to create the serializer
        data = QueryDict(mutable=True) # Querydict are by default immutable

        # Retrieve the request product data
        data.update(request.data)
        # Add the left necessary fields (supplier and campus) from the authenticated user information
        data["supplier"] = request.user.id
        data["campus"] = request.user.campus

        # Create the ProductSerializer from the .serializer_class attribute
        serializer = self.get_serializer(data=data)
        # If there is at least one invalid field, it will render a 400 Bad request
        serializer.is_valid(raise_exception=True)
        self.perform_create(serializer)

        # Add success headers
        headers = self.get_success_headers(serializer.data)

        # Render the response
        return Response(serializer.data, status=status.HTTP_201_CREATED, headers=headers)

    def destroy(self, request, *args, **kwargs):

        # Retrieve the product to delete, if no product is retrieved, raise a 404 Not Found error        
        instance = self.get_object()

        # Check whether the authenticated user is the product supplier
        if (request.user == instance.supplier):
            # Check whether the product is still Available
            if (instance.status == AVAILABLE) :
                # Create a ProductSerializer
                serializer = self.get_serializer(instance)
                # Delete the product in the database
                super().destroy(request, *args, **kwargs)
                # Render a 200 Ok request
                return Response(serializer.data, status=status.HTTP_200_OK)

        # If a condition below is not checked, render a 400 Bad request        
        return Response(status = status.HTTP_400_BAD_REQUEST)    


class OrderViewSet(mixins.CreateModelMixin,
                    mixins.UpdateModelMixin,
                    mixins.ListModelMixin,
                    mixins.DestroyModelMixin,
                    viewsets.GenericViewSet):

    """
    A viewset that provides create, list, update and destroy actions.
    To use it, override the class and set the .queryset and .serializer_class attributes.

    In this viewset, the serializer is the ``ProductSerializer`` and the queryset retrieves instances from the model :model:`api.Product`.
    There is a first order_queryset that corresponds to the list of all orders whose customer matches the authenticated user.
    Then the .queryset attribute returns the products of the orders in the initial order_queryset.

    **List**
        List a queryset.\n
        If the queryset is populated, this returns a 200 OK response, with a serialized representation of the queryset as the body of the response.\n
        The response data is paginated with the PageNumberPagination module which displays a page of 12 items.

    **Create**
        Create and save a new Order model instance with the product id passed in the body.\n
        There are a few conditions for the request to be successful, otherwise, it will render 400 Bad request :\n
        The product must be available and the authenticated user can not be the supplier and must be in the same campus as the product\n
        The authenticated user will automatically be the customer of the created order.\n
        If an Order object is created this returns a 201 Created response, with a serialized representation of the Product object as the body of the response. 
        If the representation contains a key named url, then the Location header of the response will be populated with that value.\n
        If the request data provided for creating the object was invalid, a 400 Bad Request response will be returned, 
        with the error details as the body of the response.

    **Update**
        Update and save a the status of the product from Collected to Delivered. \n
        To do so, the authenticated user must be the customer of the corresponding order and the product must be in the Collected mode,
        otherwise, it will render a 400 Bad request.\n
        If the product status of the Order object is updated this returns a 200 Ok response, 
        with a serialized representation of the Product object as the body of the response. 
        If the request data provided for creating the object was invalid, a 400 Bad Request response will be returned, 
        with the error details as the body of the response.

    **Destroy**
        Deletion of an existing Order model instance.\n
        The authenticated user can only try to delete (cancel) orders he created (he must be the cusomer). 
        Moreover, the product status of the order to be delete must be Collected 
        (it is not possible to delete an order if the transaction has already been make), otherwise it will return a 400 Bad Request. \n
        If an object is deleted this returns a 200 Ok response, with a serialized representation of the object as the body of the response, 
        otherwise it will return a 404 Not Found.
    """

    # Specify the returned serializer of the get_serializer() method
    serializer_class = ProductSerializer

    # Path variable used for detail view (id of the ordered product)
    lookup_field = "id"
    
    def get_queryset(self):

        """
        Queryset that retrieves instances from the Product model used for list and update methods.

        There is a first order_queryset that corresponds to the list of all orders whose customer matches the authenticated user.
        Then it returns a queryset of the products of the orders in the order_queryset.
        """

        # Retrieve every orders whose customer matches the authenticated user
        # Order by the updated_at field (in descendent order)
        order_queryset = Order.objects.filter(customer=self.request.user).order_by('-updated_at')
        
        product_ids = []
        # Retrieve the ids of the product of the orders in the order_queryset
        for order in order_queryset:
            product_ids.append(order.product.id)
        
        # Queryset that contains products whose id is in the product_ids array     
        return Product.objects.filter(id__in=product_ids)

    def create(self, request, *args, **kwargs):

        # QueryDict that will be used to create the serializer
        data = QueryDict(mutable=True) # Querydict are by default immutable

        # Retrieve the request product data
        data.update(request.data)
        # Add the left necessary fields (customer) from the authenticated user information
        data["customer"] = request.user.id

        # Create a product instance whose id matches the request body
        product = Product.objects.get(pk = data["product"])

        # Check whether the supplier of the product is not the authenticated user (cannot order a product he shared)
        if product.supplier != request.user :
            # Check whether the product is available
            if product.status == AVAILABLE :
                # Check whether the product location matches the authenticated user campus
                if product.campus == request.user.campus :

                    # Create an Order Serializer            
                    order_serializer = OrderSerializer(data=data)
                    # Raise exception is the data is not valid
                    order_serializer.is_valid(raise_exception=True)
                    # Save the order in the database
                    self.perform_create(order_serializer)

                    # Update and save the product status
                    product.status = COLLECTED
                    product.save()

                    # Create a ProductSerializer
                    product_serializer = self.get_serializer(product)

                    # Send emails to both the customer and the supplier
                    self.send_order_mails(customer_email = request.user.email, 
                        supplier_email = product.supplier.email, 
                        customer_name = request.user.first_name, 
                        supplier_name = product.supplier.first_name,
                        ordered_product = product.name
                    )

                    # Get success headers
                    headers = self.get_success_headers(order_serializer.data)

                    # Render a 201 Created response
                    return Response(product_serializer.data, status=status.HTTP_201_CREATED, headers=headers)

        # If a condition below is not checked, render a 400 Bad request                
        return Response(status=status.HTTP_400_BAD_REQUEST)
    
    def partial_update(self, request, *args, **kwargs):

        # Retrieve the product to update, if no product is retrieve, raise a 404 Not found error
        product = self.get_object()
        # Retrieve the corresponding order
        order = Order.objects.get(product = product)

        # Check whether the authenticated user is the customer of the order
        if request.user == order.customer :
            # Check whether the product is Collected
            if product.status == COLLECTED :

                # Update the product status and save it in the db
                product.status = DELIVERED
                product.save()

                # Send emails to both the customer and the supplier
                self.send_deliver_mails(customer_email = request.user.email, 
                    supplier_email = product.supplier.email, 
                    customer_name = request.user.first_name,
                    supplier_name = product.supplier.first_name,
                    ordered_product = product.name
                )                

                # Create and return the serialized product with a 200 Ok response code
                serializer = ProductSerializer(product)
                return Response(serializer.data, status=status.HTTP_200_OK)

        # If a condition below is not checked, render a 400 Bad request
        return Response(status=status.HTTP_400_BAD_REQUEST) 

    def destroy(self, request, *args, **kwargs):
        
        # Retrieve the product to delete
        product = self.get_object()
        order = Order.objects.get(product = product.id)
        
        # Check whether the authenticated user is the one who has ordered the product
        if request.user == order.customer :
            # Check whether the product is Collected
            if (product.status == COLLECTED) :

                # Create a product serializer
                product_serializer = ProductSerializer(product)

                # Delete the order in the database
                super().destroy(request, *args, **kwargs)
                # Update the product status in the db
                product.status = AVAILABLE
                product.save()

                # Send mail to inform the supplier the order has been canceled by the customer
                self.send_deliver_mails(customer_email = request.user.email, 
                    supplier_email = product.supplier.email, 
                    customer_name = request.user.first_name,
                    supplier_name = product.supplier.first_name,
                    ordered_product = product.name
                )

                # Render a 200 Ok request
                return Response(product_serializer.data, status=status.HTTP_200_OK)

        # If a condition below is not checked, render a 400 Bad request
        return Response(status=status.HTTP_400_BAD_REQUEST)
    
    def send_cancel_order_mails(self, customer_email, supplier_email, customer_name, supplier_name, ordered_product):

        """
            Send an email to the customer to confirm his order has been canceled
            Sen and email to the supplier to warn him that the order with the customer on the product passed in parameter is cancelled

            :param customer_email: Email address of the customer
            :param supplier_email: Email address of the supplier
            :param customer_name: First name of the customer
            :param supplier_name: First name of the customer
            :param ordered_product : Name of the product of the order to cancel
            :type customer_email : str
            :type supplier_email : str
            :type customer_name : str
            :type supplier_name : str
            :type ordered_product : str
        """

        subject_supplier = str(customer_name) + " has canceled the order of " + str(ordered_product) + "!"
        subject_customer = str(ordered_product) + " order canceled"

        message_supplier = "Hey, \n"+ str(client_name) +" doesn't want your "+ str(ordered_product) +" anymore.\
            \nThe product is now available on the CShare platform in case someone elsewants to order it! \nThank you for using CShare" 
        message_customer = "Hey, \nYou have canceled the order of "+ str(ordered_product)+ " provided by " \
            +str(supplier_name) + ". \nThank you for using CShare"

        from_email = settings.EMAIL_HOST_USER
        customer_email = [customer_email]
        supplier_email = [supplier_email]

        send_mail(subject_supplier, message_supplier, from_email, supplier_email,fail_silently=False)
        send_mail(subject_customer, message_customer, from_email, client_email, fail_silently=False )

    def send_order_mails(self, customer_email, supplier_email, customer_name, supplier_name, ordered_product):

        """
            Send an email to the customer to confirm his order has been created
            Sen and email to the supplier to inform him that someone wants to collect the productd passed in parameter

            :param customer_email: Email address of the customer
            :param supplier_email: Email address of the supplier
            :param customer_name: First name of the customer
            :param supplier_name: First name of the customer
            :param ordered_product : Name of the product of the order to cancel
            :type customer_email : str
            :type supplier_email : str
            :type customer_name : str
            :type supplier_name : str
            :type ordered_product : str
        """

        subject_supplier = "Your product has been ordered!"
        subject_customer = " Collect your "+ str(ordered_product) + "!"

        message_supplier = "Hey " + str(supplier_name) +", \n"+ str(customer_name) +" wants to collect your "+ str(ordered_product) +".\
             \nPlease contact him at " + str(customer_email).lower() +" to set up a meeting for the collection. \nThank you for using CShare" 
        message_customer = "Hey " + str(customer_name) +", \nYou have ordered "+ str(ordered_product)+ " from " +str(supplier_name) + ". \
            \nPlease contact him at " + str(supplier_email).lower() +" to set up a meeting for the collection. \nThank you for using CShare"

        from_email = settings.EMAIL_HOST_USER
        customer_email = [customer_email]
        supplier_email = [supplier_email]

        send_mail(subject_supplier, message_supplier, from_email, supplier_email,fail_silently=False)
        send_mail(subject_customer, message_customer, from_email, customer_email, fail_silently=False )

    def send_deliver_mails(self, customer_email, supplier_email, customer_name, supplier_name, ordered_product):

        """
            Send an email to both the customer  and the supplier to confirm transaction has been made successfully

            :param customer_email: Email address of the customer
            :param supplier_email: Email address of the supplier
            :param customer_name: First name of the customer
            :param supplier_name: First name of the customer
            :param ordered_product : Name of the product of the order to cancel
            :type customer_email : str
            :type supplier_email : str
            :type customer_name : str
            :type supplier_name : str
            :type ordered_product : str
        """

        subject = "Congrats, you've done a good deed today !"

        message = "Hey,\nThe CShare team wants to thank you for using its platform to reduce food wastage.\n"+ \
            "We hope the delivery of the product "+ str(ordered_product) + " took place without any problems.\nThank you for using CShare" 

        from_email = settings.EMAIL_HOST_USER
        recipients = [customer_email, supplier_email]

        send_mail(subject, message, from_email, recipients, fail_silently=False)

class UserViewSet(mixins.UpdateModelMixin,
                    mixins.RetrieveModelMixin,
                    mixins.DestroyModelMixin,
                    viewsets.GenericViewSet):
    """
    A viewset that provides retrieve, update and destroy actions.
    To use it, override the class and set the .queryset and .serializer_class attributes.

    In this viewset, the serializer is the ``UserSerializer`` and the queryset retrieves instances from the model :model:`api.User`.

    **Retrieve**    
        Return an existing model instance in a response where the ``pk`` is passed as a path variable in the url.\n
        If an object can be retrieved this returns a 200 OK response, with a serialized representation of the object as the body of the response.\n
        Otherwise it will return a 404 Not Found.

    **Update**
        Update and save the modified infromation of a user in the database.\n
        The user to be modified whose id is passed in the url path must match the authenticated user
        (eg. the authenitcated user can only edit its own profile), otherwise, it will render a 400 Bad request response.\n
        If the user is successfully updated this returns a 200 Ok response, 
        with a serialized representation of the User object that has been edited. 
        If the request data provided for creating the object was invalid, a 400 Bad Request response will be returned, 
        with the error details as the body of the response.

    **Destroy**
        Deletion of an existing User model instance.\n
        The authenticated user can only try to delete his own profile, otherwise it will return a 400 Bad Request. \n
        If a User object is deleted this returns a 200 Ok response, with a serialized representation of the User object, 
        otherwise it will return a 404 Not Found.
    """

    # Specify the returned serializer of the get_serializer() method
    serializer_class = UserSerializer
    
    # Define the queryset attribute
    queryset = User.objects.all()

    def partial_update(self, request, *args, **kwargs):

        # Retrieve the user to be edited
        user = self.get_object()

        # Check whether it matches the authenticated user
        if request.user == user :

            # Call the partial update method defined in the UpdateModelMixin class to save the changes in the database
            response_with_updated_instance = super(UserViewSet, self).partial_update(request, *args, **kwargs)
            return response_with_updated_instance
        
        # If a condition below is not checked, render a 400 Bad request
        return Response(status=status.HTTP_400_BAD_REQUEST) 

    def destroy(self, request, *args, **kwargs):

        # Retrieve the user to be edited
        user = self.get_object()

        # Check whether it matches the authenticated user            
        if (request.user == user):

            # Create a user Serializer instance
            serializer = self.get_serializer(user)
            # Call the destroy method defined in the DestroyModelMixin class to delete the user in the database
            super().destroy(request, *args, **kwargs)
            # Render a 200 OK response and the serialized deleted user
            return Response(serializer.data, status=status.HTTP_200_OK)

        # If a condition below is not checked, render a 400 Bad request
        return Response(status=status.HTTP_400_BAD_REQUEST)



