You can use the MySql Workbench to import the SQL file.

You will need to create/modify a MySQL user to match what the Tomcat configuration expects:
UserName:		tomcat
Password:		tomcat
Permissions:	SELECT, DELETE, INSERT (this is the minimum set of permissions... more permissions are OK).

Most likely, you will need to download and install the MySQL Connector/J.  Install the JAR file in the /lib/ext folder of your JRE installation.

These default users are in the database:
test/test			trader
test1/test1			trader
admin/Admin123		admin (/admin/Admin.jsp)