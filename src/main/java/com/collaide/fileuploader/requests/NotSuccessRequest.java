/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.collaide.fileuploader.requests;

/**
 *
 * @author leo
 */
public class NotSuccessRequest extends Exception {

    public NotSuccessRequest(String string) {
        super(string);
    }
    
}
