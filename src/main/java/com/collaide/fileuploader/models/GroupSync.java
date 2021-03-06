/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.collaide.fileuploader.models;

import com.collaide.fileuploader.controllers.FilesSynchronization;
import com.collaide.fileuploader.controllers.LocalFileSynchronizer;
import com.collaide.fileuploader.controllers.notifications.Polling;
import com.collaide.fileuploader.controllers.notifications.ServerSynchronization;
import com.collaide.fileuploader.models.repositorty.RepoFile;
import com.collaide.fileuploader.models.repositorty.RepoFolder;
import com.collaide.fileuploader.models.repositorty.RepoItems;
import com.collaide.fileuploader.models.repositorty.Repository;
import com.collaide.fileuploader.models.user.CurrentUser;
import com.collaide.fileuploader.requests.repository.FilesRequest;
import com.collaide.fileuploader.requests.repository.FolderNotCreatedException;
import com.collaide.fileuploader.requests.repository.FolderRequest;
import com.collaide.fileuploader.requests.repository.RepositoryRequest;
import com.collaide.fileuploader.views.listeners.FileChangeListener;
import com.collaide.fileuploader.views.listeners.FileMonitorAdapter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author leo
 */
public class GroupSync implements Serializable {

    private Group group;
    private String path;
    private transient FilesSynchronization synchronization;
    private transient static final Logger logger = LogManager.getLogger(GroupSync.class);
    private transient RepositoryRequest repositoryRequest;
    private transient FilesRequest filesRequest;
    private transient FolderRequest folderRequest;
    private transient boolean isSyncronizing = false;
    private transient ServerSynchronization serverSynchronization;

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isSynchronized() {
        return path != null;
    }

    /**
     * Synchronize the folder indicate by the instance variable
     * <code>path</code>
     */
    public void synchronize() {
        isSyncronizing = true;
        repositoryRequest = new RepositoryRequest(group.getId());
        filesRequest = new FilesRequest(group.getId());
        folderRequest = new FolderRequest(group.getId());
        synchronizeALL(path, repositoryRequest.index(), null);
        filesRequest.terminate();
        isSyncronizing = false;
    }

    /**
     * Recursive function for synchronizing all elements and sub-elements of a
     * directory<br/>
     * TODO: do not sync .ds_store os x files maybe all .dotfiles?
     *
     * @param directoryToSync The directory to sync
     * @param serverRepo Files and folders present on the server for the
     * directory to sync
     * @param currentServerFolder The information about the directory on the
     * server side
     */
    public void synchronizeALL(String directoryToSync, Repository serverRepo, RepoFolder currentServerFolder) {
        try {
            logger.debug("Synchronizing the dir: " + directoryToSync);
            File folderToSync = new File(directoryToSync);
            Map<String, RepoFile> serverFiles = serverRepo.getServerFiles();
            Map<String, RepoFolder> serverFolders = serverRepo.getServerFolders();
            int repoId = 0;
            if (currentServerFolder != null) { // we are not on the root repository
                repoId = currentServerFolder.getId(); // id of the directory on the server
            }
            // sync items present on local with the server
            for (File itemToSync : folderToSync.listFiles()) {
                if (itemToSync.getName().startsWith(".")) {
                    continue;
                }
                if (itemToSync.isDirectory()) { // is a directory
                    try {
                        logger.debug(itemToSync.getName() + " is a dir. Synchronizing");
                        RepoFolder nextFolder;
                        if (serverFolders.containsKey(itemToSync.getName())) { // itemTo Sync existe sur le serveur
                            logger.debug(itemToSync.getName() + " exist on the server");
                            nextFolder = serverFolders.get(itemToSync.getName());
                        } else {// itemToSync n'existe pas sur le serveur
                            nextFolder = (repoId == 0
                                    ? folderRequest.create(itemToSync.getName())
                                    : folderRequest.create(itemToSync.getName(), repoId));
                            logger.debug("the id of the folder syncing is: " + repoId);
                            logger.debug(itemToSync.getName() + " has been created. " + nextFolder.getId());
                        }
                        synchronizeALL(itemToSync.getPath(), repositoryRequest.get(nextFolder.getId()), nextFolder);
                    } catch (FolderNotCreatedException ex) {
                        logger.error("unable to create and sync the folder: "
                                + itemToSync.getAbsolutePath()
                                + " exception: " + ex);
                    }
                    serverFolders.remove(itemToSync.getName()); // le dossier existe en local. On l'enlève de la liste à synchronizer.
                } else { // is a file
                    logger.debug(itemToSync.getName() + " is a file.");
                    FileInputStream fis = new FileInputStream(itemToSync);
                    String md5 = DigestUtils.md5Hex(fis);
                    if (!serverFiles.containsKey(md5 + itemToSync.getName())) { // le fichier local n'est pas sur le serveur
                        logger.debug(itemToSync.getName() + " is not on the server. " + md5);
                        filesRequest.create(itemToSync, repoId); // le fichier local est mnt sur le serveur
                        logger.debug(itemToSync.getName() + " is created.");
                    } else {
                        if (!serverFiles.get(md5 + itemToSync.getName()).getName().
                                equals(itemToSync.getName())) { // le fichier local est présent sur le serveur avec le même md5 mais pas le même nom.
                            filesRequest.create(itemToSync, repoId); // on synchronize
                        }
                        serverFiles.remove(md5 + itemToSync.getName()); // le fichier existe en local. On l'enlève de la liste à synchronizer.
                    }
                    fis.close();
                }
            }
            iterateThroughServerItems(serverFiles, directoryToSync); // sync files present on server but not on local
            iterateThroughServerItems(serverFolders, directoryToSync); // sync folders with content present on server but not on local
        } catch (IOException ex) {
            logger.error("Error with a file: ", ex);

        }
    }

    private void iterateThroughServerItems(Map map, String folder) throws IOException {
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            RepoItems serverItem = (RepoItems) pairs.getValue();
            repositoryRequest.download(serverItem.getDownload(), folder);
            logger.debug(serverItem.getName() + " is saved on disk. " + folder);
            it.remove(); // avoids a ConcurrentModificationException
        }
    }

    public void startObserving() {
        if (synchronization == null) {
            synchronization = new FilesSynchronization(path);
            synchronization.start();
            addListeners(synchronization);
        }
        if (serverSynchronization != null) {
            return;
        }
        serverSynchronization = new Polling(this, CurrentUser.getUser().getId());
        serverSynchronization.addRepoItemListener(new LocalFileSynchronizer(path, group.getId()));
        serverSynchronization.start();
    }

    public void stopObserving() {
        if (synchronization != null) {
            synchronization.setStopObserving(true);
            synchronization = null;
        }
        if(serverSynchronization != null) {
            serverSynchronization = null;
            serverSynchronization.stop();
        }
    }

    private void addListeners(FilesSynchronization sync) {
        sync.addFileChangeListener(new FileMonitorAdapter(this) {

            @Override
            public void onChange(Path child) {
                logger.debug(child.toFile().getAbsoluteFile() + " is changed. Synchronizing");
                synchronize();
                if (this.isEventsMissed()) {
                    synchronize();
                }
            }
        });
    }

    public boolean isSyncronizing() {
        return isSyncronizing;
    }

}
