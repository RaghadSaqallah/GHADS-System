/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllers;

import dao.AidDistributionDAO;
import dao.FamilyDao;
import dao.OrganizationDAO;
import dao.UserDAO;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.AidDistribution;
import models.Family;
import models.Organization;
import models.User;

/**
 * FXML Controller class
 *
 * @author AL
 */
public class CoordinatorDashboardController implements Initializable {

    @FXML
    private VBox rootPane;
    @FXML

    //  nav btn
    private Button btnDashboard;
    @FXML
    private Button btnFamilies;
    @FXML
    private Button btnAid;
    @FXML
    private Button btnProfile;
    @FXML
    private Button btnPassword;

    @FXML // system name
    private Label coordNameLabel;

    //dashboard page
    @FXML
    private VBox dashboardPage;
    @FXML
    private Label orgNameLabel;
    @FXML
    private Label totalFamiliesLabel;
    @FXML
    private Label servedLabel;
    @FXML
    private Label notServedLabel;
    @FXML

    // family page
    private VBox familiesPage;
    @FXML
    private TextField famNameField;
    @FXML
    private TextField famPhoneField;
    @FXML
    private TextField famLocationField;
    @FXML
    private TextField famSizeField;
    @FXML
    private TextField famNatIdField;
    @FXML
    private ComboBox<String> famVulCombo;
    @FXML
    private TableView<Family> familiesTable;
    @FXML
    private TableColumn<Family, Integer> famIdCol;
    @FXML
    private TableColumn<Family, String> famNameCol;
    @FXML
    private TableColumn<Family, String> famPhoneCol;
    @FXML
    private TableColumn<Family, String> famLocCol;
    @FXML
    private TableColumn<Family, Integer> famSizeCol;
    @FXML
    private TableColumn<Family, String> famNatCol;
    @FXML
    private TableColumn<Family, String> famVulCol;

    // aid page
    @FXML
    private VBox aidPage;
    @FXML
    private ComboBox<String> aidFilterCombo;
    @FXML
    private TableView<Family> aidFamiliesTable;
    @FXML
    private TableColumn<Family, Integer> aidFamIdCol;
    @FXML
    private TableColumn<Family, String> aidFamNameCol;
    @FXML
    private TableColumn<Family, String> aidFamVulCol;
    @FXML
    private TableColumn<Family, String> aidFamLocCol;
    @FXML
    private TableColumn<Family, LocalDate> aidLastAidCol;
    @FXML
    private Label selectedFamilyLabel;

    // profile page
    @FXML
    private VBox profilePage;
    @FXML
    private TextField profileFullNameField;
    @FXML
    private TextField profileEmailField;
    @FXML
    private TextField profileUsernameField;
    @FXML
    private TextField profileOrgField;
    @FXML

    // cahnge password page
    private VBox passwordPage;
    @FXML
    private PasswordField currentPasswordField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Family selectedFamily;
    private User currentUser;

    public void setUser(User user) {
        this.currentUser = user;
        coordNameLabel.setText(user.getFullName());
        orgNameLabel.setText("Working on:" + user.getOrganization().getName());
        startDashboard();
    }

    // DAO
    private FamilyDao famDao = new FamilyDao();
    private OrganizationDAO orgDao = new OrganizationDAO();
    private UserDAO userDao = new UserDAO();
    private AidDistributionDAO aidDao = new AidDistributionDAO();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTables();
        setupCombos();
        AidTableListener();
        showPage(dashboardPage);
    }

    private void showPage(VBox page) {
        // أخفي كل الصفحات
        dashboardPage.setVisible(false);
        profilePage.setVisible(false);
        passwordPage.setVisible(false);
        familiesPage.setVisible(false);
        aidPage.setVisible(false);

        // أظهر الصفحة المطلوبة
        page.setVisible(true);

    }

    private void setupTables() {
        // Families table
        famIdCol.setCellValueFactory(new PropertyValueFactory<>("familyId"));
        famNameCol.setCellValueFactory(new PropertyValueFactory<>("householdName"));
        famPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        famLocCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        famSizeCol.setCellValueFactory(new PropertyValueFactory<>("familySize"));
        famNatCol.setCellValueFactory(new PropertyValueFactory<>("nationalId"));
        famVulCol.setCellValueFactory(new PropertyValueFactory<>("vulnerabilityLevel"));

        // Aid families table
        aidFamIdCol.setCellValueFactory(new PropertyValueFactory<>("familyId"));
        aidFamNameCol.setCellValueFactory(new PropertyValueFactory<>("householdName"));
        aidFamVulCol.setCellValueFactory(new PropertyValueFactory<>("vulnerabilityLevel"));
        aidFamLocCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        aidLastAidCol.setCellValueFactory(new PropertyValueFactory<>("lastAidDate"));
    }

    private void setupCombos() {
        famVulCombo.setItems(FXCollections.observableArrayList("HIGH", "MEDIUM", "LOW"));
        aidFilterCombo.setItems(FXCollections.observableArrayList(
                "Most Vulnerable", //high اول  هان ترتيب ال
                "Not Served Yet"
        ));
    }

    private void AidTableListener() {
        aidFamiliesTable.getSelectionModel().selectedItemProperty()
                .addListener((current, old, selected) -> {
                    if (selected != null) {
                        selectedFamily = selected;
                        selectedFamilyLabel.setText(
                                selected.getHouseholdName() + " " + selected.getVulnerabilityLevel()
                        );
                    }
                });
    }

    @FXML
    private void showDashboard(ActionEvent event) {
        showPage(dashboardPage);
        startDashboard();
    }

    private void startDashboard() {
        List<Family> famList = famDao.getAllFamilies();
        Long serverdByOrg = aidDao.getServedCountByOrganization(currentUser.getOrganization().getOrgId());
        Long notServed = aidDao.getNotServedFamiliesCount();

        totalFamiliesLabel.setText(String.valueOf(famList.size()));
        servedLabel.setText(String.valueOf(serverdByOrg));
        notServedLabel.setText(String.valueOf(notServed));

    }
    // end dashboard

    // start family
    @FXML
    private void showFamilies(ActionEvent event) {
        showPage(familiesPage);
        refreshFamilyTable();

    }

    private void refreshFamilyTable() {
        List famList = famDao.getAllFamilies();
        familiesTable.getItems().setAll(famList);
    }

    @FXML
    private void addFamily(ActionEvent event) {
        if (!validateFamilyFields()) {
            return;
        }

        if (famDao.getFamilyByNationalId(famNatIdField.getText().trim()) != null) { //موجودة اصلا
            showAlert("warning", "Warning", "Duplicate", "this family already registered.");
            return;
        }

        Family family = new Family();
        family.setHouseholdName(famNameField.getText().trim());
        family.setPhone(famPhoneField.getText().trim());
        family.setLocation(famLocationField.getText().trim());
        family.setFamilySize(Integer.parseInt(famSizeField.getText().trim()));
        family.setNationalId(famNatIdField.getText().trim());
        family.setVulnerabilityLevel(famVulCombo.getValue());
        family.setRegistrationDate(LocalDate.now());

        if (famDao.addFamily(family)) {
            showAlert("information", "Success", "Done", "Family registered successfully.");
            refreshFamilyTable();
            resetFamilyFields(null);
        } else {
            showAlert("warning", "warning", "warning Message", "Could not register this family.");
        }
    }

    @FXML
    private void resetFamilyFields(ActionEvent event) {
        famNameField.clear();
        famPhoneField.clear();
        famLocationField.clear();
        famSizeField.clear();
        famNatIdField.clear();
        famVulCombo.setValue(null);

    }
// end of family 

    /// start aid 
    @FXML
    private void showAid(ActionEvent event) {
        showPage(aidPage);
        refreshAidTable(null);

    }

    @FXML
    private void refreshAidTable(ActionEvent e) {
        List<Family> famList = famDao.getAllFamilies();
        aidFamiliesTable.getItems().setAll(famList);
        aidFilterCombo.setValue(null);
        clearAidSelection(null);

    }

    @FXML
    private void filterFamilies(ActionEvent event) {
        String filterBtn = aidFilterCombo.getValue();
        if (filterBtn == null) {
            showAlert("warning", "Warning", "Warning Message", "Please select a filter.");
            return;
        }

        if (filterBtn.equals("Most Vulnerable")) {
            aidFamiliesTable.getItems().setAll(famDao.getFamiliesSortedByVulnerability());
        } else {
            aidFamiliesTable.getItems().setAll(famDao.getFamiliesNotServed());
        }
    }

    @FXML
    private void recordAid(ActionEvent e) {
        if (selectedFamily == null) {
            showAlert("warning", "Warning", "Warning Message ", "Please select a family first.");
            return;
        }

        boolean canReceiveAid = famDao.canReceiveAid(selectedFamily.getFamilyId());
        if (!canReceiveAid) {
            AidDistribution lastRecord = aidDao.getLastDistributionByFamily(selectedFamily.getFamilyId());

            showAlert("warning", "Duplicate Aid", "Distribution Rejected", "family name: "
                    + selectedFamily.getHouseholdName() + " Vulnerability " + selectedFamily.getVulnerabilityLevel() + " last aid was " + selectedFamily.getLastAidDate() + " from "
                    + lastRecord.getOrganization().getName());
            return;
        }

        // save aid recored
        AidDistribution record = new AidDistribution();
        record.setFamily(selectedFamily);
        record.setOrganization(currentUser.getOrganization());
        record.setDistributedBy(currentUser);
        record.setDistributionDate(LocalDate.now());

        boolean done = aidDao.addDistribution(record);
        if (done) {
            showAlert("information", "information", "information Message",
                    "Aid added successfully ");
            refreshAidTable(null);
            startDashboard();
        } else {
            showAlert("warning", "warning", "warning Message", "Could not asve record aid.");
        }

    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/Login.fxml"));
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("GHADS - Login");
            stage.centerOnScreen();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    private void clearAidSelection(ActionEvent event
    ) {
        selectedFamily = null;
        selectedFamilyLabel.setText(" None selected ");
        aidFamiliesTable.getSelectionModel().clearSelection();
    }

    // end aid 
    // start profile
    @FXML
    private void showProfile(ActionEvent event
    ) {
        showPage(profilePage);
        refreshProfile();
    }

    private void refreshProfile() {
        profileFullNameField.setText(currentUser.getFullName());
        profileEmailField.setText(currentUser.getEmail());
        profileUsernameField.setText(currentUser.getUserName());
        profileOrgField.setText(currentUser.getOrganization().getName());
        profileOrgField.setEditable(false);
    }

    @FXML
    private void saveProfile(ActionEvent e) {
        if (profileFullNameField.getText().trim().isEmpty()
                || profileEmailField.getText().trim().isEmpty()
                || profileUsernameField.getText().trim().isEmpty()) {
            showAlert("warning", "warning", "warning Message", "All fields are required");
            return;
        }

        currentUser.setFullName(profileFullNameField.getText().trim());
        currentUser.setEmail(profileEmailField.getText().trim());
        currentUser.setUserName(profileUsernameField.getText().trim());

        userDao.updateUser(currentUser);
        coordNameLabel.setText(currentUser.getFullName());
        showAlert("information", "information", "information Message", "Profile updated successfully.");
    }

    @FXML
    private void resetProfile(ActionEvent event
    ) {
        refreshProfile();
    }

    @FXML
    private void showChangePassword(ActionEvent event) {
        showPage(passwordPage);
    }

    @FXML
    private void changePassword(ActionEvent event) {
        String current = currentPasswordField.getText();
        String newPass = newPasswordField.getText();
        String confirm = confirmPasswordField.getText();

        if (current.isEmpty() || newPass.isEmpty() || confirm.isEmpty()) {
            showAlert("warning", "warning", "warning Message", "All fields are required");
            return;
        }

        if (!current.equals(currentUser.getPassword())) {
            showAlert("warning", "warning", "warning Message", "Wrong Password");
            return;
        }

        if (newPass.length() < 8) {
            showAlert("warning", "warning", "warning Message", "password must be at least 8 characters");
            return;
        }

        if (!newPass.equals(confirm)) {
            showAlert("warning", "warning", "warning Message", "Passwords do not match");
            return;
        }

        currentUser.setPassword(newPass);
        userDao.updateUser(currentUser);
        showAlert("information", "information", "information Message", "Password changed successfully.");
        resetPasswordFields(null);
    }

    @FXML
    private void resetPasswordFields(ActionEvent event) {
        currentPasswordField.clear();
        newPasswordField.clear();
        confirmPasswordField.clear();
    }

    private boolean validateFamilyFields() {
        if (famNameField.getText().trim().isEmpty()
                || famPhoneField.getText().trim().isEmpty()
                || famLocationField.getText().trim().isEmpty()
                || famSizeField.getText().trim().isEmpty()
                || famNatIdField.getText().trim().isEmpty()
                || famVulCombo.getValue() == null) {
            showAlert("warning", "Validation", "Empty Fields", "All fields are required.");
            return false;
        }
        try {
            Integer.parseInt(famSizeField.getText().trim());
        } catch (NumberFormatException e) {
            showAlert("warning", "Validation", "Invalid Input", "Family size must be a number.");
            return false;
        }
        return true;
    }

    private boolean showAlert(String type, String title, String header, String content) {
        Alert alert = null;
        if (type.equalsIgnoreCase("information")) {
            alert = new Alert(Alert.AlertType.INFORMATION);
        } else if (type.equalsIgnoreCase("warning")) {
            alert = new Alert(Alert.AlertType.WARNING);
        } else if (type.equalsIgnoreCase("Confirm")) {
            alert = new Alert(Alert.AlertType.CONFIRMATION);
        }
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

}
