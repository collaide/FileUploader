/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.collaide.fileuploader.views.listeners;

import com.collaide.fileuploader.models.notifications.ItemChanged;
import com.collaide.fileuploader.models.notifications.ItemDeleted;

/**
 *
 * @author leo
 */
public interface RepoItemObserver {
 
    public void itemChanged(ItemChanged item);
    public void itemDeleted(ItemDeleted item);
}
