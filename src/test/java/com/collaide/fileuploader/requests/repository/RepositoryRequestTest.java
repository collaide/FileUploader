/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.collaide.fileuploader.requests.repository;

import com.collaide.fileuploader.helper.TestHelper;
import com.collaide.fileuploader.models.repositorty.RepoFile;
import com.collaide.fileuploader.models.repositorty.Repository;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author leo
 */
public class RepositoryRequestTest extends TestHelper {

    private final RepositoryRequest repositoryRequest = new RepositoryRequest(ADMIN_GROUP_ID);
    private final FolderRequest folderRequest = new FolderRequest(ADMIN_GROUP_ID);
    private final FilesRequest filesRequest = new FilesRequest(ADMIN_GROUP_ID);

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
    public void testIndex() throws Exception {
        File fileToSend = createNewFile();
        filesRequest.create(fileToSend);
        filesRequest.terminate();
        String md5 = getMd5(fileToSend);

        String folderName = folderRequest.create().getName();
        if (folderName == null) {
            fail("name sould not be null");
        }
        Repository repo = repositoryRequest.index();
        assertEquals(repo.getServerFiles().get(md5).getMd5(), md5);
        assertEquals(repo.getServerFolders().get(folderName).getName(), folderName);
    }

    /**
     * Test of get method, of class RepositoryRequest.
     * @throws java.lang.Exception
     */
    @Test
    public void testGet() throws Exception {
        int idParent = folderRequest.create().getId();
        String folderName = getRandomString();
        int id = folderRequest.create(folderName, idParent).getId();
        Repository result = repositoryRequest.get(idParent);
        if (result == null) {
            fail("request not valid");
        }
        assertNotNull(result.getRepoItem());
        assertEquals(result.getRepoItem().getId(), idParent);
        assertEquals(id, result.getServerFolders().get(folderName).getId());
    }

    /**
     * Test of download method, of class RepositoryRequest.
     * @throws java.lang.Exception
     */
    @Test
    public void testDownloadAFile() throws Exception {
        String fileName = getRandomString();
        File fileToSend = createNewFile(fileName);
        filesRequest.create(fileToSend);
        filesRequest.terminate();
        RepoFile fileServer = repositoryRequest.
                index().
                getServerFiles().
                get(getMd5(fileToSend));
        if(fileServer == null) {
            fail("the created file is not found on the server");
        }
        String downloaded = repositoryRequest.download(fileServer.getDownload(), getTestDownloadDir());
        assertNotNull(downloaded);
        File fileDownloaded = new File(downloaded);
        assertTrue(fileDownloaded.exists());
        assertEquals(fileName+".pdf", fileDownloaded.getName());
    }
}
