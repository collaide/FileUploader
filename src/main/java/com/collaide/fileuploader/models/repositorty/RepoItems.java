/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.collaide.fileuploader.models.repositorty;

import com.collaide.fileuploader.models.Model;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

/**
 *
 * @author leo
 */
public class RepoItems extends Model{
  
    @Expose
    private String name = null;
    @Expose
    private String md5 = null;
    @Expose
    private int id = 0;
    @Expose(serialize = false, deserialize = false)
    private boolean is_folder;
    @Expose
    private String download = null;

    public RepoItems() {
        gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {

            @Override
            public boolean shouldSkipField(FieldAttributes fa) {
                return fa.getName().equals("id") && id == -1;
            }

            @Override
            public boolean shouldSkipClass(Class<?> type) {
                return false;
            }
        }).create();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isIs_folder() {
        return is_folder;
    }

    public void setIs_folder(boolean is_folder) {
        this.is_folder = is_folder;
    }

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }
    
    
}
