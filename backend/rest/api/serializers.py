from rest_framework import serializers
from .models import  Product, Order, User
from rest_auth.serializers import UserDetailsSerializer
from rest_auth.registration.serializers import RegisterSerializer


# Serializers allow querysets and model instances 
# to be converted to native Python datatypes that can then be easily rendered into JSON, XML or other content types
# Also provide deserialization, allowing parsed data to be converted back into complex types

class CustomRegisterSerializer(RegisterSerializer):

    email = serializers.EmailField(required=True)
    password1 = serializers.CharField(write_only=True)
    room_number = serializers.CharField(required=True)
    first_name = serializers.CharField(required=True)
    last_name = serializers.CharField(required=True)
    profile_picture = serializers.ImageField(required=False)
    campus = serializers.CharField(required=True)

    def get_cleaned_data(self):
        super(CustomRegisterSerializer, self).get_cleaned_data()

        return {
            'password1': self.validated_data.get('password1', ''),
            'email': self.validated_data.get('email', ''),
            'room_number': self.validated_data.get('room_number', ''),
            'first_name': self.validated_data.get('first_name', ''),
            'last_name': self.validated_data.get('last_name', ''),
            'profile_picture': self.validated_data.get('profile_picture', ''),
            'campus': self.validated_data.get('campus', ''),
        }

class CustomUserDetailsSerializer(serializers.ModelSerializer):

    class Meta:
        model = User
        fields = ('email','room_number','first_name','last_name','profile_picture','campus')
        read_only_fields = ('email',)

# The HyperLinkedModelSerializer class provides a shortcut that lets you automatically create a Serializer class 
# with fields that correspond to the Model fields.
# It uses hyperlinks to represent relationships, rather than primary keys.

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