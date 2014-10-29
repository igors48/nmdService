### Orb. News server.
### How to build and deploy Orb
#### Java
1. Download and install JDK Java SE 7 from [here](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html)
 
#### GAE
1. Create empty GAE application. It is described [here](https://sites.google.com/site/gdevelopercodelabs/app-engine/creating-your-app-engine-account);
2. Download latest GAE SDK from [here](https://developers.google.com/appengine/downloads#Google_App_Engine_SDK_for_Java);
3. Unzip it to some directory;
3. Create envinronment variable `GAE_HOME` and set it`s value to path where GAE SDK was unzipped.

#### Git 
1. Download and install Git. I think you can use any Git you like. Git executable file is needed for build process.

#### Ant 
1. Download and install Ant from [here](http://ant.apache.org/bindownload.cgi).

#### Source
1. Checkout repo ... ;
2. Find `build.properties.template` in project directory and copy it to new `build.properties.local` file;
3. Open `build.properties.local` in text editor;
4. Fill `email`;
5. Fill `application.identifier`;
6. Fill `git.executable`;
7. You may leave `security.key`, `twitter.apiKey`,  `twitter.apiSecret` as is for now.
7. Save the changes.

#### Local run
1. Run `dev_server.bat`. Wait until server started;
2. You can test appliication by visit `locahost:8080` in your browser. Actually it is fully functional application but without cron jobs because they are not supported by GAE development server.
