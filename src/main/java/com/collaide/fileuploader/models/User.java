package com.collaide.fileuploader.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author leo
 */
public class User extends Model {

    private int id;
    private String name;
    private String email;
    private String password;
    private String token;
    private GroupSyncList groupSyncList;
    private File dataPath;
    private File userDataPath;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String getToken() {
        return token;
    }

    public GroupSyncList getGroupSyncList() {
        return groupSyncList;
    }

    public void setGroupSyncList(GroupSyncList groupSyncList) {
        this.groupSyncList = groupSyncList;
    }
    
    public GroupSync getGroupSynchronized(int id) {
        if(groupSyncList != null) {
            return groupSyncList.getGroupSync(id);
        }
        return null;
    }

    public void retrivePersonalData() {
        File personalData = getpersonalDataFile();
        System.out.println("retrive personal datas " + personalData.getAbsolutePath());
        if (personalData.exists()) {
            System.out.println("file exists");
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(personalData));
                setGroupSyncList((GroupSyncList)ois.readObject());
            } catch (IOException ex) {
                Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            setGroupSyncList(new GroupSyncList());
        }
    }
    
    public void savePersonalData() {
        System.out.println("writing infos");
        try {
            ObjectOutputStream oos =  new ObjectOutputStream(new FileOutputStream(getpersonalDataFile())) ;
            oos.writeObject(getGroupSyncList());
        } catch (IOException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("to " + getpersonalDataFile().getAbsolutePath());
    }
    
    public File getpersonalDataFile() {
        if(dataPath == null) {
            dataPath = new File(System.getProperty("user.home"), ".collaide");
            if(!dataPath.exists()) {
                dataPath.mkdir();
            }
        }
        if(userDataPath == null) {
            userDataPath = new File(dataPath, String.valueOf(getId()));
        }
        return userDataPath;
    }
}
