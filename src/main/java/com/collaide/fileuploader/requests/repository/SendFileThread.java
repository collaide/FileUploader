/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.collaide.fileuploader.requests.repository;

import com.collaide.fileuploader.models.user.CurrentUser;
import static com.collaide.fileuploader.requests.Collaide.getBase_uri;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HttpContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author leo
 */
public class SendFileThread extends Thread {

    private final HttpPost httpPost;
    private static final Logger logger = LogManager.getLogger(SendFileThread.class);
    private final CloseableHttpClient httpClient;
    private final HttpContext httpContext;

    public SendFileThread(HttpPost httpPost, CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
        this.httpPost = httpPost;
        this.httpContext = HttpClientContext.create();
    }

    @Override
    public void run() {
        try {
            logger.debug("start sending file");
            CloseableHttpResponse response = httpClient.execute(httpPost, httpContext);
            logger.debug("executing the query...");
            try {
                response.getEntity();
            } 
            finally {
                response.close();
            }
            logger.debug("stop sending file");

        } catch (IOException ex) {
            logger.debug("error while creating a file " + ex);
        }
    }

}
