/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userInterface.objednavka;

import userInterface.kurz.*;
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
public class ZalozObjednavkuController implements Initializable, ControlledScreen {

    private ScreensController myController;
    private Desktop desktop;
    private Stage stage;
    private boolean validace = true;
    private int selectDite = 1;
    private int selectKurz = 2;
    private int selectZak = 3;
    private PolozkaKurz kurz;

    private String selectIdDite;
    private String selectIdKurz;
    private String selectIdZak;

    @FXML
    private AnchorPane alertDatabazeNOK;
    @FXML
    private AnchorPane alertValidaceNOK;
    @FXML
    private AnchorPane alertKapacitaNOK;    
    @FXML
    private AnchorPane alertDatabazeOK;

    @FXML
    private ComboBox seznamStavu;
    private String stavHodnota;

    @FXML
    private DatePicker datum;
    private Date datumHodnota;
    @FXML
    private TextField kurzID;
    private int kurzIDHodnota;
    @FXML
    private TextField zakaznickeID;
    private int zakaznickeIDHodnota;
    @FXML
    private TextField diteID;
    private int diteIDHodnota;

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
    private void ulozObjednavku(MouseEvent event) throws Exception {

        validace();
        selectIdDite = "SELECT DITE_ID FROM DITE WHERE DITE_ID ='" + diteIDHodnota + "';";
        selectIdKurz = "SELECT KURZ_ID FROM KURZ WHERE KURZ_ID ='" + kurzIDHodnota + "';";
        selectIdZak = "SELECT ZAK_ID FROM ZAKAZNIK WHERE ZAK_ID ='" + zakaznickeIDHodnota + "';";
        najdiID(diteIDHodnota, selectIdDite, selectDite);
        najdiID(kurzIDHodnota, selectIdKurz, selectKurz);
        najdiID(zakaznickeIDHodnota, selectIdZak, selectZak);
        
        if(validace) zkontrolujKapacitu(kurzIDHodnota);

        if (validace) {
            post();
            inkrementujPrihlaskyKurzu(kurzIDHodnota);
        } else {
            zobrazAlert(alertValidaceNOK);
        }

        validace = true;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nactiStavyObjednavky();
        alertDatabazeNOK.setVisible(false);
        alertValidaceNOK.setVisible(false);
        alertKapacitaNOK.setVisible(false);
        alertDatabazeOK.setVisible(false);
    }

    private void nactiStavyObjednavky() {
        ObservableList<String> listStavu = FXCollections.observableArrayList();

        listStavu.add("Založená");
        listStavu.add("Zaplacená");
        listStavu.add("Storno");

        seznamStavu.setItems(listStavu);
    }

    private void validace() {

        if (datum.getValue() != null) {
            datum.setStyle("-fx-border-width: 0;");
            datumHodnota = java.sql.Date.valueOf(datum.getValue());
        } else {
            validace = false;
            datum.setStyle("-fx-border-color: -flatter-red; -fx-border-width: 2; -fx-border-radius: 3");
        }

        if (kurzID.getText().length() > 0) {
            kurzID.setStyle("-fx-border-width: 0;");
            try {
                kurzIDHodnota = Integer.parseInt(kurzID.getText());
                System.out.println(kurzIDHodnota);
            } catch (NumberFormatException e) {
            }
        } else {
            validace = false;
            kurzID.setStyle("-fx-border-color: -flatter-red; -fx-border-width: 2; -fx-border-radius: 3");
        }

        if (zakaznickeID.getText().length() > 0) {
            zakaznickeID.setStyle("-fx-border-width: 0;");
            try {
                zakaznickeIDHodnota = Integer.parseInt(zakaznickeID.getText());
                System.out.println(zakaznickeIDHodnota);
            } catch (NumberFormatException e) {
            }
        } else {
            validace = false;
            zakaznickeID.setStyle("-fx-border-color: -flatter-red; -fx-border-width: 2; -fx-border-radius: 3");
        }

        if (diteID.getText().length() > 0) {
            diteID.setStyle("-fx-border-width: 0;");
            try {
                diteIDHodnota = Integer.parseInt(diteID.getText());
                System.out.println(diteIDHodnota);
            } catch (NumberFormatException e) {
            }
        } else {
            validace = false;
            diteID.setStyle("-fx-border-color: -flatter-red; -fx-border-width: 2; -fx-border-radius: 3");
        }

        if (seznamStavu.getValue() != null) {
            seznamStavu.setStyle("-fx-border-width: 0;");
            stavHodnota = (String) seznamStavu.getValue();
        } else {
            validace = false;
            seznamStavu.setStyle("-fx-border-color: -flatter-red; -fx-border-width: 3; -fx-border-radius: 3");
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

        try {
            con = databaze.Databaze.getConnection();
            posted = con.prepareCall("INSERT INTO OBJEDNAVKA(DATUM,KURZ_KURZ_ID,ZAKAZNIK_ZAK_ID,DITE_DITE_ID,STAV) VALUES(?,?,?,?,?)");

            posted.setDate(1, datumHodnota);
            posted.setInt(2, kurzIDHodnota);
            posted.setInt(3, zakaznickeIDHodnota);
            posted.setInt(4, diteIDHodnota);
            posted.setString(5, stavHodnota);

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

    private void najdiID(int idUzivatel, String query, int select) throws Exception {
        Connection con = null;
        Statement stmt = null;
        int idDatabaze = -1;

        try {
            con = databaze.Databaze.getConnection();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                idDatabaze = rs.getInt(1);
            }

            if (idDatabaze == idUzivatel) {
                System.out.println("proslo");
                System.out.println("idDatabaze:" + idDatabaze + "idUzivatel:" + idUzivatel);
            } else {
                validace = false;
                System.out.println("neproslo");
                System.out.println("idDatabaze:" + idDatabaze + "idUzivatel:" + idUzivatel);
                if (select == selectDite) {
                    diteID.setStyle("-fx-border-color: -flatter-red; -fx-border-width: 2; -fx-border-radius: 3");
                }
                if (select == selectKurz) {
                    kurzID.setStyle("-fx-border-color: -flatter-red; -fx-border-width: 2; -fx-border-radius: 3");
                }
                if (select == selectZak) {
                    zakaznickeID.setStyle("-fx-border-color: -flatter-red; -fx-border-width: 2; -fx-border-radius: 3");

                }
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
    }
    
    private void zkontrolujKapacitu(int kurzID) throws Exception{
        kurz = new PolozkaKurz();
        
        if(kurz.jeKurzPlny(kurzID)==1){
           validace = false;  
           zobrazAlert(alertKapacitaNOK);
        }
    }
    
    private void inkrementujPrihlaskyKurzu(int kurzID){
        Statement stmt = null;
        PreparedStatement posted = null;
        Connection con = null;
        int pocetPrihlasek = -1000;
        
        String query = "SELECT POCET_PRIHLASEK FROM KURZ WHERE KURZ_ID =" + kurzID + ";";

        try {
            con = databaze.Databaze.getConnection();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
    
            if (rs.next()) {
                pocetPrihlasek = rs.getInt(1);
            }
            
            pocetPrihlasek = ++pocetPrihlasek;
            System.out.println("pocetPrihlasek: " + pocetPrihlasek);
            
            posted = con.prepareCall("UPDATE KURZ SET POCET_PRIHLASEK ='" + pocetPrihlasek + "' WHERE KURZ_ID ='" + kurzID + "';");
            posted.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
            if (e != null) {
                zobrazAlert(alertDatabazeNOK);
            }
        } finally {
            System.out.println("Insert completed.");
        }
    }
}
