/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userInterface.kurz;

import userInterface.zakaznik.*;
import java.awt.Desktop;
import java.net.URI;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
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
import javafx.scene.control.DatePicker;
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
public class ZalozKurzController implements Initializable, ControlledScreen {

    private ScreensController myController;
    private Desktop desktop;
    private Stage stage;
    private boolean validace = true;

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
    private DatePicker datum;
    private Date datumHodnota;
    @FXML
    private TextField delka;
    private int delkaHodnota;
    @FXML
    private TextField kapacita;
    private int kapacitaHodnota;
    @FXML
    private TextField vyucujici;
    private int vyucujiciHodnota;
    @FXML
    private TextField cena;
    private int cenaHodnota;

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
    private void ulozKurz(MouseEvent event) throws Exception {
 
        validace();
        najdiVyucujicihoID(vyucujiciHodnota);
        
        if (validace) {
            post();
        } else {
            zobrazAlert(alertValidaceNOK);
        }
       
        validace = true;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        alertDatabazeNOK.setVisible(false);
        alertValidaceNOK.setVisible(false);
        alertPredmetyNOK.setVisible(false);
        alertDatabazeOK.setVisible(false);
    }

    private void validace() {

        if (datum.getValue() != null) {
            datum.setStyle("-fx-border-width: 0;");
            datumHodnota = java.sql.Date.valueOf(datum.getValue());
        } else {
            validace = false;
            datum.setStyle("-fx-border-color: -flatter-red; -fx-border-width: 2; -fx-border-radius: 3");
        }

        if (delka.getText().length() > 0) {
            delka.setStyle("-fx-border-width: 0;");
            delkaHodnota = Integer.parseInt(delka.getText());
        } else {
            validace = false;
            delka.setStyle("-fx-border-color: -flatter-red; -fx-border-width: 2; -fx-border-radius: 3");
        }

        if (kapacita.getText().length() > 0) {
            kapacita.setStyle("-fx-border-width: 0;");
            kapacitaHodnota = Integer.parseInt(kapacita.getText());
        } else {
            validace = false;
            kapacita.setStyle("-fx-border-color: -flatter-red; -fx-border-width: 2; -fx-border-radius: 3");
        }

        if (vyucujici.getText().length() > 0) {
            vyucujici.setStyle("-fx-border-width: 0;");
            vyucujiciHodnota = Integer.parseInt(vyucujici.getText());
        } else {
            validace = false;
            vyucujici.setStyle("-fx-border-color: -flatter-red; -fx-border-width: 2; -fx-border-radius: 3");
        }

        if (cena.getText().length() > 0) {
            cena.setStyle("-fx-border-width: 0;");
            cenaHodnota = Integer.parseInt(cena.getText());
        } else {
            validace = false;
            cena.setStyle("-fx-border-color: -flatter-red; -fx-border-width: 2; -fx-border-radius: 3");
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
        Statement stmt = null;
        PreparedStatement posted = null;
        Connection con = null;
        String query = "SELECT PREDMET_ID FROM VYUCUJICI WHERE VYUC_ID =" + vyucujiciHodnota + ";";

        try {
            con = databaze.Databaze.getConnection();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
    
            if (rs.next()) {
                predmetHodnota = rs.getString(1);
            }
            
            posted = con.prepareCall("INSERT INTO KURZ(DATUM,DELKA_V_MINUT,KAPACITA,VYUCUJICI_ID,CENA,PREDMET_NAZEV_PR) VALUES(?,?,?,?,?,?)");

            posted.setDate(1, datumHodnota);
            posted.setInt(2, delkaHodnota);
            posted.setInt(3, kapacitaHodnota);
            posted.setInt(4, vyucujiciHodnota);
            posted.setInt(5, cenaHodnota);
            posted.setString(6, predmetHodnota);

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
    
    private String najdiVyucujicihoID(int vyucujiciID) throws Exception  {

        Statement stmt = null;
        int vyucID = -1;
        
        String query = "SELECT VYUC_ID FROM VYUCUJICI WHERE VYUC_ID ='" + vyucujiciID + "';";
        try {
            Connection con = databaze.Databaze.getConnection();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                 vyucID = rs.getInt(1);
            }
            
            if (vyucID == vyucujiciID) {
                System.out.println("testik");
            }
            else{
                validace = false;
                vyucujici.setStyle("-fx-border-color: -flatter-red; -fx-border-width: 2; -fx-border-radius: 3");
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
