/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.collaide.fileuploader.models.notifications;

import com.collaide.fileuploader.models.repositorty.RepoItems;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
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
    
    public ArrayList[] toArray() {
        return (ArrayList[]) notifications.values().toArray();
    }
    
    public Iterator getIterator() {
        return notifications.entrySet().iterator();
    }
    
    public void addNotification(Object object) {
        Class klass = object.getClass();
        if(notifications.get(klass) == null) {
            notifications.put(klass, new ArrayList());
        }
        notifications.get(klass).add(object);
    }
    
    public <T>T getMostRecentNotification(Class klass) {
        ArrayList list = getArrayList(klass);
        return (T)klass.cast(list.get(list.size()-1));
    }
    
    /**
     * TODO debug it
     * @param <T>
     * @param klass
     * @param method
     * @param by
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException 
     */
    public <T>T getBy(Class klass, String method, Object by) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        for(Object item : getArrayList(klass)) {
            T notification = (T)klass.cast(item);
            if(klass.getDeclaredMethod(method, by.getClass()).invoke(notification).equals(by)) {
                return notification;
            }
        }
        return null;
    } 
}
