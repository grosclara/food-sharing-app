from django.db import models
from django.contrib.auth.models import AbstractBaseUser,BaseUserManager, UserManager

# Constantes
GIF = 'G'
RENNES = 'R'
METZ = 'M'

CAMPUS_CHOICES = [
        (GIF, 'Gif'),
        (RENNES, 'Rennes'),
        (METZ, 'Metz')
    ]

# Django models to create our SQL tables

class User(AbstractBaseUser):

    username = None
    email = models.EmailField(('email'), unique=True)

    # custom fields for user
    room_number = models.CharField(max_length = 10)
    first_name = models.CharField(max_length = 50)
    last_name = models.CharField(max_length = 50)
    profile_picture = models.ImageField(upload_to="media/user/", default='media/user/android.png')
    campus = models.CharField(
        max_length=1,
        choices=CAMPUS_CHOICES,
        default=GIF)
    
    USERNAME_FIELD = 'email'
    REQUIRED_FIELDS = ['room_number','first_name','last_name','campus']

    def __str__(self):              # __unicode__ on Python 2
        return self.email

    objects = UserManager()
    

class UserManager(BaseUserManager):
    '''
    This basically tells Django how it's supposed 
    to store the Object and with what attributes depending on permissions.
    '''

    use_in_migrations = True

    def create_user(self, email, room_number, first_name, last_name, profile_picture, campus, password):
        user = self.model(
            email=self.normalize_email(email),
            first_name = first_name,
            last_name = last_name,
            room_number = room_number,
            profile_picture = profile_picture,
            campus=campus,
        )
        user.set_password(password)
        user.save(using=self._db)
        return user

    # def create_staffuser(self, email, password):
    #     user = self.create_user(
    #         email,
    #         password=password,
    #     )
    #     user.staff = True
    #     user.save(using=self._db)
    #     return user

    # def create_superuser(self, password):
    #     user = self.create_user(
    #         email,
    #         password=password,
    #     )
    #     user.staff = True
    #     user.admin = True
    #     user.save(using=self._db)
    #     return user
    

class Product(models.Model): # Product table
    name = models.CharField(max_length=200)
    is_available = models.BooleanField(default=True)
    supplier = models.ForeignKey('User' , on_delete=models.CASCADE)   # equivalent to the sql constraint ON DELETE CASCADE
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    product_picture = models.ImageField(upload_to="media/product/", default='media/product/apple.jpg')
    objects = models.Manager()

class Order(models.Model): # Order table
    client = models.ForeignKey('User',on_delete=models.CASCADE)
    product = models.ForeignKey('Product', on_delete=models.CASCADE)   
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    objects = models.Manager()

