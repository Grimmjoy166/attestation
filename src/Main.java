/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import controller.LoginController;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


/**
 *
 * @author Sinponzakra
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Preferences userPreferences = Preferences.userRoot();
        if(!userPreferences.getBoolean("dbConfigured", false)){
                stage.setTitle("منصة الاعدادات");
                stage.getIcons().add(new Image(this.getClass().getResource("/images/loginLogo.png").toString()));
                Parent root = FXMLLoader.load(getClass().getResource("vue/PreConfigurationVue.fxml"));

                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
        }else{
            if (userPreferences.getBoolean("rememberMe", false)) {
                LoadingTransition();
            } else {
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.getIcons().add(new Image(this.getClass().getResource("/images/loginLogo.png").toString()));
                Parent root = FXMLLoader.load(getClass().getResource("vue/LoginVue.fxml"));

                Scene scene = new Scene(root);
                scene.setFill(Color.TRANSPARENT);
                stage.setScene(scene);
                stage.show();
            }  
        }
        
    }

    private void LoadingTransition() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("vue/LoadingVue.fxml"));

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.getIcons().add(new Image(this.getClass().getResource("images/loginLogo.png").toString()));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
  
    
   

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        Preferences userPreferences = Preferences.userRoot();
//        userPreferences.remove("dbConfigured");
         launch(args);
    }

}
