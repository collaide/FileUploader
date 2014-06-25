/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.collaide.fileuploader.controllers;

import com.collaide.fileuploader.models.CurrentUser;
import com.collaide.fileuploader.models.Group;
import com.sun.jersey.api.client.ClientResponse;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author leo
 */
public class GroupsController {
    
    public Group[] index() {
        ClientResponse response = Collaide.request(CurrentUser.getUser().getId() + "/groups")
                .type(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);
        return null;
    }
    
}
