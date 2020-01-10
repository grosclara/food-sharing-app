from rest_framework import serializers
from rest_framework.serializers import (
	ModelSerializer,
	ValidationError,
	EmailField,
)
from django.contrib.auth import authenticate
from django.core import exceptions


from users.models import User


# Serializers allow querysets and model instances 
# to be converted to native Python datatypes that can then be easily rendered into JSON, XML or other content types
# Also provide deserialization, allowing parsed data to be converted back into complex types

# The HyperLinkedModelSerializer class provides a shortcut that lets you automatically create a Serializer class 
# with fields that correspond to the Model fields.
# It uses hyperlinks to represent relationships, rather than primary keys.

# Here we have to add a data validator so wa will make sure the form is filled properly
# for example, an email address has the syntax of an email address

class UserSerializer(ModelSerializer):
    class Meta:
        model = User
        fields = ('id', 'password', 'email', 'first_name', 'name', 'profile_picture')
        write_only_fields = ('password',)
        read_only_fields = ('id',)


    def create(self, validated_data):
        user = User.objects.create(
            email=validated_data['email'],
            first_name=validated_data['first_name'],
            name=validated_data['name']
        )

        user.set_password(validated_data['password'])
        user.save()

        return user

class AuthCustomTokenSerializer(serializers.Serializer):
    email = serializers.CharField()
    password = serializers.CharField()

    def validate(self, attrs):
        email = attrs.get('email')
        password = attrs.get('password')

        if email and password:
            user = authenticate(username=email, password=password)

            if user:
                if not user.is_active:
                    msg = ('User account is disabled.')
                    raise exceptions.ValidationError(msg)
            else:
                msg = ('Unable to log in with provided credentials.')
                raise exceptions.ValidationError(msg)
        else:
            msg = ('Must include "email or username" and "password"')
            raise exceptions.ValidationError(msg)

        attrs['user'] = user
        return attrs