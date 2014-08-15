/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.collaide.fileuploader.controllers;

import com.collaide.fileuploader.models.notifications.ItemChanged;
import com.collaide.fileuploader.models.notifications.ItemDeleted;
import com.collaide.fileuploader.views.listeners.RepoItemObserver;

/**
 * TODO: implement
 * <br/>
 * TODO think of a new way for organizing file synchronization (local and
 * server)
 *
 * @author leo
 */
public class LocalFileSynchronizer implements RepoItemObserver {

    @Override
    public void itemChanged(ItemChanged item) {
    }

    @Override
    public void itemDeleted(ItemDeleted item) {

    }

}
