/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.collaide.fileuploader.views.listeners;

import java.nio.file.Path;
import java.nio.file.WatchEvent.Kind;
import java.util.EventListener;

/**
 *
 * @author leo
 */
public interface FileChangeListener extends EventListener{
    public void fileCreated(Path child);
    public void fileModified(Path child);
    public void fileDeleted(Path child);
    public void fileChanged(Kind<?> kind, Path child);
}
