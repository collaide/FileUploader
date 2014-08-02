/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.collaide.fileuploader.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author leo
 */
public class Model {

    protected static Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
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
