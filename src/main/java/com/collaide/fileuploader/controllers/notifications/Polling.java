/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.collaide.fileuploader.controllers.notifications;

import com.collaide.fileuploader.models.GroupSync;
import com.collaide.fileuploader.models.user.User;
import com.collaide.fileuploader.requests.notifications.RepoItemsNotificationRequest;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.Timer;

/**
 * TODO: addListener
 * @author leo
 */
public class Polling extends Sync{
    private final ArrayList<Timer> timersList = new ArrayList<Timer>();
    private final int TEN_MINUTES = 1000*60*10;

    public Polling(User user) {
        super(user);
    }
    
    @Override
    public void start() {
        //TODO for each group sync of a user
        for(;;) {
            final GroupSync groupSync = null;
            Calendar calendar = null;
            Timer timer = new Timer(TEN_MINUTES, new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    // TODO use calendar
                    new RepoItemsNotificationRequest(groupSync.getGroup().getId())
                            .getNotifications(user.getId());
                    // TODO for each notifications by type, fire corressponding event
                }
            });
            timer.setRepeats(true);
            timer.start();
            timersList.add(timer);
        }
    }
    
    /*
    * TODO: implement
    */
    @Override
    public void stop() {}
}
