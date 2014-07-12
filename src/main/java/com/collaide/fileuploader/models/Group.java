/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.collaide.fileuploader.models;

import java.io.Serializable;
import java.net.URI;

/**
 *
 * @author leo
 */
public class Group implements Serializable{
    private int id;
    private String name;
    private String uri;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
    
    public URI getUri() {
        return URI.create(uri);
    }
}
