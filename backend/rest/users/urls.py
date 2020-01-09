from users import views
from django.conf.urls import url
from django.urls import path, re_path
from rest_framework.authtoken import views as authviews
from users.views import UserViewSet
from users.views import CustomObtainAuthToken

app_name='rest'

# these are the urls that will be included in the app users
urlpatterns = [
    # when we post a correct existing user to the following url, we get back a token 
    path('authenticate/', CustomObtainAuthToken.as_view()),
    ]