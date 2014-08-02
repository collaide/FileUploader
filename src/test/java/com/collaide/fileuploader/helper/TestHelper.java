/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.collaide.fileuploader.helper;

import com.collaide.fileuploader.requests.UsersRequest;
import java.io.File;
import java.math.BigInteger;
import java.security.SecureRandom;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 *
 * @author leo
 */
public class TestHelper extends CustomAssert{
    
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

    protected static File getDestFile(int count) {
        return new File(getTestCopyDir() + "/test" + getRandomString() + ".pdf");
    }
    
    private static void prepare() {
        new File(getTestCopyDir()).mkdir();
    }
    
    protected static String getRandomString() {
        return new BigInteger(130, new SecureRandom()).toString(32);
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
