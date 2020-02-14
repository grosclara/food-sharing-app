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
    ordering = ()
    filter_horizontal = () # Leave it empty. You have neither `groups` or `user_permissions`
    add_fieldsets = ((None, {'fields': ('email','password1','password2')}),('Personal info', {'fields': ('last_name','first_name','date_joined','room_number','campus','profile_picture')}),('Permissions', {'fields': ('is_active','is_superuser','is_staff')}),)  
    fieldsets = (('Personal info', {'fields': ('room_number','campus','profile_picture')}),('Permissions', {'fields': ('is_active','is_superuser','is_staff')}),)  



# Indicates the tables on which the administrator can operate
admin.site.register(User, UserAdmin)
admin.site.register(Product)
admin.site.register(Order)

