/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app;

import confiq.JPAUtil;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 *
 * @author aya
 */
public class Main extends Application{

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent p1 = FXMLLoader.load(getClass().getResource("../views/Login.fxml"));

        Scene s = new Scene(p1);
        stage.setScene(s);
        stage.setTitle("GHAD - Login");
        stage.show();
    }

    @Override
    public void stop() {
        JPAUtil.closeEMF();
    }
}

