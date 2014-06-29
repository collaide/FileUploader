/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.collaide.fileuploader.models;

import java.util.HashMap;

/**
 *
 * @author leo
 */
public class GroupSyncList{
    private final HashMap<Integer, GroupSync> groupSyncs = new HashMap<Integer, GroupSync>();
    
    public void addGroupSync(GroupSync groupSync) {
        groupSyncs.put(groupSync.getGroup().getId(), groupSync);
    }
}
