from django.shortcuts import render
from rest_framework.views import APIView
from rest_framework import viewsets
from django.http import HttpResponse
from django.http import JsonResponse

from users.serializers import UserSerializer, AuthCustomTokenSerializer

from rest_framework.generics import (CreateAPIView, RetrieveUpdateAPIView, UpdateAPIView, DestroyAPIView, ListAPIView, RetrieveAPIView)
from rest_framework.response import Response

from users.models import User





from rest_framework.authentication import TokenAuthentication
from rest_framework.authentication import SessionAuthentication


from rest_framework.permissions import IsAuthenticated
from rest_framework.permissions import (
	AllowAny,
	)

class UserViewSet(viewsets.ModelViewSet):
	permission_classes = (AllowAny,)
	#queryset = User.objects.all()
	

	serializer_class = UserSerializer
	queryset = User.objects.all()






from rest_framework.authtoken.views import ObtainAuthToken
from rest_framework.authtoken.models import Token
from rest_framework.response import Response


class CustomObtainAuthToken(ObtainAuthToken):
	def post(self, request, *args, **kwargs):
		serializer=AuthCustomTokenSerializer(data=request.data)
		serializer.is_valid(raise_exception=True)
		user=serializer.validated_data['user']
		token=Token.objects.get_or_create(user=user)
		print(token)
		return Response({'token': token[0], 'id': token[1]})
