from rest_framework import serializers
from rest_framework.serializers import (
	ModelSerializer,
	ValidationError,
	EmailField,
)

from users.models import User

class RegisterSerializer(ModelSerializer):
	# email = EmailField(label='Email adress')
	# class Meta:
	# 	model = User
	# 	fields = [
	# 		'id',
    #         'name',
    #         'profile_picture',
    #         'first_name',
	# 		'username',
	# 		'password',
	# 		'email',
	# 	]
	# extra_kwargs = {"password":
	# 				{"write_only":True},
	# 				"id":
	# 				{"read_only":True}
	# 				}

	# def validate(self, data):
	# 	return data

	# def validate_email(self, value):
	# 	email = value
	# 	user_qs = User.objects.filter(email=email)
	# 	if user_qs.exists():
	# 		raise ValidationError("Email alredy registred")
	# 	return value


	# def create(self, validated_data):
    #     user_obj = User.objects.create(username=validated_data['username'], email=validated_data['email'], first_name=validated_data['first_name'], name=validated_data['name'])
    #     user_obj.set_password(validated_data['password'])
    #     user_obj.save()
    #     return user_obj
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