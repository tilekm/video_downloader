package kz.tilek.downloader.gui.change_password;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import kz.tilek.downloader.database.DatabaseManager;
import kz.tilek.downloader.database.User;
import kz.tilek.downloader.gui.settings.SettingsStage;
import kz.tilek.downloader.utils.custom.CustomException;

import java.io.IOException;

public class ChangePasswordController {
    @FXML
    private TextField email;
    @FXML
    private PasswordField password, newPassword, confirmPassword;
    @FXML
    private Label error;
    @FXML
    private Button changePasswordButton, cancelButton;


    @FXML
    protected void initialize() {
        email.setText(User.getInstance().getEmail());
    }

    @FXML
    protected void onChangePasswordButtonHandler() {
        if (password.getText().isEmpty()) {
            password.setStyle("-fx-border-color: red");
            error.setVisible(true);
        } else {
            password.setStyle("");
            error.setVisible(false);
        }
        if (newPassword.getText().isEmpty()) {
            newPassword.setStyle("-fx-border-color: red");
            error.setVisible(true);
        } else {
            newPassword.setStyle("");
            error.setVisible(false);
        }
        if (confirmPassword.getText().isEmpty()) {
            confirmPassword.setStyle("-fx-border-color: red");
            error.setVisible(true);
        } else {
            confirmPassword.setStyle("");
            error.setVisible(false);
        }
        if (!email.getText().isEmpty() && !password.getText().isEmpty() && !newPassword.getText().isEmpty() && !confirmPassword.getText().isEmpty()) {
            if (!newPassword.getText().equals(confirmPassword.getText())) {
                newPassword.setStyle("-fx-border-color: red");
                confirmPassword.setStyle("-fx-border-color: red");
                error.setVisible(true);
                return;
            } else {
                newPassword.setStyle("");
                confirmPassword.setStyle("");
                error.setVisible(false);
            }
            password.setDisable(true);
            newPassword.setDisable(true);
            confirmPassword.setDisable(true);
            changePasswordButton.setDisable(true);
            cancelButton.setDisable(true);
            new Thread(() -> {
                try {
                    DatabaseManager.update(email.getText(), password.getText(), newPassword.getText());
                    Platform.runLater(() -> {
                        Stage s = (Stage) email.getScene().getWindow();
                        try {
                            s.setScene(SettingsStage.getFXMLScene());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                } catch (CustomException e) {
                    Platform.runLater(() -> {
                        error.setVisible(true);
                        password.setDisable(false);
                        newPassword.setDisable(false);
                        confirmPassword.setDisable(false);
                        changePasswordButton.setDisable(false);
                        cancelButton.setDisable(false);
                    });
                }
            }).start();
        }
    }

    @FXML
    protected void onCancelButtonHandler() throws IOException {
        Stage s = (Stage) email.getScene().getWindow();
        s.setScene(SettingsStage.getFXMLScene());
    }
}
