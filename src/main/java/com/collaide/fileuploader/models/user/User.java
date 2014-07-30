package com.collaide.fileuploader.models.user;
import com.collaide.fileuploader.models.GroupSync;
import com.collaide.fileuploader.models.GroupSyncList;
import com.collaide.fileuploader.models.Model;
import com.google.gson.annotations.Expose;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author leo
 */
public class User extends Model {
    
    @Expose
    private int id;
    @Expose
    private String name;
    @Expose
    private String email;
    @Expose
    private String password;
    @Expose
    private String token;
    @Expose
    private String csrf;
    private GroupSyncList groupSyncList;
    private File dataPath;
    private File userDataPath;
    private static final Logger logger = LogManager.getLogger(User.class);
    
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

    public String getCsrf() {
        return csrf;
    }

    public void setCsrf(String csrf) {
        this.csrf = csrf;
    }
    
    public void setGroupSyncList(GroupSyncList groupSyncList) {
        this.groupSyncList = groupSyncList;
    }
    
    public GroupSync getGroupSynchronized(int id) {
        if (groupSyncList != null) {
            return groupSyncList.getGroupSync(id);
        }
        return null;
    }
    
    public void retrivePersonalData() {
        File personalData = getpersonalDataFile();
        logger.debug("retrive personal datas " + personalData.getAbsolutePath());
        if (personalData.exists()) {
            logger.debug("File exists");
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(personalData));
                setGroupSyncList((GroupSyncList) ois.readObject());
            } catch (IOException ex) {
                logger.error("Error while opening file: " + ex);
            } catch (ClassNotFoundException ex) {
                logger.error("Class not found when reading datas from file: " + 
                        personalData.getAbsolutePath() + " exception: " + ex);
            }
        } else {
            logger.debug("File do no exists");
            setGroupSyncList(new GroupSyncList());
        }
    }
    
    public void savePersonalData() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(getpersonalDataFile()));
            oos.writeObject(getGroupSyncList());
        } catch (IOException ex) {
            logger.error("Error while writing user datas: " + ex);
        }
        logger.debug("writing infos to " + getpersonalDataFile().getAbsolutePath());
    }
    
    public File getpersonalDataFile() {
        if (dataPath == null) {
            dataPath = new File(System.getProperty("user.home"), ".collaide");
            if (!dataPath.exists()) {
                dataPath.mkdir();
            }
        }
        if (userDataPath == null) {
            userDataPath = new File(dataPath, String.valueOf(getId()));
        }
        return userDataPath;
    }
}
