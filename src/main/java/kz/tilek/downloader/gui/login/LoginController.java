package kz.tilek.downloader.gui.login;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import kz.tilek.downloader.Main;
import kz.tilek.downloader.database.DatabaseManager;
import kz.tilek.downloader.database.User;
import kz.tilek.downloader.gui.main.MainStage;
import kz.tilek.downloader.gui.register.RegisterStage;
import kz.tilek.downloader.utils.custom.CustomException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.prefs.Preferences;

public class LoginController {
    @FXML
    private TextField email;
    @FXML
    private PasswordField password;
    @FXML
    private CheckBox rememberMe;
    @FXML
    private Label error;
    @FXML
    private Button loginButton, registerButton;

    @FXML
    protected void onLoginButtonHandler(ActionEvent event) throws SQLException {
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
        if (!email.getText().isEmpty() && !password.getText().isEmpty()) {
            new Thread(() -> {
                try {
                    User user = User.getInstance();
                    DatabaseManager.login(email.getText(), password.getText());
                    if (user.getEmail() == null) {
                        error.setVisible(true);
                        loginButton.setDisable(false);
                        email.setDisable(false);
                        password.setDisable(false);
                        rememberMe.setDisable(false);
                        registerButton.setDisable(false);
                        return;
                    }
                    Preferences prefs = Preferences.userNodeForPackage(Main.class);
                    if (rememberMe.isSelected()) {
                        prefs.put("id", String.valueOf(user.getId()));
                    }
                    prefs.put("accountTrys", String.valueOf(user.getTrys()));
                    close();
                } catch (CustomException e) {
                    loginButton.setDisable(false);
                    error.setVisible(true);
                    email.setDisable(false);
                    password.setDisable(false);
                    rememberMe.setDisable(false);
                    registerButton.setDisable(false);
                }
            }).start();
            error.setVisible(false);
            loginButton.setDisable(true);
            email.setDisable(true);
            password.setDisable(true);
            rememberMe.setDisable(true);
            registerButton.setDisable(true);
        }
    }

    @FXML
    protected void onRegisterButtonHandler() throws IOException {
        Stage s = (Stage) email.getScene().getWindow();
        s.setScene(RegisterStage.getRegistrationScene());
        s.setTitle("Register");
        s.centerOnScreen();
    }

    private void close() {
        Platform.runLater(() -> {
            Stage s = (Stage) email.getScene().getWindow();
            s.close();
            try {
                new MainStage();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
