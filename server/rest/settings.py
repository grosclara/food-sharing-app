"""
Django settings for rest project.

Generated by 'django-admin startproject' using Django 3.0.

For more information on this file, see
https://docs.djangoproject.com/en/3.0/topics/settings/

For the full list of settings and their values, see
https://docs.djangoproject.com/en/3.0/ref/settings/
"""

import os
from decouple import config

# Build paths inside the project like this: os.path.join(BASE_DIR, ...)
BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))

# Quick-start development settings - unsuitable for production
# See https://docs.djangoproject.com/en/3.0/howto/deployment/checklist/

# SECURITY WARNING: keep the secret key used in production secret!
SECRET_KEY = config("CSHARE_SECRET_KEY")

# SECURITY WARNING: don't run with debug turned on in production!
DEBUG = True

# Allow acces to every hosts
ALLOWED_HOSTS = ['*']


#### APPLICATION SETTINGS ####

INSTALLED_APPS = [
    # App name
    'api',

    # The following apps are required:
    'django.contrib.admin',
    'django.contrib.auth',
    'django.contrib.contenttypes',
    'django.contrib.messages',
    'django.contrib.staticfiles',
    'django.contrib.sites',
    'django.contrib.sessions',

    # Django REST framework is a powerful and flexible toolkit for building Web APIs.
    'rest_framework',
    # Token-based authentication.
    'rest_framework.authtoken',

    # rest_auth has basic auth functionality like login, logout, password reset and password change
    # By default django-rest-auth uses Django’s Token-based authentication.
    'rest_auth',
    # rest_auth.registration has logic related with registration and social media authentication
    'rest_auth.registration',

    # Enable standard registration process within rest_auth.registration
    # Supports multiple authentication schemes (e.g. login by e-mail), 
    # as well as multiple strategies for account verification (ranging from none to e-mail verification).
    'allauth',
    'allauth.account',
    'allauth.socialaccount',

    # Filtering support
    'django_filters',

    # Print django models using the command : python3 manage.py graph_models -a -g -o my_project_visualized.png
    'django_extensions',

    # Generate documentation
    'django.contrib.admindocs',
]

MIDDLEWARE = [
    'django.middleware.security.SecurityMiddleware',
    'django.contrib.sessions.middleware.SessionMiddleware',
    'django.middleware.common.CommonMiddleware',
    'django.contrib.auth.middleware.AuthenticationMiddleware',
    'django.contrib.messages.middleware.MessageMiddleware',
    'django.middleware.clickjacking.XFrameOptionsMiddleware',
]

ROOT_URLCONF = 'rest.urls'

# Specify the context processors as follows

TEMPLATES = [
    {
        'BACKEND': 'django.template.backends.django.DjangoTemplates',
        'DIRS': [],
        'APP_DIRS': True,
        'OPTIONS': {
            'context_processors': [
                'django.template.context_processors.debug',
                'django.template.context_processors.request',
                'django.contrib.auth.context_processors.auth',
                'django.contrib.messages.context_processors.messages',
            ],
        },
    },
]

WSGI_APPLICATION = 'rest.wsgi.application'


# Database configuration
# LIEN QDB
# https://docs.djangoproject.com/en/3.0/ref/settings/#databases

DATABASES = {
    'default': {
        'ENGINE': 'django.db.backends.mysql',
        'NAME': config('CSHARE_DATABASE_NAME'),
        'USER': config('CSHARE_ROOT_USER'),
        'PASSWORD': config('CSHARE_ROOT_PASSWORD'),
        'HOST': config('CSHARE_HOST'),
        'PORT': config('CSHARE_DATABASE_PORT'),
    }
}


# Internationalization
# https://docs.djangoproject.com/en/3.0/topics/i18n/

LANGUAGE_CODE = 'en-us'
TIME_ZONE = 'UTC'
USE_I18N = True
USE_L10N = True
USE_TZ = True


# Static files (CSS, JavaScript, Images)
# https://docs.djangoproject.com/en/3.0/howto/static-files/

STATIC_URL = '/static/'
MEDIA_ROOT = os.path.join(BASE_DIR,"rest")
MEDIA_URL = '/'


# Rest framework configuration

REST_FRAMEWORK = {

    # Default filter backend
    'DEFAULT_FILTER_BACKENDS': [ 'django_filters.rest_framework.DjangoFilterBackend' ],

    # This authentication scheme uses a simple token-based HTTP Authentication scheme. 
    # Token authentication is appropriate for client-server setups, such as native desktop and mobile clients.
    'DEFAULT_AUTHENTICATION_CLASSES': [ 'rest_framework.authentication.TokenAuthentication', ],

    # The IsAuthenticated permission class will deny permission to any unauthenticated user, and allow permission otherwise.
    # This permission is suitable if you want your API to only be accessible to registered users.
    'DEFAULT_PERMISSION_CLASSES': ( 'rest_framework.permissions.IsAuthenticated',   ),

    # Pagination
    'DEFAULT_PAGINATION_CLASS': 'rest_framework.pagination.PageNumberPagination',
    'PAGE_SIZE': 12,

}


#### AUTH CONFIGURATION ####

# Password validation
# https://docs.djangoproject.com/en/3.0/ref/settings/#auth-password-validators

AUTH_PASSWORD_VALIDATORS = [
    {
        'NAME': 'django.contrib.auth.password_validation.MinimumLengthValidator',
        'OPTIONS': {
            'min_length': 6,
        }
    },
]

AUTH_USER_MODEL = 'api.User'

# Allauth configuration

# Specifies the login method to use – whether the user logs in by entering their username, e-mail address, or either one of both.  
ACCOUNT_AUTHENTICATION_METHOD = 'email'
# The user is required to hand over an e-mail address when signing up.
ACCOUNT_EMAIL_REQUIRED = True
# Enforce uniqueness of e-mail addresses
ACCOUNT_UNIQUE_EMAIL = True
ACCOUNT_USER_EMAIL_FIELD = 'email'
# The user is required to enter a username when signing up. 
# Note that the user will be asked to do so even if ACCOUNT_AUTHENTICATION_METHOD is set to email. 
# Set to False when you do not wish to prompt the user to enter a username.
ACCOUNT_USERNAME_REQUIRED = False
ACCOUNT_USER_MODEL_USERNAME_FIELD = None

AUTHENTICATION_BACKENDS = (
    'django.contrib.auth.backends.ModelBackend',
    # `allauth` specific authentication methods, such as login by e-mail
    'allauth.account.auth_backends.AuthenticationBackend',
)

# Rest auth configuration

SITE_ID = 1
# Set it to True if you want to have old password verification on password change enpoint
OLD_PASSWORD_FIELD_ENABLED = True
REST_AUTH_SERIALIZERS = {
    # Serializer class in rest_auth.views.UserDetailsView
    'USER_DETAILS_SERIALIZER': 'api.serializers.UserSerializer',
    # Response for successful authentication in rest_auth.views.LoginView and rest_auth.views.RegisterView
    'TOKEN_SERIALIZER': 'api.serializers.CustomTokenSerializer',
}
REST_AUTH_REGISTER_SERIALIZERS = {
    # Serializer class in rest_auth.registration.views.RegisterView
    "REGISTER_SERIALIZER": "api.serializers.CustomRegisterSerializer",
}


#### EMAIL CONFIGURATION #### 
# To be used in development
EMAIL_BACKEND = 'django.core.mail.backends.dummy.EmailBackend'


# Default settings to print graphs

GRAPH_MODELS = {
  'all_applications': True,
  'group_models': True,
}

