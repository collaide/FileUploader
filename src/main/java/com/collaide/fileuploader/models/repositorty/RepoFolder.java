/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.collaide.fileuploader.models.repositorty;

import com.google.gson.JsonObject;

/**
 *
 * @author leo
 */

public class RepoFolder extends RepoItems{
        
    @Override
    public String toJson() {
        JsonObject repoFolderJson = new JsonObject();
        repoFolderJson.add("repo_folder", gson.toJsonTree(this));
        return repoFolderJson.toString();
    }
    
    
}
