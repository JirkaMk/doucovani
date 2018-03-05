/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userInterface.dite;

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
public class VyhledejDiteController implements Initializable, ControlledScreen {

    private ScreensController myController;
    private Desktop desktop;
    private Stage stage;
    private boolean validace = true;

    @FXML
    private TableView tabulka;
    @FXML
    private TableColumn<PolozkaDite, String> tabulkaPolozka;
    @FXML
    private TableColumn<PolozkaDite, String> tabulkaHodnota;

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
    private void vymazDite(MouseEvent event) throws Exception {
        vymazDite(iDTextFieldHodnota);
    }

    @FXML
    private void hledejDite(MouseEvent event) throws Exception {
        iDTextFieldHodnota = Integer.parseInt(iDTextField.getText());
        selectDite(iDTextFieldHodnota);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tabulka.setPlaceholder(new Label("Zadejte ID dítěte a stiskněte tlačítko Hledat"));
        tabulka.setSelectionModel(null);
        tabulka.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        alertDatabazeDeleteNOK.setVisible(false);
        alertDatabazeSelectNOK.setVisible(false);
        alertValidaceNOK.setVisible(false);
        zaznamSmazanOK.setVisible(false);
    }

    public void selectDite(int detskeID) throws Exception {
        ObservableList<PolozkaDite> listPolozek = FXCollections.observableArrayList();
        Connection con = null;
        Statement stmt = null;
        int diteID = -1;

        String query = "SELECT DITE_ID,PRIJMENI,JMENO,ADRESA,RODNE_CISLO,STAT,SKOLA,ZAKAZNIK_ZAK_ID FROM DITE WHERE DITE_ID ='" + detskeID + "';";
        try {
            con = databaze.Databaze.getConnection();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                diteID = rs.getInt(1);
            }

            if (diteID == detskeID) {
                iDTextField.setStyle("-fx-border-width: 0;");
                System.out.println("testik");
                listPolozek.add(new PolozkaDite("ID dítětě", String.valueOf(diteID)));
                listPolozek.add(new PolozkaDite("Přijmení", rs.getString(2)));
                listPolozek.add(new PolozkaDite("Jméno", rs.getString(3)));
                listPolozek.add(new PolozkaDite("Adresa", rs.getString(4)));
                listPolozek.add(new PolozkaDite("Rodné číslo", rs.getString(5)));
                listPolozek.add(new PolozkaDite("Stát", rs.getString(6)));
                listPolozek.add(new PolozkaDite("Škola", rs.getString(7)));
                listPolozek.add(new PolozkaDite("ID rodiče", rs.getString(8)));

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

    public void vymazDite(int detskeID) throws Exception {
        Connection con = null;
        Statement stmt = null;

        try {
            con = databaze.Databaze.getConnection();
            PreparedStatement statement = con.prepareStatement("DELETE FROM DITE WHERE DITE_ID = ?");
            statement.setInt(1, detskeID);
            int deleteCount = statement.executeUpdate();

            if (deleteCount>0) {
                zobrazAlert(zaznamSmazanOK);
                tabulka.getItems().clear();
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
