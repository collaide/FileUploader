/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.collaide.fileuploader.controllers.notifications;

import com.collaide.fileuploader.models.user.User;

/**
 *
 * @author leo
 */
public abstract class Sync {
    protected final User user;

    public Sync(User user) {
        this.user = user;
    }
    
    public abstract void start();
    public abstract void stop();
}
