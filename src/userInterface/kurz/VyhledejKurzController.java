/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userInterface.kurz;

import userInterface.dite.*;
import userInterface.zakaznik.*;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
public class VyhledejKurzController implements Initializable, ControlledScreen {

    private ScreensController myController;
    private Desktop desktop;
    private Stage stage;
    private boolean validace = true;
    
    @FXML
    private TableView tabulka;
    @FXML
    private TableColumn<PolozkaKurz, String> tabulkaPolozka;
    @FXML
    private TableColumn<PolozkaKurz, String> tabulkaHodnota;

    @FXML
    private AnchorPane alertDatabazeDeleteNOK;
    @FXML
    private AnchorPane alertDatabazeSelectNOK;    
    @FXML
    private AnchorPane alertValidaceNOK;
    @FXML
    private AnchorPane zaznamSmazanOK;

    @FXML
    private TextField iDTextField;
    private int iDTextFieldHodnota;
    
    @FXML
    private Label plnyKurz;
    @FXML
    private Label volnyKurz;    

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
    private void vymazKurz(MouseEvent event) throws Exception {
        vymazKurz(iDTextFieldHodnota);
    }

    @FXML
    private void hledejKurz(MouseEvent event) throws Exception {
        iDTextFieldHodnota = Integer.parseInt(iDTextField.getText());

        selectKurz(iDTextFieldHodnota);
        PolozkaKurz kurz = new PolozkaKurz();
        if (kurz.jeKurzPlny(iDTextFieldHodnota) == 1){
            volnyKurz.setVisible(false);
            plnyKurz.setVisible(true);
        }
        
        if (kurz.jeKurzPlny(iDTextFieldHodnota) == 2){
            volnyKurz.setVisible(true);
            plnyKurz.setVisible(false);
        }                       
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tabulka.setPlaceholder(new Label("Zadejte ID kurzu a stiskněte tlačítko Hledat"));
        tabulka.setSelectionModel(null);
        tabulka.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        alertDatabazeDeleteNOK.setVisible(false);
        alertDatabazeSelectNOK.setVisible(false);
        alertValidaceNOK.setVisible(false);
        zaznamSmazanOK.setVisible(false);
        plnyKurz.setVisible(false);
        volnyKurz.setVisible(false);
        
    }

    public void selectKurz(int kurzID) throws Exception {
        ObservableList<PolozkaKurz> listPolozek = FXCollections.observableArrayList();
        Connection con = null;
        Statement stmt = null;
        int kID = -1;

        String query = "SELECT KURZ_ID,DATUM,DELKA_V_MINUT,KAPACITA,POCET_PRIHLASEK,CENA,PREDMET_NAZEV_PR,VYUCUJICI_ID FROM KURZ WHERE KURZ_ID ='" + kurzID + "';";
        try {
            con = databaze.Databaze.getConnection();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                kID = rs.getInt(1);
            }

            if (kID == kurzID) {
                iDTextField.setStyle("-fx-border-width: 0;");
                System.out.println("testik");
                listPolozek.add(new PolozkaKurz("ID kurzu", String.valueOf(kID)));
                listPolozek.add(new PolozkaKurz("Datum",String.valueOf(rs.getDate(2))));
                listPolozek.add(new PolozkaKurz("Délka",(String.valueOf(rs.getInt(3))) + " minut"));
                listPolozek.add(new PolozkaKurz("Kapacita", String.valueOf(rs.getInt(4))));
                listPolozek.add(new PolozkaKurz("Počet přihlášených", String.valueOf(rs.getInt(5))));
                listPolozek.add(new PolozkaKurz("Cena", (String.valueOf(rs.getInt(6))) + " CZK"));
                listPolozek.add(new PolozkaKurz("Předmět", rs.getString(7)));
                listPolozek.add(new PolozkaKurz("ID vyučujícího", String.valueOf(rs.getInt(8))));

                tabulkaPolozka.setCellValueFactory(new PropertyValueFactory<>("polozka"));
                tabulkaHodnota.setCellValueFactory(new PropertyValueFactory<>("hodnota"));

                tabulka.setItems(listPolozek);
            } else {
                iDTextField.setStyle("-fx-border-color: -flatter-red; -fx-border-width: 2; -fx-border-radius: 3");
                zobrazAlert(alertValidaceNOK);
            }
        } catch (SQLException e) {
            System.out.println(e);
            if (e != null) {
                zobrazAlert(alertDatabazeSelectNOK);
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
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

    public void vymazKurz(int kurzID) throws Exception {
        Connection con = null;
        Statement stmt = null;

        try {
            con = databaze.Databaze.getConnection();
            PreparedStatement statement = con.prepareStatement("DELETE FROM KURZ WHERE KURZ_ID = ?");
            statement.setInt(1, kurzID);
            int deleteCount = statement.executeUpdate();

            if (deleteCount>0) {
                zobrazAlert(zaznamSmazanOK);
                tabulka.getItems().clear();
                plnyKurz.setVisible(false);
                volnyKurz.setVisible(false);
            } 
        } catch (SQLException e) {
            System.out.println(e);
            if (e != null) {
                zobrazAlert(alertDatabazeDeleteNOK);
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

}
