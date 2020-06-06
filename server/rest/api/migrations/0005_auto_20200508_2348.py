# Generated by Django 3.0.2 on 2020-05-08 23:48

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('api', '0004_auto_20200502_1857'),
    ]

    operations = [
        migrations.RenameField(
            model_name='order',
            old_name='client',
            new_name='customer',
        ),
        migrations.RemoveField(
            model_name='product',
            name='room_number',
        ),
        migrations.AlterField(
            model_name='order',
            name='created_at',
            field=models.DateTimeField(auto_now_add=True, help_text='Date of creation provided by default', verbose_name='Created at'),
        ),
        migrations.AlterField(
            model_name='order',
            name='updated_at',
            field=models.DateTimeField(auto_now=True, help_text='Date of update provided by default', verbose_name='Updated at'),
        ),
        migrations.AlterField(
            model_name='product',
            name='campus',
            field=models.CharField(choices=[('Gif', 'Gif'), ('Rennes', 'Rennes'), ('Metz', 'Metz')], help_text="Must be in this list : ['Gif','Metz','Rennes']", max_length=10, verbose_name='Product location'),
        ),
        migrations.AlterField(
            model_name='product',
            name='category',
            field=models.CharField(choices=[('Féculents', 'Féculents'), ('Fruits/Légumes', 'Fruits/Légumes'), ('Conserves/Plats cuisinés', 'Conserves/Plats cuisinés'), ('Produits laitiers', 'Produits laitiers'), ('Desserts/Pain', 'Desserts/Pain'), ('Viandes/Oeufs', 'Viandes/Oeufs'), ("Produits d'hygiène", "Produits d'hygiène"), ("Produits d'entretien", "Produits d'entretien"), ('Autres', 'Autres')], help_text="Must be in this list : ['Féculents','Fruits/Légumes','Viandes/Oeufs','Desserts/Pain','Conserves/Plats cuisinés','Produits laitiers',            'Produits d'entretien','Produits d'hygiène','Autres']", max_length=50, verbose_name='Category'),
        ),
        migrations.AlterField(
            model_name='product',
            name='created_at',
            field=models.DateTimeField(auto_now_add=True, help_text='Date of creation provided by default', verbose_name='Created at'),
        ),
        migrations.AlterField(
            model_name='product',
            name='expiration_date',
            field=models.DateField(help_text='Date format is: YYYY-MM-DD.', verbose_name='Expiration date'),
        ),
        migrations.AlterField(
            model_name='product',
            name='name',
            field=models.CharField(help_text='Not empty', max_length=50, verbose_name='Name'),
        ),
        migrations.AlterField(
            model_name='product',
            name='product_picture',
            field=models.ImageField(default='media/product/apple.jpg', help_text="Set to 'media/product/apple.png' by default", upload_to='media/product/', verbose_name='Product picture url'),
        ),
        migrations.AlterField(
            model_name='product',
            name='quantity',
            field=models.CharField(help_text='Not empty', max_length=50, verbose_name='Quantity'),
        ),
        migrations.AlterField(
            model_name='product',
            name='status',
            field=models.CharField(choices=[('Available', 'Available'), ('Collected', 'Collected'), ('Delivered', 'Delivered')], default='Available', help_text="Must be in this list : ['Available','Collected','Delivered'] Set to Available by default", max_length=50, verbose_name='Status'),
        ),
        migrations.AlterField(
            model_name='product',
            name='updated_at',
            field=models.DateTimeField(auto_now=True, help_text='Date of update provided by default', verbose_name='Updated at'),
        ),
        migrations.AlterField(
            model_name='user',
            name='campus',
            field=models.CharField(choices=[('Gif', 'Gif'), ('Rennes', 'Rennes'), ('Metz', 'Metz')], help_text="Null for staff members but required for simple users : ['Gif','Metz','Rennes']", max_length=10, null=True, verbose_name='Campus'),
        ),
        migrations.AlterField(
            model_name='user',
            name='date_joined',
            field=models.DateTimeField(auto_now_add=True, help_text='Date of creation provided by default', verbose_name='Date joined'),
        ),
        migrations.AlterField(
            model_name='user',
            name='email',
            field=models.EmailField(help_text='Unique identifier required', max_length=254, unique=True, verbose_name='Email address'),
        ),
        migrations.AlterField(
            model_name='user',
            name='first_name',
            field=models.CharField(help_text='Not empty', max_length=50, verbose_name='First name'),
        ),
        migrations.AlterField(
            model_name='user',
            name='is_active',
            field=models.BooleanField(default=True, help_text='Set to True by default', verbose_name='Is active'),
        ),
        migrations.AlterField(
            model_name='user',
            name='is_staff',
            field=models.BooleanField(default=False, help_text='Set to False by default', verbose_name='Is staff'),
        ),
        migrations.AlterField(
            model_name='user',
            name='is_superuser',
            field=models.BooleanField(default=False, help_text='Set to False by default', verbose_name='Is superuser'),
        ),
        migrations.AlterField(
            model_name='user',
            name='last_name',
            field=models.CharField(help_text='Not empty', max_length=50, verbose_name='Last name'),
        ),
        migrations.AlterField(
            model_name='user',
            name='profile_picture',
            field=models.ImageField(default='media/user/android.png', help_text="Set to 'media/user/android.png' by default", upload_to='media/user/', verbose_name='Profile picture url'),
        ),
        migrations.AlterField(
            model_name='user',
            name='room_number',
            field=models.CharField(help_text='Null for staff members but required for simple users', max_length=50, null=True, verbose_name='Room number'),
        ),
    ]