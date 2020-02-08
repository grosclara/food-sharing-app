from rest_framework import serializers
from .models import  Product, Order, User
from rest_auth.serializers import UserDetailsSerializer
from rest_auth.registration.serializers import RegisterSerializer
from rest_framework.authtoken.models import Token
from django.conf import settings
from rest_framework.authtoken.views import ObtainAuthToken

try:
    from allauth.account import app_settings as allauth_settings
    from allauth.utils import (email_address_exists,
                               get_username_max_length)
    from allauth.account.adapter import get_adapter
    from allauth.account.utils import setup_user_email
    from allauth.socialaccount.helpers import complete_social_login
    from allauth.socialaccount.models import SocialAccount
    from allauth.socialaccount.providers.base import AuthProcess
except ImportError:
    raise ImportError("allauth needs to be added to INSTALLED_APPS.")

# Serializers allow querysets and model instances 
# to be converted to native Python datatypes that can then be easily rendered into JSON, XML or other content types
# Also provide deserialization, allowing parsed data to be converted back into complex types

class CustomRegisterSerializer(RegisterSerializer):

    email = serializers.EmailField(required=True)
    password1 = serializers.CharField(write_only=True)
    first_name = serializers.CharField(required=True)
    last_name = serializers.CharField(required=True)
    profile_picture = serializers.ImageField(required=False)
    campus = serializers.CharField(required=True)
    room_number = serializers.CharField(required=True)

    def get_cleaned_data(self):
        super(CustomRegisterSerializer, self).get_cleaned_data()
        print(self.validated_data)
        return {
            'email': self.validated_data.get('email', ''),
            'password1': self.validated_data.get('password1', ''),
            'is_active' : True,
            'first_name': self.validated_data.get('first_name'),
            'last_name': self.validated_data.get('last_name'),
            'profile_picture': self.validated_data.get('profile_picture','media/user/android.png'), 
            # set the default value when a profile picture is not provided
            'campus': self.validated_data.get('campus'),
            'room_number' : self.validated_data.get('room_number'),
        }
    
    def save(self, request):
        adapter = get_adapter()
        user = adapter.new_user(request)
        self.cleaned_data = self.get_cleaned_data() 
        adapter.save_user(request, user, self)
        setup_user_email(request, user, [])

        user.email = self.cleaned_data.get('email')
        user.first_name = self.cleaned_data.get('first_name')
        user.last_name = self.cleaned_data.get('last_name')
        user.profile_picture = self.cleaned_data.get('profile_picture')
        user.campus = self.cleaned_data.get('campus')
        user.room_number = self.cleaned_data.get('room_number')

        user.save()
        return user 

class CustomUserDetailsSerializer(serializers.ModelSerializer):

    def put(self, request):
        user = adapter.new_user(request)
        self.cleaned_data = self.get_cleaned_data() 
        adapter.save_user(request, user, self)
        setup_user_email(request, user, [])

        user.email = self.cleaned_data.get('email')
        user.first_name = self.cleaned_data.get('first_name')
        user.last_name = self.cleaned_data.get('last_name')
        user.profile_picture = self.cleaned_data.get('profile_picture')
        user.campus = self.cleaned_data.get('campus')
        user.room_number = self.cleaned_data.get('room_number')
        user.is_active = True

        user.save()
        return user 

    class Meta:
        model = User
        fields = ('id','email','first_name','last_name','room_number','campus','profile_picture','is_active','last_login','date_joined')

class CustomTokenSerializer(serializers.ModelSerializer):
    """
    Serializer for Token model.
    """

    user = CustomUserDetailsSerializer(read_only=True)

    class Meta:
        model = Token
        fields = ('key','user')


# The HyperLinkedModelSerializer class provides a shortcut that lets you automatically create a Serializer class 
# with fields that correspond to the Model fields.
# It uses hyperlinks to represent relationships, rather than primary keys.

class ProductSerializer(serializers.ModelSerializer):
    id = serializers.IntegerField(read_only=True)
    class Meta:
        model=Product
        fields='__all__'

class OrderDetailsSerializer(serializers.ModelSerializer):

    product = ProductSerializer(read_only=True)

    class Meta:
        model=Order
        fields=('__all__')

class OrderSerializer(serializers.ModelSerializer):

    class Meta:
        model=Order
        fields=('__all__')