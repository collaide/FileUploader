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
class Folder {
    private String computerPath;
    private ArrayList<RepoFolder> repoFolders = new ArrayList<RepoFolder>();

    public String getPath() {
        return computerPath;
    }

    public void setPath(String path) {
        this.computerPath = path;
    }
    
    
}
