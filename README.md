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
- [x] display user's repos
- [x] select a repo to sync with a folder of the computer and remember the choice
- [x] observe changes on the selected folder
- [ ] push change to server
- [ ] synchronize file and folders observed with the selected repo
- [ ] enable notifications (polling)
- [ ] more ? (discussions, messages, etc)?

### Organization

* Follow the mvc pattern
* controllers do request to collaide.com/api
* models handles datas to send and retreived from collaide
* views call controllers methods and use the datas
