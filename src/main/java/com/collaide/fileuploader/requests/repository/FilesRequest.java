/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.collaide.fileuploader.requests.repository;

import com.collaide.fileuploader.models.user.CurrentUser;
import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author leo
 */
public class FilesRequest extends RepositoryRequest {

    private static final Logger logger = LogManager.getLogger(FilesRequest.class);
    private List<SendFileThread> sendFileList;
    private CloseableHttpClient httpClient;
    private final int maxConnection;

    public FilesRequest(int groupID) {
        super(groupID);
        this.maxConnection = 10;
    }

    public FilesRequest(int maxConnextion, int groupID) {
        super(groupID);
        this.maxConnection = maxConnextion;
    }
    
    public void create(File file) {
        create(file, 0);
    }



    /**
     * send a file on the server
     *
     * @param file the file to send
     * @param id the id of the folder (on the server) to send the file. If the
     * id is equal to zero, the file is send to the root repository
     */
    public void create(File file, int id) {
        HttpPost httppost = getHttpPostForCreate();
        FileBody bin = new FileBody(file);
        MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create()
                .addPart("repo_file[file]", bin)
                .addTextBody("authenticity_token", CurrentUser.getUser().getCsrf());
        if (id != 0) {
            reqEntity.addTextBody("repo_file[id]", String.valueOf(id));
        }
        httppost.setEntity(reqEntity.build());
        httppost.setHeader("X-CSRF-Token", CurrentUser.getUser().getCsrf());
        SendFileThread sendFile = new SendFileThread(httppost, getHttpClient());
        sendFile.start();
        getSendFileList().add(sendFile);
        if (getSendFileList().size() >= getMaxConnection()) {
            terminate();
        }
    }

    /**
     * wait until that all create request are terminated and close the
     * connection
     *
     * @return <code>true</code> if all threads are terminated and the pool
     * ready for doing new erquests. <code>false</code> otherwise
     */
    public boolean terminate() {
        if (sendFileList == null || sendFileList.isEmpty()) {
            return false;
        }
        for (SendFileThread sendFileThread : sendFileList) {
            try {
                sendFileThread.join();
            } catch (InterruptedException ex) {
                logger.error("request for creatign a file interputpted " + ex);
            }
        }
        logger.debug("All threads are terminated");
        try {
            httpClient.close();
            httpClient = null;
            sendFileList = null;
        } catch (IOException ex) {
            logger.error(ex);
        }
        return true;
    }

    public CloseableHttpClient getHttpClient() {
        if (httpClient == null) {
            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
            cm.setMaxTotal(getMaxConnection());
            httpClient = HttpClients.custom().setConnectionManager(cm).build();
        }
        return httpClient;
    }

    private HttpPost getHttpPostForCreate() {
        return new HttpPost(getBase_uri() + getGroupUri() + "/file.json?"
                + CurrentUser.getAuthParams() + "&authenticity_token="
                + CurrentUser.getUser().getCsrf());

    }

    public int getMaxConnection() {
        return maxConnection;
    }

    private List<SendFileThread> getSendFileList() {
        if (sendFileList == null) {
            sendFileList = new ArrayList<SendFileThread>();
        }
        return sendFileList;
    }

    /**
     * test if all request are terminated. Blocking method
     *
     * @return <code>false</code> is all threads are terminated
     */
    public boolean hasActiveConnections() {
        if (sendFileList == null || sendFileList.isEmpty()) {
            return httpClient == null;
        }
        for (SendFileThread sendFileThread : sendFileList) {
            try {
                sendFileThread.join();
            } catch (InterruptedException ex) {
                logger.error(ex);
            }
        }
        return false;
    }
}
