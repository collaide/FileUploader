package com.collaide.fileuploader.models;

/**
 *
 * @author leo
 */
public class User extends Model{
    private int id;
    private String name;
    private String email;
    private String password;
    private String token;
    private GroupSyncList groupSyncList;
    
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
}
