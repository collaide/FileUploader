/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.collaide.fileuploader.requests.repository;

import com.collaide.fileuploader.models.repositorty.RepoFolder;
import com.collaide.fileuploader.requests.UsersRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author leo
 */
public class FolderRequestTest {
    
    private static final Logger logger = LogManager.getLogger(FolderRequestTest.class);
    
    public FolderRequestTest() {
    }
    
    @BeforeClass
    public static void beforeClass() {
        UsersRequest.signIn("admin@example.com", "password");
    }

    /**
     * Test of create method, of class FolderRequest.
     */
    @Test
    public void testCreate() {
        String name = "test";
        FolderRequest instance = new FolderRequest(3);
        RepoFolder expResult = new RepoFolder();
        expResult.setName(name);
        RepoFolder result = null;
        try {
            result = instance.create(name);
            assertEquals(expResult.getName(), result.getName());
        } catch (FolderNotCreatedException ex) {
            logger.error("error while testing create: " + ex);
            fail("folder not created");
        }
        
        // TODO review the generated test code and remove the default call to fail.
        
    }
    
}
