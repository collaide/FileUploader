FileUploader
============

Collaide app for synchronizing files and folders

# Contibute

1. Install maven
2. run ` maven install package exec:java  `
3. If you are not under mac os x comment the plugin ` os-app-bundle ` in pom.xml
4. code it !
5. push

# features

* sign in to collaide
* change panel

# Work in progress !

# Organization

* Follow the mvc pattern
* views designed with netbeans
* controllers do request to collaide
* models handles datas to send and retreived from collaide
* views call controllers methods and use the datas
* custom listener are used to know when to change the view in the main frame
