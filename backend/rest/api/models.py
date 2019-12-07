from django.db import models

# Create your models here.

class User(models.Model):
    name = models.CharField(max_length=200)
    first_name = models.CharField(max_length=200)    
    objects = models.Manager()

class Product(models.Model):
    name = models.CharField(max_length=200)
    is_available = models.BooleanField()
    offerer = models.ForeignKey('User',on_delete=models.CASCADE)
    objects = models.Manager()

class Order(models.Model):
    client = models.ForeignKey('User',on_delete=models.CASCADE)
    product = models.ForeignKey('Product',on_delete=models.CASCADE)
    objects = models.Manager()
