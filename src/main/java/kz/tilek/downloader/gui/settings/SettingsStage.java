package kz.tilek.downloader.gui.settings;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import kz.tilek.downloader.Main;

import java.io.IOException;

public class SettingsStage extends Stage {
    public SettingsStage() throws IOException {
        Scene scene = getFXMLScene();
        setTitle("Settings");
        setScene(scene);
        requestFocus();
        setResizable(false);
        show();
        this.setOnCloseRequest(event1 -> {
            try {
                new Main().start(new Stage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static Scene getFXMLScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/settings.fxml"));
        return new Scene(fxmlLoader.load());
    }
}
