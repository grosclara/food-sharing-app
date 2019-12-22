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

    #queryset = Product.objects.all()

    filter_backends = [filters.OrderingFilter]
    # Explicitly specify which fields the API may be ordered against
    ordering_fields = ['created_at']
    search_field = ['is_available']
    # This will be used as the default ordering
    ordering = ('-created_at')
    serializer_class = ProductSerializer



    def get_queryset(self):
        """
        Optionally restricts the returned products to a given user,
        by filtering against a `is_available` query parameter in the URL.
        """
        queryset = Product.objects.all()
        is_available = self.request.query_params.get('is_available', None)
        if is_available is not None:
            queryset = queryset.filter(is_available=is_available)
        return queryset

    # Defines the function to call for a PATCH request
    def patch(self, request, *args, **kwargs):
        return self.partial_update(request, *args, **kwargs)

class OrderViewSet(viewsets.ModelViewSet):
    queryset = Order.objects.all()
    serializer_class = OrderSerializer