from django.core.management.base import BaseCommand
from django.core.management import CommandError
from api.models import User
from api.managers import CustomUserManager

class Command(BaseCommand):
    help = 'Create an admin whithout other features'

    def add_arguments(self, parser):
        parser.add_argument(
            '--email',dest = 'email', type=str,
        )
        parser.add_argument(
            '--password',dest ='password', type=str, 
        )

        


    def handle(self, *args, **options):
        password = options.get('password')
        email = options.get('email')

        email = CustomUserManager.normalize_email(email)

        if password and email:
            user = User(email=email,
                          first_name = " ",
                          last_name = " ",
                          profile_picture = None,
                          room_number = " ",
                          campus = "Gif",
                          is_active = True,
                          is_staff = True,
                          is_superuser = True
                         )
            user.set_password(password)
            user.save()
    
        
            

        else:
            print("Use this syntax: python3 manage.py createsuperuser2 --email <email> --password <password>")
