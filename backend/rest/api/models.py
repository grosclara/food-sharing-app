from django.db import models

# Django models to create our SQL tables

class User(models.Model): # User table
    name = models.CharField(max_length=200)
    first_name = models.CharField(max_length=200) 
    objects = models.Manager() # A Manager is the interface through which database query operations are provided to Django models.
    profile_picture = models.ImageField(upload_to="media/user/", default='media/user/android.png')

class Product(models.Model): # Product table
    name = models.CharField(max_length=200)
    is_available = models.BooleanField()
    offerer = models.ForeignKey('User', on_delete=models.CASCADE) # equivalent to the sql constraint ON DELETE CASCADE
    objects = models.Manager()
    product_picture = models.ImageField(upload_to="media/product/", default='media/product/apple.jpg')


class Order(models.Model): # Order table
    client = models.ForeignKey('User',on_delete=models.CASCADE)
    product = models.ForeignKey('Product',on_delete=models.CASCADE)
    objects = models.Manager()
