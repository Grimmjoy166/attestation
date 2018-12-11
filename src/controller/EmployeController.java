/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import beans.Employe;
import beans.Etablissement;
import beans.Profil;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import service.EmployeService;
import service.EtablissementService;
import service.ProfilService;
import util.Util;

/**
 * FXML Controller class
 *
 * @author Sinponzakra
 */
public class EmployeController implements Initializable {

    //Utils
    Util util = new Util();

    //services
    ProfilService ps = new ProfilService();
    EmployeService es = new EmployeService();
    EtablissementService ets = new EtablissementService();

    //init Lists
    ObservableList<Profil> profils = FXCollections.observableArrayList();
    ObservableList<Employe> employes = FXCollections.observableArrayList();
    ObservableList<Etablissement> etablissements = FXCollections.observableArrayList();

    //inner static varriable
    private static int index;
    private static int selectedProfilId;
    private static Etablissement currentEtab;

    Date dt = new Date();
    Date dt2 = new Date();

    //Fields
    @FXML
    private TextField nom;
    @FXML
    private TextField prenom;
    @FXML
    private DatePicker date;
    @FXML
    private DatePicker dateEmb;
    @FXML
    private ComboBox<Profil> profil;
    @FXML
    private TextField email;
    @FXML
    private TextField password;
    @FXML
    private ComboBox<Etablissement> etablissement;
    @FXML
    private TableView<Employe> mTable;
    @FXML
    private TableColumn<Employe, String> nomColumn;
    @FXML
    private TableColumn<Employe, String> prenomColumn;
    @FXML
    private TableColumn<Employe, LocalDate> dateColumn;
    @FXML
    private TableColumn<Employe, LocalDate> dateEmbColumn;
    @FXML
    private TableColumn<Employe, Profil> profilColumn;
    @FXML
    private TableColumn<Employe, String> emailColumn;
    @FXML
    private TableColumn<Employe, Etablissement> etablissementColumn;

    //FXML Methods
    @FXML
    private void saveAction(ActionEvent e) {
        Instant instant = Instant.from(date.getValue().atStartOfDay(ZoneId.systemDefault()));
        Instant instant2 = Instant.from(dateEmb.getValue().atStartOfDay(ZoneId.systemDefault()));

        dt = Date.from(instant);
        dt2 = Date.from(instant2);

        es.create(new Employe(nom.getText(), prenom.getText(), dt, dt2, ps.findById(selectedProfilId), email.getText(), util.MD5(password.getText()), currentEtab));
        init();
        clearFields();
    }

    @FXML
    private void deleteAction(ActionEvent e) throws IOException {

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.initStyle(StageStyle.UNDECORATED);
        window.getIcons().add(new Image(this.getClass().getResource("/images/loginLogo.png").toString()));

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vue/ConfirmBoxVue.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        ConfirmBoxController controller = fxmlLoader.<ConfirmBoxController>getController();
        controller.setmMessage("هل حقا تريد حدف هذا الموظف ؟");
        controller.setmTitle("تأكيد الحدف");

        Scene scene = new Scene(root);

        window.setScene(scene);
        window.showAndWait();

        if (controller.getCurrentState()) {
            es.delete(es.findById(index));
            init();
            clearFields();
        }
    }

    @FXML
    private void updateAction(ActionEvent e) throws IOException {

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.initStyle(StageStyle.UNDECORATED);
        window.getIcons().add(new Image(this.getClass().getResource("/images/loginLogo.png").toString()));

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vue/ConfirmBoxVue.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        ConfirmBoxController controller = fxmlLoader.<ConfirmBoxController>getController();
        controller.setmMessage("هل تريد تغيير معلومات هذا الموظف ؟");
        controller.setmTitle("تأكيد التغيير");

        Scene scene = new Scene(root);

        window.setScene(scene);
        window.showAndWait();

        if (controller.getCurrentState()) {
            Instant instant = Instant.from(date.getValue().atStartOfDay(ZoneId.systemDefault()));
            dt = Date.from(instant);
            Instant instant2 = Instant.from(dateEmb.getValue().atStartOfDay(ZoneId.systemDefault()));
            dt2 = Date.from(instant2);

            Employe emp = es.findById(index);
            emp.setNom(nom.getText());
            emp.setPrenom(prenom.getText());
            emp.setDateNaissance(dt);
            emp.setDateEmbauche(dt2);
            emp.setProfil(ps.findById(selectedProfilId));
            emp.setEmail(email.getText());
            if (!password.getText().isEmpty()) {
                emp.setPassword(util.MD5(password.getText()));
            }
            emp.setEtablissement(currentEtab);
            es.update(emp);
            init();
            clearFields();
        }
    }

    //clear Fields function
    public void clearFields() {
        nom.clear();
        prenom.clear();
        date.setValue(null);
        dateEmb.setValue(null);
        profil.getSelectionModel().clearSelection();
        email.clear();
        password.clear();
    }

    //init function
    private void init() {
        profils.clear();
        employes.clear();
        etablissements.clear();

        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateNaissance"));
        dateEmbColumn.setCellValueFactory(new PropertyValueFactory<>("dateEmbauche"));
        profilColumn.setCellValueFactory(new PropertyValueFactory<>("profil"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        etablissementColumn.setCellValueFactory(new PropertyValueFactory<>("etablissement"));

        if (ps.findAll() != null) {
            profils.addAll(ps.findAll());
        }
        if (es.findAll() != null) {
            employes.addAll(es.findAll());
        }
        if (ets.findAll() != null) {
            etablissements.addAll(ets.findAll());
        }

        profil.setItems(profils);
        mTable.setItems(employes);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        init();
        mTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        profil.setOnAction(e -> {
            try {
                Profil p = profil.getSelectionModel().getSelectedItem();
                selectedProfilId = p.getId();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        });

        mTable.setOnMousePressed(e -> {
            TablePosition pos = (TablePosition) mTable.getSelectionModel().getSelectedCells().get(0);
            int row = pos.getRow();
            Employe item = mTable.getItems().get(row);
            index = item.getId();

            nom.setText(item.getNom());
            prenom.setText(item.getPrenom());

            Date dts = item.getDateNaissance();
            Date dts2 = item.getDateEmbauche();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            LocalDate localDate = LocalDate.parse(sdf.format(dts), formatter);
            LocalDate localDate2 = LocalDate.parse(sdf.format(dts2), formatter);

            date.setValue(localDate);
            dateEmb.setValue(localDate2);

            profil.getSelectionModel().select(item.getProfil());

            email.setText(item.getEmail());

        });

        Preferences userPreferences = Preferences.userRoot();
        int currentUserId = userPreferences.getInt("currentUserId", 0);
        Employe e = es.findById(currentUserId);
        currentEtab = e.getEtablissement();
        etablissement.getSelectionModel().select(currentEtab);
    }

}
