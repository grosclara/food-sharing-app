from django.contrib import admin
from api.models import Product, Order, User
from django.conf import settings
# Provide a admin interface

# username : admin
# password : P@ssword1

# Indicates the tables on which the administrator can operate
admin.site.register(User)
admin.site.register(Product)
admin.site.register(Order)

