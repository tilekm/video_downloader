package kz.tilek.downloader.gui.register;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import kz.tilek.downloader.Main;

import java.io.IOException;

public class RegisterStage {
    public static Scene getRegistrationScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/register.fxml"));
        return new Scene(fxmlLoader.load());
    }
}
