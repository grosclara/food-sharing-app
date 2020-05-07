from django.contrib.auth.base_user import BaseUserManager

class CustomUserManager(BaseUserManager):
    """
        Create a custom manager from the BaseUserManager
 
        A Manager is the interface through which database query operations are provided to Django models.
        This custom manager allows to replace the username by an email as a unique identifier. 
        It provides the methods to create a user as well as a staff member and a super user.
    """

    def create_user(self, email, password, first_name, last_name, profile_picture, room_number, campus, **extra_fields):
        
        """
        Create and save a user in the database with the given parameters.
 
        :param email: Email address
        :param password: Password
        :param first_name: First name
        :param last_name: Last name
        :param profile_picture: Filepath of the profile picture
        :param room_number: Room number
        :param campus: Campus
        :param **extra_fields: Information on user permissions
        :type email: str
        :type password: str
        :type first_name: str
        :type last_name: str
        :type profile_picture: str (None allowed)
        :type room_number: str (None allowed)
        :type campus: see CAMPUS_CHOICES in constants.py (None allowed)
        :type **extra_fields: dict (None allowed)

        :return: The created user
        :rtype: UserObject
        """

        # Activate the user account as soon as it is created
        extra_fields.setdefault('is_active', True)
        # By default, the user created is not part of the staff team neither a superuser
        extra_fields.setdefault('is_staff', False)
        extra_fields.setdefault('is_superuser', False)

        if not email:
            raise ValueError(('The Email must be set'))
        # Normalize email
        email = self.normalize_email(email)

        # Provide default null value if the following fields are not specified (especially for a staff member creation)
        """ if not campus:
            campus = None
        if not room_number:
            room_number = None
        if not profile_picture:
            profile_picture = None """

        # Creation of the user from the User model defined in models.py
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

        """
        Create and save a superuser in the database with the given parameters.
 
        :param email: Email address
        :param password: Password
        :param first_name: First name
        :param last_name: Last name
        :param **extra_fields: Information on user permissions
        :type email: str
        :type password: str
        :type first_name: str
        :type last_name: str
        :type **extra_fields: dict (None allowed)

        :return: The created user
        :rtype: UserObject
        """

        # Activate the user account as soon as it is created
        extra_fields.setdefault('is_active', True)
        # By default, the superuser created is part of the staff team and is a superuser
        extra_fields.setdefault('is_staff', True)
        extra_fields.setdefault('is_superuser', True)

        return self.create_user(email, password, first_name, last_name, profile_picture = 'media/user/superuser.jpeg', campus = None, room_number = None, **extra_fields)

    def create_staffuser(self, email, password, first_name, last_name, **extra_fields):

        """
        Create and save a staff member who is not a superuser in the database with the given parameters.
 
        :param email: Email address
        :param password: Password
        :param first_name: First name
        :param last_name: Last name
        :param **extra_fields: Information on user permissions
        :type email: str
        :type password: str
        :type first_name: str
        :type last_name: str
        :type **extra_fields: dict (None allowed)

        :return: The created user
        :rtype: UserObject
        """

        # Activate the user account as soon as it is created
        extra_fields.setdefault('is_active', True)
        # By default, the staff member created is not a superuser
        extra_fields.setdefault('is_staff', True)
        extra_fields.setdefault('is_superuser', False)

        return self.create_user(email, password, first_name, last_name, profile_picture = 'media/user/superuser.jpeg', campus = None, room_number = None, **extra_fields)
