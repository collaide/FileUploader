/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.collaide.fileuploader.models.notifications;

import com.google.gson.annotations.Expose;

/**
 *
 * @author leo
 */
public class ItemChanged extends Notification{
    
    @Expose
    private int notifier_id;

    public int getNotifierId() {
        return notifier_id;
    }

    public void setNotifierId(int notifierId) {
        this.notifier_id = notifierId;
    }
    
    
}
