package kz.tilek.downloader.database;

import kz.tilek.downloader.Main;

import java.util.Date;
import java.util.prefs.Preferences;

public class User {
    private static User instance;
    private int id;
    private String email;
    private String password;
    private int downloadLimit;
    private int trys;
    private Date reloadTime;

    public static User getInstance() {
        if (instance == null) {
            instance = new User();
        }
        return instance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getDownloadLimit() {
        return downloadLimit;
    }

    public void setDownloadLimit(int downloadLimit) {
        this.downloadLimit = downloadLimit;
    }

    public int getTrys() {
        return trys;
    }
    public void setTrys(int trys) {
        this.trys = trys;
    }
    public void setDBTrys() {
        new Thread(() -> DatabaseManager.setTrys(instance)).start();
    }

    public Date getReloadTime() {
        return reloadTime;
    }

    public void setReloadTime() {
        this.reloadTime = new Date(new Date().getTime() + 86400000);
        new Thread(() -> DatabaseManager.setReloadTime(instance)).start();
    }

    public void setReloadTime(Date date) {
        this.reloadTime = date;
    }


    public void logout() {
        Preferences preferences = Preferences.userNodeForPackage(Main.class);
        preferences.remove("id");
        preferences.remove("accountTrys");
        DatabaseManager.logout(instance);
        instance = null;
        id = 0;
        email = null;
        password = null;
        downloadLimit = 0;
        trys = 0;
        reloadTime = null;

    }
}
