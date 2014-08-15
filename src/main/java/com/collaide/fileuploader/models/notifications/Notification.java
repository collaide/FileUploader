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
public class Notification {
    
    @Expose
    private String notifier_type;
    @Expose
    private String type;
    @Expose
    private String from_path;

    public String getNotifierType() {
        return notifier_type;
    }

    public void setNotifierType(String notifierType) {
        this.notifier_type = notifierType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFromPath() {
        return from_path;
    }

    public void setFromPath(String fromPath) {
        this.from_path = fromPath;
    }
    
    @Override
    public String toString() {
        return "type: " + getType() + "; notifierType: " + getNotifierType() + 
                "; fromPath: " + getFromPath();
    }
}
