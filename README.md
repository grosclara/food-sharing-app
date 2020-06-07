# Deployment mode


This `README.md` contains instructions for configuring the database, the server and the mobile application in the deployment phase.


## Database configuration

Follow these instructions to install and configure MySQL database. [Oracle MySQL Database Installation and Configuration for Advanced Management Console](https://docs.oracle.com/javacomponents/advanced-management-console-2/install-guide/mysql-database-installation-and-configuration-advanced-management-console.htm#JSAMI122)

After you’ve installed MySQL, you can create a user and create your database.

## Backend configuration

### Create vitualenv

In the `server` directory (your python project directory), create a vritual environment runnning the command.
```
python -m venv env
```

To activate the virtual environement
```
source env/bin/activate
```

Then install your pip dependencies
```
pip install -r requirements.txt
```

To exit the virtual environement, `CTRL+d` or run
```
deactivate
```

### Setup environment variables

In the `server` directory, create a `.env` file that will contain our local variables. The `.env` file should look like this :

`server/.env` file

```
CSHARE_DATABASE_NAME=cshare
CSHARE_ROOT_USER=root
CSHARE_ROOT_PASSWORD=my-secret-pw
CSHARE_HOST=localhost
CSHARE_DATABASE_PORT=3306
CSHARE_SECRET_KEY=my-secret-key
SERVER_PORT=8000
SERVER_IP_ADDR=localhost
```

The `CSHARE_DATABASE_NAME`, `CSHARE_ROOT_USER`, `CSHARE_ROOT_PASSWORD`, `CSHARE_HOST`, `CSHARE_DATABASE_PORT` must match the corresponding values of the SQL database you've just created.

### Apply database migrations
You should think of migrations as a version control system for your database schema. `makemigrations` is responsible for packaging up your model changes into individual migration files - analogous to commits - and `migrate` is responsible for applying those to your database.

In the `server` directory, run the following commands to apply migrations
```
python3 manage.py makemigrations
python3 manage.py migrate
```

### Run server
Your server is now configured properly.
To run it, make sure the `SERVER_PORT` and the `SERVER_IP_ADDR` variables defined in the `.env` file correspond to the address and port you wish to use and run the following command
```
python3 manage.py runserver
```

## Mobile app configuration

The mobile application is developped under AndroidStudio, to install this IDE, please follow this link [Install AndroidStudio](https://developer.android.com/studio/install).

Then the application requires the server address and the port. To setup this environemnt variable, create a `gradle.properties` file in the `mobile_app/app` directory. The file should look like this (and the variable should match the `SERVER_IP_ADDR` and `SERVER_PORT` variables in the `server/.env` file)

`mobile_app/app/gradle.properties` file

```
SERVER_URL=localhost
``` 

Your app is ready to run. To test is on a hardware device, please follow these instructions : [Test Django Locally on a Mobile Device](http://davidwilson.me/2013/08/18/Testing-Django-on-mobile-device-locally.html?fbclid=IwAR3E7FDTZL7Gcxth4EYSdZ3UGUquUYkihi4xoTyUVFkEovmP4i1KLRkr7HA
)

