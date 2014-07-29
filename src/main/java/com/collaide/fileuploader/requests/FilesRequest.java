/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.collaide.fileuploader.requests;

import com.collaide.fileuploader.models.repositorty.RepoFile;
import com.collaide.fileuploader.models.user.CurrentUser;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.log4j.Logger;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 *
 * @author leo
 */
public class FilesRequest extends RepositoryRequest implements Runnable {

    private File fileToSend;
    private int idRepo;
    private static final Logger logger = Logger.getLogger(FilesRequest.class);

    public FilesRequest(int groupID) {
        super(groupID);
    }

    public void create(File file, int id) {
        this.fileToSend = file;
        this.idRepo = id;
        this.fileToSend = file;
        new Thread(this).start();
    }


    @Override
    public void run() {
        System.out.println("starting sending file " + Thread.currentThread().getName());        

        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httppost = new HttpPost(getBase_uri() + getGroupUri() + "/file.json?" + 
                    CurrentUser.getAuthParams() + "&authenticity_token=" + 
                    CurrentUser.getUser().getCsrf());
            FileBody bin = new FileBody(fileToSend);
            MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create()
                    .addPart("repo_file[file]", bin)
                    .addTextBody("authenticity_token", CurrentUser.getUser().getCsrf());
            if (idRepo != 0) {
                reqEntity.addTextBody("repo_file[id]", String.valueOf(idRepo));
            }
            httppost.setEntity(reqEntity.build());
            httppost.setHeader("X-CSRF-Token", CurrentUser.getUser().getCsrf());
            httpclient.execute(httppost);
            System.out.println("stop sending file " + Thread.currentThread().getName());
        } catch (IOException ex) {
            logger.error(ex);
        } finally {
            try {
                httpclient.close();
            } catch (IOException ex) {
                logger.error(ex);
            }
        }
//        try {
//            System.out.println("sdfvlsajdhvfjlashdfvjlhasdvfhjlsadvfl");
//            RepoFile repoFile = new RepoFile();
//            if (idRepo != 0) {
//                //createUri = getRepoItemUrl(idRepo);
//            }
//            HttpClient client = HttpClients.createDefault();
//            HttpPost post = new HttpPost(uri);
//
//            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//
//            /* example for setting a HttpMultipartMode */
//            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
//
//            /* example for adding an image part */
//            FileBody fileBody = new FileBody(fileToSend); //image should be a String
//            builder.addPart("my_file", fileBody);
//            post.setEntity(builder.build());
//
//            HttpResponse response = client.execute(post);
//            System.out.println(response.getStatusLine().getStatusCode());
//            System.out.println("ésdjkbféaskdjbfékasdjbfékas");
//            //FormDataMultiPart part = new FormDataMultiPart().field("file", stream, MediaType.TEXT_PLAIN_TYPE);
////            request(uri)
////                    .type(MediaType.APPLICATION_JSON)
////                    .header("X-CSRF-Token", CurrentUser.getUser().getCsrf())
////                    .post(new FileInputStream(fileToSend));
//
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(FilesRequest.class
//                    .getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(FilesRequest.class
//                    .getName()).log(Level.SEVERE, null, ex);
//        }
    }

}
