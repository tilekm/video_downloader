package kz.tilek.downloader.gui.login;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import kz.tilek.downloader.Main;

import java.io.IOException;

public class LoginStage extends Stage {
    public LoginStage() throws IOException {
        setTitle("Login");
        setScene(getLoginScene());
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

    public static Scene getLoginScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/login.fxml"));
        return new Scene(fxmlLoader.load());
    }
}
