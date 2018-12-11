/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Sinponzakra
 */
public class LoadingController implements Initializable {

    @FXML
    private StackPane rootPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         new SplashScreen().start();
    }

    class SplashScreen extends Thread {

        @Override
        public void run() {
            try {
                Thread.sleep(3000);

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                           Parent root = null;
                        try {
                            root = FXMLLoader.load(getClass().getResource("/vue/MainVue.fxml"));
                            
                            Scene scene = new Scene(root);
                            Stage stage = new Stage();
                            stage.setMaximized(true);
                            stage.getIcons().add(new Image(this.getClass().getResource("/images/loginLogo.png").toString())); 
                            stage.initStyle(StageStyle.UNDECORATED);
                            stage.setScene(scene);
                            stage.show();
                            
                            rootPane.getScene().getWindow().hide();
                        } catch (IOException ex) {
                            Logger.getLogger(LoadingController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });

            } catch (InterruptedException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
