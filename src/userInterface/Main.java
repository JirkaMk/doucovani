/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userInterface;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author jirka_000
 */
public class Main extends Application {

    private double xOffset = 0;
    private double yOffset = 0;
    public static String hlavniStrankaID = "hlavniStranka";
    public static String hlavniStrankaFXML = "HlavniStranka.fxml";
    public static String zalozZakaznikaID = "zalozZakaznika";
    public static String zalozZakaznikaFXML = "/userInterface/zakaznik/ZalozZakaznika.fxml";
    public static String vyhledejZakaznikaID = "vyhledejZakaznika";
    public static String vyhledejZakaznikaFXML = "/userInterface/zakaznik/VyhledejZakaznika.fxml";
    public static String zalozDiteID = "zalozDite";
    public static String zalozDiteFXML = "/userInterface/dite/ZalozDite.fxml";
    public static String vyhledejDiteID = "vyhledejDite";
    public static String vyhledejDiteFXML = "/userInterface/dite/VyhledejDite.fxml";
    public static String zalozVycucjicihoID = "zalozVyucujiciho";
    public static String zalozVycucjicihoFXML = "/userInterface/vyucujici/ZalozVyucujiciho.fxml";
    public static String zalozKurzID = "zalozKurz";
    public static String zalozKurzFXML = "/userInterface/kurz/ZalozKurz.fxml";
    public static String vyhledejKurzID = "vyhledejKurz";
    public static String vyhledejKurzFXML = "/userInterface/kurz/VyhledejKurz.fxml";
    public static String zalozObjednavkuID = "zalozObjednavku";
    public static String zalozObjednavkuFXML = "/userInterface/objednavka/ZalozObjednavku.fxml";
    public static String vyhledejObjednavkuID = "vyhledejObjednavku";
    public static String vyhledejObjednavkuFXML = "/userInterface/objednavka/VyhledejObjednavku.fxml";

    @Override
    public void start(Stage stage) {

        ScreensController mainContainer = new ScreensController();
        mainContainer.loadScreen(hlavniStrankaID, hlavniStrankaFXML, stage);
        mainContainer.loadScreen(zalozZakaznikaID, zalozZakaznikaFXML, stage);
        mainContainer.loadScreen(vyhledejZakaznikaID, vyhledejZakaznikaFXML, stage);
        mainContainer.loadScreen(zalozDiteID, zalozDiteFXML, stage);
        mainContainer.loadScreen(vyhledejDiteID, vyhledejDiteFXML, stage);
        mainContainer.loadScreen(zalozVycucjicihoID, zalozVycucjicihoFXML, stage);
        mainContainer.loadScreen(zalozKurzID, zalozKurzFXML, stage);
        mainContainer.loadScreen(vyhledejKurzID, vyhledejKurzFXML, stage);
        mainContainer.loadScreen(zalozObjednavkuID, zalozObjednavkuFXML, stage);
        mainContainer.loadScreen(vyhledejObjednavkuID, vyhledejObjednavkuFXML, stage);

        mainContainer.setScreen(hlavniStrankaID);

        Group root = new Group();
        root.getChildren().addAll(mainContainer);
        Scene scene = new Scene(root);

        nastavPohybliveOkno(root, stage);

        stage.setScene(scene);

        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().setAll(Main.class.getResource("styleSheet.css").toString());

        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public void nastavPohybliveOkno(Parent root, Stage stage) {

        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });
    }

}
