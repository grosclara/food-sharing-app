from users import views
from django.conf.urls import url
from django.urls import path, re_path
from rest_framework.authtoken import views as authviews
from users.views import UserViewSet, CustomObtainAuthToken

app_name='rest'

urlpatterns = [
	path('api-token-auth/', CustomObtainAuthToken, name='api-token-auth'),
    ]