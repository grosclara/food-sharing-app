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

        extra_fields.setdefault('is_staff', False)
        extra_fields.setdefault('is_superuser', False)
        extra_fields.setdefault('is_active', True)

        if not email:
            raise ValueError(_('The Email must be set'))
        email = self.normalize_email(email)

        if not campus:
            campus = None
        
        if not room_number:
            room_number = None
        
        if not profile_picture:
            profile_picture = None

        user = self.model(email=email,
                          first_name = first_name,
                          last_name = last_name,
                          profile_picture = profile_picture,
                          room_number = room_number,
                          campus = campus,
                          is_active = extra_fields["is_active"],
                          is_staff = extra_fields['is_staff'],
                          is_superuser = extra_fields['is_superuser']
                         )
        user.set_password(password)
        user.save()
        return user
    
    def create_superuser(self, email, password, first_name, last_name, **extra_fields):

        extra_fields.setdefault('is_staff', True)
        extra_fields.setdefault('is_superuser', True)
        extra_fields.setdefault('is_active', True)

        return self.create_user(email, password, first_name, last_name, profile_picture = None, campus = None, room_number = None, **extra_fields)