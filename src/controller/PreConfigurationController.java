/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
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
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * FXML Controller class
 *
 * @author Sinponzakra
 */
public class PreConfigurationController implements Initializable {
    @FXML
    private JFXTextField url;
    @FXML
    private JFXTextField username;
    @FXML
    private JFXTextField password;
    @FXML
    private JFXTextField dbName;
    @FXML
    private JFXButton mBtn;
    
    private void createDb() {
        try {
            String query = "CREATE DATABASE "+dbName.getText()+" CHARACTER SET utf8 COLLATE utf8_general_ci";
            Statement st = null;
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url.getText(), username.getText(), password.getText());
            st = con.createStatement();
            st.executeUpdate(query);
        } catch (Exception e) {
        }
    }
    
    private  void setupConnectionSettings(){
        Configuration configuration = new Configuration();

        configuration.configure("/config/hibernate.cfg.xml");
        
        System.out.println("URL BEFORE : "+configuration.getProperty("hibernate.connection.url"));
        configuration.setProperty("hibernate.connection.url", url.getText()+dbName.getText());
        configuration.setProperty("hibernate.connection.username", username.getText());
        configuration.setProperty("hibernate.connection.password", password.getText());
        
        System.out.println("URL AFTER : "+configuration.getProperty("hibernate.connection.url"));
    
        SessionFactory sessionFactory = configuration.buildSessionFactory();

        Session s = sessionFactory.openSession();
        s.beginTransaction();
    }
    
    private void buildConnectionPreferences(){
        Preferences userPreferences = Preferences.userRoot();
        userPreferences.put("DB_URL", url.getText()+dbName.getText());
        userPreferences.put("DB_USERNAME", username.getText());
        userPreferences.put("DB_PASSWORD", password.getText());
        userPreferences.putBoolean("dbConfigured", true);
    }
    
    private void loginStageShow() throws IOException{
        Stage stage = new Stage();
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.getIcons().add(new Image(this.getClass().getResource("/images/loginLogo.png").toString()));
        Parent root = FXMLLoader.load(getClass().getResource("/vue/LoginVue.fxml"));

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            mBtn.setOnAction(e -> {
                try {
                    createDb();
                    setupConnectionSettings();
                    ((Stage) ((Node)e.getSource()).getScene().getWindow()).close();
                     buildConnectionPreferences();
                     loginStageShow();
                } catch (IOException ex) {
                    Logger.getLogger(PreConfigurationController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        });
    }    
    
}
