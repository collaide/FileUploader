/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.collaide.fileuploader.requests.repository;

import com.collaide.fileuploader.models.repositorty.RepoFile;
import com.collaide.fileuploader.models.repositorty.RepoFolder;
import com.collaide.fileuploader.models.repositorty.RepoItems;
import com.collaide.fileuploader.models.repositorty.Repository;
import com.collaide.fileuploader.models.user.CurrentUser;
import com.collaide.fileuploader.requests.Collaide;
import com.collaide.fileuploader.requests.NotSuccessRequest;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.ws.rs.core.MediaType;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FilenameUtils;
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
     * get the root of a repository
     *
     * @return Repository a set of folders and files present on the server
     */
    public Repository index() {
        try {
            JsonElement repoItems = getResponseToJsonElement(
                    doGet(uri + "?" + CurrentUser.getAuthParams())
            ).getAsJsonObject().get("repo_items");
            return getRepository(repoItems);
        } catch (NotSuccessRequest ex) {
            notSuccesLog("index", ex);
            return null;
        }
    }

    /**
     * get infos about a elements of a repository represented by an id
     *
     * @param id the id of the elements
     * @return Repository a set of folders and files present on the server
     */
    public Repository get(int id) {
        Repository repo = null;
        try {
            JsonObject repoItems = getResponseToJsonElement(
                    doGet(getRepoItemUrl(id) + "?" + CurrentUser.getAuthParams())
            ).getAsJsonObject();
            if (repoItems.has("children") && repoItems.get("children").isJsonArray()) {
                repo = getRepository(repoItems.get("children").getAsJsonArray());
                repo.setRepoItem(gson.fromJson(repoItems, RepoItems.class));
            }
        } catch (NotSuccessRequest ex) {
            notSuccesLog("get/" + String.valueOf(id), ex);
        }
        return repo;
    }

    public String download(int repoItemId, String folderToSave) throws IOException {
        return download(getRepoItemUrl(repoItemId) + "/download?" + CurrentUser.getAuthParams(), folderToSave);
    }
    
    /**
     * download a file or a folder from the server to the disk a folder is
     * downloaded as a zip and the unzipped<br/>
     *
     * @param url the URL of the repo item to download
     * @param folderToSave the folderin which to save the downloaded item
     * @return The path of the saved file or folder
     * @throws IOException
     */
    public String download(String url, String folderToSave) throws IOException {
        ClientResponse response = Client.create().resource(url + "?" + CurrentUser.getAuthParams())
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);
        if (response.getStatus() != 200) {
            return null;
        }
        File downloadedFile = response.getEntity(File.class);

        File fileToSave = new File(folderToSave, getFileName(response));
        downloadedFile.renameTo(fileToSave);
        FileWriter fr = new FileWriter(downloadedFile);
        fr.flush();
        String itemName = fileToSave.getAbsolutePath();
        if (isAZip(response)) {
            unzip(fileToSave, new File(folderToSave));
            fileToSave.delete();
            itemName = FilenameUtils.removeExtension(itemName);
        }
        downloadedFile.getAbsolutePath();
        return itemName;
    }
    
    public void delete(int fileId) throws RepoItemNotDeleted {
        ClientResponse response = request(getRepoItemUrl(fileId) + "?" + CurrentUser.getAuthParams())
                .delete(ClientResponse.class);
        if(response.getStatus() != 200) throw new RepoItemNotDeleted(response.getStatus());
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
        for (JsonElement jsonElement : json.getAsJsonArray()) {
            JsonObject repoItem = jsonElement.getAsJsonObject();
            if (repoItem.get("is_folder").getAsBoolean()) {
                repo.getServerFolders().put(
                        repoItem.get("name").getAsString(),
                        gson.fromJson(repoItem, RepoFolder.class)
                );
            } else {
                repo.getServerFiles().put(
                        repoItem.get("md5").getAsString() + repoItem.get("name").getAsString(),
                        gson.fromJson(repoItem, RepoFile.class)
                );
            }
        }
        return repo;
    }

    private String getFileName(ClientResponse response) {
        String contentDisposition = response.getHeaders().get("Content-disposition").get(0);
        if (contentDisposition == null) {
            return "unknow";
        }
        String fileName = contentDisposition.replaceFirst("^inline; filename=\"", "");
        fileName = fileName.substring(0, fileName.length() - 1);
        if (isAZip(response)) {
            fileName = fileName + ".zip";
        }
        return fileName;
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

    private boolean isAZip(ClientResponse response) {
        String contentType = response.getHeaders().get("Content-Type").get(0);
        return contentType.equals("application/zip");
    }

    private void unzip(File source, File destination) {
//        try {
        String finalDestionation = new File(
                destination, FilenameUtils.removeExtension(source.getName())
        ).getAbsolutePath();
        logger.debug("source: " + source.getAbsolutePath());
        logger.debug("destination: " + finalDestionation);
        try {
            ZipFile zipFile = new ZipFile(source);
            zipFile.extractAll(finalDestionation);
        } catch (ZipException ex) {
            logger.error("error while unzipping: " + ex);
        }
//            destination.mkdirs();
//            FileSystemManager fileManager = VFS.getManager();
//            FileObject fileObject = fileManager.resolveFile(source.getAbsolutePath());
//            FileObject zipFile = fileManager.createFileSystem(fileObject);
//            fileManager.toFileObject(destination).copyFrom(zipFile, new AllFileSelector());
//            zipFile.close();
//            fileObject.close();
//
//        } catch (FileSystemException ex) {
//            logger.error("error while unzipping: " + ex);
//        }
    }

}
