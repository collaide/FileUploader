/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.collaide.fileuploader.models;

import java.io.File;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author leo
 */
public class UserTest {
    
    public UserTest() {
    }

    /**
     * Test of getpersonalDataFile method, of class User.
     */
    @Test
    public void testRetrivePersonalDatas() {
        System.out.println("retrivePersonalData");
        User instance = new User(null, null);
        instance.setId(1);
        instance.retrivePersonalData();
        assertEquals(instance.getGroupSyncList().size(), 0);
        Group group  = new Group();
        group.setName("test");
        GroupSync gs = new GroupSync();
        gs.setGroup(group);
        gs.setPath("/test");
        instance.getGroupSyncList().addGroupSync(gs);
        instance.savePersonalData();
        assertEquals(instance.getpersonalDataFile().exists(), true);
        User u2 = new User(null, null);
        u2.setId(1);
        u2.retrivePersonalData();
        assertEquals(instance.getGroupSyncList().size(), 1);
    }
    
}
