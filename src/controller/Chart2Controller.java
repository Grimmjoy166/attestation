/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import service.EmployeService;

/**
 * FXML Controller class
 *
 * @author Sinponzakra
 */
public class Chart2Controller implements Initializable {

    @FXML
    private AnchorPane container;
    @FXML
    private BarChart<String, Number> mBarChart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    
    EmployeService emps = new EmployeService();
    

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mBarChart.getData().clear();
        mBarChart.setTitle("عدد الموظفين في كل وظيفة");
        xAxis.setLabel("الوظائف");
        yAxis.setLabel("عدد الموظفين");

        XYChart.Series series = new XYChart.Series();
        for (Object[] o : emps.getChartData()) {
            series.getData().add(new XYChart.Data(o[0].toString(), Integer.parseInt(o[1].toString())));
        }

        mBarChart.getData().add(series);
    }    
    
}
