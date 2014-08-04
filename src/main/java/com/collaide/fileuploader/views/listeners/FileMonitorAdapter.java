/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.collaide.fileuploader.views.listeners;

import com.collaide.fileuploader.models.GroupSync;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import javax.swing.Timer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author leo
 */
public abstract class FileMonitorAdapter implements FileChangeListener {

    public abstract void onChange(Path child);
    private static final int WAIT_DELAY = 5000;
    private Timer timer;
    private boolean eventsMissed = false;
    private static final Logger logger = LogManager.getLogger(FileMonitorAdapter.class);
    private final GroupSync groupSync;

    public FileMonitorAdapter(GroupSync groupSync) {
        this.groupSync = groupSync;
    }
    
    @Override
    public void fileCreated(final Path child) {
        fireOnChange(child);
    }

    @Override
    public void fileModified(Path child) {
        fireOnChange(child);
    }

    @Override
    public void fileDeleted(Path child) {
        if (timer == null || !timer.isRunning()) {
            timer = new Timer(WAIT_DELAY, null);
            timer.setRepeats(false);
            timer.start();
        }
    }

    @Override
    public void fileChanged(WatchEvent.Kind<?> kind, Path child) {
    }

    private synchronized void fireOnChange(final Path child) {
        logger.debug("entering to fireOnChange() for path: " + child.toFile().getAbsolutePath());
        if (timer != null && timer.isRunning()) {
            logger.debug("event missed");
            eventsMissed = true;
            return;
        }
        if (timer != null) {
            logger.debug(String.valueOf("is running" + timer.isRunning()) + " for: " + child.toFile().getName());
        } else {
            logger.debug(child.toFile().getName() + " is the first.");
        }
        logger.debug("event will be emitted in 5secs for: " + child.toFile().getName());
        timer = new Timer(WAIT_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onChange(child);
            }
        });
        timer.setRepeats(false);
        timer.start();
        logger.debug("end of fireOnChange() for: " + child.toFile().getName());
    }

    public boolean isEventsMissed() {
        boolean temp = eventsMissed;
        eventsMissed = false;
        return temp;
    }
}
