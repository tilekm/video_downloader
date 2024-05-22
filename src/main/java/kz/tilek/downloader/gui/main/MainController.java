package kz.tilek.downloader.gui.main;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import kz.tilek.downloader.Main;
import kz.tilek.downloader.database.DatabaseManager;
import kz.tilek.downloader.database.User;
import kz.tilek.downloader.downloader.InstagramDownloader;
import kz.tilek.downloader.downloader.YoutubeDownloader;
import kz.tilek.downloader.gui.login.LoginStage;
import kz.tilek.downloader.utils.custom.CustomException;
import kz.tilek.downloader.utils.URLParser;
import kz.tilek.downloader.utils.custom.CustomRunnable;
import kz.tilek.downloader.downloader.Downloader;
import kz.tilek.downloader.utils.custom.ProgressWorker;
import kz.tilek.downloader.downloader.TikTokDownloader;
import kz.tilek.downloader.gui.settings.SettingsStage;

import java.io.IOException;
import java.util.Date;
import java.util.prefs.Preferences;


public class MainController {
    @FXML
    private TextField link;
    @FXML
    private Label info, trys;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private ImageView preview;
    @FXML
    private ChoiceBox<String> mediaType;
    @FXML
    private Button downloadButton, loginButton;
    private Downloader downloader;

    @FXML
    protected void initialize() {
        Preferences preferences = Preferences.userNodeForPackage(Main.class);
        if (User.getInstance().getEmail() != null) {
            loginButton.setText("Logout");
            loginButton.setOnAction(e -> {
                User.getInstance().logout();
                Stage s = (Stage) link.getScene().getWindow();
                s.close();
                try {
                    new MainStage();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            });
            if (User.getInstance().getReloadTime() != null && User.getInstance().getReloadTime().before(new Date())) {
                User.getInstance().setTrys(User.getInstance().getDownloadLimit());
                User.getInstance().setReloadTime();
                DatabaseManager.setTrys(User.getInstance());
                preferences.put("accountTrys", User.getInstance().getTrys() + "");
            }
            trys.setText(preferences.get("accountTrys", User.getInstance().getTrys() + ""));
        } else {
            if (preferences.get("trys", "3").isEmpty()) {
                preferences.put("trys", "3");
            }
            trys.setText(preferences.get("trys", "3"));
        }
        mediaType.getItems().addAll("Video", "Audio");
        mediaType.setValue("Video");
    }
    @FXML
    protected void onLinkEnteredHandler() {
        checkTrys();
        downloadButton.setDisable(true);
        if (trys.getText().equals("0")) {
            info.setText("You have no more attempts");
            return;
        }
        if (link.getText().isEmpty()) {
            info.setText("");
            preview.setImage(null);
            return;
        }
        preview.setImage(new Image(String.valueOf(this.getClass().getResource("/kz/tilek/downloader/images/loading.gif"))));
        switch (URLParser.checkURL(link.getText())) {
            case "tiktok":
                this.downloader = new TikTokDownloader();
                break;
            case "instagram":
                this.downloader = new InstagramDownloader();
                break;
            case "youtube":
                this.downloader = new YoutubeDownloader();
                break;
            default:
                info.setText("Unsupported link");
                downloadButton.setDisable(true);
                preview.setImage(null);
                return;
        }
        info.setText("");
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws CustomException {
                try {
                    preview.setImage(new Image(downloader.getPreview(link.getText())));
                    downloadButton.setDisable(false);
                } catch (CustomException e) {
                    preview.setImage(new Image(String.valueOf(this.getClass().getResource("/kz/tilek/downloader/images/error.png"))));
                    throw new CustomException(e.getMessage());
                }
                return null;
            }
        };
        new Thread(task).start();
        task.setOnFailed(e -> info.setText(task.getException().getMessage()));
    }

    @FXML
    protected void downloadButtonPress() {
        if (trys.getText().equals("0")) {
            info.setText("You have no more attempts");
            downloadButton.setDisable(true);
            return;
        }
        info.setVisible(false);
        link.setDisable(true);
        downloadButton.setDisable(true);
        progressBar.setVisible(true);
        CustomRunnable customRunnable = new CustomRunnable(this.downloader, mediaType.getValue());
        ProgressWorker task = new ProgressWorker(customRunnable);
        progressBar.progressProperty().unbind();
        progressBar.progressProperty().bind(task.progressProperty());
        task.setOnSucceeded(e -> {
            Preferences preferences = Preferences.userNodeForPackage(Main.class);
            trys.setText(Integer.parseInt(trys.getText()) - 1 + "");
            if (User.getInstance().getEmail() != null) {
                preferences.put("accountTrys", trys.getText());
                User.getInstance().setTrys(Integer.parseInt(trys.getText()));
                checkTrys();
                User.getInstance().setDBTrys();
            } else {
                preferences.put("trys", trys.getText());
            }
            if (trys.getText().equals("0")) {
                info.setText("You have no more attempts");
                downloadButton.setDisable(true);
            } else {
                info.setText("Downloaded");
                downloadButton.setDisable(false);
            }
            progressBar.setVisible(false);
            info.setVisible(true);
            link.setDisable(false);
        });
        task.setOnFailed(e -> {
            progressBar.setVisible(false);
            link.setDisable(false);
            info.setText(customRunnable.getMessage());
            downloadButton.setDisable(false);
            info.setVisible(true);
        });
        customRunnable.start();
        new Thread(task).start();
    }

    @FXML
    protected void settingsButtonPress() throws IOException {
        Stage s = (Stage) link.getScene().getWindow();
        s.close();
        new SettingsStage();
    }

    @FXML
    protected void loginButtonPress() throws IOException {
        Stage s = (Stage) link.getScene().getWindow();
        s.close();
        new LoginStage();
    }

    private void checkTrys() {
        if (User.getInstance().getEmail() != null) {
            if (User.getInstance().getReloadTime() != null && User.getInstance().getReloadTime().before(new Date())) {
                User.getInstance().setTrys(User.getInstance().getDownloadLimit());
                User.getInstance().setReloadTime();
                DatabaseManager.setTrys(User.getInstance());
                Preferences preferences = Preferences.userNodeForPackage(Main.class);
                preferences.put("accountTrys", User.getInstance().getTrys() + "");
            }
            if (User.getInstance().getTrys() == User.getInstance().getDownloadLimit()) {
                User.getInstance().setReloadTime();
            }
        }
    }
}

