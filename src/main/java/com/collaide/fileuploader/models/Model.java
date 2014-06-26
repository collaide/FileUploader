/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.collaide.fileuploader.models;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 *
 * @author leo
 */
public class Model {

    private static final Gson gson = new Gson();
    private static final JsonParser parser = new JsonParser();
    
    public String toJson() {
        return gson.toJson(this);
    }
    
    public static <T>T getJson(Class type, String json) {
        JsonObject jsonObject = parser.parse(json).getAsJsonObject();
        if(jsonObject == null) {
            return null;
        }
        Object ret_value = gson.fromJson(jsonObject, type); 
        return (T) type.cast(ret_value);
    }
}
