"""Django's command-line utility for administrative tasks."""
import os
import sys
from decouple import config


def main():
    os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'rest.settings')

    # Override default port for `runserver` command
    from django.core.management.commands.runserver import Command as runserver
    runserver.default_port = config("SERVER_PORT")
    runserver.default_addr = config("SERVER_IP_ADDR")

    try:
        from django.core.management import execute_from_command_line
    except ImportError as exc:
        raise ImportError(
            "Couldn't import Django. Are you sure it's installed and "
            "available on your PYTHONPATH environment variable? Did you "
            "forget to activate a virtual environment?"
        ) from exc
    execute_from_command_line(sys.argv)


if __name__ == '__main__':
    main()
