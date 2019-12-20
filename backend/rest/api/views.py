from django.shortcuts import render
from rest_framework import viewsets, filters
from .models import User, Product, Order
from .serializers import UserSerializer, ProductSerializer, OrderSerializer

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

    queryset = Product.objects.all()
    filter_backends = [filters.OrderingFilter]
    # Explicitly specify which fields the API may be ordered against
    ordering_fields = ['created_at','updated_at']
    # This will be used as the default ordering
    ordering = ('-created_at')
    serializer_class = ProductSerializer

    # Defines the function to call for a PATCH request
    def patch(self, request, *args, **kwargs):
        return self.partial_update(request, *args, **kwargs)

class OrderViewSet(viewsets.ModelViewSet):
    queryset = Order.objects.all()
    serializer_class = OrderSerializer