from django.contrib import admin
from api.models import User, Product, Order

# username : admin
# password : P@ssword1

# Register your models here.

admin.site.register(User)
admin.site.register(Product)
admin.site.register(Order)
