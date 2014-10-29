### Orb. News server.
### How to build and deploy Orb
#### Java
1. Download and install Java SE 7 from ...
 
#### GAE
1. Create GAE application in admin interface. It is described [here](https://sites.google.com/site/gdevelopercodelabs/app-engine/creating-your-app-engine-account);
2. Download latest GAE SDK from https://developers.google.com/appengine/downloads#Google_App_Engine_SDK_for_Java;
3. Unzip it to some directory;
3. Create envinronment variable GAE_HOME and set it`s value to path where GAE SDK was unzipped.

#### Git 
7. Download and install Git

#### Ant 
6. Download and install Ant from http://ant.apache.org/bindownload.cgi

#### Source
. Checkout repo ...

Run 'ant release'. You may leave application identifier and security key as is for now. Ensure that build completes without errors.
Now we will start local server to check everything is Ok. 
Run 'dev_server.bat'. 
You may leave 'application identifier' and 'security key' as is for now.
Go 'localhost:8080' to ensure that app is running. 
