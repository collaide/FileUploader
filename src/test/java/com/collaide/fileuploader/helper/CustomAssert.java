/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.collaide.fileuploader.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author leo
 */
public class CustomAssert {
    
    protected void assertContains(String contain, String subString) {
        assertNotNull(contain);
        assertNotNull(subString);
        assertTrue(contain.contains(subString));
    }

    protected void assertStartWith(String startWith, String toTest) {
        assertTrue(toTest.startsWith(startWith));
    }
    
}
