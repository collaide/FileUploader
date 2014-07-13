/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.collaide.fileuploader.models;

import com.collaide.fileuploader.controllers.FilesSynchronization;
import com.collaide.fileuploader.views.listeners.FileChangeListener;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.WatchEvent;

/**
 *
 * @author leo
 */
public class GroupSync implements Serializable {

    private Group group;
    private String path;
    private transient FilesSynchronization synchronization;
    

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
    }

    public void startObserving() {
        if(synchronization == null) {
            synchronization = new FilesSynchronization(path);
            synchronization.start();
            addListeners(synchronization);
        }
    }

    public void stopObserving() {
        if(synchronization != null) {
            synchronization.setStopObserving(true);
            synchronization = null;
        }
    }
    
    private void addListeners(FilesSynchronization sync) {
        sync.addFileChangeListener(new FileChangeListener() {

            @Override
            public void fileCreated(Path child) {
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
