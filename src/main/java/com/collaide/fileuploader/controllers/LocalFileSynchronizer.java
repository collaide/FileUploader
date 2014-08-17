/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.collaide.fileuploader.controllers;

import com.collaide.fileuploader.models.notifications.ItemChanged;
import com.collaide.fileuploader.models.notifications.ItemDeleted;
import com.collaide.fileuploader.requests.repository.RepositoryRequest;
import com.collaide.fileuploader.views.listeners.RepoItemListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * TODO: implement
 * <br/>
 * TODO think of a new way for organizing file synchronization (local and
 * server)
 *
 * @author leo
 */
public class LocalFileSynchronizer implements RepoItemListener {

    private final String path;
    private final RepositoryRequest repositoryRequest;
    
    public LocalFileSynchronizer(String path, int groupId) {
        this.path = path;
        repositoryRequest = new RepositoryRequest(groupId);
    }

    @Override
    public void itemChanged(ItemChanged item) {
        try {
            repositoryRequest.download(item.getNotifierId(), path + item.getFromPath());
        } catch (IOException ex) {
            LogManager.getLogger(LocalFileSynchronizer.class).error("cannot save file : " + ex);
        }
    }

    @Override
    public void itemDeleted(ItemDeleted item) {
        try {
            //TODO: delete folder recursively
            Files.deleteIfExists(new File(item.getFromPath()).toPath());
        } catch (IOException ex) {
            Logger.getLogger(LocalFileSynchronizer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
