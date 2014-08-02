/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.collaide.fileuploader.requests.repository;

import com.collaide.fileuploader.helper.TestHelper;
import com.collaide.fileuploader.models.repositorty.Repository;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author leo
 */
public class RepositoryRequestTest extends TestHelper{
    
    private final RepositoryRequest repositoryRequest = new RepositoryRequest(3);
    private final FolderRequest folderRequest = new FolderRequest(3);
    
    public RepositoryRequestTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        beforeClass();
    }
    
    @AfterClass
    public static void tearDownClass() {
        afterClass();
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of index method, of class RepositoryRequest.
     */
    @Test
    public void testIndex() {
//        RepositoryRequest instance = null;
//        Repository expResult = null;
//        Repository result = instance.index();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of get method, of class RepositoryRequest.
     */
    @Test
    public void testGet() {
        try {
            int idParent = folderRequest.create().getId();
            String folderName = getRandomString();
            int id = folderRequest.create(folderName, idParent).getId();
            Repository result = repositoryRequest.get(idParent);
            if(result == null) {
                fail("request not valid");
            }
            assertEquals(id, result.getServerFolders().get(folderName).getId());
            
        } catch (FolderNotCreatedException ex) {
            fail("error while creating a folder " + ex);
        }
    }

    /**
     * Test of download method, of class RepositoryRequest.
     */
    @Test
    public void testDownload() throws Exception {
//        System.out.println("download");
//        String url = "";
//        String folderToSave = "";
//        RepositoryRequest instance = null;
//        instance.download(url, folderToSave);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of getRepoItemUrl method, of class RepositoryRequest.
     */
    @Test
    public void testGetRepoItemUrl() {
//        System.out.println("getRepoItemUrl");
//        int id = 0;
//        RepositoryRequest instance = null;
//        String expResult = "";
//        String result = instance.getRepoItemUrl(id);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }
}
