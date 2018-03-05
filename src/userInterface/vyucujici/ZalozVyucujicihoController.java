/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userInterface.vyucujici;

import java.awt.Desktop;
import java.net.URI;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import userInterface.ControlledScreen;
import userInterface.Main;
import userInterface.ScreensController;

/**
 *
 * @author jirka_000
 */
public class ZalozVyucujicihoController implements Initializable, ControlledScreen {

    private ScreensController myController;
    private Desktop desktop;
    private Stage stage;
    private boolean validace = true;

    @FXML
    private AnchorPane formularStranaDva;

    @FXML
    private AnchorPane alertDatabazeNOK;
    @FXML
    private AnchorPane alertValidaceNOK;
    @FXML
    private AnchorPane alertPredmetyNOK;
    @FXML
    private AnchorPane alertDatabazeOK;

    @FXML
    private ComboBox seznamPredmetu;
    private String predmetHodnota;

    @FXML
    private Label zpet;
    @FXML
    private Label vpred;

    @FXML
    private TextField titul;
    private String titulHodnota;
    @FXML
    private TextField jmeno;
    private String jmenoHodnota;
    @FXML
    private TextField prijmeni;
    private String prijmeniHodnota;
    @FXML
    private TextField email;
    private String emailHodnota;
    @FXML
    private TextArea adresa;
    private String adresaHodnota;

    @FXML
    private TextField stat;
    private String statHodnota;
    @FXML
    private TextField telefon;
    private String telefonHodnota;
    @FXML
    private TextField rodneCislo;
    private String rodneCisloHodnota;
    @FXML
    private TextField vzdelani;
    private String vzdelaniHodnota;
    @FXML
    private TextField doporuceni;
    private String doporuceniHodnota;
    @FXML
    private TextField zkusenosti;
    private String zkusenostiHodnota;

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }

    @Override
    public void giveStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void otevriHlavniStranka(MouseEvent event) {
        myController.setScreen(Main.hlavniStrankaID);
    }

    @FXML
    private void zavriOkno(MouseEvent event) {
        System.exit(0);
    }

    @FXML
    private void minimalizujOkno(MouseEvent event) {
        stage.setIconified(true);
    }

    @FXML
    private void zobrazFormularStranaDva(MouseEvent event) {
        formularStranaDva.setVisible(true);
    }

    @FXML
    private void schovejFormularStranaDva(MouseEvent event) {
        formularStranaDva.setVisible(false);
    }

    @FXML
    private void nastavZpetOranzova(MouseEvent event) {
        zpet.setStyle("-fx-text-fill: #ED4425;");
    }

    @FXML
    private void nastavZpetBila(MouseEvent event) {
        zpet.setStyle("-fx-text-fill: #dddddd;");
    }

    @FXML
    private void nastavVpredOranzova(MouseEvent event) {
        vpred.setStyle("-fx-text-fill: #ED4425;");
    }

    @FXML
    private void nastavVpredBila(MouseEvent event) {
        vpred.setStyle("-fx-text-fill: #dddddd;");
    }

    @FXML
    private void ulozVyucujiciho(MouseEvent event) throws Exception {
        validace();
        if (validace) {
            post();
        } else {
            zobrazAlert(alertValidaceNOK);
        }
        validace = true;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        adresa.setWrapText(true);
        try {
            selectPredmety();
        } catch (Exception ex) {
            Logger.getLogger(ZalozVyucujicihoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        formularStranaDva.setVisible(false);
        alertDatabazeNOK.setVisible(false);
        alertValidaceNOK.setVisible(false);
        alertDatabazeOK.setVisible(false);
        alertPredmetyNOK.setVisible(false);
    }

    private void validace() {

        if (titul.getText().length() <= 10) {
            titul.setStyle("-fx-border-width: 0;");
            titulHodnota = titul.getText();
        } else {
            validace = false;
            titul.setStyle("-fx-border-color: -flatter-red; -fx-border-width: 2; -fx-border-radius: 3");
        }

        if (email.getText().length() <= 30) {
            email.setStyle("-fx-border-width: 0;");
            emailHodnota = email.getText();
        } else {
            validace = false;
            email.setStyle("-fx-border-color: -flatter-red; -fx-border-width: 2; -fx-border-radius: 3");
        }

        if (jmeno.getText().length() > 0 && jmeno.getText().length() <= 20) {
            jmeno.setStyle("-fx-border-width: 0;");
            jmenoHodnota = jmeno.getText();
        } else {
            validace = false;
            jmeno.setStyle("-fx-border-color: -flatter-red; -fx-border-width: 2; -fx-border-radius: 3");
        }

        if (prijmeni.getText().length() > 0 && prijmeni.getText().length() <= 20) {
            prijmeni.setStyle("-fx-border-width: 0;");
            prijmeniHodnota = prijmeni.getText();
        } else {
            validace = false;
            prijmeni.setStyle("-fx-border-color: -flatter-red; -fx-border-width: 2; -fx-border-radius: 3");
        }

        if (adresa.getText().length() > 0 && adresa.getText().length() <= 50) {
            adresa.setStyle("-fx-border-width: 0;");
            adresaHodnota = adresa.getText();
        } else {
            validace = false;
            adresa.setStyle("-fx-border-color: -flatter-red; -fx-border-width: 2; -fx-border-radius: 3");
        }

        if (rodneCislo.getText().length() > 0 && rodneCislo.getText().length() <= 10) {
            rodneCislo.setStyle("-fx-border-width: 0;");
            rodneCisloHodnota = rodneCislo.getText();
        } else {
            validace = false;
            rodneCislo.setStyle("-fx-border-color: -flatter-red; -fx-border-width: 2; -fx-border-radius: 3");
        }

        if (stat.getText().length() > 0 && stat.getText().length() <= 30) {
            stat.setStyle("-fx-border-width: 0;");
            statHodnota = stat.getText();
        } else {
            validace = false;
            stat.setStyle("-fx-border-color: -flatter-red; -fx-border-width: 2; -fx-border-radius: 3");
        }

        if (telefon.getText().length() > 4 && telefon.getText().length() <= 20) {
            telefon.setStyle("-fx-border-width: 0;");
            telefonHodnota = telefon.getText();
        } else {
            validace = false;
            telefon.setStyle("-fx-border-color: -flatter-red; -fx-border-width: 2; -fx-border-radius: 3");
        }
        if (vzdelani.getText().length() > 0 && vzdelani.getText().length() <= 30) {
            vzdelani.setStyle("-fx-border-width: 0;");
            vzdelaniHodnota = vzdelani.getText();
        } else {
            validace = false;
            vzdelani.setStyle("-fx-border-color: -flatter-red; -fx-border-width: 2; -fx-border-radius: 3");
        }
        if (doporuceni.getText().length() <= 50) {
            doporuceni.setStyle("-fx-border-width: 0;");
            doporuceniHodnota = doporuceni.getText();
        } else {
            validace = false;
            doporuceni.setStyle("-fx-border-color: -flatter-red; -fx-border-width: 2; -fx-border-radius: 3");
        }
        if (zkusenosti.getText().length() <= 50) {
            zkusenosti.setStyle("-fx-border-width: 0;");
            zkusenostiHodnota = zkusenosti.getText();
        } else {
            validace = false;
            zkusenosti.setStyle("-fx-border-color: -flatter-red; -fx-border-width: 2; -fx-border-radius: 3");
        }
        if (seznamPredmetu.getValue() != null) {
            seznamPredmetu.setStyle("-fx-border-width: 0;");
            predmetHodnota = (String) seznamPredmetu.getValue();
        } else {
            validace = false;
            seznamPredmetu.setStyle("-fx-border-color: -flatter-red; -fx-border-width: 3; -fx-border-radius: 3");
        }
    }

    private void zobrazAlert(AnchorPane alertDatabaze) {

        PauseTransition visiblePause = new PauseTransition(
                Duration.seconds(5)
        );
        alertDatabaze.setVisible(true);

        visiblePause.setOnFinished((ActionEvent event) -> {
            alertDatabaze.setVisible(false);
        });
        visiblePause.play();
    }

    private Connection post() throws Exception {

        try {
            Connection con = databaze.Databaze.getConnection();
            PreparedStatement posted = con.prepareCall("INSERT INTO VYUCUJICI(PRIJMENI,JMENO,ADRESA,RODNE_CISLO,STAT,TELEFON,VZDELANI,EMAIL,TITUL,ZKUSENOSTI,DOPORUCENI,PREDMET_ID) "
                    + "VALUES('" + prijmeniHodnota + "','" + jmenoHodnota + "','" + adresaHodnota + "','" + rodneCisloHodnota + "','" + statHodnota + "','" + telefonHodnota + "','" + vzdelaniHodnota + "',?,?,?,?,?)");

            if (email.getText().length() != 0) {
                posted.setString(1, email.getText());
            } else {
                posted.setNull(1, Types.VARCHAR);
                System.out.println("test");
            }

            if (titul.getText().length() != 0) {
                posted.setString(2, titul.getText());
            } else {
                posted.setNull(2, Types.VARCHAR);
                System.out.println("test");
            }

            if (zkusenosti.getText().length() != 0) {
                posted.setString(3, zkusenosti.getText());
            } else {
                posted.setNull(3, Types.VARCHAR);
                System.out.println("test");
            }

            if (doporuceni.getText().length() != 0) {
                posted.setString(4, doporuceni.getText());
            } else {
                posted.setNull(4, Types.VARCHAR);
                System.out.println("test");
            }
           
            posted.setString(5, predmetHodnota);
            
            if (posted.executeUpdate() > 0) {
                zobrazAlert(alertDatabazeOK);
            }
        } catch (Exception e) {
            System.out.println(e);
            if (e != null) {
                zobrazAlert(alertDatabazeNOK);
            }
        } finally {
            System.out.println("Insert completed.");
        }
        return null;
    }

    public void selectPredmety() throws Exception {
        ObservableList<String> listPredmetu = FXCollections.observableArrayList();
        Connection con = null;
        Statement stmt = null;

        String query = "SELECT NAZEV_PR FROM PREDMET;";
        try {
            con = databaze.Databaze.getConnection();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                listPredmetu.add(rs.getString(1));
            }

            if (listPredmetu.size() > 0) {
                seznamPredmetu.setItems(listPredmetu);
                seznamPredmetu.setStyle("-fx-border-width: 0;");
            } else {
                seznamPredmetu.setStyle("-fx-border-color: -flatter-red; -fx-border-width: 3; -fx-border-radius: 3");
                zobrazAlert(alertPredmetyNOK);
            }
        } catch (SQLException e) {
            System.out.println(e);
            if (e != null) {
                seznamPredmetu.setStyle("-fx-border-color: -flatter-red; -fx-border-width: 3; -fx-border-radius: 3");
                zobrazAlert(alertDatabazeNOK);
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

}
