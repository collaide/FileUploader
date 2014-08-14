/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.collaide.fileuploader.models.notifications;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author leo
 */
public class NotificationsMap {
    private final Map<Class, ArrayList> notifications = new HashMap<Class, ArrayList>();
    
    public ArrayList getArrayList(Class t) {
        return notifications.get(t);
    }
    
    public void addNotification(Object object) {
        Class klass = object.getClass();
        if(notifications.get(klass) == null) {
            notifications.put(klass, new ArrayList());
        }
        notifications.get(klass).add(object);
    }
}
