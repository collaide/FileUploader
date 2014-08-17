/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.collaide.fileuploader.controllers.notifications;

import com.collaide.fileuploader.models.GroupSync;
import com.collaide.fileuploader.requests.notifications.RepoItemsNotificationRequest;
import com.collaide.fileuploader.views.listeners.RepoItemListener;
import javax.swing.event.EventListenerList;

/**
 *
 * @author leo
 */
public abstract class ServerSynchronization {
    protected final GroupSync groupSync;
    protected final int userId;
    protected final RepoItemsNotificationRequest request;
    private final EventListenerList listeners = new EventListenerList();

    public ServerSynchronization(GroupSync groupSync, int userId) {
        this.groupSync = groupSync;
        this.userId = userId;
        request = new RepoItemsNotificationRequest(this.groupSync.getGroup().getId());
    }
    
    public abstract void start();
    public abstract void stop();
    
    public void addRepoItemListener(RepoItemListener listener) {
        listeners.add(RepoItemListener.class, listener);
    }
    
    protected RepoItemListener[] listenersToArray() {
        return listeners.getListeners(RepoItemListener.class);
    }
}
