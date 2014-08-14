/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.collaide.fileuploader.models.notifications;

/**
 *
 * @author leo
 */
public class ItemChanged extends Notification{
    
    private int notifierId;
    private String fromPath;

    public int getNotifierId() {
        return notifierId;
    }

    public void setNotifierId(int notifierId) {
        this.notifierId = notifierId;
    }

    public String getFromPath() {
        return fromPath;
    }

    public void setFromPath(String fromPath) {
        this.fromPath = fromPath;
    }
    
    
}
