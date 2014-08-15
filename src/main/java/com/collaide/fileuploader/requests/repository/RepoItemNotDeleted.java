/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.collaide.fileuploader.requests.repository;

/**
 *
 * @author leo
 */
public class RepoItemNotDeleted extends Exception {

    public RepoItemNotDeleted(int status) {
        super(Integer.toString(status));
    }
    
}
