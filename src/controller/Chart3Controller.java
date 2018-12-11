/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.jfoenix.controls.JFXComboBox;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import service.EtudiantService;

/**
 * FXML Controller class
 *
 * @author Sinponzakra
 */
public class Chart3Controller implements Initializable {

    @FXML
    private JFXComboBox<Integer> mYears;
    ObservableList<Integer> years
            = FXCollections.observableArrayList();

    @FXML
    private Label noDataToDisplay;
    @FXML
    private PieChart mPieChart;
    ObservableList<PieChart.Data> pieChartData
            = FXCollections.observableArrayList();

    EtudiantService es = new EtudiantService();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set years values
        for (Integer i = 1900; i <= 2200; i++) {
            years.add(i);
        }
        mYears.getItems().clear();
        mYears.getItems().addAll(years);

        mYears.setOnAction(e -> {
            pieChartData.clear();
            
            try {
               es.getPieChartData2(mYears.getValue()).forEach((o) -> {
                if (!o[0].toString().equals("")) {
                    pieChartData.add(
                            new PieChart.Data(o[0].toString(), Integer.parseInt(o[1].toString()))
                    );
                }
            }); 
            } catch (Exception ee) {
            }
            

            mPieChart.getData().clear();
            if (pieChartData.size() != 0) {
                noDataToDisplay.setVisible(false);
                mPieChart.setTitle("نسب المستويات الدراسية");
                mPieChart.getData().addAll(pieChartData);
            }else{
                mPieChart.setTitle("");
                noDataToDisplay.setVisible(true);
            }
            
            mPieChart.getData().forEach(d -> {
            Optional<Node> opTextNode = mPieChart.lookupAll(".chart-pie-label").stream().filter(n -> n instanceof Text && ((Text) n).getText().contains(d.getName())).findAny();
            if (opTextNode.isPresent()) {
                Double res = ((d.getPieValue() / es.getEtudiantsCount()) * 100);
                DecimalFormat df = new DecimalFormat();
                df.setMaximumFractionDigits(0);
                ((Text) opTextNode.get()).setText(d.getName() + " " + df.format(res) + " %");
            }
        });

        });
        
        
    }

}
