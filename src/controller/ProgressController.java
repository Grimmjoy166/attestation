/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 * FXML Controller class
 *
 * @author Sinponzakra
 */
public class ProgressController implements Initializable {

    @FXML
    private ImageView exitBarBtn;
    @FXML
    private ProgressBar mProgress;
    @FXML
    private ProgressIndicator mIndicator;
    @FXML
    private Label mMessage;

    public ProgressBar getmProgress() {
        return mProgress;
    }

    public void setmProgress(ProgressBar mProgress) {
        this.mProgress = mProgress;
    }

    public ProgressIndicator getmIndicator() {
        return mIndicator;
    }

    public void setmIndicator(ProgressIndicator mIndicator) {
        this.mIndicator = mIndicator;
    }

    public Label getmMessage() {
        return mMessage;
    }

    public void setmMessage(Label mMessage) {
        this.mMessage = mMessage;
    }
    
    
    
   
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        exitBarBtn.setOnMousePressed(e -> {
            ((Stage)exitBarBtn.getScene().getWindow()).close();
        });
    }    
    
}
