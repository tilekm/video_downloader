package kz.tilek.downloader.gui.change_password;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import kz.tilek.downloader.Main;

import java.io.IOException;

public class ChangePasswordStage {
    public static Scene getChangePasswordScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/changePassword.fxml"));
        return new Scene(fxmlLoader.load());
    }
}
