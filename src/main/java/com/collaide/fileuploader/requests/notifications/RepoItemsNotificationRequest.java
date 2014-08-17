/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.collaide.fileuploader.requests.notifications;

import com.collaide.fileuploader.models.Model;
import com.collaide.fileuploader.models.notifications.Notification;
import com.collaide.fileuploader.models.notifications.NotificationsMap;
import com.collaide.fileuploader.models.user.CurrentUser;
import com.collaide.fileuploader.requests.Collaide;
import com.collaide.fileuploader.views.listeners.RepoItemListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.sun.jersey.api.client.ClientResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.EventListener;
import java.util.logging.Level;
import javax.swing.event.EventListenerList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author leo
 */
public class RepoItemsNotificationRequest extends Collaide {

    private final int groupId;
    private static final Gson gson = new Gson();
    private static final Logger logger = LogManager.getLogger(RepoItemsNotificationRequest.class);

    public RepoItemsNotificationRequest(int groupId) {
        this.groupId = groupId;
    }

    public void notificationWithListener(int userId, Calendar c, RepoItemListener[] listeners) {
        for (JsonElement notificationElement : getJsonNotifications(userId, c)) {
            try {
                Class notification = getClassFromString(
                        notificationElement.
                        getAsJsonObject().
                        get("type").
                        getAsString()
                );
                for (RepoItemListener listener : listeners) {
                    new CallListenerMethodThread(
                            notification, 
                            listener, 
                            notificationElement.toString()
                    ).start();
                }
            } catch (ClassNotFoundException ex) {
                logger.error("Notification cannot be found: " + ex);
            }
        }
    }

    /**
     * TODO implement
     * <br/>
     * get the notifications between the time passed in params and now
     *
     * @param userId
     * @param c
     * @return
     */
    public NotificationsMap getNotifications(int userId, Calendar c) {
        NotificationsMap notificationsMap = new NotificationsMap();
        for (JsonElement notificationElement : getJsonNotifications(userId, null)) {
            try {
                Class notification = getClassFromString(
                        notificationElement.
                        getAsJsonObject().
                        get("type").
                        getAsString()
                );
                logger.debug("json: " + notificationElement.toString());
                notificationsMap.addNotification(Model.getJson(notification, notificationElement.toString()));
            } catch (ClassNotFoundException ex) {
                logger.error("Notification cannot be found: " + ex);
                return null;
            }
        }
        return notificationsMap;
    }

    public NotificationsMap getNotifications(int userId) {
        return getNotifications(userId, null);
    }

    private String getNotificationType(String fullType) {
        String[] splitedType = fullType.split("::");
        return splitedType[splitedType.length - 1];
    }

    private JsonArray getJsonNotifications(int userId, Calendar c) {
        ClientResponse response = request(
                "user/" + String.valueOf(userId) + "/groups/"
                + String.valueOf(groupId) + "/notify?"
                + CurrentUser.getAuthParams()).get(ClientResponse.class);
        if (response.getStatus() != 200) {
            return null;
        }
        JsonElement notifications = getResponseToJsonElement(response);
        return notifications.getAsJsonArray();
    }

    private String toFullJavaClass(String type) {
        return Notification.class.getPackage().getName() + "." + getNotificationType(type);
    }

    private Class getClassFromString(String klass) throws ClassNotFoundException {
        return Class.forName(toFullJavaClass(klass));
    }
}
