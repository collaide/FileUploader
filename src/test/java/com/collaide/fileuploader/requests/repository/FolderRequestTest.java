/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.collaide.fileuploader.requests.repository;

import com.collaide.fileuploader.models.repositorty.RepoFolder;
import com.collaide.fileuploader.requests.UsersRequest;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.logging.Level;
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
    private final FolderRequest instance = new FolderRequest(3);

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
    public void testCreateWithName() {
        String name = getRandomString();
        
        try {
            RepoFolder result = instance.create(name);
            assertEquals(name, result.getName());
        } catch (FolderNotCreatedException ex) {
            logger.error("error while testing create: " + ex);
            fail("folder not created");
        }
    }

    @Test
    public void testCreateWithId() {
        try {
            int parentId = instance.create().getId();
            String name = getRandomString();
            assertEquals(name, instance.create(name, parentId).getName());
        } catch (FolderNotCreatedException ex) {
            fail("folder not created: " + ex);
        }
    }

    @Test
    public void testCreateWithoutNameAndId() {
        try {
            assertStartWith("Nouveau dossier", instance.create().getName());
        } catch (FolderNotCreatedException ex) {
            fail("folder not created: " + ex);
        }
    }

    public void assertContains(String contain, String subString) {
        assertNotNull(contain);
        assertNotNull(subString);
        assertTrue(contain.contains(subString));
    }

    public void assertStartWith(String startWith, String toTest) {
        assertTrue(toTest.startsWith(startWith));
    }
    
    public String getRandomString() {
        return new BigInteger(130, new SecureRandom()).toString(32);
    }
}
