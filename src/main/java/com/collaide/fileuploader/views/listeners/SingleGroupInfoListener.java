/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.collaide.fileuploader.views.listeners;

import com.collaide.fileuploader.models.Group;
import com.collaide.fileuploader.views.SingleGroupInfoPanel;
import java.util.EventListener;

/**
 *
 * @author leo
 */
public interface SingleGroupInfoListener extends EventListener{
    
    public void modifyClicked(SingleGroupInfoPanel panel);
}
