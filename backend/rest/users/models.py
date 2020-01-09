from django.db import models
from django.contrib.auth.models import AbstractUser
from django.contrib.auth.models import UserManager

# Django models that will create our SQL user table
class User(AbstractUser):
    name = models.CharField(max_length=200)
    first_name = models.CharField(max_length=200) 
    objects = models.Manager() # A Manager is the interface through which database query operations are provided to Django models.
    profile_picture = models.ImageField(upload_to="media/user/", default='media/user/android.png')
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)

    objects = UserManager()

    def __str__(self):
        return self.username