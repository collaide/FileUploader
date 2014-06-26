/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.collaide.fileuploader.requests;

import com.collaide.fileuploader.models.CurrentUser;
import com.collaide.fileuploader.models.User;
import com.google.gson.JsonParser;
import com.sun.jersey.api.client.ClientResponse;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author leo
 */
public class UsersRequest {

    public static boolean signIn(String email, String password) {
        User user = new User(email, password);
        ClientResponse response = Collaide.request("auth_token")
                .type(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .post(ClientResponse.class, user.toJson());
        if(response.getStatus() == 200) {
            User currentUser = (User) User.getJson(User.class, response.getEntity(String.class));
            CurrentUser.getInstance().setUser(currentUser);
        }
        return response.getStatus() == 200;
    }
}
