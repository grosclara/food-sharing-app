from django.db import models
from django.contrib.auth.models import AbstractBaseUser
from .managers import CustomUserManager
from django.core.validators import EmailValidator
from django.utils import timezone
from django.conf import settings
from django.contrib.auth.models import PermissionsMixin
from .constants import *


class User(AbstractBaseUser, PermissionsMixin):
    # The easiest way to construct a compliant custom user model is to inherit from AbstractBaseUser. 
    # AbstractBaseUser provides the core implementation of a user model, including hashed passwords and tokenized password resets.

    # Django’s provides a PermissionsMixin which we can include in the class hierarchy for our user model to support Django’s permissions.

    """ General description """

    username = None
    email = models.EmailField(('Email address'), unique=True, help_text="Unique identifier required")
    
    date_joined = models.DateTimeField(('Date joined'), auto_now_add=True)

    # Permissions
    # A boolean attribute that indicates whether the user is considered “active”.
    is_active = models.BooleanField(('Is active'), default=True)
    is_staff = models.BooleanField(('Is staff'), default=False)
    is_superuser = models.BooleanField(('Is superuser'), default=False)
    
    first_name = models.CharField(('First name'), max_length = 50)
    last_name = models.CharField(('Last name'), max_length = 50)

    profile_picture = models.ImageField(('Profile picture url'), upload_to="media/user/", default='media/user/android.png')
    room_number = models.CharField(('Room number'), max_length = 50, null=True)
    campus = models.CharField(('Campus'), 
        max_length=10,
        choices=CAMPUS_CHOICES,
        null=True)

    
    # Key implementation details to provide with AbstractBaseUser
    # the field email is used as the identifying field
    USERNAME_FIELD = 'email'
    #A list of the field names that will be prompted for when creating a user via the createsuperuser management command. 
    # The user will be prompted to supply a value for each of these fields. 
    # It must include any field for which blank is False or undefined and may include additional fields you want prompted 
    # for when a user is created interactively. REQUIRED_FIELDS has no effect in other parts of Django, like creating a user in the admin.
    REQUIRED_FIELDS = ['first_name','last_name']

    objects = CustomUserManager()


class Product(models.Model): 

    """ Product model
        NOTE REQUIRED  OR OPTIONAL FIELDS <br/>
        DESCRIPTION
        help text
    """

    name = models.CharField(('Name'), max_length=50)

    supplier = models.ForeignKey('api.User', on_delete = models.CASCADE, help_text="LOOOOOOOOOOOOOL")
    category = models.CharField(('Category'), 
        max_length=50,
        choices=PRODUCT_CHOICES,
        default=FECULENTS
    )
    status = models.CharField(('Status'), 
        max_length=50,
        choices=STATUS_CHOICES,
        default=AVAILABLE
    )
    product_picture = models.ImageField(('Product picture url'), upload_to="media/product/", default='media/product/apple.jpg')
    quantity = models.CharField(('Quantity'), max_length=50)
    expiration_date = models.DateField(('Expiration date'), default=timezone.now)
    campus = models.CharField(('Product location'), 
        max_length=10,
        choices=CAMPUS_CHOICES,
        default=GIF) 

    created_at = models.DateTimeField(('Created at'), auto_now_add=True)
    updated_at = models.DateTimeField(('Updated at'), auto_now=True)

    objects = models.Manager()


class Order(models.Model):

    """ Order model
        NOTE REQUIRED  OR OPTIONAL FIELDS
        DESCRIPTION
    """

    customer = models.ForeignKey('api.User',on_delete=models.CASCADE)
    product = models.ForeignKey('api.Product', on_delete=models.CASCADE)
    created_at = models.DateTimeField(('Created at'), auto_now_add=True)
    updated_at = models.DateTimeField(('Updated at'), auto_now=True)

    objects = models.Manager()

