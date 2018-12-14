/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import beans.Attestation;
import beans.AttestationPK;
import beans.Employe;
import beans.Etablissement;
import beans.Etudiant;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import org.hibernate.HibernateException;
import service.AttestationService;
import service.EmployeService;
import service.EtablissementService;
import service.EtudiantService;

/**
 * FXML Controller class
 *
 * @author Sinponzakra
 */
public class SearchController implements Initializable {

    EtudiantService es = new EtudiantService();
    EtablissementService ets = new EtablissementService();
    EmployeService eps = new EmployeService();
    AttestationService as = new AttestationService();

    ObservableList<Etudiant> etudiants = FXCollections.observableArrayList();
    ObservableList<Etudiant> fetchedEtudiants = FXCollections.observableArrayList();
    ObservableList<Etudiant> etudiants2 = FXCollections.observableArrayList();

    //inner static varriable
    private static int index;
    private static Etablissement currentEtab;

    Date dt = new Date();

    @FXML
    private TextField nom;
    //Table 1
    @FXML
    private TableView<Etudiant> mTable;
    @FXML
    private TableColumn<Etudiant, String> nomCompletColumn;
    @FXML
    private TableColumn<Etudiant, LocalDate> dateColumn;

    //Table 2
    @FXML
    private TableView<Etudiant> mTable1;
    @FXML
    private TableColumn<Etudiant, String> nomCompletColumn1;
    @FXML
    private TableColumn<Etudiant, LocalDate> dateColumn1;
    @FXML
    private TableColumn<Etudiant, String> lieuColumn1;
    @FXML
    private TableColumn<Etudiant, String> cneColumn1;
    @FXML
    private TableColumn<Etudiant, String> niveauColumn1;
    @FXML
    private TableColumn<Etudiant, String> numInscriptionColumn1;
    @FXML
    private TableColumn<Etudiant, String> decisionColumn1;
    @FXML
    private TableColumn<Etudiant, Integer> numDossierColumn1;
    @FXML
    private TableColumn<Etudiant, Etablissement> etablissementColumn1;

    private void configTableColumn() {
        nomCompletColumn.setCellValueFactory(new PropertyValueFactory<>("nomComplet"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateNaissance"));

        nomCompletColumn1.setCellValueFactory(new PropertyValueFactory<>("nomComplet"));
        dateColumn1.setCellValueFactory(new PropertyValueFactory<>("dateNaissance"));
        lieuColumn1.setCellValueFactory(new PropertyValueFactory<>("lieuNaissance"));
        cneColumn1.setCellValueFactory(new PropertyValueFactory<>("cne"));
        niveauColumn1.setCellValueFactory(new PropertyValueFactory<>("niveauEtude"));
        numInscriptionColumn1.setCellValueFactory(new PropertyValueFactory<>("numInscription"));
        decisionColumn1.setCellValueFactory(new PropertyValueFactory<>("decision"));
        numDossierColumn1.setCellValueFactory(new PropertyValueFactory<>("numDossier"));
        etablissementColumn1.setCellValueFactory(new PropertyValueFactory<>("etablissement"));
    }

    //init function
    private void init() {
        etudiants.clear();

        configTableColumn();

        if (es.findAllbyEtab(currentEtab) != null) {
            etudiants.addAll(es.findAllbyEtab(currentEtab));
        }

        mTable.setItems(etudiants);

    }

    private void fillTab1(ObservableList<Etudiant> searchedEtud) {
        mTable.setItems(searchedEtud);
        mTable1.setVisible(false);
    }

    private void fillTab2(Etudiant selectedEtud) {
        etudiants2.clear();

        if (es.findById(selectedEtud.getId()) != null) {
            etudiants2.add(es.findById(selectedEtud.getId()));
        }

        mTable1.setItems(etudiants2);
        mTable1.setVisible(true);
    }

    private String getValideNumInscription(String numInsc) {
        String numInscrip = numInsc;
        String newNumInsc = numInscrip.replaceAll("[^0-9-\\/]*", "");

        if (newNumInsc.length() != 0) {
            return newNumInsc;
        } else {
            return numInscrip;
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        mTable1.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        mTable.setOnMousePressed(e -> {
            TablePosition pos = (TablePosition) mTable.getSelectionModel().getSelectedCells().get(0);
            int row = pos.getRow();
            Etudiant item = mTable.getItems().get(row);
            index = item.getId();

            fillTab2(item);

        });

        mTable1.setOnMousePressed(e -> {
            try {
                TablePosition pos = (TablePosition) mTable1.getSelectionModel().getSelectedCells().get(0);
                int row = pos.getRow();
                Etudiant item = mTable1.getItems().get(row);

                Preferences userPreferences = Preferences.userRoot();
                int currentUserId = userPreferences.getInt("currentUserId", 0);

                Employe currentEmploye = eps.findById(currentUserId);

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                int res = userPreferences.getInt("nextNumero", -1);
                if (res != -1) {
                    int next = userPreferences.getInt("nextNumero", -1) + 1;
                    System.out.println("----------------------next1 = " + next);
                    userPreferences.putInt("nextNumero", next);
                } else if (as.getLastInsertedYear() != new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear()) {
                    userPreferences.remove("nextNumero");
                } else {
                    userPreferences.putInt("nextNumero", 1);
                }
                Attestation attestationCreated = null;
               
                    attestationCreated = as.createWithFeedBack(new Attestation(new AttestationPK(new Date(), currentEmploye.getId(), item.getId()), currentEmploye, item));

                    if (attestationCreated != null) {
                        JasperReport jr = JasperCompileManager.compileReport("src/report/HorizontalAttestationReport.jrxml");
                        Map<String, Object> parameters = new HashMap<String, Object>();
                        parameters.put("nomComplet", item.getNomComplet());
                        parameters.put("lieu", item.getLieuNaissance());
                        parameters.put("dateNaissance", sdf.format(item.getDateNaissance()).toString());
                        parameters.put("numInscription", getValideNumInscription(item.getNumInscription()));
                        parameters.put("cne", item.getCne());
                        parameters.put("niveau", item.getNiveauEtude());
                        parameters.put("dateSortie", sdf.format(item.getDateSortie()).toString());
                        parameters.put("decision", item.getDecision());
                        parameters.put("nomResponsable", eps.getDirecteur().getNom() + " " + eps.getDirecteur().getPrenom());
                        parameters.put("nomEtablissement", currentEmploye.getEtablissement().getNom());
                        parameters.put("ville", currentEmploye.getEtablissement().getVille());
                        parameters.put("codeEtablissement", currentEmploye.getEtablissement().getCodeEtablissement());
                        parameters.put("telephone", currentEmploye.getEtablissement().getTelephone());
                        parameters.put("code", attestationCreated.getCode());
                        parameters.put("region", currentEmploye.getEtablissement().getRegion());
                        parameters.put("direction", currentEmploye.getEtablissement().getDirection());

                        JRDataSource dataSource = new JREmptyDataSource();
                        JasperPrint jp = JasperFillManager.fillReport(jr, parameters, dataSource);
                        JasperViewer.viewReport(jp, false);
                    }else{
                        int next = userPreferences.getInt("nextNumero", -1) - 1;
                        System.out.println("----------------------next1 = " + next);
                        userPreferences.putInt("nextNumero", next);
                    }
               
            } catch (JRException ex) {
                Logger.getLogger(SearchController.class.getName()).log(Level.SEVERE, null, ex);
            }catch(Exception e1){
            }
        });

        nom.textProperty().addListener(e -> {

            fetchedEtudiants.clear();

            for (Etudiant ee : es.findAllbyEtab(currentEtab)) {
                if (ee.toString().contains(nom.getText())) {
                    fetchedEtudiants.add(ee);
                }
            }
            fillTab1(fetchedEtudiants);
        });

        Preferences userPreferences = Preferences.userRoot();
        int currentUserId = userPreferences.getInt("currentUserId", 0);
        Employe e = eps.findById(currentUserId);
        currentEtab = e.getEtablissement();

        init();
    }

}
