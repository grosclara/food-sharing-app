# Generated by Django 3.0 on 2020-01-20 14:22

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('api', '0001_initial'),
    ]

    operations = [
        migrations.AlterField(
            model_name='user',
            name='room_number',
            field=models.CharField(max_length=50),
        ),
    ]
