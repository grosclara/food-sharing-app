from rest_framework import serializers
from .models import User, Product, Order

class UserSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model=User 
        fields=('name','first_name')

class ProductSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model=Product
        fields=('name','offerer','is_available')

class OrderSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model=Order
        fields=('product','client')