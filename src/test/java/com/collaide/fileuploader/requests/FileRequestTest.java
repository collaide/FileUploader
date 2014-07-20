/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.collaide.fileuploader.requests;

import java.io.File;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author leo
 */
public class FileRequestTest {
    
    public FileRequestTest() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    @Test
    public void sendFile() {
        UsersRequest.signIn("admin@example.com", "password");
        FilesRequest f = new FilesRequest(3);
        f.create(new File("/Users/leo/Desktop/test/test.pdf"), 0);
    }
}
