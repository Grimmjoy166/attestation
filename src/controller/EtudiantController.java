/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import beans.Employe;
import beans.Etablissement;
import beans.Etudiant;
import com.jfoenix.controls.JFXComboBox;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import service.EmployeService;
import service.EtablissementService;
import service.EtudiantService;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author Sinponzakra
 */
public class EtudiantController implements Initializable {

    EtudiantService es = new EtudiantService();
    EtablissementService ets = new EtablissementService();
    EmployeService eps = new EmployeService();

    ObservableList<Etudiant> etudiants = FXCollections.observableArrayList();
    ObservableList<Etudiant> fetchedEtudiants = FXCollections.observableArrayList();
    ObservableList<Etablissement> etablissemnts = FXCollections.observableArrayList();
    ObservableList<String> decisions = FXCollections.observableArrayList();

    //inner static varriable
    private static int index;
    private static int selectedEtablissementId;
    private static Etablissement currentEtab;

    Date dt = new Date();
    Date dt2 = new Date();

    //Fields
    @FXML
    private TextField nom;
    @FXML
    private TextField nomComplet;
    @FXML
    private DatePicker date;
    @FXML
    private TextField lieu;
    @FXML
    private TextField cne;
    @FXML
    private TextField niveau;
    @FXML
    private TextField numInscription;
    @FXML
    private ComboBox<String> decision;
    @FXML
    private DatePicker dateSortie;
    @FXML
    private TextField numDossier;
    @FXML
    private JFXComboBox<Etablissement> etablissement;
    @FXML
    private TableView<Etudiant> mTable;
    @FXML
    private TableColumn<Etudiant, String> nomCompletColumn;
    @FXML
    private TableColumn<Etudiant, LocalDate> dateColumn;
    @FXML
    private TableColumn<Etudiant, String> lieuColumn;
    @FXML
    private TableColumn<Etudiant, String> cneColumn;
    @FXML
    private TableColumn<Etudiant, String> niveauColumn;
    @FXML
    private TableColumn<Etudiant, String> numInscriptionColumn;
    @FXML
    private TableColumn<Etudiant, String> decisionColumn;
    @FXML
    private TableColumn<Etudiant, Integer> numDossierColumn;
    @FXML
    private TableColumn<Etudiant, LocalDate> dateSortieColumn;
    @FXML
    private TableColumn<Etudiant, Etablissement> etablissementColumn;

    private ProgressController controller;
    private Stage window;
    public static int j = 0;
    public static int EtudAdded = 0;
    public static int rowWithOutBlank = 0;

    //FXML Methods
    @FXML
    private void saveAction(ActionEvent e) {
        Instant instant = Instant.from(date.getValue().atStartOfDay(ZoneId.systemDefault()));
        dt = Date.from(instant);
        Instant instant2 = Instant.from(dateSortie.getValue().atStartOfDay(ZoneId.systemDefault()));
        dt2 = Date.from(instant2);

        es.create(new Etudiant(nomComplet.getText(), dt, lieu.getText(), cne.getText(), niveau.getText(), numInscription.getText(), decision.getSelectionModel().getSelectedItem(), Integer.parseInt(numDossier.getText()), dt2, ets.findById(selectedEtablissementId)));
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
        controller.setmMessage("هل تريد حدف هذا التلميذ ؟");
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
        controller.setmMessage("هل تريد تغيير معلومات هذا التلميذ ؟");
        controller.setmTitle("تأكيد التغيير");

        Scene scene = new Scene(root);

        window.setScene(scene);
        window.showAndWait();

        if (controller.getCurrentState()) {
            Instant instant = Instant.from(date.getValue().atStartOfDay(ZoneId.systemDefault()));
            dt = Date.from(instant);
            Instant instant2 = Instant.from(dateSortie.getValue().atStartOfDay(ZoneId.systemDefault()));
            dt2 = Date.from(instant2);

            Etudiant et = es.findById(index);
            et.setNomComplet(nomComplet.getText());
            et.setDateNaissance(dt);
            et.setLieuNaissance(lieu.getText());
            et.setCne(cne.getText());
            et.setNiveauEtude(niveau.getText());
            et.setNumInscription(numInscription.getText());
            et.setDecision(decision.getSelectionModel().getSelectedItem());
            et.setNumDossier(Integer.parseInt(numDossier.getText()));
            et.setDateSortie(dt2);
            et.setEtablissement(ets.findById(selectedEtablissementId));
            es.update(et);
            init();
            clearFields();
        }
    }

    @FXML
    private void importeFile(ActionEvent e) throws IOException, FileNotFoundException, ParseException {
        FileChooser fc = new FileChooser();

        fc.setTitle("قم باختيار الملف");
        // fc.setInitialDirectory(new File("C:\\Users\\Sinponzakra\\Desktop"));
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Fichier Excel", "*.xlsx"),
                new FileChooser.ExtensionFilter("Fichier CSV", "*.csv")
        );

        Stage mStage = (Stage) ((Node) e.getSource()).getScene().getWindow();

        if (mStage != null) {
            File mFile = fc.showOpenDialog(mStage);
            if (mFile != null) {
                saveFromFile(mFile);
            }
        }

    }

   /* private boolean isValideNumInscription(String numInscription) {
        String res = numInscription.replaceAll("[0-9-\\/]", "");
        if (res.length() == 0) {
            return true;
        } else {
            return false;
        }
    }*/

    List<String> formatStrings = Arrays.asList("dd-MMM-yyyy", "dd-MM-yyyy");

    private Date tryParse(String dateString) {
        for (String formatString : formatStrings) {
            try {
                return new SimpleDateFormat(formatString, Locale.FRENCH).parse(dateString);
            } catch (ParseException e) {
            }
        }

        return null;
    }

    private void saveFromFile(File mFile) throws FileNotFoundException, IOException, ParseException {
        FileInputStream file = new FileInputStream(mFile);
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt(1);
        
        System.out.println("hello :"+sheet.getLastRowNum());
        
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            if (sheet.getRow(i).getCell(0).toString().equals("") && sheet.getRow(i).getCell(1).toString().equals("") && sheet.getRow(i).getCell(2).toString().equals("")) {
                rowWithOutBlank = sheet.getRow(i).getRowNum();
                break;
            }else{
                rowWithOutBlank = sheet.getLastRowNum();
            }
        }
        System.out.println("ListROW = " + rowWithOutBlank);
        showProgress();

        Task<Integer> task = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                Row row;
                String errors = "";
                for (j = 1; j <= rowWithOutBlank; j++) {
                    if (sheet.getRow(j) != null) {
                        row = (Row) sheet.getRow(j);

                        String numInscription = "";
                        if (row.getCell(1) == null || row.getCell(1).toString().equals("")) {
                            errors += " Error Row :" + j + " Cell :" + 1;
                            continue;
                        } else {
                           
                                numInscription = row.getCell(1).toString();
                          
                        }

                        String nomComplet;
                        if (row.getCell(2) == null) {
                            nomComplet = null;
                        } else {
                            nomComplet = row.getCell(2).toString();
                        }

                        Date dt;
                        if (row.getCell(3) == null) {
                            dt = null;
                        } else {
                            dt = tryParse(row.getCell(3).toString());
                        }

                        Date dt2;
                        if (row.getCell(7) == null) {
                            dt2 = null;
                        } else {
                            dt2 = tryParse(row.getCell(7).toString());
                        }

                        String lieu;
                        if (row.getCell(4) == null) {
                            lieu = null;
                        } else {
                            lieu = row.getCell(4).toString();
                        }

                        String cne;
                        if (row.getCell(6) == null) {
                            cne = null;
                        } else {
                            cne = row.getCell(6).toString().replaceAll("\\.", "");
                        }

                        String niveau;
                        if (row.getCell(5) == null) {
                            niveau = null;
                        } else {
                            niveau = row.getCell(5).toString();
                        }

                        String decision = "";
                        if (row.getCell(8) == null) {
                            decision = null;
                        } else {
                            decision = row.getCell(8).toString();
                        }

                        int numDossier = -1;
                        if (row.getCell(9) == null) {
                            numDossier = -1;
                        } else {
                            Double getNumber = Double.parseDouble(row.getCell(9).toString());
                            numDossier = getNumber.intValue();
                        }

                        if (es.isNotExist(nomComplet, dt)) {
                            Etudiant e = new Etudiant(nomComplet, dt, lieu, cne, niveau, numInscription, decision, numDossier, dt2, currentEtab);
                            es.create(e);
                            EtudAdded++;

                            if (isCancelled()) {
                                break;
                            } else {
                                updateProgress(j, rowWithOutBlank);
                                updateMessage(row.getCell(0).toString()+"  "+ row.getCell(2).toString());
                            }

                        } else {
                            if (isCancelled()) {
                                break;
                            } else {
                                updateProgress(j, rowWithOutBlank);
                                updateMessage(row.getCell(0).toString()+"  "+ row.getCell(2).toString());
                            }
                        }

                    } else {
                        break;
                    }
                }
                Thread.sleep(10);
                return j;
            }

            @Override
            protected void cancelled() {
                super.cancelled();
                init();
                window.close();            
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                init();
                if (EtudAdded != 0) {

                    String title = "إعلام";
                    String message = "لقد تمت الإضافة بنجاح";

                    TrayNotification tray = new TrayNotification();
                    tray.setTitle(title);
                    tray.setMessage(message);
                    tray.setRectangleFill(Paint.valueOf("#2A9A84"));
                    tray.setAnimationType(AnimationType.POPUP);
                    tray.setNotificationType(NotificationType.SUCCESS);
                    tray.showAndDismiss(Duration.seconds(3));
                } else {

                    String title = "إعلام";
                    String message = "ليس هناك أي تحديث متاح";

                    TrayNotification tray = new TrayNotification();
                    tray.setTitle(title);
                    tray.setMessage(message);
                    tray.setRectangleFill(Paint.valueOf("#f44248"));
                    tray.setAnimationType(AnimationType.POPUP);
                    tray.setNotificationType(NotificationType.ERROR);
                    tray.showAndDismiss(Duration.seconds(3));

                }
                window.close();
            }

        };

        task.messageProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            controller.getmMessage().setText(newValue + "...");

        });

        controller.getmProgress().progressProperty().unbind();
        controller.getmIndicator().progressProperty().unbind();
        controller.getmProgress().progressProperty().bind(task.progressProperty());
        controller.getmIndicator().progressProperty().bind(task.progressProperty());

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

    }

    private void showProgress() throws IOException {
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.initStyle(StageStyle.UNDECORATED);
        window.getIcons().add(new Image(this.getClass().getResource("/images/loginLogo.png").toString()));

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vue/ProgressVue.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        controller = fxmlLoader.<ProgressController>getController();
        Scene scene = new Scene(root);
        window.setScene(scene);
        window.show();
        controller = fxmlLoader.<ProgressController>getController();

    }

    //clear Fields function
    public void clearFields() {
        nomComplet.clear();
        date.setValue(null);
        lieu.clear();
        cne.clear();
        niveau.clear();
        numInscription.clear();
        dateSortie.setValue(null);
        etablissement.getSelectionModel().clearSelection();
    }

    //init function
    private void init() {
        etudiants.clear();
        etablissemnts.clear();
        decisions.clear();

        nomCompletColumn.setCellValueFactory(new PropertyValueFactory<>("nomComplet"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateNaissance"));
        lieuColumn.setCellValueFactory(new PropertyValueFactory<>("lieuNaissance"));
        cneColumn.setCellValueFactory(new PropertyValueFactory<>("cne"));
        niveauColumn.setCellValueFactory(new PropertyValueFactory<>("niveauEtude"));
        numInscriptionColumn.setCellValueFactory(new PropertyValueFactory<>("numInscription"));
        decisionColumn.setCellValueFactory(new PropertyValueFactory<>("decision"));
        numDossierColumn.setCellValueFactory(new PropertyValueFactory<>("numDossier"));
        dateSortieColumn.setCellValueFactory(new PropertyValueFactory<>("dateSortie"));
        etablissementColumn.setCellValueFactory(new PropertyValueFactory<>("etablissement"));

        if (es.findAll() != null) {
            etudiants.addAll(es.findAll());
        }

        if (ets.findAll() != null) {
            etablissemnts.addAll(ets.findAll());
        }

        decisions.addAll("تشطيب", "يفصل", "انتقل و لم يلتحق", "ناجح", "تكرار وعدم الإلتحاق", "ناجحة في امتحان البكالوريا");

        decision.setItems(decisions);
        etablissement.setItems(etablissemnts);
        mTable.setItems(etudiants);

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        init();
        mTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        etablissement.setOnAction(e -> {
            Etablissement et = etablissement.getSelectionModel().getSelectedItem();
            selectedEtablissementId = et.getId();
        });

        mTable.setOnMousePressed(e -> {
            TablePosition pos = (TablePosition) mTable.getSelectionModel().getSelectedCells().get(0);
            int row = pos.getRow();
            Etudiant item = mTable.getItems().get(row);
            index = item.getId();

            nomComplet.setText(item.getNomComplet());

            Date dts = item.getDateNaissance();
            Date dts2 = item.getDateSortie();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            LocalDate localDate = LocalDate.parse(sdf.format(dts), formatter);
            LocalDate localDate2 = LocalDate.parse(sdf.format(dts2), formatter);

            date.setValue(localDate);
            dateSortie.setValue(localDate2);

            lieu.setText(item.getLieuNaissance());
            cne.setText(item.getCne());
            niveau.setText(item.getNiveauEtude());
            numInscription.setText(item.getNumInscription());

            decision.getSelectionModel().select(item.getDecision());
            numDossier.setText(String.valueOf(item.getNumDossier()));
            etablissement.getSelectionModel().select(item.getEtablissement());
        });
        
         nom.textProperty().addListener(e -> {
                
                fetchedEtudiants.clear();
    
                es.findAllbyEtab(currentEtab).stream().filter((ee) -> (ee.getNomComplet().contains(nom.getText()))).forEachOrdered((ee) -> {
                    fetchedEtudiants.add(ee);
            });
                mTable.setItems(fetchedEtudiants);
        });
        

        Preferences userPreferences = Preferences.userRoot();
        int currentUserId = userPreferences.getInt("currentUserId", 0);
        Employe e = eps.findById(currentUserId);
        currentEtab = e.getEtablissement();
        etablissement.getSelectionModel().select(currentEtab);

    }

}
