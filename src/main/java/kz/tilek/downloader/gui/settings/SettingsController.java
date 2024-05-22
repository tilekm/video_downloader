package kz.tilek.downloader.gui.settings;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import kz.tilek.downloader.database.User;
import kz.tilek.downloader.gui.change_password.ChangePasswordStage;
import kz.tilek.downloader.gui.main.MainStage;
import kz.tilek.downloader.utils.Configs;

import java.io.*;


public class SettingsController {
    @FXML
    private TextField downloadPath;
    @FXML
    private CheckBox openAfterDownload;
    @FXML
    private Button changePasswordButton;

    @FXML
    protected void initialize() {
        File file = new File(Configs.SETTINGS_FILE);
        if (file.exists()) {
            try (BufferedReader bis = new BufferedReader(new FileReader(Configs.SETTINGS_FILE))) {
                String line;
                while ((line = bis.readLine()) != null) {
                    if (line.startsWith(Configs.DOWNLOAD_PATH)) {
                        downloadPath.setText(line.substring(Configs.DOWNLOAD_PATH.length()));
                    } else if (line.startsWith(Configs.OPEN_AFTER_DOWNLOAD)) {
                        openAfterDownload.setSelected(Boolean.parseBoolean(line.substring(Configs.OPEN_AFTER_DOWNLOAD.length())));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            downloadPath.setText(System.getProperty("user.home") + File.separator + "Videos" + File.separator + "VideoDownloader" + File.separator);
        }
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose download directory");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        downloadPath.setOnMouseClicked(e -> {
            File file1 = directoryChooser.showDialog(new Stage());
            if (file1 != null) {
                downloadPath.setText(file1.getAbsolutePath());
            }
        });
        changePasswordButton.setVisible(User.getInstance().getEmail() != null);
    }
    @FXML
    protected void saveButtonClicked() throws IOException {
        String path = downloadPath.getText();
        if (!path.endsWith(File.separator)) {
            path += File.separator;
        }
        boolean open = openAfterDownload.isSelected();
        File file = new File("settings.ini");
        file.createNewFile();
        try (BufferedWriter bis = new BufferedWriter(new FileWriter("settings.ini"))) {
            bis.write("downloadPath=" + path + "\n");
            bis.write("openAfterDownload=" + open);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage s = (Stage) downloadPath.getScene().getWindow();
        s.close();
        new MainStage();
    }

    @FXML
    protected void openSaveFolder() throws IOException {
        ProcessBuilder pb = new ProcessBuilder("explorer.exe", downloadPath.getText());
        pb.start();
    }

    @FXML
    protected void changePasswordButtonClicked() throws IOException {
        Stage s = (Stage) downloadPath.getScene().getWindow();
        s.setScene(ChangePasswordStage.getChangePasswordScene());
    }
}
