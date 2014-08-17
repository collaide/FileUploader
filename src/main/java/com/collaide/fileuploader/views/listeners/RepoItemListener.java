/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.collaide.fileuploader.views.listeners;

import com.collaide.fileuploader.models.notifications.ItemChanged;
import com.collaide.fileuploader.models.notifications.ItemDeleted;
import java.util.EventListener;

/**
 *
 * @author leo
 */
public interface RepoItemListener extends EventListener{
     
    public void itemChanged(ItemChanged item);
    public void itemDeleted(ItemDeleted item);
}
