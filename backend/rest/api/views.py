from django.shortcuts import render
from rest_framework import viewsets, filters
from .models import Product, Order, User
from .serializers import ProductSerializer, ProductDetailsSerializer, OrderSerializer, OrderDetailsSerializer, CustomUserDetailsSerializer
from rest_framework.permissions import IsAuthenticated
from rest_auth.registration.views import RegisterView
from rest_auth.views import LogoutView, UserDetailsView
from rest_auth.serializers import UserDetailsSerializer
from django.contrib.auth import authenticate
from rest_framework.authtoken.models import Token
from rest_framework.decorators import api_view, permission_classes
from rest_framework.permissions import AllowAny
from rest_framework.status import (
    HTTP_400_BAD_REQUEST,
    HTTP_404_NOT_FOUND,
    HTTP_200_OK
)
from rest_framework.views import APIView
from rest_framework.response import Response
from django.conf import settings

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

    # Defines the function to call for a PATCH request
    def patch(self, request, *args, **kwargs):
        return self.partial_update(request, *args, **kwargs)

    def get_queryset(self):
        """
        Optionally restricts the returned purchases to a given user,
        by filtering against a `supplier` or a 'status' query parameter in the URL.
        """
        queryset = Product.objects.all()
        supplier = self.request.query_params.get('supplier', None)
        status = self.request.query_params.get('status', None)
        campus = self.request.query_params.get('campus',None)
        category = self.request.query_params.get('category',None)
        id = self.request.query_params.get('id',None)

        if campus is not None :
            queryset = Product.objects.filter(supplier__campus=campus)
        if supplier is not None:
            queryset = queryset.filter(supplier=supplier)
        if status is not None:
            queryset = queryset.filter(status=status)
        if id is not None:
            queryset = queryset.filter(id=id)
        if category is not None:
            queryset = queryset.filter(category=category)

        return queryset

class OrderViewSet(viewsets.ModelViewSet):
    
    filter_backends = [filters.OrderingFilter]
    # Explicitly specify which fields the API may be ordered against
    ordering_fields = ['created_at','updated_at']
    # This will be used as the default ordering
    ordering = ('-updated_at')  

    def get_serializer_class(self):
        if self.request.method == 'GET':
            return OrderDetailsSerializer
        else:
            return OrderSerializer
    
    def get_queryset(self):
        queryset = Order.objects.all()
        client = self.request.query_params.get('client', None)
        if client is not None:
            queryset = queryset.filter(client=client)
        return queryset

class UserViewSet(viewsets.ModelViewSet):
    queryset = User.objects.all()
    serializer_class = CustomUserDetailsSerializer




