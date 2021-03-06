/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.collaide.fileuploader.controllers;

import com.collaide.fileuploader.views.listeners.FileChangeListener;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import javax.swing.event.EventListenerList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author leo
 */
public class FilesSynchronization extends Thread {

    private WatchService watcher;
    private Path watchDir;
    private final Map<WatchKey, Path> keys = new HashMap<WatchKey, Path>();
    private boolean stopObserving;
    private EventListenerList events = new EventListenerList();
    private static final Logger logger = LogManager.getLogger(FilesSynchronization.class);

    public FilesSynchronization(String path) {
        stopObserving = false;
        try {
            watcher = FileSystems.getDefault().newWatchService();
            watchDir = FileSystems.getDefault().getPath(path);
        } catch (IOException ex) {
            logger.error("Error while starting file observation: " + ex);
        }
    }

    @Override
    public void run() {
        try {
            registerAll(watchDir);
        } catch (IOException ex) {
            logger.error(ex);
        }
        processEvents();
    }

    /**
     * Register the given directory with the WatchService
     */
    private void register(Path dir) throws IOException {
        if(dir.startsWith(".")) {
            return;
        }
        WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        keys.put(key, dir);
        logger.debug("registering " + dir.getFileName());
    }

    /**
     * Register the given directory, and all its sub-directories, with the
     * WatchService.
     */
    private void registerAll(final Path start) throws IOException {
        // register directory and sub-directories
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                register(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * Process all events for keys queued to the watcher
     */
    private void processEvents() {
        while (!stopObserving) {
            // wait for key to be signalled
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException x) {
                return;
            }
            Path dir = keys.get(key);
            if (dir == null) {
                logger.info("WatchKey not recognized!! " + key);
                continue;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind kind = event.kind();

                // TBD - provide example of how OVERFLOW event is handled
                if (kind == OVERFLOW) {
                    logger.info("Overflow for the dir " + dir);
                    continue;
                }

                // Context for directory entry event is the file name of entry
                WatchEvent<Path> ev = cast(event);
                Path name = ev.context();
                Path child = dir.resolve(name);

                // print out event
                for (FileChangeListener listener : getFileChangeListeners()) {
                    listener.fileChanged(event.kind(), child);
                    if (kind == ENTRY_CREATE) {
                        listener.fileCreated(child);
                    } else if (kind == ENTRY_MODIFY) {
                        listener.fileModified(child);
                    } else if (kind == ENTRY_DELETE) {
                        listener.fileDeleted(child);
                    }
                }
                // if directory is created, and watching recursively, then
                // register it and its sub-directories
                if (kind == ENTRY_CREATE) {
                    try {
                        if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
                            registerAll(child);
                        }
                    } catch (IOException x) {
                        // ignore to keep sample readbale
                    }
                }
            }

            // reset key and remove from set if directory no longer accessible
            boolean valid = key.reset();
            if (!valid) {
                keys.remove(key);

                // all directories are inaccessible
                if (keys.isEmpty()) {
                    break;
                }
            }
        }
    }

    private static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>) event;
    }

    public boolean isStopObserving() {
        return stopObserving;
    }

    public void setStopObserving(boolean stopObserving) {
        this.stopObserving = stopObserving;
    }

    public void addFileChangeListener(FileChangeListener listener) {
        events.add(FileChangeListener.class, listener);
    }

    public FileChangeListener[] getFileChangeListeners() {
        return events.getListeners(FileChangeListener.class);
    }
}
