/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.collaide.fileuploader.models;

import java.io.Serializable;

/**
 *
 * @author leo
 */
public class GroupSync implements Serializable{
    private Group group;
    private String path;

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    
    public boolean isSynchronized() {
        if (path == null) {
            return false;
        }
        return true;
    }
    
    public void synchronize() {}
}