/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.collaide.fileuploader.requests.repository;

import com.collaide.fileuploader.models.repositorty.RepoFolder;
import com.collaide.fileuploader.models.user.CurrentUser;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sun.jersey.api.client.ClientResponse;
import javax.ws.rs.core.MediaType;
import jdk.nashorn.internal.parser.JSONParser;

/**
 *
 * @author leo
 */
public class FolderRequest extends RepositoryRequest {

    public FolderRequest(int groupID) {
        super(groupID);
    }

    /**
     * Create a folder on a repository
     * <br/>
     *
     * @param name the name of the folder to create
     * @return RepoFolder infos about the folder created.
     * @throws
     * com.collaide.fileuploader.requests.repository.FolderNotCreatedException
     */
    public RepoFolder create(String name) throws FolderNotCreatedException {
        return create(name, 0);
    }
    
    public RepoFolder create() throws FolderNotCreatedException {
        return create(null, 0);
    }

    /**
     * create a folder on a repository, the specified id is the id of the parent
     * folder
     *
     * @param name name of the folder
     * @param id id of the parent folder
     * @return the created folder
     * @throws FolderNotCreatedException when the folder is not created
     */
    public RepoFolder create(String name, int id) throws FolderNotCreatedException {
        RepoFolder folder = new RepoFolder();
        folder.setName(name);
        folder.setId(id);
        ClientResponse response = request(getGroupUri() + "/folder?" + CurrentUser.getAuthParams()).
                type(MediaType.APPLICATION_JSON).
                post(ClientResponse.class, folder.toJson());
        if (response.getStatus() != 201) {
            throw new FolderNotCreatedException("The folder cannot be create: " + response.getStatus());
        }
        return RepoFolder.getJson(RepoFolder.class, response.getEntity(String.class));
    }
}
