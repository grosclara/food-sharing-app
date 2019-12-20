from django.db import models

# Django models to create our SQL tables

class User(models.Model): # User table
    name = models.CharField(max_length=200)
    first_name = models.CharField(max_length=200) 
    objects = models.Manager() # A Manager is the interface through which database query operations are provided to Django models.
    profile_picture = models.ImageField(upload_to="media/user/", default='media/user/android.png')
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)

class Product(models.Model): # Product table
    name = models.CharField(max_length=200)
    is_available = models.BooleanField(default=True)
    supplier = models.ForeignKey('User', on_delete=models.CASCADE) # equivalent to the sql constraint ON DELETE CASCADE
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    product_picture = models.ImageField(upload_to="media/product/", default='media/product/apple.jpg')
    objects = models.Manager()

class Order(models.Model): # Order table
    client = models.ForeignKey('User',on_delete=models.CASCADE)
    product = models.ForeignKey('Product',on_delete=models.CASCADE)
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    objects = models.Manager()
