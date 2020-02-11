from django.contrib.auth.base_user import BaseUserManager

class CustomUserManager(BaseUserManager):
    """
    Custom user model manager where email is the unique identifiers
    for authentication instead of usernames.
    """

    def create_user(self, email, password, first_name, last_name, profile_picture, room_number, campus, **extra_fields):
        """
        Create and save a User with the given features.
        """
        if not email:
            raise ValueError(_('The Email must be set'))
        email = self.normalize_email(email)
        user = self.model(email=email,
                          first_name = first_name,
                          last_name = last_name,
                          profile_picture = profile_picture,
                          room_number = room_number,
                          campus = campus,
                          is_active = True,
                          is_staff = False
                         )
        user.set_password(password)
        user.save()
        return user


        # extra_fields.setdefault('is_staff', True)
        # extra_fields.setdefault('is_superuser', True)
        #extra_fields.setdefault('is_active', True)

        # if extra_fields.get('is_staff') is not True:
        #     raise ValueError(_('Superuser must have is_staff=True.'))
        # if extra_fields.get('is_superuser') is not True:
        #     raise ValueError(_('Superuser must have is_superuser=True.'))
        # return self.create_user(email, password, profile_picture=None ,**extra_fields)

# class UserManager(BaseUserManager):
#     '''
#     This basically tells Django how it's supposed 
#     to store the Object and with what attributes depending on permissions.
#     '''

#     use_in_migrations = True

#     def create_user(self, email, password, is_active, first_name, last_name, profile_picture, room_number, campus):
#         user = self.model(
#             email=self.normalize_email(email),
#             is_active = True,
#             first_name = first_name,
#             last_name = last_name,
#             profile_picture = profile_picture,
#             room_number = room_number,
#             campus=campus,
#         )
#         user.set_password(password)
#         user.save()
#         return user

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