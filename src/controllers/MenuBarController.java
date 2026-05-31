/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 *
 * @author Raghad Saqallahٌ
 */
public class MenuBarController implements Initializable {

    @FXML
    private MenuBar menuBar;
    @FXML
    private MenuItem exitMenuItem;
    @FXML
    private RadioMenuItem Arial;
    @FXML
    private ToggleGroup fontfamily;
    @FXML
    private RadioMenuItem Georgia;
    @FXML
    private RadioMenuItem Verdana;
    @FXML
    private RadioMenuItem Times_New_Roman;
    @FXML
    private RadioMenuItem fontSize12;
    @FXML
    private ToggleGroup fontSize;
    @FXML
    private RadioMenuItem fontSize14;
    @FXML
    private RadioMenuItem fontSize16;
    @FXML
    private RadioMenuItem fontNormalItem;
    @FXML
    private ToggleGroup FontStyle;
    @FXML
    private RadioMenuItem fontBoldItem;
    @FXML
    private RadioMenuItem fontItalicItem;
    @FXML
    private MenuItem aboutMenuItem;

    /**
     *
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void exitHandle(ActionEvent event) {
        javafx.scene.Parent rootPane = menuBar.getScene().getRoot();
        ((Stage) menuBar.getScene().getWindow()).close();

    }

    @FXML
    private void fontFamilyHandle(ActionEvent event) {
        javafx.scene.Parent rootPane = menuBar.getScene().getRoot();
        RadioMenuItem source = (RadioMenuItem) event.getSource();
        if (source.getId().equals("Arial")) {
            rootPane.setStyle("-fx-font-family:Arial;");
        } else if (source.getId().equals("Georgia")) {
            rootPane.setStyle("-fx-font-family:Georgia;");
        } else if (source.getId().equals("Verdana")) {
            rootPane.setStyle("-fx-font-family:Verdana;");
        } else if (source.getId().equals("Times_New_Roman")) {
            rootPane.setStyle("-fx-font-family:'Times new roman';");
        }
    }

    @FXML
    private void fontSizeHandle(ActionEvent event) {
        javafx.scene.Parent rootPane = menuBar.getScene().getRoot();
        RadioMenuItem source = (RadioMenuItem) event.getSource();
        if (source.getId().equals("fontSize12")) {
            rootPane.setStyle("-fx-font-size:12" + "px;");
        } else if (source.getId().equals("fontSize14")) {
            rootPane.setStyle("-fx-font-size:14" + "px;");
        } else if (source.getId().equals("fontSize16")) {
            rootPane.setStyle("-fx-font-size:16" + "px;");
        }

    }

    @FXML
    private void fontStyleHandle(ActionEvent event) {
        RadioMenuItem source = (RadioMenuItem) event.getSource();
        javafx.scene.Parent rootPane = menuBar.getScene().getRoot();

        if (source.getId().equals("fontBoldItem")) {
            rootPane.setStyle("-fx-font-weight: bold;");
        } else if (source.getId().equals("fontItalicItem")) {
            rootPane.setStyle("-fx-font-style:italic;");
        } else if (source.getId().equals("fontNormalItem")) {
            rootPane.setStyle("-fx-font-weight:normal;");

        }
    }

    @FXML
    private void aboutHandle(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About GHADS");
        alert.setHeaderText("Gaza Humanitarian Aid Distribution System.\n"
                + "desktop application that helps multiple humanitarian organizations in Gaza ");
        Image m = new Image("/img/logo.png");
        ImageView img = new ImageView(m);

        img.setFitHeight(100);
        img.setFitWidth(150);
        alert.setGraphic(img);
        alert.setContentText("Version: 1.0\nDEV: Raghad Saqallah");
        alert.showAndWait();
    }

    public void showAlert(String type, String title, String header, String content) {
        Alert alert = null;
        if (type.equals("information")) {
            alert = new Alert(Alert.AlertType.INFORMATION);
        } else if (type.equals("warning")) {
            alert = new Alert(Alert.AlertType.WARNING);
        }
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
