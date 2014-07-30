/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.collaide.fileuploader.requests.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author leo
 */
public class FileUploadPool extends Thread{
    private List<SendFileThread> fileUploadThreadList;
    private CloseableHttpClient httpClient;
    private static final Logger logger = LogManager.getLogger();

    public FileUploadPool(ArrayList<SendFileThread> fileUploadThread, CloseableHttpClient httpClient) {
        this.fileUploadThreadList = fileUploadThread;
        this.httpClient = httpClient;
    }
    
    @Override
    public void run() {
        for (SendFileThread sendFileThread : fileUploadThreadList) {
            sendFileThread.start();
        }
        for (SendFileThread sendFileThread : fileUploadThreadList) {
            try {
                sendFileThread.join();
            } catch (InterruptedException ex) {
                logger.error("request for creatign a file interputpted " + ex);
            }
        }
        try {
            httpClient.close();
            httpClient = null;
        } catch (IOException ex) {
            logger.error(ex);
        }
    }
}
