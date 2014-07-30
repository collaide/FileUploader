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
public class FilesRequest extends RepositoryRequest{

    private static final Logger logger = LogManager.getLogger(FilesRequest.class);
    private List<SendFileThread> sendFileList = new ArrayList<SendFileThread>();
    private CloseableHttpClient httpClient;
    private final int maxConnection;
    private int countFileToSend;

    public FilesRequest(int groupID) {
        super(groupID);
        this.maxConnection = 10;
    }

    public FilesRequest(int maxConnextion, int groupID) {
        super(groupID);
        this.maxConnection = maxConnextion;
    }

    /**
     * send a file on the server TODO: develop multi-threading TODO test it
     *
     * @param file the file to send
     * @param id the id of the folder (on the server) to send the file. If the
     * id is equal to zero, the file is send to the root repository
     */
    public void prepareForCreate(File file, int id) {
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
        sendFileList.add(new SendFileThread(httppost, getHttpClient()));
        if (countFileToSend >= getMaxConnection()) {
            create();
        }
        countFileToSend++;
    }

    /**
     * When required start uploading files to the server
     */
    public void create() {
        for (SendFileThread sendFileThread : sendFileList) {
            sendFileThread.start();
        }
        for (SendFileThread sendFileThread : sendFileList) {
            try {
                sendFileThread.join();
            } catch (InterruptedException ex) {
                logger.error("request for creatign a file interputpted " + ex);
            }
        }
        try {
            httpClient.close();
        } catch (IOException ex) {
            logger.error(ex);
        }
        countFileToSend = 0;
        httpClient = null;
        sendFileList = null;
    }
    

    public CloseableHttpClient getHttpClient() {
        if (httpClient == null) {
            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
            cm.setMaxTotal(getMaxConnection());
            return HttpClients.custom().setConnectionManager(cm).build();
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
}
