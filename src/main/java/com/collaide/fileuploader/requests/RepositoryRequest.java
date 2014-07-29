/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.collaide.fileuploader.requests;

import com.collaide.fileuploader.models.Group;
import com.collaide.fileuploader.models.repositorty.RepoFile;
import com.collaide.fileuploader.models.repositorty.RepoFolder;
import com.collaide.fileuploader.models.repositorty.RepoItems;
import com.collaide.fileuploader.models.repositorty.Repository;
import com.google.gson.Gson;
import com.sun.jersey.api.client.ClientResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;

/**
 *
 * @author leo
 */
public class RepositoryRequest extends Collaide {

    private int groupID;
    protected final String uri;
    private static final Logger logger = Logger.getLogger(RepositoryRequest.class);

    public RepositoryRequest(int groupID) {
        this.groupID = groupID;
        this.uri = "groups/" + String.valueOf(groupID) + "/repo_items/";
    }

    public Repository index() {
        ClientResponse response = request(uri)
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);
        return getOrIndex(response);
    }

    public Repository get(int id) {
        ClientResponse response = request(getRepoItemUrl(id))
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);
        return getOrIndex(response);
    }

    public void download(String url, String folderToSave) throws IOException {
        ClientResponse response = request(url)
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);
        if (response.getStatus() != 200) {
            return;
        }
        File downloadedFile = response.getEntity(File.class);
        File fileToSave = new File(folderToSave, downloadedFile.getName());
        downloadedFile.renameTo(fileToSave);
        FileWriter fr = new FileWriter(downloadedFile);
        fr.flush();
        if (isAZip(downloadedFile)) {
            unzip(downloadedFile, new File(folderToSave));
            downloadedFile.delete();
        }
    }

    protected String getRepoItemUrl(int id) {
        return uri + "/" + String.valueOf(id);
    }

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    public String getGroupUri() {
        return uri;
    }
    
    

    private Repository getOrIndex(ClientResponse response) {
        if (response.getStatus() != 200) {
            return null;
        }
        String json = response.getEntity(String.class);
        Gson gson = new Gson();
        RepoItems[] items = gson.fromJson(json, RepoItems[].class);
        Repository repo = new Repository();
        for (RepoItems item : items) {
            if (item.isIs_folder()) {
                repo.getServerFolders().put(item.getName(), (RepoFolder) item);
            } else {
                repo.getServerFiles().put(item.getMd5(), (RepoFile) item);
            }
        }
        return repo;
    }

    private boolean isAZip(File file) {
        ZipFile zipfile = null;
        try {
            zipfile = new ZipFile(file);
            return true;
        } catch (ZipException e) {
            return false;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (zipfile != null) {
                    zipfile.close();
                    zipfile = null;
                }
            } catch (IOException e) {
            }
        }
    }

    private void unzip(File zip, File output) {
        byte[] buffer = new byte[1024];
        try {
            //create output directory is not exists
            if (!output.exists()) {
                output.mkdir();
            }
            //get the zip file content
            ZipInputStream zis = new ZipInputStream(new FileInputStream(zip));
            //get the zipped file list entry
            ZipEntry ze = zis.getNextEntry();

            while (ze != null) {
                String fileName = ze.getName();
                File newFile = new File(output + File.separator + fileName);

                logger.debug("file unzip : " + newFile.getAbsoluteFile());

                //create all non exists folders
                //else you will hit FileNotFoundException for compressed folder
                new File(newFile.getParent()).mkdirs();

                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                ze = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();

        } catch (IOException ex) {
            logger.error(ex);
        }
    }

}
