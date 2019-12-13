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

from rest_framework import routers

router=routers.DefaultRouter()
# Router that includes a default API root view,
# that returns a response containing hyperlinks to all the list views.
# It also generates routes for optional .json style format suffixes.

router.register(r'api/v1/user',views.UserViewSet)
router.register(r'api/v1/product',views.ProductViewSet)
router.register(r'api/v1/order',views.OrderViewSet)

urlpatterns = [
    url(r'^admin/', admin.site.urls),
    url(r'^',include(router.urls))
] + static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)
