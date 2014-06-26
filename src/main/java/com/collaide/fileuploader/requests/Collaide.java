/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.collaide.fileuploader.requests;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

/**
 *
 * @author leo
 */
public class Collaide {
    
    private static final String lang = "api/";
    private static final String base_uri = "http://0.0.0.0:3000/" + lang;
    
    public static WebResource.Builder request(String uri) {
        Client client = Client.create();
        WebResource ressource = client.resource(base_uri + uri);
        return ressource.accept("application/json");
    }
}
