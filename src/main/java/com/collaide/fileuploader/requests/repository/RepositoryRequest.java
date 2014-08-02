/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.collaide.fileuploader.requests.repository;

import com.collaide.fileuploader.requests.NotSuccessRequest;
import com.collaide.fileuploader.models.repositorty.RepoFile;
import com.collaide.fileuploader.models.repositorty.RepoFolder;
import com.collaide.fileuploader.models.repositorty.RepoItems;
import com.collaide.fileuploader.models.repositorty.Repository;
import com.collaide.fileuploader.models.user.CurrentUser;
import com.collaide.fileuploader.requests.Collaide;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author leo
 */
public class RepositoryRequest extends Collaide {

    private int groupID;
    protected final String uri;
    private static final Logger logger = LogManager.getLogger(RepositoryRequest.class);
    private static final Gson gson = new Gson();

    public RepositoryRequest(int groupID) {
        this.groupID = groupID;
        this.uri = "groups/" + String.valueOf(groupID) + "/repo_items/";
    }

    /**
     * get the root of a repository TODO: test it
     *
     * @return Repository a set of folders and files present on the server
     */
    public Repository index() {
        try {
            return getRepository(getResponseToJsonObject(doGet(uri + "?" + CurrentUser.getAuthParams())));
        } catch (NotSuccessRequest ex) {
            notSuccesLog("index", ex);
            return null;
        }
    }

    /**
     * get infos about a elements of a repository represented by an id TODO:
     * test it
     *
     * @param id the id of the elements
     * @return Repository a set of folders and files present on the server
     */
    public Repository get(int id) {
        logger.debug("id=" + id);
        try {
            JsonObject repoItems = getResponseToJsonObject(doGet(getRepoItemUrl(id) + "?" + CurrentUser.getAuthParams()));
            if (repoItems.has("children") && repoItems.get("children").isJsonArray()) {
                return getRepository(repoItems.get("children").getAsJsonArray());
            }
        } catch (NotSuccessRequest ex) {
            notSuccesLog("get/" + String.valueOf(id), ex);
            return null;
        }
        return null;
    }

    /**
     * download a file or a folder from the server to the disk a folder is
     * downloaded as a zip and the unzipped TODO: test it
     *
     * @param url the URL of the repo item to download
     * @param folderToSave the folderin which to save the downloaded item
     * @throws IOException
     */
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
        return uri + String.valueOf(id);
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

    /**
     * Get a list of items(files or folders) from the server
     *
     * @param response The Request to execute
     * @return a class <code>Repository</code> containing a list of files and a
     * list of folders
     */
    private Repository getRepository(JsonElement json) {
        Repository repo = new Repository();
        for(JsonElement jsonElement : json.getAsJsonArray()) {
            JsonObject repoItem = jsonElement.getAsJsonObject();
            if(repoItem.get("is_folder").getAsBoolean()) {
                repo.getServerFolders().put(
                        repoItem.get("name").getAsString(),
                        gson.fromJson(repoItem, RepoFolder.class)
                );
            } else {
                repo.getServerFiles().put(
                        repoItem.get("md5").getAsString(),
                        gson.fromJson(repoItem, RepoFile.class)
                );
            }
        }
        return repo;
    }

    private JsonObject getResponseToJsonObject(ClientResponse response) {
        return new JsonParser().parse(response.getEntity(String.class)).getAsJsonObject();
    }

    private ClientResponse doGet(String url) throws NotSuccessRequest {
        ClientResponse response = request(url)
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);
        if (response.getStatus() != 200) {
            throw new NotSuccessRequest("the response status is not equal to 200: " + String.valueOf(response.getStatus()));
        }
        return response;
    }

    private void notSuccesLog(String action, Exception ex) {
        logger.error("error while fetching repo_items#" + action + ": " + ex);
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
