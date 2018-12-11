package controller;

import beans.Employe;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import service.AttestationService;
import service.EmployeService;
import service.EtablissementService;
import service.EtudiantService;
import service.ProfilService;
import util.EffectUtilities;

/**
 *
 * @author Sinponzakra
 */
public class MainController implements Initializable {

    EmployeService es = new EmployeService();
    ProfilService ps = new ProfilService();
    EtudiantService etuds = new EtudiantService();
    EtablissementService etabs = new EtablissementService();
    AttestationService at = new AttestationService();

    @FXML
    private Label headerText;
    @FXML
    private BorderPane mBorder;
    @FXML
    private Text profilCount;
    @FXML
    private Text employeCount;
    @FXML
    private Text etudiantCount;
    @FXML
    private Text attestationCount;
    @FXML
    private VBox mainCenter;
    @FXML
    private VBox profilTicket;
    @FXML
    private VBox employeTicket;
    @FXML
    private VBox etudiantTicket;
    @FXML
    private VBox attestationTicket;
    @FXML
    private JFXButton logOutBtn;
    @FXML
    private Label userName;
    @FXML
    private Label userEmail;
    @FXML
    private ImageView exitBarBtn;
    @FXML
    private ImageView maxBarBtn;
    @FXML
    private ImageView minBarBtn;
    @FXML
    private HBox mTopBar;
    @FXML
    private JFXButton employeScene;
    @FXML
    private JFXButton profilScene;
    @FXML
    private JFXButton etablissementScene;
    @FXML
    private JFXButton etudiantScene;
    @FXML
    private JFXButton attestationScene;
    @FXML
    private Separator separator1;
    @FXML
    private Separator separator2;
    @FXML
    private Separator separator3;
    @FXML
    private Separator separator4;
    @FXML
    private Separator separator5;
    
    @FXML
    private PieChart mChart;

    ObservableList<PieChart.Data> pieChartData
            = FXCollections.observableArrayList();

    @FXML
    private void switchtoProfil(ActionEvent e) throws IOException {
        headerText.setText("الوظائف");
        VBox v = FXMLLoader.load(getClass().getResource("/vue/ProfilVue.fxml"));
        mBorder.setCenter(fadeAnimate(v));
    }

    @FXML
    private void switchtoHome(ActionEvent e) throws IOException {
        headerText.setText("الواجهة");
        mBorder.setCenter(fadeAnimate(mainCenter));
        setChart();
        setCountsHome();
    }

    @FXML
    private void switchtoEmploye(ActionEvent e) throws IOException {
        headerText.setText("الموظفون");
        VBox v = FXMLLoader.load(getClass().getResource("/vue/EmployeVue.fxml"));
        mBorder.setCenter(fadeAnimate(v));
    }

    @FXML
    private void switchEtudiant(ActionEvent e) throws IOException {
        headerText.setText("التلاميذ");
        VBox v = FXMLLoader.load(getClass().getResource("/vue/EtudiantVue.fxml"));
        mBorder.setCenter(fadeAnimate(v));
    }

    @FXML
    private void switchEtablissement(ActionEvent e) throws IOException {
        headerText.setText("المؤسسة");
        VBox v = FXMLLoader.load(getClass().getResource("/vue/EtablissementVue.fxml"));
        mBorder.setCenter(fadeAnimate(v));
    }

    @FXML
    private void switchSearch(ActionEvent e) throws IOException {
        headerText.setText("البحت");
        VBox v = FXMLLoader.load(getClass().getResource("/vue/SearchVue.fxml"));
        mBorder.setCenter(fadeAnimate(v));
    }

    @FXML
    private void switchCharts(ActionEvent e) throws IOException {
        headerText.setText("الرسوم البيانية");
        VBox v = FXMLLoader.load(getClass().getResource("/vue/ChartsContainerVue.fxml"));
        mBorder.setCenter(fadeAnimate(v));
    }
    
    @FXML
    private void switchAttestation(ActionEvent e) throws IOException {
        headerText.setText("الشهادات المدرسية");
        VBox v = FXMLLoader.load(getClass().getResource("/vue/AttestationVue.fxml"));
        mBorder.setCenter(fadeAnimate(v));
    }
    
    public VBox fadeAnimate(VBox v) throws IOException {
        FadeTransition ft = new FadeTransition(Duration.millis(1500));
        ft.setNode(v);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();
        return v;
    }

    private void setCountsHome() {
        profilCount.setText(ps.getProfilsCount() + "");
        employeCount.setText(es.getEmployesCount() + "");
        etudiantCount.setText(etuds.getEtudiantsCount() + "");
        attestationCount.setText(at.getAttestationsCount() + "");
    }

    private void setChart() {
        mChart.getData().clear();
        pieChartData.clear();
        mChart.setTitle("عدد الموظفين في كل وظيفة");

        es.getChartData().forEach((o) -> {
            pieChartData.add(new PieChart.Data(o[0].toString(), Integer.parseInt(o[1].toString())));
        });
        
        mChart.getData().addAll(pieChartData);
        
        mChart.getData().forEach(d -> {
             Optional<Node> opTextNode = mChart.lookupAll(".chart-pie-label").stream().filter(n -> n instanceof Text && ((Text) n).getText().contains(d.getName())).findAny();
        if (opTextNode.isPresent()) {
           Double res = ((d.getPieValue() / ps.getProfilsCount())*100);
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(0);
          ((Text) opTextNode.get()).setText(d.getName() + " " + df.format(res) + " %");
        }
      });
        
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setChart();
        setCountsHome();

        Platform.runLater(() -> {
            Stage mStage = ((Stage) ((Node) mTopBar).getScene().getWindow());
            EffectUtilities.makeDraggable(mStage, mTopBar);
        });

        exitBarBtn.setOnMousePressed(e -> {
            System.exit(0);
        });

        maxBarBtn.setOnMousePressed(e -> {
            if (((Stage) ((Node) e.getSource()).getScene().getWindow()).isMaximized()) {
                ((Stage) ((Node) e.getSource()).getScene().getWindow()).setMaximized(false);
            } else {
                ((Stage) ((Node) e.getSource()).getScene().getWindow()).setMaximized(true);

            }
        });

        minBarBtn.setOnMousePressed(e -> {
            ((Stage) ((Node) e.getSource()).getScene().getWindow()).setIconified(true);
        });

        profilTicket.setOnMousePressed(e -> {
            try {
                headerText.setText("الوظائف");
                VBox v = FXMLLoader.load(getClass().getResource("/vue/ProfilVue.fxml"));
                mBorder.setCenter(v);
            } catch (IOException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        employeTicket.setOnMousePressed(e -> {
            try {
                headerText.setText("الموظفون");
                VBox v = FXMLLoader.load(getClass().getResource("/vue/EmployeVue.fxml"));
                mBorder.setCenter(v);
            } catch (IOException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        etudiantTicket.setOnMousePressed(e -> {
            try {
                headerText.setText("التلاميذ");
                VBox v = FXMLLoader.load(getClass().getResource("/vue/EtudiantVue.fxml"));
                mBorder.setCenter(v);
            } catch (IOException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        attestationTicket.setOnMousePressed(e -> {
            try {
                headerText.setText("الشهادات المدرسية");
                VBox v = FXMLLoader.load(getClass().getResource("/vue/AttestationVue.fxml"));
                mBorder.setCenter(v);
            } catch (IOException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        Preferences userPreferences = Preferences.userRoot();
        int currentUserId = userPreferences.getInt("currentUserId", 0);

        Employe currentEmploye = es.findById(currentUserId);
        userName.setText(currentEmploye.getPrenom() + " " + currentEmploye.getNom());
        userEmail.setText(currentEmploye.getEmail());

        if (!currentEmploye.getProfil().getLibelle().equals("مدير")) {
            employeScene.setVisible(false);
            etablissementScene.setVisible(false);
            profilScene.setVisible(false);
            etudiantScene.setVisible(false);
            attestationScene.setVisible(false);
            separator1.setVisible(false);
            separator2.setVisible(false);
            separator3.setVisible(false);
            separator4.setVisible(false);
            separator5.setVisible(false);
            profilTicket.setVisible(false);
            employeTicket.setVisible(false);
            etudiantTicket.setVisible(false);
            attestationTicket.setVisible(false);
        }

        logOutBtn.setOnAction(e -> {
            try {

                Stage window = new Stage();
                window.initModality(Modality.APPLICATION_MODAL);
                window.initStyle(StageStyle.UNDECORATED);
                window.getIcons().add(new Image(this.getClass().getResource("/images/loginLogo.png").toString()));

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vue/ConfirmBoxVue.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                ConfirmBoxController controller = fxmlLoader.<ConfirmBoxController>getController();
                controller.setmMessage("هل تريد تسجيل الخروج ؟");
                controller.setmTitle("تأكيد");

                Scene scene = new Scene(root);

                window.setScene(scene);
                window.showAndWait();

                if (controller.getCurrentState()) {
                    userPreferences.remove("currentUserId");
                    userPreferences.remove("rememberMe");

                    Stage stage = new Stage();
                    stage.initStyle(StageStyle.TRANSPARENT);
                    stage.getIcons().add(new Image(this.getClass().getResource("/images/loginLogo.png").toString()));
                    Parent root1 = FXMLLoader.load(getClass().getResource("/vue/LoginVue.fxml"));

                    Scene scene1 = new Scene(root1);
                    scene1.setFill(Color.TRANSPARENT);
                    stage.setScene(scene1);
                    stage.show();

                    ((Stage) logOutBtn.getScene().getWindow()).close();
                }

            } catch (IOException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

}
