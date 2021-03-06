/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.collaide.fileuploader.requests.notifications;

import com.collaide.fileuploader.helper.TestHelper;
import com.collaide.fileuploader.models.notifications.ItemChanged;
import com.collaide.fileuploader.models.notifications.ItemDeleted;
import com.collaide.fileuploader.models.notifications.NotificationsMap;
import com.collaide.fileuploader.models.repositorty.RepoFile;
import com.collaide.fileuploader.models.user.CurrentUser;
import com.collaide.fileuploader.requests.repository.FilesRequest;
import com.collaide.fileuploader.requests.repository.RepoItemNotDeleted;
import com.collaide.fileuploader.requests.repository.RepositoryRequest;
import com.sun.jersey.api.client.ClientResponse;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author leo
 */
public class RepoItemsNotificationRequestTest extends TestHelper {

    private final RepoItemsNotificationRequest request = new RepoItemsNotificationRequest(ADMIN_GROUP_ID);
    private final RepositoryRequest repositoryRequest = new RepositoryRequest(ADMIN_GROUP_ID);
    private final FilesRequest fileRequest = new FilesRequest(ADMIN_GROUP_ID);

    @BeforeClass
    public static void beforeClass() {
        TestHelper.beforeClass();
    }

    @AfterClass
    public static void afterClass() {
        TestHelper.afterClass();
    }

    @Test
    public void testGetAllNotifications() throws Exception {
        for (int i = 0; i < 5; i++) {
            fileRequest.create(createNewFile());
        }
        File file = createNewFile();
        fileRequest.create(file);
        fileRequest.terminate();
        RepoFile repoFile = repositoryRequest.index().getServerFiles().get(getMd5(file) + file.getName());
        repositoryRequest.delete(repoFile.getId());
        NotificationsMap notifications = request.getNotifications(CurrentUser.getUser().getId());
        assertNotNull(notifications);
        ItemChanged itemChanged = null;
        ArrayList itemsList = notifications.getArrayList(ItemChanged.class);
        for (Object o : itemsList) {
            ItemChanged item = (ItemChanged) o;
            if (item.getNotifierId() == repoFile.getId()) {
                itemChanged = item;
                break;
            }
        }
        assertNotNull(itemChanged);
        assertEquals(repoFile.getId(), itemChanged.getNotifierId());
        assertNotNull(notifications.getMostRecentNotification(ItemDeleted.class));
    }

    @Test
    public void testNotificationWithDate() {
        long timestamp = System.currentTimeMillis();
        File fileToSend = createNewFile();
        fileRequest.create(fileToSend);
        fileRequest.terminate();
        NotificationsMap notifications = request.getNotifications(CurrentUser.getUser().getId(), timestamp);
        assertNotNull(notifications);
        ArrayList notifItemChanged = notifications.getArrayList(ItemChanged.class);
    }

    @Test
    public void testGetNotificationType() throws Exception {
        String sendedType = "TestClass";
        String fullType = dummyFullType(sendedType);
        Method getFileName = invokePrivateMethod(
                request.getClass(), "getNotificationType", String.class
        );
        try {
            String expectedClassName = (String) getFileName.invoke(request, fullType);
            assertEquals(sendedType, expectedClassName);
        } catch (InvocationTargetException ex) {
            logger.error("error while invoking getFileName: " + ex.getCause());
            fail("invocation error");
        }
    }

    @Test
    public void testGetClassFromString() throws Exception {
        String sendedType = "ItemChanged";
        Method getFileName = invokePrivateMethod(
                request.getClass(), "getClassFromString", String.class
        );
        try {
            Class expectedClass = (Class) getFileName.invoke(request, sendedType);
            assertEquals(ItemChanged.class, expectedClass);
        } catch (InvocationTargetException ex) {
            logger.error("error while invoking getFileName: " + ex.getCause());
            fail("invocation error");
        }
    }
    
    @Test
    public void testToCollaideFormat() throws Exception  {
        //Method m = invokePrivateMethod(request.getClass(), "toCollaideFormat", Long.class);
        String result = request.toCollaideFormat(System.currentTimeMillis());//(String)m.invoke(request, System.currentTimeMillis());
        assertNotNull(result);
        logger.debug(result);
    }

    private String dummyFullType(String type) {
        return "SomeModule::" + type;
    }
}
