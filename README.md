FileUploader
============

Collaide app for synchronizing files and folders

## Work in progress !

### Contibute

1. Install maven (` brew install maven ` for mac os x)
2. fork it
3. If you are not under mac os x comment the plugin ` os-app-bundle ` in pom.xml
2. run ` mvn install package exec:java  `
4. code it !
5. submit pull request

### features

- [x] sign in to collaide
- [x] change pane
- [x] view infos about the repositories which the connected user can handle
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
