package com.abikuneebus;

import java.util.Optional;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

// * search for existing account & choice to delete/modify

public class ModifyAccountMenu extends GridPane {

  private EmailApp emailApp; // reference to main app class
  private TextField userInput;
  private TextField firstNameField;
  private TextField lastNameField;
  private Text departmentText;
  private TextField mailCapacityField;
  private Text emailText;
  private String accountDepartment;
  private String accountEmail;
  private String accountHashPass;

  public ModifyAccountMenu(EmailApp emailApp) {
    this.emailApp = emailApp;
    showSearchMenu();
  }

  // * search menu
  private void showSearchMenu() {
    getChildren().clear();
    setHgap(10);
    setVgap(10);
    setPadding(new Insets(20, 10, 10, 10));

    // username input
    TextField userInput = new TextField();
    userInput.setPromptText("Enter Username");
    add(userInput, 0, 1);

    // find account button
    Button userSearchBtn = new Button("Find Account");
    userSearchBtn.setOnAction(e -> findAccount());
    add(userSearchBtn, 0, 1);

    // back to main menu button
    Button backToMainMenuBtn = new Button("Back to Main Menu");
    backToMainMenuBtn.setOnAction(e -> emailApp.showStartMenu());
    add(backToMainMenuBtn, 0, 2);

  }

  private void findAccount() {
    // gets username from form
    String username = userInput.getText();
    // validates username
    String validationMsg = Email.isNameValid(username);

    // checks for empty field
    if (username.isEmpty()) {
      Alert alert = new Alert(AlertType.WARNING);
      alert.setTitle("Input Error");
      alert.setHeaderText("Missing Information");
      alert.setContentText("Username required!");
      alert.showAndWait();
      return;
    }

    // handles username validation
    if (validationMsg != null) {
      Alert alert = new Alert(AlertType.WARNING);
      alert.setTitle("Input Error");
      alert.setHeaderText("Invalid Input");
      alert.setContentText(validationMsg);
      alert.showAndWait();
      return;
    }

    // populates form with account details if account exists
    DatabaseManager dbManager = new DatabaseManager();
    EmailAccount account = dbManager.getAccountByUsername(username);
    if (account != null) {
      showUpdateDeleteMenu(account);
    } else {
      // throws alert if not
      Alert alert = new Alert(Alert.AlertType.ERROR, "Username not found!");
      alert.showAndWait();
    }
  }

  // allows modification of account details or deletion of account
  private void showUpdateDeleteMenu(EmailAccount account) {
    getChildren().clear();
    String accountDepartment = account.getDepartment();
    String accountEmail = account.getEmail();
    String accountHashPass = account.getHashedPassword();

    // populating form with retrieved account details
    firstNameField = new TextField(account.getFirstName());
    add(firstNameField, 0, 0);

    lastNameField = new TextField(account.getLastName());
    add(lastNameField, 0, 1);

    // display only
    departmentText = new Text(accountDepartment);
    add(departmentText, 0, 2);

    // display only
    emailText = new Text(accountEmail);
    add(emailText, 1, 0);

    // TODO 'Change Password' Button — (1, 1)

    mailCapacityField = new TextField(String.valueOf(account.getMailCapacity()));
    add(mailCapacityField, 1, 2);

    Button updateAccountBtn = new Button("Update Account");
    updateAccountBtn.setOnAction(e -> updateAccount());
    add(updateAccountBtn, 2, 0);

    Button deleteAccountBtn = new Button("Delete Account");
    deleteAccountBtn.setOnAction(e -> deleteAccount(account));
    add(deleteAccountBtn, 2, 1);

    Button homeBtn = new Button("Back to Main Menu");
    homeBtn.setOnAction(e -> emailApp.showStartMenu());
    add(homeBtn, 2, 2);

  }

  // * utility functions

  // * UPDATE
  private void updateAccount() {
    // getting updated values from text fields
    String updatedFirstName = firstNameField.getText();
    String updatedLastName = lastNameField.getText();
    int updatedMailCapacity = Integer.parseInt(mailCapacityField.getText());

    // creating EmailAccount object with updated values
    EmailAccount updatedAccount = new EmailAccount(updatedFirstName, updatedLastName, accountEmail, updatedMailCapacity,
        accountDepartment, accountHashPass);

    // creating instance
    DatabaseManager dbManager = new DatabaseManager();

    // updating account in database
    dbManager.updateAccount(updatedAccount);
  }

  // * DELETE

  private void deleteAccount(EmailAccount account) {
    // confirm intent
    Alert confirmAlert = new Alert(AlertType.CONFIRMATION);
    confirmAlert.setTitle("Confirmation");
    confirmAlert.setHeaderText("Delete Account");
    confirmAlert.setContentText("Are you sure you want to delete this account?");

    ButtonType btnYes = new ButtonType("Yes");
    ButtonType btnNo = new ButtonType("No");

    confirmAlert.getButtonTypes().setAll(btnYes, btnNo);

    Optional<ButtonType> result = confirmAlert.showAndWait();
    if (result.get() == btnYes) {
      // deleting account
      DatabaseManager dbManager = new DatabaseManager();
      dbManager.deleteAccount(account.getEmail());

      Alert successAlert = new Alert(AlertType.INFORMATION, "Account deleted.");
      successAlert.showAndWait();

      emailApp.showStartMenu();

    } else {
      showUpdateDeleteMenu(account);
    }

  }
}