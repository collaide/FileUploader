/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.collaide.fileuploader.models;

import com.collaide.fileuploader.controllers.FilesSynchronization;
import com.collaide.fileuploader.models.repositorty.RepoFile;
import com.collaide.fileuploader.models.repositorty.RepoFolder;
import com.collaide.fileuploader.models.repositorty.RepoItems;
import com.collaide.fileuploader.models.repositorty.Repository;
import com.collaide.fileuploader.requests.FilesRequest;
import com.collaide.fileuploader.requests.FolderRequest;
import com.collaide.fileuploader.requests.RepositoryRequest;
import com.collaide.fileuploader.views.listeners.FileChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author leo
 */
public class GroupSync implements Serializable {

    private Group group;
    private String path;
    private transient FilesSynchronization synchronization;
    private transient static final Logger logger = Logger.getLogger(GroupSync.class);
    private transient RepositoryRequest repositoryRequest;
    private transient FilesRequest filesRequest;

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

    public void synchronize() {
        repositoryRequest = new RepositoryRequest(group.getId());
        filesRequest = new FilesRequest(group.getId());
        synchronizeALL(path, repositoryRequest.index(), null);
    }

    public void synchronizeALL(String startPath, Repository serverRepo, RepoFolder currentServerFolder) {
        try {
            File folderToSync = new File(startPath);
            Map<String, RepoFile> serverFiles = serverRepo.getServerFiles();
            Map<String, RepoFolder> serverFolders = serverRepo.getServerFolders();
            int repoId = 0;
            if (currentServerFolder != null) {
                repoId = currentServerFolder.getId();
            }
            // sync items present on local with the server
            for (File fileMd5 : folderToSync.listFiles()) {
                if (fileMd5.isDirectory()) {
                    logger.debug(fileMd5.getName() + " is a dir. Synchronizing");
                    RepoFolder nextFolder;
                    if(!serverFolders.containsKey(fileMd5.getName())) {// dir n'existe pas sur le serveur
                        nextFolder = FolderRequest.create(fileMd5.getName());
                    } else { // existe sur le serveur
                        nextFolder = serverFolders.get(fileMd5.getName());
                    }
                    synchronizeALL(fileMd5.getPath(), repositoryRequest.get(nextFolder.getId()), nextFolder);
                } else {
                    FileInputStream fis = new FileInputStream(fileMd5);
                    String md5 = DigestUtils.md5Hex(fis);
                    if (!serverFiles.containsKey(md5)) { // le fichier local n'est pas sur le serveur
                        filesRequest.create(fileMd5, repoId);
                        serverFiles.remove(md5);
                    } // le fichier local est mnt sur le serveur
                    fis.close();
                    logger.debug("digest for: " + fileMd5.getName() + " " + md5);
                }
            }
            iterateThroughServerItems(serverFiles, startPath); // sync files present on server but not on local
            iterateThroughServerItems(serverFolders, startPath); // sync folder with content present on server but not on local
        } catch (IOException ex) {
            logger.error("Error while generating checksum: ", ex);

        }
    }

    private void iterateThroughServerItems(Map map, String folder) throws IOException {
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            RepoItems serverFile = (RepoItems)pairs.getValue();
            repositoryRequest.download(serverFile.getDownload(), folder);
            it.remove(); // avoids a ConcurrentModificationException
        }
    }
    public void startObserving() {
        if (synchronization == null) {
            synchronization = new FilesSynchronization(path);
            synchronization.start();
            addListeners(synchronization);
        }
    }

    public void stopObserving() {
        if (synchronization != null) {
            synchronization.setStopObserving(true);
            synchronization = null;
        }
    }

    private void addListeners(FilesSynchronization sync) {
        sync.addFileChangeListener(new FileChangeListener() {

            @Override
            public void fileCreated(Path child) {
                logger.debug("File created");
            }

            @Override
            public void fileModified(Path child) {
            }

            @Override
            public void fileDeleted(Path child) {
            }

            @Override
            public void fileChanged(WatchEvent.Kind<?> kind, Path child) {
            }
        });
    }
}
