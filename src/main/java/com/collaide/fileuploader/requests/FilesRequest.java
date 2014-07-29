/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.collaide.fileuploader.requests;

import com.collaide.fileuploader.models.repositorty.RepoFile;
import com.collaide.fileuploader.models.user.CurrentUser;
import com.sun.jersey.api.representation.Form;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author leo
 */
public class FilesRequest extends RepositoryRequest implements Runnable {

    private File fileToSend;
    private int idRepo;

    public FilesRequest(int groupID) {
        super(groupID);
    }

    public void create(File file, int id) {
        this.fileToSend = file;
        this.idRepo = id;
        if (idRepo != 0) {
            //createUri = getRepoItemUrl(idRepo);
        }
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httppost = new HttpPost("http://0.0.0.0:3000/api/"+uri+"/file.json?"+CurrentUser.getAuthParams()+"&authenticity_token="+CurrentUser.getUser().getCsrf());
            FileBody bin = new FileBody(file);
            HttpEntity reqEntity = MultipartEntityBuilder.create()
                    .addPart("repo_file_file", bin)
                    .addTextBody("authenticity_token", CurrentUser.getUser().getCsrf())
                    .build();
            httppost.setEntity(reqEntity);
            httppost.setHeader("X-CSRF-Token", CurrentUser.getUser().getCsrf());
            httpclient.execute(httppost);
            
        } catch (IOException ex) {
            Logger.getLogger(FilesRequest.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                httpclient.close();
            } catch (IOException ex) {
                Logger.getLogger(FilesRequest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void update() {
    }

    public void delete() {
    }

    @Override
    public void run() {
        try {
            System.out.println("sdfvlsajdhvfjlashdfvjlhasdvfhjlsadvfl");
            RepoFile repoFile = new RepoFile();
            if (idRepo != 0) {
                //createUri = getRepoItemUrl(idRepo);
            }
            HttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost(uri);

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();

            /* example for setting a HttpMultipartMode */
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            /* example for adding an image part */
            FileBody fileBody = new FileBody(fileToSend); //image should be a String
            builder.addPart("my_file", fileBody);
            post.setEntity(builder.build());

            HttpResponse response = client.execute(post);
            System.out.println(response.getStatusLine().getStatusCode());
            System.out.println("ésdjkbféaskdjbfékasdjbfékas");
            //FormDataMultiPart part = new FormDataMultiPart().field("file", stream, MediaType.TEXT_PLAIN_TYPE);
//            request(uri)
//                    .type(MediaType.APPLICATION_JSON)
//                    .header("X-CSRF-Token", CurrentUser.getUser().getCsrf())
//                    .post(new FileInputStream(fileToSend));

        } catch (FileNotFoundException ex) {
            Logger.getLogger(FilesRequest.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FilesRequest.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

}
