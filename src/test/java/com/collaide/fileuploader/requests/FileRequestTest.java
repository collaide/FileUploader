/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.collaide.fileuploader.requests;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author leo
 */
public class FileRequestTest {

    private int countSendFile = 0;
    private final static Logger logger = Logger.getLogger(FileRequestTest.class);

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
        prepare();
        sendAFile(f);
        sendAFile(f);
        clean();
    }

    private void sendAFile(FilesRequest f) {
        try {
            System.out.println("Sending the File: " + countSendFile);
            countSendFile++;
            Files.copy(getTestFile().toPath(), getDestFile(countSendFile).toPath());
            f.create(getDestFile(countSendFile), 0);
        } catch (IOException ex) {
            logger.error("Error while copying", ex);
        }
    }

    private void prepare() {
        new File(getTestCopyDir()).mkdir();
    }

    private void clean() {
        File index = new File(getTestCopyDir());
        String[] entries = index.list();
        for (String s : entries) {
            File currentFile = new File(index.getPath(), s);
            currentFile.delete();
        }
    }

    private File getTestFile() {
        return new File(getTestFilePath() + ".pdf");
    }

    private File getDestFile(int count) {
        return new File(getTestCopyDir() + "/test" + String.valueOf(count) + ".pdf");
    }

    private String getTestFilePath() {
        return getTestFileDir() + "/test";
    }

    private String getTestFileDir() {
        return "/Users/leo/Desktop/test";
    }

    private String getTestCopyDir() {
        return getTestFileDir() + "/pdf";
    }
}
