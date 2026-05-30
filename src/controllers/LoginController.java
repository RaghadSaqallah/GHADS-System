package controllers;

import dao.UserDAO;
import models.User;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class LoginController implements Initializable {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;
    @FXML
    private Button loginButton;

    private UserDAO userDAO = new UserDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorLabel.setVisible(false);

        passwordField.setOnAction(e -> handleLogin());
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        //if is empty
        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter username and password.");
            return;
        }
        if (password.length() < 8) {
            showError("Password must be at least 8 char");
            return;
        }

        User user = userDAO.login(username, password);
        if (user == null) {
            showError("Invalid username or password.");
            return;
        } else {
            // valid user 
            if (user.getRole().equalsIgnoreCase("ADMIN")) {
                System.out.println("admin");
                goTo("/views/AdminDashboard.fxml", user);
            } else if (user.getRole().equalsIgnoreCase("COORDINATOR")) {
                System.out.println("coor");
                goTo("/views/CoordinatorDashboard.fxml", user);
            } else {
                showError("Unknown role");
            }
        }
    }

    private void goTo(String fxmlPath, User user) {
        try {

            FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("/views/MenuBar.fxml"));
            Parent menuBar = menuLoader.load();

            FXMLLoader pageLoader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent pageContent = pageLoader.load();

            BorderPane root = new BorderPane();
            root.setTop(menuBar);      // نضع المنيو في الأعلى
            root.setCenter(pageContent); // نضع المحتوى في الوسط

            // تعين يوزر 
            if (user.getRole().equals("ADMIN")) {
                AdminDashboardController controller = pageLoader.getController();
                controller.setUser(user);
            } else {
                CoordinatorDashboardController controller = pageLoader.getController();
                controller.setUser(user);
            }

            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene s = new Scene(root);
            stage.setScene(s);
            stage.setTitle("GHADS - " + (user.getRole().equals("ADMIN") ? "Admin Dashboard" : "Coordinator Dashboard"));
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error loading dashboard. Please try again.");
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}
