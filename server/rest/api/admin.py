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
from .constants import CAMPUS_CHOICES, STATUS_CHOICES

"""
    Provides an administrator interface to view the list of products, orders and users in the database. 
    The administrator also has the ability to ban users or delete products / orders if he wants to. 
    
    To access this interface, it is necessary to be at least a member of the staff 
    and therefore have an email address and a valid password to log in.

    To create a new staff member or a superuser, use the py commands createsuperuser or createstaff
"""

class UserAdmin(admin.ModelAdmin):

    """
        Customize representation of the user model in the administration interface
        Give access to the list of all users and allow to ban or activate certain users
    """

    # Disable delete functionality
    def has_delete_permission(self, request, obj=None):
        return False
    # Disable add functionality
    def has_add_permission(self, request, obj=None):
        return False
    
    # Fields to display in the admin/api/user/ page.
    list_display = ('email', 'campus', 'date_joined', 'is_active')
    # Filters to display subsets of the whole queryset
    list_filter = ['campus', 'date_joined', 'is_active', 'is_staff', 'is_superuser']
    # Order the elements of the queryset
    ordering = ('email',)
    # Allow the search of users according to some fields
    search_fields = ['email', 'room_number', 'last_name']

    # Fields to display in the admin/api/user/46/change/ page.
    fieldsets = (
        ('Personal info', {'fields': ('email','first_name','last_name','profile_picture')}),
        ('Location', {'fields': ('campus', 'room_number')}),
        ('Permissions', {'fields': ('is_active','is_superuser','is_staff', 'user_permissions')}),
        ('Login information', {'fields': ('date_joined', 'last_login')})
        )
    # Read only fileds that cannot be modified by the administrator
    readonly_fields = ('email', 'first_name', 'last_name', 'profile_picture', 'campus', 'room_number', 'date_joined', 'last_login') 

    def ban(self, request, queryset):
        """
            Allow to ban the users selected by the admin in the admin/api/user/ page. 
            For each user selected, its is_active attribute is set to False.
        """

        # Perform the database operations
        rows_updated = queryset.update(is_active='False')

        # Display a message on the admin panel when the operation is successful
        if rows_updated == 1:
            message_bit = "1 user was"
        else:
            message_bit = "%s users were" % rows_updated
        self.message_user(request, "%s successfully banned." % message_bit)
    # Add a short description to the ban method 
    ban.short_description = "Ban user"

    def activate(self, request, queryset):
        """
            Allow to activate the users selected by the admin in the admin/api/user/ page. 
            For each user selected, its is_active attribute is set to True.
        """

        # Perform the database operations
        rows_updated = queryset.update(is_active='True')

        # Display a message on the admin panel when the operation is successful
        if rows_updated == 1:
            message_bit = "1 user was"
        else:
            message_bit = "%s users were" % rows_updated
        self.message_user(request, "%s successfully activated." % message_bit)
    # Add a short description to the activate method 
    activate.short_description = "Activate user"

    # Add the ban and activate method to the action menu in the admin/api/user/ page.
    actions = [ban, activate]


class ProductAdmin(admin.ModelAdmin):

    """
        Customize representation of the product model in the administration interface
        Give access to the list of all products and their corresponding suppliers and allow to delete certain users
    """

    # Disable create functionality
    def has_add_permission(self, request, obj=None):
        return False

    # Disable change functionality
    def has_change_permission(self, request, obj=None):
        return False

    def link_to_supplier(self, obj):

        """
            Generate a link to the supplier change admin page of the product object passed as a parameter
        """

        # Create a link to the admin/api/user/{obj.supplier.id}/change/ page
        link = reverse("admin:api_user_change", args=[obj.supplier.id])
        return format_html('<a href="{}">{}</a>', link, obj.supplier.email)
    # Add a short description to name the corresponding column
    link_to_supplier.short_description = 'Supplier'

    # Fields to display in the admin/api/product/ page.
    list_display = ('name','expiration_date','campus','status','category','link_to_supplier','created_at')
    # Filters to display subsets of the whole queryset
    list_filter = ('status','campus','category','expiration_date','created_at')
    # Order the elements of the queryset
    ordering = ('-expiration_date',)
    # Allow the search of products according to some fields
    search_fields = ('name',)

    # Fields to display in the admin/api/product/4/change/ page.
    fieldsets = (
        ('Product details', {'fields': ('name','status','category','quantity','expiration_date')}),
        ('Location', {'fields': ('campus',)}),
        ('Supplier', {'fields': ('link_to_supplier','created_at','updated_at')}),
    )
 

class OrderAdmin(admin.ModelAdmin):

    """
        Customize representation of the order model in the administration interface
        Give access to the list of all orders as well as their related customer and product and allow to delete certain orders
    """

    # Disable create functionality
    def has_add_permission(self, request, obj=None):
        return False

    # Disable edit functionality
    def has_change_permission(self, request, obj=None):
        return False

    def link_to_customer(self, obj):

        """
            Generate a link to the customer change admin page of the order object passed as a parameter
        """

        # Create a link to the admin/api/user/{obj.customer.id}/change/ page
        link = reverse("admin:api_user_change", args=[obj.customer.id])
        return format_html('<a href="{}">{}</a>', link, obj.customer.email)
    # Add a short description to name the corresponding column
    link_to_customer.short_description = 'Customer'

    def link_to_product(self, obj):

        """
            Generate a link to the product change admin page of the order object passed as a parameter
        """

        # Create a link to the admin/api/product/{obj.product.id}/change/ page
        link = reverse("admin:api_product_change", args=[obj.product.id])
        return format_html('<a href="{}">{}</a>', link, obj.product.name)
    # Add a short description to name the corresponding column
    link_to_product.short_description = 'Product'

    class StatusFilter(admin.SimpleListFilter):

        """ Add filtering against product status values in the admin filter sidebar. """

        # Name of the parameter by which the query is filtered
        title = "status"
        parameter_name = 'status'

        def lookups(self, request, model_admin):

            """ Define possible values that the status parameter can take. """

            return STATUS_CHOICES 

        def queryset(self, request, queryset):

            """
                Return the queryset after having filtered against the self.value() parameter
                If self.value() is None, it returns the whole list of orders
            """

            if self.value() != None :
                filtered_queryset_id = []
                for element in queryset:
                    # If the product status of the order matches the parameters self.value():
                    # append its id to the filtered_queryset_id
                    if element.product.status == self.value():
                        filtered_queryset_id.append(element.id)
                # Each order whose id is in the filtered_queryset_id will be in the returned queryset
                return queryset.filter(pk__in=filtered_queryset_id)

            return queryset

    class CampusFilter(admin.SimpleListFilter):

        """ Add filtering against product campus values in the admin filter sidebar. """

        # Name of the parameter by which the query is filtered
        title = "campus"
        parameter_name = 'campus'

        def lookups(self, request, model_admin):

            """ Define possible values that the status parameter can take. """

            return CAMPUS_CHOICES

        def queryset(self, request, queryset):

            """
                Return the queryset after having filtered against the self.value() parameter
                If self.value() is None, it returns the whole list of orders
            """

            if self.value() != None :
            
                filtered_queryset_id = []
                for element in queryset:
                    # If the product status of the order matches the parameters self.value():
                    # append its id to the filtered_queryset_id
                    if element.product.campus == self.value():
                        filtered_queryset_id.append(element.id)
                # Each order whose id is in the filtered_queryset_id will be in the returned queryset
                return queryset.filter(pk__in=filtered_queryset_id)

            return queryset

    def status(self, obj):

        """ Return the product status of the object order passed as a parameter. """

        return obj.product.status
    
    def campus(self, obj):

        """ Return the product campus of the object order passed as a parameter. """

        return obj.product.campus

    # Fields to display in the admin/api/order/ page.
    list_display = ('link_to_product', 'status', 'campus', 'link_to_customer', 'created_at', 'updated_at')
    # Filters to display subsets of the whole queryset
    list_filter = ('created_at', StatusFilter, CampusFilter)
    # Order the elements of the queryset
    ordering = ('-updated_at',)

# Site header
admin.site.site_header = "CShare admin dashboard"

# Indicate the tables on which the administrator can operate
admin.site.register(Product, ProductAdmin)
admin.site.register(Order, OrderAdmin)
admin.site.register(User, UserAdmin)
# Remove this tables from the admin dashboard
admin.site.unregister(Group)
admin.site.unregister(Token)
admin.site.unregister(EmailAddress)
admin.site.unregister(Site)
