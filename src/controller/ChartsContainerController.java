/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Sinponzakra
 */
public class ChartsContainerController implements Initializable {

    @FXML
    private StackPane parentContainer;

    @FXML
    private AnchorPane anchorRoot;

    @FXML
    private JFXButton btnPrev;

    @FXML
    private JFXButton btnNext;
    ArrayList<String> chartsPaths = new ArrayList<String>();
    private static int cmp = 0;
    
    private void loadPrevScene(String Path) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(Path));
        root.translateXProperty().set(parentContainer.getWidth());

        parentContainer.getChildren().add(root);

        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
        timeline.getKeyFrames().add(kf);
        timeline.setOnFinished(t -> {
            parentContainer.getChildren().remove(0);
        });
        timeline.play();

    }
    
    private void loadNextScene(String Path) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(Path));
        root.translateXProperty().set(-parentContainer.getWidth());

        parentContainer.getChildren().add(root);

        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
        timeline.getKeyFrames().add(kf);
        timeline.setOnFinished(t -> {
            parentContainer.getChildren().remove(0);
        });
        timeline.play();

    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            loadPrevScene("/vue/Chart1Vue.fxml");
        } catch (IOException ex) {
            Logger.getLogger(ChartsContainerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        chartsPaths.add("/vue/Chart1Vue.fxml");
        chartsPaths.add("/vue/Chart2Vue.fxml");
        chartsPaths.add("/vue/Chart3Vue.fxml");
        
        btnNext.setOnAction(e->{
            try {
                if(cmp == 2){
                    cmp = 0;
                    loadNextScene(chartsPaths.get(cmp));
                }else{
                    cmp++;
                    loadNextScene(chartsPaths.get(cmp));
                }
            } catch (IOException ex) {
                Logger.getLogger(ChartsContainerController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        btnPrev.setOnAction(e->{
             try {
                if(cmp == 0){
                    cmp = 2;
                    loadPrevScene(chartsPaths.get(cmp));
                }else{
                    cmp--;
                    loadPrevScene(chartsPaths.get(cmp));
                }
            } catch (IOException ex) {
                Logger.getLogger(ChartsContainerController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }    
    
}
