# Generated by Django 5.2 on 2025-04-17 12:29

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ("users", "0002_myuser_bio_myuser_profile_picture"),
    ]

    operations = [
        migrations.RenameField(
            model_name="myuser",
            old_name="bio",
            new_name="UserId",
        ),
    ]
