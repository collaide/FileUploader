/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.collaide.fileuploader.requests;

import com.collaide.fileuploader.models.user.CurrentUser;
import com.collaide.fileuploader.models.Group;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.sun.jersey.api.client.ClientResponse;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author leo
 */
public class GroupsRequest {

    @SuppressWarnings("empty-statement")
    public static Group[] index() {
        ClientResponse response = Collaide.request("user/" + CurrentUser.getUser().getId() + "/groups?" + CurrentUser.getAuthParams())
                .type(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);
        if(response.getStatus() != 200) {
            return null;
        }
        String json = response.getEntity(String.class);
        Gson gson = new Gson();
        return gson.fromJson(new JsonParser().parse(json).getAsJsonObject().get("groups"), Group[].class);
    }

}
