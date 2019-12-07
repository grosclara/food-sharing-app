from django.db import models

# Create your models here.

class User(models.Model):
    name = models.CharField(max_length = 50)
    first_name = models.CharField(max_length = 50)
    
class Product(models.Model):
    name = models.CharField(max_length = 200)
    is_available = models.BooleanField()
    offerer = models.ForeignKey('User',on_delete=models.CASCADE)

class Order(models.Model):
    client = models.ForeignKey('User',on_delete=models.CASCADE)
    Product = models.ForeignKey('Product',on_delete=models.CASCADE)
