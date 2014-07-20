/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.collaide.fileuploader.models.repositorty;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author leo
 */
public class Repository {

    private Map<String, RepoFile> serverFiles = new HashMap<String, RepoFile>();
    private Map<String, RepoFolder> serverFolders = new HashMap<String, RepoFolder>();

    public Map<String, RepoFile> getServerFiles() {
        return serverFiles;
    }

    public void setServerFiles(Map<String, RepoFile> serverFiles) {
        this.serverFiles = serverFiles;
    }

    public Map<String, RepoFolder> getServerFolders() {
        return serverFolders;
    }

    public void setServerFolders(Map<String, RepoFolder> serverFolders) {
        this.serverFolders = serverFolders;
    }

}
