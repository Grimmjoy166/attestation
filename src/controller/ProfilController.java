/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import beans.Profil;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import service.ProfilService;

/**
 * FXML Controller class
 *
 * @author Sinponzakra
 */
public class ProfilController implements Initializable {

    ProfilService ps = new ProfilService();
    ObservableList<Profil> profils = FXCollections.observableArrayList();
    private static int index;
    @FXML
    private TextField code;
    @FXML
    private TextField libelle;
    @FXML
    private TableView<Profil> mTable;
    @FXML
    private TableColumn<Profil, String> codeColumn;
    @FXML
    private TableColumn<Profil, String> libelleColumn;

    @FXML
    private void saveAction(ActionEvent e) {
        ps.create(new Profil(code.getText(), libelle.getText()));
        init();
        clearFields();
    }

    @FXML
    private void deleteAction(ActionEvent event) throws IOException {

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.initStyle(StageStyle.UNDECORATED);
        window.getIcons().add(new Image(this.getClass().getResource("/images/loginLogo.png").toString()));

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vue/ConfirmBoxVue.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        ConfirmBoxController controller = fxmlLoader.<ConfirmBoxController>getController();
        controller.setmMessage("هل حقا تريد حدف هده الوظيفة ؟");
        controller.setmTitle("تأكيد الحدف");

        Scene scene = new Scene(root);

        window.setScene(scene);
        window.showAndWait();

        if (controller.getCurrentState()) {
            ps.delete(ps.findById(index));
            init();
            clearFields();
        }
    }

    @FXML
    private void updateAction(ActionEvent event) throws IOException {

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.initStyle(StageStyle.UNDECORATED);
        window.getIcons().add(new Image(this.getClass().getResource("/images/loginLogo.png").toString()));

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vue/ConfirmBoxVue.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        ConfirmBoxController controller = fxmlLoader.<ConfirmBoxController>getController();
        controller.setmMessage("هل تريد حقا تغيير معلومات هذه الوظيفة ؟");
        controller.setmTitle("تأكيد التغيير");

        Scene scene = new Scene(root);

        window.setScene(scene);
        window.showAndWait();

        if (controller.getCurrentState()) {
            Profil p = ps.findById(index);
            p.setCode(code.getText());
            p.setLibelle(libelle.getText());
            ps.update(p);
            init();
            clearFields();
        }
    }

    private void clearFields() {
        code.clear();
        libelle.clear();
    }

    private void init() {
        profils.clear();
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        libelleColumn.setCellValueFactory(new PropertyValueFactory<>("libelle"));

        if (ps.findAll() != null) {
            profils.addAll(ps.findAll());
        }

        mTable.setItems(profils);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        init();
        mTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        mTable.setOnMousePressed(e -> {
            TablePosition pos = (TablePosition) mTable.getSelectionModel().getSelectedCells().get(0);
            int row = pos.getRow();
            Profil item = mTable.getItems().get(row);
            index = item.getId();

            code.setText(item.getCode());
            libelle.setText(item.getLibelle());
        });
    }

}
