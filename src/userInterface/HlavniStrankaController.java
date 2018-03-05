/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userInterface;

import java.awt.Desktop;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author jirka_000
 */
public class HlavniStrankaController implements Initializable, ControlledScreen {

    private ScreensController myController;
    private Desktop desktop;
    private Stage stage;

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }

    @Override
    public void giveStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void otevriZalozZakaznika(MouseEvent event) {
        myController.setScreen(Main.zalozZakaznikaID);
    }

    @FXML
    private void otevriVyhledejZakaznika(MouseEvent event) {
        myController.setScreen(Main.vyhledejZakaznikaID);
    }

    @FXML
    private void otevriZalozDite(MouseEvent event) {
        myController.setScreen(Main.zalozDiteID);
    }

    @FXML
    private void otevriVyhledejDite(MouseEvent event) {
        myController.setScreen(Main.vyhledejDiteID);
    }

    @FXML
    private void otevriZalozVyucujiciho(MouseEvent event) {
        myController.setScreen(Main.zalozVycucjicihoID);
    }

    @FXML
    private void otevriZalozKurz(MouseEvent event) {
        myController.setScreen(Main.zalozKurzID);
    }
    
    @FXML
    private void otevriVyhledejKurz(MouseEvent event) {
        myController.setScreen(Main.vyhledejKurzID);
    }

    @FXML
    private void otevriZalozObjednavku(MouseEvent event) {
        myController.setScreen(Main.zalozObjednavkuID);
    }    

    @FXML
    private void otevriVyhledejObjednavku(MouseEvent event) {
        myController.setScreen(Main.vyhledejObjednavkuID);
    }     
    
    @FXML
    private void zavriOkno(MouseEvent event) {
        System.exit(0);
    }

    @FXML
    private void otevriHTML(MouseEvent event) {
        try {
            desktop = java.awt.Desktop.getDesktop();
            URI oURL = new URI("https://www.doucovanipraha.cz");
            desktop.browse(oURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void minimalizujOkno(MouseEvent event) {
        stage.setIconified(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
