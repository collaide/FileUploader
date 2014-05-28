FileUploader
============

Collaide app for synchronizing files and folders

## Work in progress !

### Contibute

1. Install maven
2. run ` maven install package exec:java  `
3. If you are not under mac os x comment the plugin ` os-app-bundle ` in pom.xml
4. code it !
5. push

### features

- [x] sign in to collaide
- [x] change pane
- [ ] view repositories on collaide of the user
- [ ] select one repo
- [ ] select files and folders to observes
- [ ] synchronize file and folders observed with the selected repo
- [ ] enable notifications (maybe with a server in the app, so collaide can send POST notifications to the registred apps ?)
- [ ] more ? (discussions, messages, etc)?

### Organization

* Follow the mvc pattern
* views designed with netbeans
* controllers do request to collaide
* models handles datas to send and retreived from collaide
* views call controllers methods and use the datas
* custom listener are used to know when to change the view in the main frame
