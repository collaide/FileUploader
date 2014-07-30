/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.collaide.fileuploader.requests.repository;

import com.collaide.fileuploader.models.repositorty.RepoFolder;
import com.collaide.fileuploader.models.user.CurrentUser;

import com.sun.jersey.api.client.ClientResponse;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author leo
 */
public class FolderRequest extends RepositoryRequest {

    public FolderRequest(int groupID) {
        super(groupID);
    }

    /**
     * Create a folder on a repository TODO: finish implementation
     * <br/>
     * TODO: test it
     *
     * @param name the name of the folder to create
     * @return RepoFolder infos about the folder created.
     * @throws com.collaide.fileuploader.requests.repository.FolderNotCreatedException
     */
    public RepoFolder create(String name) throws FolderNotCreatedException {
        ClientResponse response = request(getGroupUri() + "/folder?" + CurrentUser.getAuthParams()).
                type(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON).
                post(ClientResponse.class);
        if (response.getStatus() != 204) {
            throw new FolderNotCreatedException("The folder cannot be create: " + response.getStatus());
        }
        return null;
    }

}
