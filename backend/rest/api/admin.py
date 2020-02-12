from django.contrib import admin
from api.models import Product, Order, User
from django.conf import settings
from django.contrib.auth.admin import UserAdmin
# Provide a admin interface

# username : admin
# password : P@ssword1


#this will be the custom admin
class UserAdmin(UserAdmin):
    model = User
    list_display = ()  # Contain only fields in your `custom-user-model`
    list_filter = ()  # Contain only fields in your `custom-user-model` intended for filtering. Do not include `groups`since you do not have it
    search_fields = ()  # Contain only fields in your `custom-user-model` intended for searching
    ordering = ()  # Contain only fields in your `custom-user-model` intended to ordering
    filter_horizontal = () # Leave it empty. You have neither `groups` or `user_permissions`
    add_fieldsets = ((None, {'fields': ('email', 'password')}),('Personal info', {'fields': ('last_name','first_name','room_number','campus','profile_picture')}),('Permissions', {'fields': ('is_active','is_superuser','is_staff')}),)  
  


# Indicates the tables on which the administrator can operate
admin.site.register(User, UserAdmin)
admin.site.register(Product)
admin.site.register(Order)

