/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.collaide.fileuploader.requests.notifications;

import com.collaide.fileuploader.models.notifications.FileCreated;
import com.collaide.fileuploader.models.notifications.Notification;
import com.collaide.fileuploader.models.user.CurrentUser;
import com.collaide.fileuploader.requests.repository.RepositoryRequest;
import com.google.gson.Gson;
import com.sun.jersey.api.client.ClientResponse;

/**
 *
 * @author leo
 */
public class RepoItemsNotificationRequest extends RepositoryRequest {
    
    private int groupId;
    
    public RepoItemsNotificationRequest(int groupId) {
        super(groupId);
    }
    
    public FileCreated[] getFileCreated(int userI) {
        ClientResponse response = request(uri + 
                "/" + CurrentUser.getUser().getId()+
                "/notify?event=file_created&" + 
                CurrentUser.getAuthParams()).get(ClientResponse.class);
        if(response.getStatus() != 200) {
            return null;
        }
        return new Gson().fromJson(response.getEntity(String.class), FileCreated[].class);
    }
}
