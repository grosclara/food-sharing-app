from django.shortcuts import render
from rest_framework import viewsets, filters
from django_filters.rest_framework import DjangoFilterBackend
from .models import User, Product, Order
from .serializers import UserSerializer, ProductSerializer, OrderSerializer
from django.db.models import Q

class UserViewSet(viewsets.ModelViewSet):
    """
    This viewset automatically provides `list` and `detail` actions.
    """
    queryset = User.objects.all()
    serializer_class = UserSerializer
    
class ProductViewSet(viewsets.ModelViewSet):
    """ 
    This viewset provides default create(), retrieve(), update(), partial_update(), destroy() and list() actions
    """

    model = Product
    lookup_field = 'id'

    filter_backends = [DjangoFilterBackend , filters.OrderingFilter]
    
    # Explicitly specify which fields the API may be ordered against
    ordering_fields = ['created_at']
    filterset_fields = ['is_available', 'supplier']

    # This will be used as the default ordering
    ordering = ('-created_at')

    serializer_class = ProductSerializer

    def get_queryset(self):
        """
        Enables to retrieve a set of products whose ids are in a set
        The url then looks like this: http://localhost/api/product/?id=1&id=7&id=28
        """
        queryset = Product.objects.all() # Basic query without filtering
        # Retrieve the list of the ids oh the products to GET (default = None)
        ids = self.request.query_params.getlist('id',None)
        if ids is not None:
            query = Q()
            for id in ids:
                q = Q(id=id)
                query |= q
            queryset = queryset.filter(query)
        return queryset

    def patch(self, request, *args, **kwargs):
        """
        Defines the function to call for a PATCH request (partial update)
        """
        return self.partial_update(request, *args, **kwargs)

class OrderViewSet(viewsets.ModelViewSet):
    """ 
    This viewset provides default create(), retrieve(), update(), partial_update(), destroy() and list() actions
    """

    model = Order
    lookup_field = 'id'
    
    queryset = Order.objects.all()

    filter_backends = [DjangoFilterBackend, filters.OrderingFilter]
    
    # Explicitly specify which fields the API may be ordered against
    ordering_fields = ['created_at']
    filterset_fields = ['client']

    # This will be used as the default ordering
    ordering = ('-created_at')

    serializer_class = OrderSerializer
