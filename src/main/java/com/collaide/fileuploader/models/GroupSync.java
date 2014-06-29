/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.collaide.fileuploader.models;

import java.util.ArrayList;

/**
 *
 * @author leo
 */
public class GroupSync {
    private Group group;
    private ArrayList<Folder> folders = new ArrayList<Folder>();

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Folder[] getFolders() {
        return (Folder[]) folders.toArray();
    }

    public void setFolders(ArrayList<Folder> folders) {
        this.folders = folders;
    }
    
    public void addFolder(Folder folder) {
        folders.add(folder);
    }
    
    public int countFolders() {
        return folders.size();
    }
    
    
}
