# Generated by Django 3.0 on 2020-01-20 15:16

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('api', '0004_auto_20200120_1603'),
    ]

    operations = [
        migrations.AlterField(
            model_name='user',
            name='campus',
            field=models.CharField(choices=[('Gif', 'G'), ('Rennes', 'R'), ('Metz', 'M')], max_length=6),
        ),
    ]
