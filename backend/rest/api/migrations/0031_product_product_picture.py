# Generated by Django 3.0 on 2019-12-18 12:06

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('api', '0030_remove_product_product_picture'),
    ]

    operations = [
        migrations.AddField(
            model_name='product',
            name='product_picture',
            field=models.ImageField(default='media/product/apple.jpg', upload_to='media/product/'),
        ),
    ]