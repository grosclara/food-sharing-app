from rest_framework import serializers
from .models import  Product, Order, User
from rest_auth.registration.serializers import RegisterSerializer
from rest_framework.authtoken.models import Token


from allauth.account.adapter import get_adapter
from django.http import HttpRequest
from django.utils.translation import ugettext_lazy as _
from django.contrib.auth import get_user_model

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

from rest_framework import serializers
from requests.exceptions import HTTPError

"""
    Serializers allow complex data such as querysets and model instances to be converted to native Python datatypes 
    that can then be easily rendered into JSON, XML or other content types. Serializers also provide deserialization, 
    allowing parsed data to be converted back into complex types, after first validating the incoming data.

    The serializers in REST framework work very similarly to Django's Form and ModelForm classes. 
    DRF provides a Serializer class which gives a powerful, generic way to control the output of your responses, 
    as well as a ModelSerializer class which provides a useful shortcut for creating serializers that deal with model instances and querysets.
"""


class CustomRegisterSerializer(RegisterSerializer):

    """
        Validate and serialize rest-auth registration request.

        Inherit from serializer fields email, password1 and password2.

        Use of a customized RegisterSerializer to remove the username field that is present in the RegisterSerializer
        and to add other required fields when registrating (first_name, last_name, campus, room_number
        and the optional profile_picture field that comes with a default picture).
    """

    # Add other required serializer fields to registrate a new user
    first_name = serializers.CharField(required = True)
    last_name = serializers.CharField(required = True)
    campus = serializers.CharField(required = True)
    room_number = serializers.CharField(required = True)
    # Set the default value when a profile picture is not provided (optional field)
    profile_picture = serializers.ImageField(default = 'media/user/android.png')

    def get_cleaned_data(self):

        """
            Retrieve data after serializer validation
        """

        return {
            'email': super(CustomRegisterSerializer, self).get_cleaned_data().get('email'),
            'first_name': self.validated_data.get('first_name'),
            'last_name': self.validated_data.get('last_name'),
            'profile_picture': self.validated_data.get('profile_picture'), 
            'campus': self.validated_data.get('campus'),
            'room_number' : self.validated_data.get('room_number'),
        }
    
    def custom_signup(self, request, user):

        """
            Add the additional attributes defined in the custom serializer to the user 
            who will then finally be registered in the database.
        """

        user.first_name = self.cleaned_data.get('first_name')
        user.last_name = self.cleaned_data.get('last_name')
        user.profile_picture = self.cleaned_data.get('profile_picture')
        user.campus = self.cleaned_data.get('campus')
        user.room_number = self.cleaned_data.get('room_number')
    
    def save(self, request):

        """
            Save the user in the databse
            Call the custom_signup before save actions to provide other necessary information
        """

        adapter = get_adapter()
        user = adapter.new_user(request)
        self.cleaned_data = self.get_cleaned_data()
        self.custom_signup(request, user)
        adapter.save_user(request, user, self)
        setup_user_email(request, user, [])
        return user


class UserSerializer(serializers.ModelSerializer):

    """
        The UserSerializer class provides a shortcut that lets you automatically 
        create a Serializer class with fields that correspond to the User fields.
    """

    class Meta:

        """
            Specifies the model to be used by the serializer as well as the list of fields to be considered for serialization.
        """

        model = User
        fields = ('id','email','first_name','last_name','room_number','campus','profile_picture')


class CustomTokenSerializer(serializers.ModelSerializer):
    
    """
        This custom TokenSerializer class provides a shortcut that lets you automatically 
        create a Serializer class with fields that correspond to the Token fields. 
        
        It is used to render a response for successful authentication in rest_auth.views.LoginView and rest_auth.views.RegisterView
    """

    # Nested serializer in read-inly mode to display the user details
    user = UserSerializer(read_only=True)

    class Meta:

        """
            Specifies the model to be used by the serializer as well as the list of fields to be considered for serialization.
        """

        # For clients to authenticate, the token key should be included in the Authorization HTTP header. 
        # The key should be prefixed by the string literal "Token", with whitespace separating the two strings.
        # For example : Authorization: Token 9944b09199c62bcf9418ad846dd0e4bbdfc6ee4b
        model = Token
        fields = ('key', 'user')


class ProductSerializer(serializers.ModelSerializer):

    """
        The ProductSerializer class provides a shortcut that lets you automatically 
        create a Serializer class with fields that correspond to the Product fields.
    """
    
    class Meta:

        """
            Specifies the model to be used by the serializer as well as the list of fields to be considered for serialization.
        """

        model = Product
        fields = ('__all__')


class OrderSerializer(serializers.ModelSerializer):

    """
        The OrderSerializer class provides a shortcut that lets you automatically 
        create a Serializer class with fields that correspond to the Order fields.
    """

    class Meta:

        """
            Specifies the model to be used by the serializer as well as the list of fields to be considered for serialization.
        """

        model = Order
        fields = ('__all__')