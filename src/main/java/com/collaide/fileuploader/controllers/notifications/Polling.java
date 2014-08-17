/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.collaide.fileuploader.controllers.notifications;

import com.collaide.fileuploader.models.Group;
import com.collaide.fileuploader.models.GroupSync;
import com.collaide.fileuploader.models.notifications.NotificationsMap;
import com.collaide.fileuploader.models.repositorty.RepoItems;
import com.collaide.fileuploader.models.user.User;
import com.collaide.fileuploader.requests.notifications.RepoItemsNotificationRequest;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import javax.swing.Timer;

/**
 *
 * @author leo
 */
public class Polling extends ServerSynchronization {

    private final int TEN_MINUTES = 1000 * 60 * 10;
    private Timer timer;

    public Polling(GroupSync groupSync, int userId) {
        super(groupSync, userId);
    }

    @Override
    public void start() {
        System.currentTimeMillis();
        if (timer != null) {
            return;
        }
        timer = new Timer(TEN_MINUTES, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO use calendar
                request.notificationWithListener(userId, null, listenersToArray());
            }
        });
        timer.setRepeats(true);
        timer.start();
    }

    /*
     */
    @Override
    public void stop() {
        if (timer != null) {
            timer = null;
            timer.stop();
        }
    }
}
