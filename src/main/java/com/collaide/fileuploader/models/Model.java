/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.collaide.fileuploader.models;

import com.google.gson.Gson;

/**
 *
 * @author leo
 */
public class Model {

    public String toJson() {
        return new Gson().toJson(this);
    }
}
