/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.collaide.fileuploader.controllers;

import com.collaide.fileuploader.views.App;

/**
 *
 * @author leo
 */
public class AppController {
    
    private static final AppController instance = new AppController();
    private  App app;
    
    private AppController() {}
    
    public static AppController getInstance() {
        return instance;
    }
    
    public void signIn() {
        app.addGroupPanel();
    }

    public void setApp(App app) {
        this.app = app;
    }
}
