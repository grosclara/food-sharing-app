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

    """ 
    Model of a user\n

    Each user, staff member or not, has the following attributes:\n

    A integer as identifier\n
    A valid and unique email (also identifier) for each user\n
    A password (at least 6 letters)\n 
    A non empty first name\n 
    A non empty last name\n
    A profile picture (that can be provided by default)\n
    A creation date: date_joined\n
    Boolean is_active : if is_active is set to False, the user will not have the permissions to perform most of the queries that the API offers\n
    Boolean is_staff : In addition, if the boolean is_staff and/or is_superuser are True\
                       then the user is an administrator member and can access the administration panel to carry out operations of moderation\n
    Boolean is_superuser : Grant more privileges than a simple staff member\n
    \n       
    Otherwise, the user is a client of the CShare application and may use the mobile app and want to interact with other users of the same status. 
    Therefore, he has supplementary required fields : \n
    
    A campus field in the list ["Gif","Rennes","Metz"]\n
    A room number\n
    His last login date
    """

    username = None
    email = models.EmailField(('Email address'), unique=True, help_text="Unique identifier required")
    
    date_joined = models.DateTimeField(('Date joined'), auto_now_add=True, help_text="Date of creation provided by default")

    # Permissions
    # A boolean attribute that indicates whether the user is considered “active”.
    is_active = models.BooleanField(('Is active'), default=True, help_text="Set to True by default")
    is_staff = models.BooleanField(('Is staff'), default=False, help_text="Set to False by default")
    is_superuser = models.BooleanField(('Is superuser'), default=False, help_text="Set to False by default")
    
    first_name = models.CharField(('First name'), max_length = 50, help_text="Not empty")
    last_name = models.CharField(('Last name'), max_length = 50, help_text="Not empty")

    profile_picture = models.ImageField(('Profile picture url'), upload_to="media/user/",\
        default='media/user/android.png', help_text="Set to 'media/user/android.png' by default")
    room_number = models.CharField(('Room number'), max_length = 50, null=True, help_text="Null for staff members but required for simple users")
    campus = models.CharField(('Campus'), 
        max_length=10,
        choices=CAMPUS_CHOICES,
        null=True,
        help_text="Null for staff members but required for simple users : ['Gif','Metz','Rennes']")

    
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

    """ 
    Model of a product that has the following attributes:\n

    A integer as identifier\n
    A supplier that is an existing user from :model:`api.user`\n
    A non empty name\n 
    A non empty status in the following list : ['Available','Collected','Delivered']. Set to Available by default\n
    A non empty category in the following list : ['Féculents','Fruits/Légumes','Viandes/Oeufs','Desserts/Pain',\
        'Conserves/Plats cuisinés','Produits laitiers','Produits d'entretien','Produits d'hygiène','Autres']"\n
    A product picture (that can be provided by default)\n
    A campus field in the list ["Gif","Rennes","Metz"]\n
    An required expiration date with the following format : YYYY-MM-DD
    A non empty quantity 
    A creation date provided by default\n
    An update date provided by default\n
    """

    name = models.CharField(('Name'), max_length=50, help_text="Not empty")
    supplier = models.ForeignKey('api.User', on_delete = models.CASCADE)
    category = models.CharField(('Category'), 
        max_length=50,
        choices=PRODUCT_CHOICES,
        help_text="Must be in this list : ['Féculents','Fruits/Légumes','Viandes/Oeufs','Desserts/Pain','Conserves/Plats cuisinés','Produits laitiers',\
            'Produits d'entretien','Produits d'hygiène','Autres']"
    )
    status = models.CharField(('Status'), 
        max_length=50,
        choices=STATUS_CHOICES,
        default=AVAILABLE,
        help_text="Must be in this list : ['Available','Collected','Delivered'] Set to Available by default"
    )
    product_picture = models.ImageField(('Product picture url'), upload_to="media/product/",\
        default='media/product/apple.jpg', help_text="Set to 'media/product/apple.png' by default")
    quantity = models.CharField(('Quantity'), max_length=50, help_text="Not empty")
    expiration_date = models.DateField(('Expiration date'), help_text="Date format is: YYYY-MM-DD.")
    campus = models.CharField(('Product location'), 
        max_length=10,
        choices=CAMPUS_CHOICES,
        help_text="Must be in this list : ['Gif','Metz','Rennes']"
    ) 
    created_at = models.DateTimeField(('Created at'), auto_now_add=True, help_text="Date of creation provided by default")
    updated_at = models.DateTimeField(('Updated at'), auto_now=True, help_text="Date of update provided by default")

    objects = models.Manager()


class Order(models.Model):

    """ 
    Model of an order:\n

    Each order contains the customer and the product to order (the supplier can be retrieved from the product attribute supplier)

    A integer as identifier\n
    A customer that is an existing user from :model:`api.user`\n
    A product that is an existing product from :model:`api.product`\n
    """

    customer = models.ForeignKey('api.User', on_delete = models.CASCADE)
    product = models.ForeignKey('api.Product', on_delete = models.CASCADE)
    created_at = models.DateTimeField(('Created at'), auto_now_add = True, help_text="Date of creation provided by default")
    updated_at = models.DateTimeField(('Updated at'), auto_now = True, help_text="Date of update provided by default")

    objects = models.Manager()

