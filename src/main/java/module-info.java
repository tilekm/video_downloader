module kz.tilek.downloader {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.google.gson;
    requires java.sql;
    requires java.prefs;
    requires spring.security.crypto;
    requires org.apache.commons.logging;
    requires org.bouncycastle.provider;
    requires org.postgresql.jdbc;
    requires io.github.cdimascio.dotenv.java;

    exports kz.tilek.downloader;
    opens kz.tilek.downloader to javafx.fxml;
    opens kz.tilek.downloader.utils.response.tiktok to com.google.gson;
    opens kz.tilek.downloader.utils.response.instagram to com.google.gson;
    opens kz.tilek.downloader.utils.response.youtube to com.google.gson;
    exports kz.tilek.downloader.gui.main;
    opens kz.tilek.downloader.gui.main to javafx.fxml;
    exports kz.tilek.downloader.gui.settings;
    opens kz.tilek.downloader.gui.settings to javafx.fxml;
    exports kz.tilek.downloader.gui.login;
    opens kz.tilek.downloader.gui.login to javafx.fxml;
    exports kz.tilek.downloader.gui.register;
    opens kz.tilek.downloader.gui.register to javafx.fxml;
    exports kz.tilek.downloader.gui.change_password;
    opens kz.tilek.downloader.gui.change_password to javafx.fxml;
    opens kz.tilek.downloader.database to java.sql;
}