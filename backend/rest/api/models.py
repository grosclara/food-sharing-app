from django.db import models
from django.contrib.auth.models import AbstractBaseUser
from .managers import CustomUserManager
from django.utils import timezone
from django.conf import settings

# Constantes
GIF = 'G'
RENNES = 'R'
METZ = 'M'

CAMPUS_CHOICES = [
        (GIF, 'Gif'),
        (RENNES, 'Rennes'),
        (METZ,'Metz')
    ]

# Django models to create our SQL tables

class User(AbstractBaseUser):

    username = None
    email = models.EmailField(('email'), unique=True)
    date_joined = models.DateTimeField(default=timezone.now)
    is_active = models.BooleanField(default=True)
    is_staff = models.BooleanField(default=False)
    
    # custom fields for user
    first_name = models.CharField(max_length = 50)
    last_name = models.CharField(max_length = 50)
    profile_picture = models.ImageField(upload_to="media/user/", default='media/user/android.png')
    room_number = models.CharField(max_length = 50)
    campus = models.CharField(
        max_length=1,
        choices=CAMPUS_CHOICES)
    
    USERNAME_FIELD = 'email'
    REQUIRED_FIELDS = ['first_name','last_name','room_number','campus']

    def __str__(self):              # __unicode__ on Python 2
        return self.email

    objects = CustomUserManager()    

class Product(models.Model): # Product table
    name = models.CharField(max_length=200)
    is_available = models.BooleanField(default=True)
    supplier = models.ForeignKey(settings.AUTH_USER_MODEL , on_delete=models.CASCADE)   # equivalent to the sql constraint ON DELETE CASCADE
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    product_picture = models.ImageField(upload_to="media/product/", default='media/product/apple.jpg')
    objects = models.Manager()

class Order(models.Model): # Order table
    client = models.ForeignKey(settings.AUTH_USER_MODEL,on_delete=models.CASCADE)
    product = models.ForeignKey('Product', on_delete=models.CASCADE)   
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    objects = models.Manager()

