package kz.tilek.downloader.gui.register;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import kz.tilek.downloader.database.DatabaseManager;
import kz.tilek.downloader.database.User;
import kz.tilek.downloader.gui.login.LoginStage;
import kz.tilek.downloader.utils.custom.CustomException;

import java.io.IOException;

public class RegisterController {
    @FXML
    private TextField email;
    @FXML
    private PasswordField password, confirmPassword;
    @FXML
    private Label error;
    @FXML
    private Button registerButton, cancelButton;

    @FXML
    protected void onRegisterButtonHandler() throws IOException {
        error.setVisible(false);
        error.setText("Check the fields below");
        if (email.getText().isEmpty()) {
            email.setStyle("-fx-border-color: red");
            error.setVisible(true);
        } else {
            email.setStyle("");
            error.setVisible(false);
        }
        if (password.getText().isEmpty()) {
            password.setStyle("-fx-border-color: red");
            error.setVisible(true);
        } else {
            password.setStyle("");
            error.setVisible(false);
        }
        if (confirmPassword.getText().isEmpty()) {
            confirmPassword.setStyle("-fx-border-color: red");
            error.setVisible(true);
        } else {
            confirmPassword.setStyle("");
            error.setVisible(false);
        }
        if (!password.getText().isEmpty() && !confirmPassword.getText().isEmpty() && !password.getText().equals(confirmPassword.getText())) {
            password.setStyle("-fx-border-color: red");
            confirmPassword.setStyle("-fx-border-color: red");
            error.setVisible(true);
        } else if (!password.getText().isEmpty() && !confirmPassword.getText().isEmpty()) {
            password.setStyle("");
            confirmPassword.setStyle("");
            error.setVisible(false);
        }
        if (!email.getText().isEmpty() && !password.getText().isEmpty() && !confirmPassword.getText().isEmpty() && password.getText().equals(confirmPassword.getText())) {
            email.setDisable(true);
            password.setDisable(true);
            confirmPassword.setDisable(true);
            registerButton.setDisable(true);
            cancelButton.setDisable(true);
            new Thread(() -> {
                User u = User.getInstance();
                u.setEmail(email.getText());
                u.setPassword(password.getText());
                try {
                    DatabaseManager.register(u);
                    Platform.runLater(() -> {
                        Stage s = (Stage) email.getScene().getWindow();
                        s.setTitle("Login");
                        try {
                            s.setScene(LoginStage.getLoginScene());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    );
                } catch (CustomException e) {
                    email.setDisable(false);
                    password.setDisable(false);
                    confirmPassword.setDisable(false);
                    registerButton.setDisable(false);
                    cancelButton.setDisable(false);
                    error.setVisible(true);
                    Platform.runLater(() -> error.setText(e.getMessage()));
                }

            }).start();
        }
    }

    @FXML
    protected void onCancelButtonHandler() throws IOException {
        Stage s = (Stage) email.getScene().getWindow();
        s.setTitle("Login");
        s.setScene(LoginStage.getLoginScene());
    }
}
