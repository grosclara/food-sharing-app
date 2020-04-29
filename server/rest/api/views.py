from django.shortcuts import render
from rest_framework import viewsets, filters
from rest_framework.decorators import action
from .models import Product, Order, User
from .serializers import ProductSerializer, OrderSerializer, CustomUserDetailsSerializer
from rest_framework.permissions import IsAuthenticated
from rest_auth.registration.views import RegisterView
from rest_auth.views import LogoutView, UserDetailsView
from rest_auth.serializers import UserDetailsSerializer
from django.contrib.auth import authenticate
from rest_framework.authtoken.models import Token
from rest_framework.decorators import api_view, permission_classes
from rest_framework.permissions import AllowAny
from rest_framework.status import HTTP_400_BAD_REQUEST, HTTP_404_NOT_FOUND, HTTP_200_OK, HTTP_201_CREATED, HTTP_204_NO_CONTENT, HTTP_400_BAD_REQUEST
from rest_framework.views import APIView
from rest_framework.response import Response
from django.conf import settings
from django.core.mail import send_mail
from django.http import QueryDict
from .constants import AVAILABLE, COLLECTED, DELIVERED
from django.shortcuts import get_object_or_404

class ProductViewSet(viewsets.ModelViewSet):
    """ 
    This viewset provides default create(), retrieve(), update(), partial_update(), destroy() and list() actions
    """
    model = Product
    lookup_field = 'id'

    filter_backends = [filters.OrderingFilter]
    # Explicitly specify which fields the API may be ordered against
    ordering_fields = ['created_at','updated_at']
    # This will be used as the default ordering
    ordering = ('-updated_at')

    serializer_class = ProductSerializer

    def create(self, request, *args, **kwargs):
        """ 
        the authenticated user can create a new product of which he will be the supplier
        """

        data = QueryDict(mutable=True)
        data.update(request.data)
        data["supplier"] = request.user.id
        data["campus"] = request.user.campus

        serializer = self.get_serializer(data=data)
        serializer.is_valid(raise_exception=True)
        self.perform_create(serializer)

        headers = self.get_success_headers(serializer.data)

        return Response(serializer.data, status=HTTP_201_CREATED, headers=headers)

    def get_queryset(self):
        """
        Display every product in the same campus as the auth user
        Optionally restricts the returned purchases to a given user,
        by filtering against a `supplier`, category' or a 'status' query parameter in the URL.
        """
        queryset = Product.objects.filter(campus=self.request.user.campus)
        status = self.request.query_params.get('status', None)
        category = self.request.query_params.get('category',None)
        supplier = self.request.query_params.get('supplier',None)

        if status is not None:
            queryset = queryset.filter(status=status)
        if category is not None:
            queryset = queryset.filter(category=category)
        if supplier is not None:
            queryset = queryset.filter(supplier=supplier)

        return queryset

    def destroy(self, request, *args, **kwargs):
        """
        The auth user can only try to delete the products he created. These must be available (cannot delete a product already ordered by someone else).
        If not, it will render a 400 BAd request error
        """
            
        instance = self.get_object()

        if (request.user == instance.supplier):

            if (instance.status == AVAILABLE) :

                serializer = self.get_serializer(instance)
                super().destroy(request, *args, **kwargs)
                return Response(serializer.data, status=HTTP_200_OK)
        
        return Response(status=HTTP_400_BAD_REQUEST)    

class OrderViewSet(viewsets.ModelViewSet):

    # Path variable
    lookup_field = 'product'

    serializer_class = OrderSerializer

    
    def get_queryset(self):
        """
        Returns all orders from the auth user ordered by their updated_date
        """
        queryset = Order.objects.filter(customer=self.request.user).order_by('-updated_at')
        return queryset
    
    def list(self, request):
        """
        Display a list of the products the auth user has ordered 
        """
        queryset = self.get_queryset()

        product_ids = []

        for order in queryset:
            product_ids.append(order.product.id)
        

        product_queryset = Product.objects.filter(id__in=product_ids).order_by('-updated_at')


        # Pagination
        paged_queryset = self.paginate_queryset(product_queryset)
        if paged_queryset is not None:
            serializer = ProductSerializer(paged_queryset, many=True)
            return self.get_paginated_response(serializer.data)

        serializer = ProductSerializer(products, many=True)
        return Response(serializer.data)

    def send_order_mails(self, client_mail, supplier_mail, product, client_name,supplier_name):
        subject_supplier = "Your product has been ordered!"
        subject_client = " Collect your "+ str(product) + "!"
        message_supplier = "Hey, \n"+ str(client_name) +" wants to collect your "+ str(product) +". \nPlease contact him at " + str(client_mail).lower() +" to set up a meeting for the collection. \nThank you for using CShare" 
        message_client = "Hey, \nYou have ordered "+ str(product)+ " from " +str(supplier_name) + ". \nPlease contact him at " + str(supplier_mail).lower() +" to set up a meeting for the collection. \nThank you for using CShare"
        from_email = settings.EMAIL_HOST_USER
        client_mail = [client_mail]
        supplier_mail = [supplier_mail]
        send_mail(subject_supplier, message_supplier, from_email, supplier_mail,fail_silently=False)
        send_mail(subject_client, message_client, from_email, client_mail, fail_silently=False )

    def create(self, request, *args, **kwargs):
        """
            Create an order with the specific product in query parameters.
            The product concerned must be available
            The auth user can't be the supplier
            The auth user (customer) must be in the same campus as the product
            Otherwise it will render HTTP 400 error code
            The auth user will automatically be the customer
        """

        data = QueryDict(mutable=True)
        data.update(request.data)
        data["customer"] = request.user.id

        product = Product.objects.get(pk = data["product"])
        product_serializer = ProductSerializer(product)

        if product.supplier != request.user :

            if product.status == AVAILABLE :

                if product.campus == request.user.campus :
            
                    serializer = self.get_serializer(data=data)
                    serializer.is_valid(raise_exception=True)
                    self.perform_create(serializer)

                    product.status = COLLECTED
                    product.save()

                    #Send emails
                    customer_id = request.user.id
                    customer_name = request.user.first_name
                    customer_mail = request.user.email
                    supplier_name = product.supplier.first_name
                    supplier_mail = product.supplier.email
                    
                    self.send_order_mails(customer_mail, supplier_mail, product, customer_name, supplier_name)

                    headers = self.get_success_headers(serializer.data)

                    return Response(product_serializer.data, status=HTTP_201_CREATED, headers=headers)
        
        return Response(status=HTTP_400_BAD_REQUEST)

    def destroy(self, request, *args, **kwargs):
        """
        The auth user can only try to delete the order that he created (he must be the customer of the order). 
        The product must be collected (cannot delete an order if the transaction has already been make (status = DELIVERED)).
        If not, it will render a 400 BAd request error
        """

        order = self.get_object()

        if request.user == order.customer :

            product = order.product

            if (product.status == COLLECTED) :

                product_serializer = ProductSerializer(product)
                # Returns the serialized product
                serializer = self.get_serializer(self.get_object())
                super().destroy(request, *args, **kwargs)
                # Update the product status in the db
                product.status = AVAILABLE
                product.save()

                # Send mail to inform the supplier the order has been canceled by the customer 
                #Send emails
                customer_id = request.user.id
                customer_name = request.user.first_name
                customer_mail = request.user.email
                supplier_name = product.supplier.first_name
                supplier_mail = product.supplier.email
                    
                self.send_cancel_order_mails(customer_mail, supplier_mail, product, customer_name, supplier_name)

                return Response(product_serializer.data, status=HTTP_200_OK)
        
        return Response(status=HTTP_400_BAD_REQUEST)
    
    def send_cancel_order_mails(self, client_mail, supplier_mail, product, client_name,supplier_name):

        subject_supplier = str(client_name) + " has canceled the order of " + str(product) + "!"
        subject_client = str(product) + " order canceled"
        message_supplier = "Hey, \n"+ str(client_name) +" doesn't want your "+ str(product) +" anymore. \nThe product is now available on the CShare platform in case someone elsewants to order it! \nThank you for using CShare" 
        message_client = "Hey, \nYou have canceled the order of "+ str(product)+ " provided by " +str(supplier_name) + ". \nThank you for using CShare"
        from_email = settings.EMAIL_HOST_USER
        client_mail = [client_mail]
        supplier_mail = [supplier_mail]
        send_mail(subject_supplier, message_supplier, from_email, supplier_mail,fail_silently=False)
        send_mail(subject_client, message_client, from_email, client_mail, fail_silently=False )
        
    def partial_update(self, request, *args, **kwargs):
        """
            Change a product status from Collected to Delivered 
            (only the customer can do so)
        """

        if request.user == self.get_object().customer :

            product = self.get_object().product

            if product.status == COLLECTED :

                #Send emails
                customer_id = request.user.id
                customer_name = request.user.first_name
                customer_mail = request.user.email
                supplier_name = product.supplier.first_name
                supplier_mail = product.supplier.email
         
                self.send_deliver_mails(customer_mail, supplier_mail, product.name, customer_name, supplier_name)


                product.status = DELIVERED
                product.save()

                # Returns the serialized product
                serializer = ProductSerializer(product)
                return Response(serializer.data, status=HTTP_200_OK)
        
        return Response(status=HTTP_400_BAD_REQUEST) 

    def send_deliver_mails(self, client_mail, supplier_mail, product, client_name,supplier_name):
        subject = "Congrats, you've done a good deed today !"
        message = "Hey,\nThe CShare team wants to thank you for using its platform to reduce food wastage.\n"+ "We hope the delivery of the product "+str(product) +" took place without any problems.\nThank you for using CShare" 
        from_email = settings.EMAIL_HOST_USER
        recipients = [client_mail, supplier_mail]
        send_mail(subject, message, from_email, recipients,fail_silently=False)

class UserViewSet(viewsets.ModelViewSet):

    queryset = User.objects.all()
    serializer_class = CustomUserDetailsSerializer

    def partial_update(self, request, *args, **kwargs):
        """
        The auth user can only edit its own profile
        Otherwise, it will render 400 Bad request error
        """

        if request.user == self.get_object() :
            response_with_updated_instance = super(UserViewSet, self).partial_update(request, *args, **kwargs)
            return response_with_updated_instance
        
        return Response(status=HTTP_400_BAD_REQUEST) 

    def destroy(self, request, *args, **kwargs):
        """
        The auth user can only delete its own profile
        Otherwise, it will render 400 Bad request error
        """
            
        if (request.user == self.get_object()):

            serializer = self.get_serializer(self.get_object())
            super().destroy(request, *args, **kwargs)
            return Response(serializer.data, status=HTTP_200_OK)

        
        return Response(status=HTTP_400_BAD_REQUEST)



