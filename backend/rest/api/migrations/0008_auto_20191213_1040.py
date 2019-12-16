# Generated by Django 3.0 on 2019-12-13 09:40

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('api', '0007_auto_20191213_1037'),
    ]

    operations = [
        migrations.AlterField(
            model_name='product',
            name='product_picture',
            field=models.ImageField(default='product/apple.jpg/', upload_to='product/'),
        ),
        migrations.AlterField(
            model_name='user',
            name='profile_picture',
            field=models.ImageField(default='user/android.png', upload_to='user/'),
        ),
    ]