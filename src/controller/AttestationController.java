/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import beans.Attestation;
import beans.Etudiant;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import service.AttestationService;
import service.EmployeService;

/**
 * FXML Controller class
 *
 * @author Sinponzakra
 */
public class AttestationController implements Initializable {

    ObservableList<Attestation> attestations = FXCollections.observableArrayList();
    ObservableList<Attestation> fetchAttestations = FXCollections.observableArrayList();

    AttestationService at = new AttestationService();
    EmployeService eps = new EmployeService();
    
    @FXML
    private JFXTextField search;

    @FXML
    private TableView<Attestation> mTable;

    @FXML
    private TableColumn<Attestation, Integer> numeroColumn;

    @FXML
    private TableColumn<Attestation, LocalDate> dateColumn;

    @FXML
    private TableColumn<Attestation, String> etudiantColumn;

    @FXML
    private TableColumn<Attestation, String> employeColumn;

    private void init() {
        attestations.clear();

        numeroColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        etudiantColumn.setCellValueFactory(new PropertyValueFactory<>("etudiant"));
        employeColumn.setCellValueFactory(new PropertyValueFactory<>("employe"));

        if (at.findAll() != null) {
            attestations.addAll(at.findAll());
        }

        mTable.setItems(attestations);
    }
    
    private String getValideNumInscription(String numInsc) {
       String numInscrip = numInsc;
       String newNumInsc = numInscrip.replaceAll("[^0-9-\\/]*", "");
 
       if(newNumInsc.length() != 0){
           return newNumInsc;
       }else{
           return numInscrip;
       } 
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        mTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        search.textProperty().addListener(e -> {
            fetchAttestations.clear();
            for (Attestation at : at.findAll()) {
                if (at.getEtudiant().toString().contains(search.getText())) {
                    fetchAttestations.add(at);
                }
            }
            mTable.setItems(fetchAttestations);
        });
        
        mTable.setOnMousePressed(e -> {
            try {
                TablePosition pos = (TablePosition) mTable.getSelectionModel().getSelectedCells().get(0);
                int row = pos.getRow();
                Attestation item = mTable.getItems().get(row);
                
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            
                JasperReport jr = JasperCompileManager.compileReport("src/report/HorizontalAttestationReport.jrxml");
                Map<String, Object> parameters = new HashMap<String, Object>();
                parameters.put("nomComplet", item.getEtudiant().getNomComplet());
                parameters.put("lieu", item.getEtudiant().getLieuNaissance());
                parameters.put("dateNaissance", sdf.format(item.getEtudiant().getDateNaissance()).toString() );
                parameters.put("numInscription", getValideNumInscription(item.getEtudiant().getNumInscription()));
                parameters.put("cne", item.getEtudiant().getCne());
                parameters.put("niveau", item.getEtudiant().getNiveauEtude());
                parameters.put("dateSortie", sdf.format(item.getEtudiant().getDateSortie()).toString() );
                parameters.put("decision", item.getEtudiant().getDecision());
                parameters.put("nomResponsable", eps.getDirecteur().getNom()+" "+ eps.getDirecteur().getPrenom());
                parameters.put("nomEtablissement", item.getEmploye().getEtablissement().getNom());
                parameters.put("ville", item.getEmploye().getEtablissement().getVille());
                parameters.put("codeEtablissement", item.getEmploye().getEtablissement().getCodeEtablissement());
                parameters.put("telephone", item.getEmploye().getEtablissement().getTelephone());
                parameters.put("code", item.getCode());
                parameters.put("region", item.getEmploye().getEtablissement().getRegion());
                parameters.put("direction", item.getEmploye().getEtablissement().getDirection());
                
                JRDataSource dataSource = new JREmptyDataSource();
                JasperPrint jp = JasperFillManager.fillReport(jr, parameters, dataSource);
                JasperViewer.viewReport(jp, false);
            } catch (JRException ex) {
                Logger.getLogger(AttestationController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        init();
    }

}
