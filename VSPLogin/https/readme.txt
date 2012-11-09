

Follow these steps to enable HTTPS on port 8081 for VSP within Eclipse:

1.  Copy vsp.keystore to the /conf folder of your Tomcat installation.

2.  Copy server.xml from this directory over the existing server.xml in your "Tomcat v7.0 Server at localhost-config" node under "Servers" in Project Explorer.
    The configuration under "Servers" is the server configuration that is used when Eclipse starts Tomcat.
    
3.  If you try to run anything in Tomcat, it will still load HTTP by default.  You can change the protocol to https and the port to 8081 manually.

4.  Note that you will need to import/approve the certificate since it is self-signed.

Enjoy!