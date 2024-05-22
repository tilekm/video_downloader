package kz.tilek.downloader;

import javafx.application.Application;
import javafx.stage.Stage;
import kz.tilek.downloader.database.DatabaseManager;
import kz.tilek.downloader.gui.main.MainStage;

import java.io.IOException;
import java.util.prefs.Preferences;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Preferences preferences = Preferences.userNodeForPackage(Main.class);
        String id = preferences.get("id", "");
        if (!id.isEmpty()) {
            DatabaseManager.loadUser(Integer.parseInt(id));
        }
        new MainStage();
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void stop() {
        DatabaseManager.closeConnection();
    }
}