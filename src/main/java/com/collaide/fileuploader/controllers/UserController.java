/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.collaide.fileuploader.controllers;

import com.collaide.fileuploader.models.User;
import com.sun.jersey.api.client.ClientResponse;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author leo
 */
public class UserController {

    public boolean signIn(String email, String password) {
        User user = new User(email, password);
        ClientResponse response = Collaide.request("auth_token")
                .type(MediaType.APPLICATION_JSON)
                .post(ClientResponse.class, user.toJson());
        return response.getStatus() == 200;
    }
}
