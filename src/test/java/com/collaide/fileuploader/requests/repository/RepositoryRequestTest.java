/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.collaide.fileuploader.requests.repository;

import com.collaide.fileuploader.helper.TestHelper;
import com.collaide.fileuploader.models.repositorty.RepoFile;
import com.collaide.fileuploader.models.repositorty.RepoFolder;
import com.collaide.fileuploader.models.repositorty.Repository;
import com.collaide.fileuploader.models.user.CurrentUser;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;
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
        assertEquals(repo.getServerFiles().get(md5 + fileToSend.getName()).getMd5(), md5);
        assertEquals(repo.getServerFolders().get(folderName).getName(), folderName);
    }

    /**
     * Test of get method, of class RepositoryRequest.
     *
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
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testDownloadAFile() throws Exception {
        File fileToSend = createNewFile();
        filesRequest.create(fileToSend);
        filesRequest.terminate();
        RepoFile fileServer = getFile(fileToSend);
        if (fileServer == null) {
            fail("the created file is not found on the server");
        }
        String downloaded = repositoryRequest.download(fileServer.getDownload(), getTestDownloadDir());
        assertNotNull(downloaded);
        File fileDownloaded = new File(downloaded);
        assertTrue(fileDownloaded.exists());
        assertEquals(fileToSend.getName(), fileDownloaded.getName());
    }
    
    @Test
    public void testDownloadAFolder() throws Exception {
        String folderName = getRandomString();
        String subFolderName = getRandomString();
        File firstFile = createNewFile();
        File secondFile = createNewFile();
        RepoFolder mainFolder = folderRequest.create(folderName);
        int folderId = mainFolder.getId();
        int subFolderId = folderRequest.create(subFolderName, folderId).getId();
        filesRequest.create(firstFile, folderId);
        filesRequest.create(secondFile, subFolderId);
        filesRequest.terminate();
        String download = repositoryRequest.download(
                mainFolder.getDownload(), getTestDownloadDir()
        );
        assertNotNull(download);
        File downloadedFile = new File(download);
        assertDirectoryExist(downloadedFile);
        File subDir = new File(downloadedFile, subFolderName);
        assertDirectoryExist(subDir);
    }

    @Test
    public void testGetFileName() throws Exception {
        File fileToSend = createNewFile();
        filesRequest.create(fileToSend);
        filesRequest.terminate();
        ClientResponse response = Client.create()
                .resource(
                        getFile(fileToSend).getDownload() + "?" + CurrentUser.getAuthParams()
                ).accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);
        Method getFileName = invokePrivateMethod(
                repositoryRequest.getClass(), "getFileName", ClientResponse.class
        );
        try {
            String expectedName = (String) getFileName.invoke(repositoryRequest, response);
            assertEquals(fileToSend.getName(), expectedName);
        } catch (InvocationTargetException ex) {
            logger.error("error while invoking getFileName: " + ex.getCause());
            fail("invocation error");
        }
    }

    private RepoFile getFile(File f) {
        return repositoryRequest.
                index().
                getServerFiles().
                get(getMd5(f) + f.getName());
    }
}
