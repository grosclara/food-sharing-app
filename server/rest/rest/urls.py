"""rest URL Configuration

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
from django.conf.urls import url, include
from django.contrib import admin
from api import views
from django.conf.urls.static import static
from django.conf import settings
from django.urls import include, path, re_path
from rest_auth.views import PasswordResetConfirmView

from rest_framework import routers

router=routers.DefaultRouter()
# Router that includes a default API root view,
# that returns a response containing hyperlinks to all the list views.
# It also generates routes for optional .json style format suffixes.

router.register(r'api/v1/product',views.ProductViewSet,basename='product')
router.register(r'api/v1/order',views.OrderViewSet,basename='order')
router.register(r'api/v1/user',views.UserViewSet, basename='user')

urlpatterns = [
    url(r'^admin/', admin.site.urls),
    url(r'^',include(router.urls)),
    url(r'^api/v1/rest-auth/', include('rest_auth.urls')),
    url(r'^api/v1/rest-auth/registration/', include('rest_auth.registration.urls')),
    re_path(r'^rest-auth/password/reset/confirm/(?P<uidb64>[0-9A-Za-z_\-]+)/(?P<token>[0-9A-Za-z]{1,13}-[0-9A-Za-z]{1,20})/$', PasswordResetConfirmView.as_view(),
            name='password_reset_confirm')
] + static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)
