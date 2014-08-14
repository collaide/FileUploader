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
public class Notification {
    
    private String notifierType;
    private String type;

    public String getNotifierType() {
        return notifierType;
    }

    public void setNotifierType(String notifierType) {
        this.notifierType = notifierType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    
}
