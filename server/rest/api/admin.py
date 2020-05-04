from django.contrib import admin
from api.models import Product, Order, User
from django.conf import settings
from django.contrib.auth.admin import UserAdmin
from django.contrib.auth.models import Group
from rest_framework.authtoken.admin import Token
from django.contrib.sites.admin import SiteAdmin
#from allauth.account.admin import EmailAddressAdmin
#from allauth.socialaccount.admin import SocialAppAdmin, SocialTokenAdmin, SocialAccountAdmin
#from allauth.socialaccount.models import SocialApp, SocialToken, SocialAccount
# Provide a admin interface

# email : admin@admin.fr
# password : P@ssword1


#this will be the custom admin
class UserAdmin(UserAdmin):
    model = User
    ordering = ()
    filter_horizontal = () # Leave it empty. You have neither `groups` or `user_permissions`
    add_fieldsets = ((None, {'fields': ('email','password1','password2')}),('Personal info', {'fields': ('last_name','first_name','date_joined','room_number','campus','profile_picture')}),('Permissions', {'fields': ('is_active','is_superuser','is_staff')}),)  
    fieldsets = (('Personal info', {'fields': ('room_number','campus','profile_picture')}),('Permissions', {'fields': ('is_active','is_superuser','is_staff')}),)  

class UserAdminModel(admin.ModelAdmin):
    list_display = ('email', 'campus')
    list_filter = ['campus', 'is_active', 'is_staff']
    fields = ('email','campus','first_name', 'last_name', 'date_joined', 'last_login', 'is_active' )
    readonly_fields = ('email', 'campus', 'first_name', 'last_name', 'date_joined', 'last_login',)

class ProductAdmin(admin.ModelAdmin):
    list_display = ('name','expiration_date','status','category', 'supplier')
    list_filter = ('status','category','supplier')
    fields = ('name', 'supplier', 'status', 'campus', 'room_number','expiration_date', 'quantity' )
    readonly_fields = ('name', 'supplier', 'status', 'campus', 'room_number','expiration_date', 'quantity')

class OrderAdmin(admin.ModelAdmin):
    list_display = ('id','product', 'client', 'created_at')
    list_filter = ('client',)
    fields = ('client', 'product', 'fields')
    readonly_fields = ('client', 'product', 'fields')
admin.site.site_header = "CShare admin dashboard"
# Indicates the tables on which the administrator can operate
admin.site.register(User, UserAdminModel)
admin.site.register(Product, ProductAdmin)
admin.site.register(Order, OrderAdmin)

admin.site.unregister(Group)
admin.site.unregister(Token)
#admin.site.unregister(SiteAdmin)
#admin.site.unregister(EmailAddressAdmin)
