/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Sinponzakra
 */
public class ConfirmBoxController implements Initializable {
    //class attr
    private String mTitle;
    private String mMessage;
    private static boolean state = false;
    Preferences userPreferences = Preferences.userRoot();
    
    //Fxml Attr
    @FXML
    private ImageView exitBarBtn;

    @FXML
    private Label mainText;

    @FXML
    private JFXButton cancelBtn;

    @FXML
    private JFXButton confirmBtn;

    @FXML
    private Label title;
    @FXML
    private HBox mTopBar;

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public boolean getCurrentState(){
        return state;
    }

    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        exitBarBtn.setOnMousePressed(e -> {
            ((Stage)exitBarBtn.getScene().getWindow()).close();
        });
        
          cancelBtn.setOnAction(e -> {
            state = false;
            ((Stage)cancelBtn.getScene().getWindow()).close();
        });
        
        confirmBtn.setOnAction(e -> {
           state = true;
          ((Stage)cancelBtn.getScene().getWindow()).close();
        }); 
        
        
//        cancelBtn.setOnAction(e -> {
//            ((Stage)cancelBtn.getScene().getWindow()).close();
//        });
//        
//        confirmBtn.setOnAction(e -> {
//            try {
//                userPreferences.clear();
//                
//                Parent root = FXMLLoader.load(getClass().getResource("/vue/LoginVue.fxml"));
//                
//                Scene scene = new Scene(root);
//                scene.setFill(Color.TRANSPARENT);
//                Stage stage = new Stage();
//                stage.initStyle(StageStyle.TRANSPARENT);
//                stage.getIcons().add(new Image(this.getClass().getResource("/images/loginLogo.png").toString()));
//                stage.setScene(scene);
//                stage.show();
//                
//                ((Stage)confirmBtn.getScene().getWindow()).close();
//                previousStage.close();
                
//            } catch (BackingStoreException ex) {
//                Logger.getLogger(ConfirmBoxController.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (IOException ex) {
//                Logger.getLogger(ConfirmBoxController.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//        });
        
        Platform.runLater(() -> {
            title.setText(mTitle);
            mainText.setText(mMessage);
        });
      

    }    
    
}
