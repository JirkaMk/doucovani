/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userInterface.dite;

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
import javafx.animation.PauseTransition;
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
public class ZalozDiteController implements Initializable, ControlledScreen {

    private ScreensController myController;
    private Desktop desktop;
    private Stage stage;
    private boolean validace = true;

    @FXML
    private AnchorPane alertDatabazeNOK;
    @FXML
    private AnchorPane alertValidaceNOK;
    @FXML
    private AnchorPane alertDatabazeOK;
    

    @FXML
    private TextField jmeno;
    private String jmenoHodnota;
    @FXML
    private TextField prijmeni;
    private String prijmeniHodnota;
    @FXML
    private TextArea adresa;
    private String adresaHodnota;
    @FXML
    private TextField stat;
    private String statHodnota;
    @FXML
    private TextField zakaznik;
    private int zakaznikHodnota;
    @FXML
    private TextField rodneCislo;
    private String rodneCisloHodnota;
    @FXML
    private TextField skola;
    private String skolaHodnota;

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
    private void ulozDite(MouseEvent event) throws Exception {
        validace();
        najdiZakaznikID(zakaznikHodnota);

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
        alertDatabazeNOK.setVisible(false);
        alertValidaceNOK.setVisible(false);
        alertDatabazeOK.setVisible(false);
    }

    private void validace() {

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

        if (zakaznik.getText().length() > 0 && zakaznik.getText().length() <= 10) {
            zakaznik.setStyle("-fx-border-width: 0;");
            zakaznikHodnota = Integer.parseInt(zakaznik.getText());
        } else {
            validace = false;
            zakaznik.setStyle("-fx-border-color: -flatter-red; -fx-border-width: 2; -fx-border-radius: 3");
        }

        if (skola.getText().length() > 0 && skola.getText().length() <= 20) {
            skola.setStyle("-fx-border-width: 0;");
            skolaHodnota = skola.getText();
        } else {
            validace = false;
            skola.setStyle("-fx-border-color: -flatter-red; -fx-border-width: 2; -fx-border-radius: 3");
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
            PreparedStatement posted = con.prepareCall("INSERT INTO DITE(PRIJMENI,JMENO,ADRESA,RODNE_CISLO,STAT,SKOLA,ZAKAZNIK_ZAK_ID) "
                    + "VALUES('" + prijmeniHodnota + "','" + jmenoHodnota + "','" + adresaHodnota + "','" + rodneCisloHodnota + "','" + statHodnota + "','" + skolaHodnota + "','" + zakaznikHodnota + "')");

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

    private String najdiZakaznikID(int zakaznikID) throws Exception  {

        Statement stmt = null;
        int zakID = -1;
        
        String query = "SELECT ZAK_ID FROM ZAKAZNIK WHERE ZAK_ID ='" + zakaznikID + "';";
        try {
            Connection con = databaze.Databaze.getConnection();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                 zakID = rs.getInt(1);
            }
            
            if (zakID == zakaznikID) {
                System.out.println("testik");
            }
            else{
                validace = false;
                zakaznik.setStyle("-fx-border-color: -flatter-red; -fx-border-width: 2; -fx-border-radius: 3");
            }
        } catch (SQLException e) {
            System.out.println(e);
            if (e != null) {
                zobrazAlert(alertDatabazeNOK);
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return null;
    }
    
    

}
