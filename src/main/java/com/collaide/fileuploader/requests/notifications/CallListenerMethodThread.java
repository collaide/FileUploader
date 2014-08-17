/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.collaide.fileuploader.requests.notifications;

import com.collaide.fileuploader.models.Model;
import com.collaide.fileuploader.views.listeners.RepoItemListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author leo
 */
public class CallListenerMethodThread extends Thread {

    private final Class notification;
    private final RepoItemListener listener;
    private final String json;
    private static final Logger logger = LogManager.getLogger(CallListenerMethodThread.class);

    public CallListenerMethodThread(Class notification, RepoItemListener listener, String json) {
        this.notification = notification;
        this.listener = listener;
        this.json = json;
    }
    
    
    
    @Override
    public void run() {
        try {
            String methodName = notification.getName();
            Method call = listener.getClass().getDeclaredMethod(methodName, notification);
            call.invoke(listener, Model.getJson(notification, json));
        } catch (IllegalAccessException ex) {
                logger.error("error while calling the event for the notification: " + ex);
            } catch (IllegalArgumentException ex) {
                logger.error("error while calling the event for the notification: " + ex);
            } catch (InvocationTargetException ex) {
                logger.error("error while calling the event for the notification: " + ex);
            } catch (NoSuchMethodException ex) {
                logger.error("error while calling the event for the notification: " + ex);
            } catch (SecurityException ex) {
                logger.error("error while calling the event for the notification: " + ex);
            }
    }
}
