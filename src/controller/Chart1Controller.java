/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

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
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import service.EtudiantService;

/**
 * FXML Controller class
 *
 * @author Sinponzakra
 */
public class Chart1Controller implements Initializable {

    @FXML
    private AnchorPane container;
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
         mPieChart.setTitle("عدد التلاميذ في مستويات الدراسة");
        for (Object[] o : es.getPieChartData()) {
            if (!o[0].toString().equals("") || o[0] != null) {
                pieChartData.add(
                        new PieChart.Data(o[0].toString(), Integer.parseInt(o[1].toString())));
            }
        }

        mPieChart.getData().addAll(pieChartData);

        mPieChart.getData().forEach(d -> {
            Optional<Node> opTextNode = mPieChart.lookupAll(".mPieChart-pie-label").stream().filter(n -> n instanceof Text && ((Text) n).getText().contains(d.getName())).findAny();
            if (opTextNode.isPresent()) {
                Double res = ((d.getPieValue() / es.getEtudiantsCount()) * 100);
                DecimalFormat df = new DecimalFormat();
                df.setMaximumFractionDigits(0);
                ((Text) opTextNode.get()).setText(d.getName() + " " + df.format(res) + " %");
            }
        });
    }    
    
}
