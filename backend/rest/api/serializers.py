from rest_framework import serializers
from .models import User, Product, Order

# Serializers allow querysets and model instances 
# to be converted to native Python datatypes that can then be easily rendered into JSON, XML or other content types
# Also provide deserialization, allowing parsed data to be converted back into complex types

# The HyperLinkedModelSerializer class provides a shortcut that lets you automatically create a Serializer class 
# with fields that correspond to the Model fields.
# It uses hyperlinks to represent relationships, rather than primary keys.

class UserSerializer(serializers.ModelSerializer):
    """
    The HyperLinkedModelSerializer class provides a shortcut that lets you automatically create 
    a Serializer class with fields that correspond to the Model fields. 
    It uses hyperlinks to represent relationships, rather than primary keys.
    """
    id = serializers.IntegerField(read_only=True) # should not be included in the input during create or update operations
    class Meta:
        model=User 
        fields='__all__'

class ProductSerializer(serializers.ModelSerializer):
    id = serializers.IntegerField(read_only=True)
    class Meta:
        model=Product
        fields='__all__'

class OrderSerializer(serializers.ModelSerializer):
    id = serializers.IntegerField(read_only=True)
    class Meta:
        model=Order
        fields='__all__'