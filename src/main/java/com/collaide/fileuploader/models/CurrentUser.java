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
    
    public static String getAuthParams() {
        return "user_email=" + getInstance().user.getEmail() + "&user_token=" + getInstance().user.getToken();
    }
    
    public static boolean isGroupSynchronized(int id) {
        return getUser() != null && getUser().getGroupSyncList() != null
                && getUser().getGroupSyncList().getGroupSync(id) != null
                && getUser().getGroupSyncList().getGroupSync(id).isSynchronized();
    }
}
