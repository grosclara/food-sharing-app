# Generated by Django 3.0 on 2019-12-13 09:35

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('api', '0004_auto_20191208_1841'),
    ]

    operations = [
        migrations.AddField(
            model_name='product',
            name='profile_picture',
            field=models.ImageField(default='/media/user/android.png', upload_to='media/product/'),
            preserve_default=False,
        ),
        migrations.AddField(
            model_name='user',
            name='profile_picture',
            field=models.ImageField(default='/media/user/android.png/', upload_to='media/user/'),
            preserve_default=False,
        ),
    ]