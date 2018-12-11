/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import beans.Employe;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.security.Key;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import service.EmployeService;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;
import util.EffectUtilities;
import util.Util;

/**
 * FXML Controller class
 *
 * @author Sinponzakra
 */
public class LoginController implements Initializable {

    EmployeService es = new EmployeService();
    Util util = new Util();
    private static Employe emp = null;

    @FXML
    private ImageView loginMinimiseBtn;
    @FXML
    private ImageView loginCloseBtn;
    @FXML
    private JFXButton btnLogin;
    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;
    @FXML
    private Label usernameError;
    @FXML
    private Label passwordError;
    @FXML
    private JFXCheckBox rememberMe;
    @FXML
    private HBox mTopBar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        Platform.runLater(()-> {
             Stage mStage = ((Stage) ((Node) mTopBar).getScene().getWindow());
             EffectUtilities.makeDraggable(mStage, mTopBar);
        });

        loginCloseBtn.setOnMousePressed(e -> {
            Platform.exit();
        });

        loginMinimiseBtn.setOnMousePressed(e -> {
            ((Stage) ((Node) e.getSource()).getScene().getWindow()).setIconified(true);
        });

        username.textProperty().addListener(e -> {
            usernameError.setVisible(false);
        });

        password.textProperty().addListener(e -> {
            passwordError.setVisible(false);
        });

        btnLogin.setOnAction(e -> {
           Authentification();
        });
    }
    
    private void Authentification() {
         String mUsername = username.getText();
            String mPassword = password.getText();

            emp = es.CheckLogin(mUsername);
            if (emp == null) {
                usernameError.setVisible(true);
                password.requestFocus();
            } else {
                if (util.MD5(mPassword).equals(emp.getPassword())) {
                    LoadingTransition();
                    saveUserConfig();
                    
                    TrayNotification tray = new TrayNotification();
                    tray.setNotificationType(NotificationType.CUSTOM);
                    tray.setTitle("تم تسجيل الدخول بنجاح");
                    tray.setMessage("مرحا  "+emp.getPrenom()+" "+emp.getNom());
                    tray.setAnimationType(AnimationType.FADE);
                    tray.showAndDismiss(Duration.millis(1500));
                    tray.setRectangleFill(Color.valueOf("#4183D7"));
                    tray.setImage(new Image("/images/icons8_Male_User_100px_2.png"));
                } else {
                    passwordError.setVisible(true);
                    password.requestFocus();
                }
            }

    }

    private void LoadingTransition() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/vue/LoadingVue.fxml"));

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.getIcons().add(new Image(this.getClass().getResource("/images/loginLogo.png").toString()));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.show();

            ((Stage) btnLogin.getScene().getWindow()).close();

        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void saveUserConfig() {
        Preferences userPreferences = Preferences.userRoot();
        userPreferences.putInt("currentUserId", emp.getId());
        
        if (rememberMe.isSelected()) {
            userPreferences.putBoolean("rememberMe", true);
        } else {
            userPreferences.putBoolean("rememberMe", false);
        }

    }

}
