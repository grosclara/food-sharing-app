from django.shortcuts import render
from rest_framework import viewsets, filters
from .models import Product, Order
from .serializers import ProductSerializer, OrderSerializer # UserSerializer,
from rest_framework.permissions import IsAuthenticated


from django.contrib.auth import authenticate
from django.views.decorators.csrf import csrf_exempt
from rest_framework.authtoken.models import Token
from rest_framework.decorators import api_view, permission_classes
from rest_framework.permissions import AllowAny
from rest_framework.status import (
    HTTP_400_BAD_REQUEST,
    HTTP_404_NOT_FOUND,
    HTTP_200_OK
)
from rest_framework.response import Response




# class UserViewSet(viewsets.ModelViewSet):
#     """
#     This viewset automatically provides `list` and `detail` actions.
#     """
#     #permission_classes = (IsAuthenticated,)  
#     queryset = User.objects.all()
#     serializer_class = UserSerializer
    
class ProductViewSet(viewsets.ModelViewSet):
    """ 
    This viewset provides default create(), retrieve(), update(), partial_update(), destroy() and list() actions
    """
    #permission_classes = (IsAuthenticated,)  
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
    permission_classes = (IsAuthenticated,)  
    queryset = Order.objects.all()
    serializer_class = OrderSerializer



