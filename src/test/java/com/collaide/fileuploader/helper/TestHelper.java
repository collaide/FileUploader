/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.collaide.fileuploader.helper;

import com.collaide.fileuploader.requests.UsersRequest;
import com.collaide.fileuploader.requests.repository.FilesRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.SecureRandom;
import java.util.logging.Level;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author leo
 * @param <TypeForLogger>
 */
public class TestHelper extends CustomAssert {

    protected final Logger logger = LogManager.getLogger(this.getClass());
    protected final int ADMIN_GROUP_ID = 3;

    public static void beforeClass() {
        UsersRequest.signIn("admin@example.com", "password");
        prepare();
    }

    public static void afterClass() {
        clean();
    }

    protected static File getTestFile() {
        return new File(getTestFilePath() + ".pdf");
    }

    protected static File getDestFile() {
        return getDestFile(null);
    }

    protected static File getDestFile(String name) {
        if (name == null) {
            name = getRandomString();
        }
        return new File(getTestCopyDir() + "/test" + name + ".pdf");
    }

    private static void prepare() {
        new File(getTestCopyDir()).mkdir();
    }

    protected static String getRandomString() {
        return new BigInteger(130, new SecureRandom()).toString(32);
    }

    protected File createNewFile() {
        return createNewFile(null);
    }

    protected File createNewFile(String name) {
        try {
            return Files.copy(getTestFile().toPath(), getDestFile(name).toPath()).toFile();
        } catch (IOException ex) {
            logger.error("Error while copying", ex);
        }
        return null;
    }

    protected String getMd5(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            return DigestUtils.md5Hex(fis);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(TestHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private static void clean() {
        File index = new File(getTestCopyDir());
        String[] entries = index.list();
        for (String s : entries) {
            File currentFile = new File(index.getPath(), s);
            currentFile.delete();
        }
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
