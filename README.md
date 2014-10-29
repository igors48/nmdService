Orb. News server.
==========

How to build and deploy Orb
==========
1. Create GAE account
2. Create GAE application in admin interface
3. Download and install Java SE 7 from ...
4. Download and install GAE SDK from https://developers.google.com/appengine/downloads#Google_App_Engine_SDK_for_Java
5. Create envinronment variable GAE_HOME and set it to actual value
6. Download and install Ant from http://ant.apache.org/bindownload.cgi
7. Download and install Git
8. 
. Checkout repo ...
Run 'ant release'. You may leave application identifier and security key as is for now. Ensure that build completes without errors.
Now we will start local server to check everything is Ok. 
Run 'dev_server.bat'. 
You may leave 'application identifier' and 'security key' as is for now.
Go 'localhost:8080' to ensure that app is running. 
