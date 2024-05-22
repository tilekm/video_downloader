package kz.tilek.downloader.gui.main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import kz.tilek.downloader.Main;

import java.io.IOException;

public class MainStage extends Stage {
    public MainStage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 400);
        setResizable(false);
        setTitle("Downloader Application");
        setScene(scene);
        show();
    }
}
