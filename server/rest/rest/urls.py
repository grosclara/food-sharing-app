""" Rest URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/3.0/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""

from django.conf import settings
from django.conf.urls.static import static
from django.contrib import admin
from django.urls import include, path, re_path
from rest_framework import routers
from rest_auth.views import PasswordResetConfirmView
from api import views

# Resource routing allows you to quickly declare all of the common routes for a given resourceful controller.
# This router includes routes for the standard set of list, create, retrieve, update, partial_update and destroy actions.
router = routers.SimpleRouter()

# Registering the viewsets with the router is similar to providing a urlpattern.
router.register(r'api/v1/product', views.ProductViewSet, basename = 'product')
router.register(r'api/v1/order', views.OrderViewSet, basename = 'order')
router.register(r'api/v1/user', views.UserViewSet, basename = 'user')

urlpatterns = [
    # Admin panel
    path('admin/', admin.site.urls),
    # Path to the documentation in the admin panel
    path('admin/doc/', include('django.contrib.admindocs.urls')),
    # The API URLs are now determined automatically by the router.
    path('',include(router.urls)),
    # Add rest_auth urls
    path('api/v1/rest-auth/', include('rest_auth.urls')),
    # Add rest_auth.registration urls
    path('api/v1/rest-auth/registration/', include('rest_auth.registration.urls')),
    # Redirect the user to the confirm page after having reset password
    re_path('rest-auth/password/reset/confirm/(?P<uidb64>[0-9A-Za-z_\-]+)/(?P<token>[0-9A-Za-z]{1,13}-[0-9A-Za-z]{1,20})/$', PasswordResetConfirmView.as_view(),
        name = 'password_reset_confirm')

] + static(settings.MEDIA_URL, document_root = settings.MEDIA_ROOT) # Helper function to return a URL pattern for serving files in debug mode
