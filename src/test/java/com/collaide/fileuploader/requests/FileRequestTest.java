/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.collaide.fileuploader.requests;

import com.collaide.fileuploader.requests.repository.FilesRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author leo
 */
public class FileRequestTest {

    private static int countSendFile = 0;
    private static FilesRequest f;
    private final static Logger logger = LogManager.getLogger(FileRequestTest.class);

    public FileRequestTest() {
    }

    @BeforeClass
    public static void beforeClass() {
        UsersRequest.signIn("admin@example.com", "password");
        prepare();
    }

    @Before
    public void beforeTest() {
        f = new FilesRequest(3);
    }

    @AfterClass
    public static void afterClass() {
        clean();
        countSendFile = 0;
    }

    @Test
    public void sendTwoFiles() {
        sendAFile(f);
        sendAFile(f);
        logger.debug("will call terminate() when all files are uploaded");
        assertEquals(f.terminate(), true);
        sendAFile(f);
        assertEquals(f.terminate(), true);
        logger.debug("terminate() called");
    }

    @Ignore
    @Test
    public void sendALotOfFilesWithDefaultParams() {
        for (int i = 0; i < 20; i++) {
            sendAFile(f);
        }
        assertEquals(f.hasActiveConnections(), false);
    }

    @Ignore("Think after that terminate() is called. Assert will be true")
    @Test
    public void sendALotOfFilesWithMAxConnections20() {
        f = new FilesRequest(22, 3);
        for (int i = 0; i < 20; i++) {
            sendAFile(f);
        }
        assertEquals(f.hasActiveConnections(), false);
    }

    private void sendAFile(FilesRequest f) {
        try {
            logger.debug("Sending the file: " + countSendFile);
            countSendFile++;
            Files.copy(getTestFile().toPath(), getDestFile(countSendFile).toPath());
            f.create(getDestFile(countSendFile), 0);
        } catch (IOException ex) {
            logger.error("Error while copying", ex);
        }
    }

    private static void prepare() {
        new File(getTestCopyDir()).mkdir();
    }

    private static void clean() {
        File index = new File(getTestCopyDir());
        String[] entries = index.list();
        for (String s : entries) {
            File currentFile = new File(index.getPath(), s);
            currentFile.delete();
        }
    }

    private static File getTestFile() {
        return new File(getTestFilePath() + ".pdf");
    }

    private static File getDestFile(int count) {
        return new File(getTestCopyDir() + "/test" + String.valueOf(count) + ".pdf");
    }

    private static String getTestFilePath() {
        return getTestFileDir() + "/test";
    }

    private static String getTestFileDir() {
        return "/Users/leo/Desktop/test";
    }

    private static String getTestCopyDir() {
        return getTestFileDir() + "/pdf";
    }
}
