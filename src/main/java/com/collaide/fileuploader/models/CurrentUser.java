/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.collaide.fileuploader.models;

/**
 *
 * @author leo
 */
public class CurrentUser {

    private final static CurrentUser instance = new CurrentUser();
    private User user;
    
    private CurrentUser() {}

    public static CurrentUser getInstance() {
        return instance;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public static User getUser() {
        return getInstance().user;
    }
}