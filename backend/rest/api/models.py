from django.db import models
from django.contrib.auth.models import AbstractUser

from users import models as userModels

# Django models to create our SQL tables

class Product(models.Model): # Product table
    name = models.CharField(max_length=200)
    is_available = models.BooleanField(default=True)
    supplier = models.ForeignKey(userModels.User , on_delete=models.CASCADE)   # equivalent to the sql constraint ON DELETE CASCADE
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    product_picture = models.ImageField(upload_to="media/product/", default='media/product/apple.jpg')
    objects = models.Manager()

class Order(models.Model): # Order table
    client = models.ForeignKey(userModels.User,on_delete=models.CASCADE)
    product = models.ForeignKey('Product', on_delete=models.CASCADE)   
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    objects = models.Manager()

