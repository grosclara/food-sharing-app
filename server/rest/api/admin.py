from django.contrib import admin
from api.models import Product, Order, User
from django.conf import settings
from django.contrib.auth.admin import UserAdmin
from django.contrib.auth.models import Group
from rest_framework.authtoken.admin import Token
from django.contrib.sites.admin import SiteAdmin, Site
from django.urls import reverse
from django.utils.html import format_html
from allauth.account.admin import EmailAddressAdmin, EmailAddress

"""
    Provides an administrator interface to view the list of products, orders and users in the database. 
    The administrator also has the ability to ban users or delete products / orders if he wants to. 
    
    To access this interface, it is necessary to be at least a member of the staff 
    and therefore have an email address and a valid password to log in.

    To create a new staff member or a superuser, use the py commands createsuperuser or createstaff
"""

# email : admin@admin.fr
# password : P@ssword1

class UserAdmin(admin.ModelAdmin):

    list_display = ('email', 'campus', 'date_joined', 'is_active')
    list_filter = ['campus', 'date_joined', 'is_active', 'is_staff', 'is_superuser']

    ordering = ('email',)

    fieldsets = (
        ('Personal info', {'fields': ('email','first_name','last_name','profile_picture')}),
        ('Location', {'fields': ('campus', 'room_number')}),
        ('Permissions', {'fields': ('is_active','is_superuser','is_staff', 'user_permissions')}),
        ('Login information', {'fields': ('date_joined', 'last_login')})
        )  
    
    readonly_fields = ('email', 'first_name', 'last_name', 'profile_picture', 'campus', 'room_number', 'date_joined', 'last_login')

    search_fields = ['email', 'room_number', 'last_name']
    
    # This will help you to disable delete functionality
    def has_delete_permission(self, request, obj=None):
        return False
    
    def has_add_permission(self, request, obj=None):
        return False

    def ban(self, request, queryset):
        rows_updated = queryset.update(is_active='False')

        if rows_updated == 1:
            message_bit = "1 user was"
        else:
            message_bit = "%s users were" % rows_updated

        self.message_user(request, "%s successfully banned." % message_bit)
    ban.short_description = "Ban user"
    

    def activate(self, request, queryset):
        rows_updated = queryset.update(is_active='True')

        if rows_updated == 1:
            message_bit = "1 user was"
        else:
            message_bit = "%s users were" % rows_updated

        self.message_user(request, "%s successfully activated." % message_bit)
    activate.short_description = "Activate user"

    actions = [ban, activate]


class ProductAdmin(admin.ModelAdmin):

    def link_to_supplier(self, obj):
        link = reverse("admin:api_user_change", args=[obj.supplier.id])
        return format_html('<a href="{}">{}</a>', link, obj.supplier.email)
    link_to_supplier.short_description = 'Supplier'

    list_display = ('name','expiration_date','campus','status','category','link_to_supplier','created_at')
    list_filter = ('status','campus','category','expiration_date','created_at')

    ordering = ('-expiration_date',)

    fieldsets = (
        ('Product details', {'fields': ('name','status','category','quantity','expiration_date')}),
        ('Location', {'fields': ('campus',)}),
        ('Supplier', {'fields': ('link_to_supplier','created_at','updated_at')}),
        )

    # This will help you to disable create functionality
    def has_add_permission(self, request, obj=None):
        return False

    # This will help you to disable change functionality
    def has_change_permission(self, request, obj=None):
        return False

class OrderAdmin(admin.ModelAdmin):

    def link_to_customer(self, obj):
        link = reverse("admin:api_user_change", args=[obj.customer.id])
        return format_html('<a href="{}">{}</a>', link, obj.customer.email)
    link_to_customer.short_description = 'Customer'

    def link_to_product(self, obj):
        link = reverse("admin:api_product_change", args=[obj.product.id])
        return format_html('<a href="{}">{}</a>', link, obj.product.name)
    link_to_product.short_description = 'Product'

    class StatusFilter(admin.SimpleListFilter):
        title = 'Product status'
        parameter_name = 'status'

        def lookups(self, request, model_admin):
            return [ 
                ('Available', 'Available'),
                ('Collected', 'Collected'),
                ('Delivered', 'Delivered')]

        def queryset(self, request, queryset):
            if self.value() != None :
                array = []
                for element in queryset:
                    if element.product.status == self.value():
                        array.append(element.id)
                return queryset.filter(pk__in=array)
            return queryset

    class CampusFilter(admin.SimpleListFilter):
        title = 'Product location'
        parameter_name = 'product'

        def lookups(self, request, model_admin):
            return [ 
                ('Gif', 'Gif'),
                ('Metz', 'Metz'),
                ('Rennes', 'Rennes')]

        def queryset(self, request, queryset):
            if self.value() != None :
                array = []
                for element in queryset:
                    if element.product.campus == self.value():
                        array.append(element.id)
                return queryset.filter(pk__in=array)
            return queryset

    def status(self,obj):
        return obj.product.status
    
    def campus(self,obj):
        return obj.product.campus

    list_display = ('link_to_product', 'status', 'campus', 'link_to_customer', 'created_at', 'updated_at')

    list_filter = ('created_at',StatusFilter,CampusFilter)
    ordering = ('-updated_at',)

    # This will help you to disable create functionality
    def has_add_permission(self, request, obj=None):
        return False

    # This will help you to disable change functionality
    def has_change_permission(self, request, obj=None):
        return False



class CustomAllauthAdmin(EmailAddressAdmin):

    def get_queryset(self, request):
        qs = super(EmailAddressAdmin, self).get_queryset(request)
        if request.user.is_superuser:
            return qs
        return qs.filter(user__profile__country=request.user.profile.country)

admin.site.site_header = "CShare admin dashboard"

# Indicates the tables on which the administrator can operate
admin.site.register(Product, ProductAdmin)
admin.site.register(Order, OrderAdmin)
admin.site.register(User, UserAdmin)

admin.site.unregister(Group)
admin.site.unregister(Token)
admin.site.unregister(EmailAddress)

admin.site.unregister(Site)
