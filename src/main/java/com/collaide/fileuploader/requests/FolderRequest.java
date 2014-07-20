/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.collaide.fileuploader.requests;

import com.collaide.fileuploader.models.repositorty.RepoFolder;

/**
 *
 * @author leo
 */
public class FolderRequest extends RepositoryRequest{
    
    public FolderRequest(int groupID) {
        super(groupID);
    }
    
    public static RepoFolder create(String name) {
        return null;
    }
    
}
