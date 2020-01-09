from rest_framework import serializers
from rest_framework.serializers import (
	ModelSerializer,
	ValidationError,
	EmailField,
)

from users.models import User


# Serializers allow querysets and model instances 
# to be converted to native Python datatypes that can then be easily rendered into JSON, XML or other content types
# Also provide deserialization, allowing parsed data to be converted back into complex types

# The HyperLinkedModelSerializer class provides a shortcut that lets you automatically create a Serializer class 
# with fields that correspond to the Model fields.
# It uses hyperlinks to represent relationships, rather than primary keys.

# Here we have to add a data validator so wa will make sure the form is filled properly
# for example, an email address has the syntax of an email address

class RegisterSerializer(ModelSerializer):
    class Meta:
        model = User
        fields = ('id', 'username', 'password', 'email', 'first_name', 'name', 'profile_picture')
        write_only_fields = ('password',)
        read_only_fields = ('id',)


    def create(self, validated_data):
        user = User.objects.create(
            username=validated_data['username'],
            email=validated_data['email'],
            first_name=validated_data['first_name'],
            name=validated_data['name']
        )

        user.set_password(validated_data['password'])
        user.save()

        return user


# class UserSerializer(serializers.ModelSerializer):
#     """
#     The HyperLinkedModelSerializer class provides a shortcut that lets you automatically create 
#     a Serializer class with fields that correspond to the Model fields. 
#     It uses hyperlinks to represent relationships, rather than primary keys.
#     """
#     id = serializers.IntegerField(read_only=True) # should not be included in the input during create or update operations
#     class Meta:
#         model=User 
#         fields=('id', 'name', 'profile_picture')