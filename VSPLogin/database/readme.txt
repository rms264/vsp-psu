You can use the MySql Workbench to import the SQL file.

You will need to create/modify a MySQL user to match what the Tomcat configuration expects:
UserName:		tomcat
Password:		tomcat
Permissions:	SELECT, DELETE, INSERT (this is the minimum set of permissions... more permissions are OK).