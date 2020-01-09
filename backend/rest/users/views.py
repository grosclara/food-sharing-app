from django.shortcuts import render
from rest_framework.views import APIView
from rest_framework import viewsets
from django.http import HttpResponse
from django.http import JsonResponse

from users.serializers import RegisterSerializer
#from users.serializers import UserSerializer

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
	
	slug_field = "username"  # permet d'acceder au user via son username n'est pas utilisé à ce stade

	serializer_class = RegisterSerializer
	queryset = User.objects.all()






from rest_framework.authtoken.views import ObtainAuthToken
from rest_framework.authtoken.models import Token
from rest_framework.response import Response


class CustomObtainAuthToken(ObtainAuthToken):
    def post(self, request, *args, **kwargs):
        response = super(CustomObtainAuthToken, self).post(request, *args, **kwargs)
        token = Token.objects.get(key=response.data['token'])
        return Response({'token': token.key, 'id': token.user_id})
