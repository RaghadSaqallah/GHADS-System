package controllers;

import dao.AidDistributionDAO;
import dao.FamilyDao;
import dao.OrganizationDAO;
import dao.UserDAO;
import models.AidDistribution;
import models.Family;
import models.Organization;
import models.User;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminDashboardController implements Initializable {

    // ===== FXML Fields =====
    @FXML
    private VBox rootPane;
    @FXML
    private Button btnDashboard, btnOrgs, btnUsers, btnFamilies, btnAid;
    @FXML
    private Label adminNameLabel;

    // Dashboard
    @FXML
    private VBox dashboardPage;
    @FXML
    private Label totalOrgsLabel, totalUsersLabel, totalFamiliesLabel, notServedLabel;

    // Organizations
    @FXML
    private VBox organizationsPage;
    @FXML
    private TextField orgNameField, orgTypeField, orgContactField;
    @FXML
    private TableView<Organization> orgsTable;
    @FXML
    private TableColumn<Organization, Integer> orgIdCol;
    @FXML
    private TableColumn<Organization, String> orgNameCol, orgTypeCol, orgContactCol;

    // Users
    @FXML
    private VBox usersPage;
    @FXML
    private TextField userFullNameField, userNameField, userPasswordField, userEmailField;
    @FXML
    private ComboBox<String> userRoleCombo; // coor
    @FXML
    private ComboBox<Organization> userOrgCombo;
    @FXML
    private TableView<User> usersTable;
    @FXML
    private TableColumn<User, Integer> userIdCol;
    @FXML
    private TableColumn<User, String> userFullNameCol, userNameCol, userEmailCol, userRoleCol;
    @FXML
    private TableColumn<User, String> userOrgCol;

    // Families
    @FXML
    private VBox familiesPage;
    @FXML
    private TextField familyNameField, familyPhoneField, familyLocationField;
    @FXML
    private TextField familySizeField, familyNationalIdField;
    @FXML
    private ComboBox<String> familyVulnerabilityCombo;
    @FXML
    private TableView<Family> familiesTable;
    @FXML
    private TableColumn<Family, Integer> famIdCol;
    @FXML
    private TableColumn<Family, String> famNameCol, famPhoneCol, famLocCol;
    @FXML
    private TableColumn<Family, Integer> famSizeCol;
    @FXML
    private TableColumn<Family, String> famNatIdCol, famVulCol;
    @FXML
    private TableColumn<Family, LocalDate> famLastAidCol, famRegisterCol;

    // Aid
    @FXML
    private VBox aidPage;
    @FXML
    private ComboBox<Organization> aidOrgCombo;
    @FXML
    private TableView<AidDistribution> aidTable;
    @FXML
    private TableColumn<AidDistribution, Integer> aidIdCol;
    @FXML
    private TableColumn<AidDistribution, String> aidFamilyCol, aidOrgCol, aidByCol;
    @FXML
    private TableColumn<AidDistribution, LocalDate> aidDateCol;

    //DAO
    private FamilyDao famDao = new FamilyDao();
    private OrganizationDAO orgDao = new OrganizationDAO();
    private UserDAO userDao = new UserDAO();
    private AidDistributionDAO aidDao = new AidDistributionDAO();

    // Current user
    private User currentUser;

    public void setUser(User user) {
        this.currentUser = user;
        adminNameLabel.setText(user.getFullName());
        startDashboard();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTables();
        setupCombos();
        TableSelection();
        showPage(dashboardPage);
    }

    // لما اختار يمتلأ تلقائي
    private void TableSelection() {
        // Organizations
        orgsTable.getSelectionModel().selectedItemProperty().addListener((current, old, selected) -> {
            if (selected != null) {
                orgNameField.setText(selected.getName());
                orgTypeField.setText(selected.getType());
                orgContactField.setText(selected.getContactInfo());
            }
        });

        // Users
        usersTable.getSelectionModel().selectedItemProperty().addListener((current, old, selected) -> {
            if (selected != null) {
                userFullNameField.setText(selected.getFullName());
                userNameField.setText(selected.getUserName());
                userPasswordField.setText(selected.getPassword());
                userEmailField.setText(selected.getEmail());
                userRoleCombo.setValue(selected.getRole());
                userOrgCombo.setValue(selected.getOrganization());
            }
        });

        // Families
        familiesTable.getSelectionModel().selectedItemProperty().addListener((currretn, old, selected) -> {
            if (selected != null) {
                familyNameField.setText(selected.getHouseholdName());
                familyPhoneField.setText(selected.getPhone());
                familyLocationField.setText(selected.getLocation());
                familySizeField.setText(String.valueOf(selected.getFamilySize()));
                familyNationalIdField.setText(selected.getNationalId());
                familyVulnerabilityCombo.setValue(selected.getVulnerabilityLevel());
            }
        });
    }

    private void setupCombos() {
        userRoleCombo.setItems(FXCollections.observableArrayList("COORDINATOR"));
        familyVulnerabilityCombo.setItems(FXCollections.observableArrayList("HIGH", "MEDIUM", "LOW"));

        List<Organization> orgs = orgDao.getAllOrganizations();
        userOrgCombo.getItems().addAll(orgs); // اسماء المنظمات في اليوزير
        aidOrgCombo.getItems().addAll(orgs); //اسماء المنظمات في ال aid
    }

    private void setupTables() {
        // Organizations
        orgIdCol.setCellValueFactory(new PropertyValueFactory("orgId"));
        orgNameCol.setCellValueFactory(new PropertyValueFactory("name"));
        orgTypeCol.setCellValueFactory(new PropertyValueFactory("type"));
        orgContactCol.setCellValueFactory(new PropertyValueFactory("contactInfo"));

        // Users
        userIdCol.setCellValueFactory(new PropertyValueFactory("userId"));
        userFullNameCol.setCellValueFactory(new PropertyValueFactory("fullName"));
        userNameCol.setCellValueFactory(new PropertyValueFactory("userName"));
        userEmailCol.setCellValueFactory(new PropertyValueFactory("email"));
        userRoleCol.setCellValueFactory(new PropertyValueFactory("role"));
//        userRoleCol.setCellValueFactory(new PropertyValueFactory("organization"));

        userOrgCol.setCellValueFactory(data -> { // من user -> org -> string
            Organization org = data.getValue().getOrganization();
            if (org == null) {
                return new javafx.beans.property.SimpleStringProperty("-");
            }
            return new javafx.beans.property.SimpleStringProperty( // بغلف السترينج بالنوع الي يقبله  التابل
                    //                org != null ? org.getName() : "—" 
                    org.getName()
            );
        });

        // Families
        famIdCol.setCellValueFactory(new PropertyValueFactory<>("familyId"));
        famNameCol.setCellValueFactory(new PropertyValueFactory<>("householdName"));
        famPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        famLocCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        famSizeCol.setCellValueFactory(new PropertyValueFactory<>("familySize"));
        famNatIdCol.setCellValueFactory(new PropertyValueFactory<>("nationalId"));
        famVulCol.setCellValueFactory(new PropertyValueFactory<>("vulnerabilityLevel"));
        famRegisterCol.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));
        famLastAidCol.setCellValueFactory(new PropertyValueFactory<>("LastAidDate"));

        // Aid
        aidIdCol.setCellValueFactory(new PropertyValueFactory<>("distributionId"));
        aidFamilyCol.setCellValueFactory(data
                -> new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getFamily().getHouseholdName()));
        aidOrgCol.setCellValueFactory(data
                -> new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getOrganization().getName()));
        aidByCol.setCellValueFactory(data
                -> new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getDistributedBy().getFullName()));
        aidDateCol.setCellValueFactory(new PropertyValueFactory<>("distributionDate"));
    }

    private void showPage(VBox page) {
        // أخفي كل الصفحات
        dashboardPage.setVisible(false);
        organizationsPage.setVisible(false);
        usersPage.setVisible(false);
        familiesPage.setVisible(false);
        aidPage.setVisible(false);

        // أظهر الصفحة المطلوبة
        page.setVisible(true);

    }

    @FXML
    private void showDashboard(ActionEvent e) {
        showPage(dashboardPage);
        startDashboard();
    }

    @FXML
    private void showOrganizations(ActionEvent e) {
        showPage(organizationsPage);
        refreshOrgTable(null);
    }

    @FXML
    private void showUsers(ActionEvent e) {
        showPage(usersPage);
        refreshUsersTable(null);
    }

    @FXML
    private void showFamilies(ActionEvent e) {
        showPage(familiesPage);
        refreshFamilyTable(null);
    }

    @FXML
    private void showAid(ActionEvent e) {
        showPage(aidPage);
        startAid(null);
    }

    // DASHBOARD start
    private void startDashboard() {
        List<Organization> orgs = orgDao.getAllOrganizations();
        List<User> users = userDao.getAllUsers();
        List<Family> families = famDao.getAllFamilies();

        // عدد المنظمات
        totalOrgsLabel.setText(String.valueOf(orgs.size()));

        // عدد الـ Coordinators بس
        long count = users.stream()
                .filter(u -> u.getRole().equalsIgnoreCase("COORDINATOR"))
                .count();
        totalUsersLabel.setText(String.valueOf(count));

        // عدد العائلات
        totalFamiliesLabel.setText(String.valueOf(families.size()));

        // العائلات اللي ما استلمت مساعدة
        long notServed = families.stream()
                .filter(f -> f.getLastAidDate() == null)
                .count();
        notServedLabel.setText(String.valueOf(notServed));
    }

    // organization start
    private void refreshOrgTable(ActionEvent e) {
        List<Organization> list = orgDao.getAllOrganizations();
        orgsTable.getItems().setAll(list);
    }

    @FXML // adding to org
    private void addOrg(ActionEvent e) {
        if (!validateOrg()) {
            return;
        }

        Organization org = new Organization();
        org.setName(orgNameField.getText().trim());
        org.setType(orgTypeField.getText().trim());
        org.setContactInfo(orgContactField.getText().trim());

        orgDao.addOrganization(org);
        showAlert("INFORMATION", "Success message", "Success", "Organization added successfully");
        refreshOrgTable(null); // عشان يعمل تحديث على التابل
        resetOrgFields();
        refreshOrgCombosUser();  // الموجود في جدول اليوزر
    }

    @FXML
    private void updateOrg(ActionEvent e) {
        Organization selected = orgsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("WARNING", "Warning", "Warning message", "Please select an organization first");
            return;
        }

        if (!validateOrg()) {
            return;
        }

        selected.setName(orgNameField.getText().trim());
        selected.setType(orgTypeField.getText().trim());
        selected.setContactInfo(orgContactField.getText().trim());

        orgDao.updateOrganization(selected);
        showAlert("INFORMATION", "Success", "Success Message", "Organization updated successfully.");
        refreshOrgTable(null);
        resetOrgFields();
    }

    @FXML
    private void deleteOrg(ActionEvent e) {
        Organization selected = orgsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("WARNING", "Warning", "Warning Message", "Please select an organization.");
            return;
        }

        if (showAlert("Confirm", "Confirm Message", "Delete?", "Are you sure you want to delete this recored ?")) {
            orgDao.deleteOrganization(selected.getOrgId());
            showAlert("INFORMATION", "Success", "Success Message", "Organization deleted successfully.");
            refreshOrgTable(null);
            refreshOrgCombosUser();
        }

    }

    @FXML
    private void resetOrgFields() {
        orgNameField.clear();
        orgTypeField.clear();
        orgContactField.clear();
        orgsTable.getSelectionModel().clearSelection();
    }

    private void refreshOrgCombosUser() {
        ObservableList<Organization> orgs = FXCollections.observableArrayList(orgDao.getAllOrganizations());
        userOrgCombo.setItems(orgs);
        aidOrgCombo.setItems(orgs);
    }

    // start user
    private void refreshUsersTable(ActionEvent e) {
        List<User> list = userDao.getAllUsers();
        usersTable.getItems().setAll(list);
    }

    @FXML
    private void addUser(ActionEvent e) {
        if (!validateUserFields()) {
            return;
        }

        // اليوزر اصلا موجود  
        if (userDao.usernameExist(userNameField.getText().trim())) {
            showAlert("WARNING", "Warning", "Warning Message", "Username already exists.");
            return;
        }

        User user = new User();
        user.setFullName(userFullNameField.getText().trim());
        user.setUserName(userNameField.getText().trim());
        user.setPassword(userPasswordField.getText().trim());
        user.setEmail(userEmailField.getText().trim());
        user.setRole(userRoleCombo.getValue());
        user.setOrganization(userOrgCombo.getValue());
        user.setOrganization(userOrgCombo.getValue());

        userDao.addUser(user);
        showAlert("INFORMATION", "Success", "Success Message", "User added successfully.");
        refreshUsersTable(null);
        resetUserFields(null);
    }

    @FXML
    private void updateUser(ActionEvent e) {
        User selected = usersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("WARNING", "Warning", "Warning Message", "Please select a user.");
            return;
        }
        if (!validateUserFields()) {
            return;
        }

        selected.setFullName(userFullNameField.getText().trim());
        selected.setUserName(userNameField.getText().trim());
        selected.setPassword(userPasswordField.getText().trim());
        selected.setEmail(userEmailField.getText().trim());
        selected.setRole(userRoleCombo.getValue());
        selected.setOrganization(userOrgCombo.getValue());

        if (selected.getRole().equalsIgnoreCase("ADMIN")) {
            selected.setOrganization(null);
        }
        userDao.updateUser(selected);
        showAlert("INFORMATION", "Success", "Success Message", "User updated successfully.");
        refreshUsersTable(null);
        resetUserFields(null);
    }

    @FXML
    private void deleteUser(ActionEvent e) {
        User selected = usersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("WARNING", "Warning", "Warning Message", "Please select a user.");
            return;
        }

        if (showAlert("Confirm", "Confirm Message", "Delete?", "Are you sure you want to delete this recored ?")) {
            userDao.deleteUser(selected.getUserId());
            showAlert("INFORMATION", "Success", "Success Message", "User deleted successfully.");
            refreshUsersTable(null);

        }
    }

    @FXML
    private void resetUserFields(ActionEvent e) {
        userFullNameField.clear();
        userNameField.clear();
        userPasswordField.clear();
        userEmailField.clear();
        userRoleCombo.setValue(null);
        userOrgCombo.setValue(null);
        usersTable.getSelectionModel().clearSelection();
    }

    //end of user 
    // start family code
    private void refreshFamilyTable(ActionEvent e) {
        List<Family> list = famDao.getAllFamilies();
        familiesTable.getItems().setAll(list);
    }

    @FXML
    private void addFamily(ActionEvent e) {
        if (!validateFamilyFields()) {
            return;
        }

        // التحقق من خلال الid 
        if (famDao.getFamilyByNationalId(familyNationalIdField.getText().trim()) != null) {
            showAlert("WARNING", "Warning", "Warning Message", "this family already exist.");
            return;
        }

        Family family = new Family();
        family.setHouseholdName(familyNameField.getText().trim());
        family.setPhone(familyPhoneField.getText().trim());
        family.setLocation(familyLocationField.getText().trim());
        family.setFamilySize(Integer.parseInt(familySizeField.getText().trim()));
        family.setNationalId(familyNationalIdField.getText().trim());
        family.setVulnerabilityLevel(familyVulnerabilityCombo.getValue());
        family.setRegistrationDate(LocalDate.now());

        famDao.addFamily(family);
        showAlert("INFORMATION", "Success", "Success Message", "Family registered successfully.");
        refreshFamilyTable(null);
        resetFamilyFields(null);
    }

    @FXML
    private void updateFamily(ActionEvent e) {
        Family selected = familiesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("WARNING", "Warning", "Warning Message", "Please select a family.");
            return;
        }
        if (!validateFamilyFields()) {
            return;
        }

        selected.setHouseholdName(familyNameField.getText().trim());
        selected.setPhone(familyPhoneField.getText().trim());
        selected.setLocation(familyLocationField.getText().trim());
        selected.setFamilySize(Integer.parseInt(familySizeField.getText().trim()));
        selected.setNationalId(familyNationalIdField.getText().trim());
        selected.setVulnerabilityLevel(familyVulnerabilityCombo.getValue());

        famDao.updateFamily(selected);
        showAlert("INFORMATION", "Success", "Success Message", "Family updated successfully.");
        refreshFamilyTable(null);
        resetFamilyFields(null);
    }

    @FXML
    private void deleteFamily(ActionEvent e) {
        Family selected = familiesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("WARNING", "Warning", "Warning Message", "Please select a family.");
            return;
        }

        if (showAlert("Confirm", "Confirm Message", "Delete?", "Are you sure you want to delete this recored ?")) {
            famDao.deleteFamily(selected.getFamilyId());
            showAlert("INFORMATION", "Success", "Success Message", "Family deleted.");
            refreshFamilyTable(null);
        }
    }

    @FXML
    private void resetFamilyFields(ActionEvent e) {
        familyNameField.clear();
        familyPhoneField.clear();
        familyLocationField.clear();
        familySizeField.clear();
        familyNationalIdField.clear();
        familyVulnerabilityCombo.setValue(null);
        familiesTable.getSelectionModel().clearSelection();
    }

    // end family code
    // AID code start
    @FXML
    private void startAid(ActionEvent e) {
        List<AidDistribution> list = aidDao.getAllDistributionRecords();
        aidTable.getItems().setAll(list);
        aidOrgCombo.setValue(null);
    }

    @FXML
    private void searchAid(ActionEvent e) {
        Organization selected = aidOrgCombo.getValue();
        if (selected == null) {
            showAlert("WARNING", "Warning", "Warning Message", "Please select an organization.");
            return;
        }

        List<AidDistribution> list = aidDao.getRecordsByOrganization(selected.getOrgId());
        aidTable.getItems().setAll(list);
    }

    // LOGOUT
    @FXML
    private void handleLogout(ActionEvent e) {
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

    // validate
    private boolean validateOrg() {
        if (orgNameField.getText().isEmpty()
                || orgTypeField.getText().isEmpty()
                || orgContactField.getText().isEmpty()) {
            showAlert("WARNING", "Validation message", "Validation error", "All fields are required.");
            return false;
        }
        return true;
    }

    private boolean validateUserFields() {
        if (userFullNameField.getText().trim().isEmpty()
                || userNameField.getText().trim().isEmpty()
                || userPasswordField.getText().trim().isEmpty()
                || userEmailField.getText().trim().isEmpty()
                || userRoleCombo.getValue() == null
                || userOrgCombo.getValue() == null) {
            showAlert("WARNING", "Validation", "Validation Message", "All fields are required.");
            return false;
        }
        if (userPasswordField.getText().trim().length() < 8) {
            showAlert("WARNING", "Validation", "Validation Message", "Password must be at least 8 characters.");
            return false;
        }
        return true;
    }

    private boolean validateFamilyFields() {
        if (familyNameField.getText().trim().isEmpty()
                || familyPhoneField.getText().trim().isEmpty()
                || familyLocationField.getText().trim().isEmpty()
                || familySizeField.getText().trim().isEmpty()
                || familyNationalIdField.getText().trim().isEmpty()
                || familyVulnerabilityCombo.getValue() == null) {
            showAlert("WARNING", "Validation", "Validation message", "All fields are required.");
            return false;
        }
        try {
            Integer.parseInt(familySizeField.getText().trim());
        } catch (NumberFormatException e) {
            showAlert("WARNING", "Validation", "Validation Message", "Family size must be a number.");
            return false;
        }
        return true;
    }

    // =============================================
    // HELPERS
    // =============================================
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
