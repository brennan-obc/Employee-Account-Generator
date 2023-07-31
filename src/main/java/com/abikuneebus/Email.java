package com.abikuneebus;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Email {
  // * declaring class-level variables
  private String firstName;
  private String lastName;
  private char[] password;
  private String department;
  private int mailboxCapacity = 500;
  private String emailAddress;
  private int defaultPasswordLength = 16;
  private String companyName = "thesoftwarefarm";
  private String employeeDomain;
  private String employeeUsername;
  private Scanner scanner;

  public Email(String firstName, String lastName, Scanner scanner) {
    // * initializing class-level variables
    this.firstName = firstName;
    this.lastName = lastName;
    System.out.println("EMAIL CREATED: " + this.firstName + " " + this.lastName);

    // forbidden password substrings
    Constants.FORBIDDEN_SUBSTRINGS.clear();
    Constants.initializeForbiddenSubstrings(this.firstName, this.lastName, this.department, this.companyName);

    // calling method that asks for & retrieves department
    this.department = setDepartment(scanner);
    System.out.println("Department: " + this.department);

    // initializing employee username
    this.scanner = scanner;
    // parameterizing
    String proposedUsername = String.format("%s.%s", this.firstName.toLowerCase(), this.lastName.toLowerCase());

    if (isUsernameTaken(proposedUsername)) {
      System.out.println("An account with this username already exists. Please enter a new username.");
      changeUsername(this.scanner.next()); // calling changeUsername with user input
    } else {
      this.employeeUsername = proposedUsername;
    }

    // parameterizing employee domain
    this.employeeDomain = (String.format("@%s%s.com",
        (this.department.equals("N/A") ? "" : String.format("%s.", this.department)), this.companyName)).toLowerCase();

    // combine elements to create email
    this.emailAddress = String.format("%s%s", this.employeeUsername, this.employeeDomain);
    System.out.println("Your email address: " + this.emailAddress);

    // calling method that returns randomly generated password
    this.password = generatePassword(defaultPasswordLength);
    String passwordString = new String(this.password);
    System.out.println("Your password is: " + passwordString);
  }

  // * initialization utility functions
  private String setDepartment(Scanner scanner) {
    System.out.print(
        "DEPARTMENT CODES:\n0) N/A\n1) Sales\n2) Development\n3) Accounting\nEnter employee's department code: ");
    int depChoice = scanner.nextInt();
    if (depChoice == 1) {
      return "sales";
    } else if (depChoice == 2) {
      return "dev";
    } else if (depChoice == 3) {
      return "acct";
    } else {
      return "N/A";

    }
  }

  private char[] generatePassword(int length) {
    Random rnd = new Random();
    char[] password = new char[length];

    boolean genPasswordValid = false;
    while (!genPasswordValid) {
      for (int i = 0; i < length; i++) {
        password[i] = Constants.PASSWORD_CHARS.charAt(rnd.nextInt(Constants.PASSWORD_CHARS.length()));
      }
      String passwordValid = isPasswordValid(password);
      if (passwordValid == null) {
        genPasswordValid = true;
      }
    }

    return password;
  }

  // * setters & setter utility functions
  // mailbox capacity
  public void setMailboxCapacity(int mailboxCapacity) {
    // validation to ensure capacity is within acceptable range
    if (mailboxCapacity < 250) {
      System.out.println("Insufficient capacity, please choose between 250 & 1500.");
    } else if (mailboxCapacity > 1500) {
      System.out.println("Value exceeds capacity limit, please choose between 250 & 1500.");
    } else {
      this.mailboxCapacity = mailboxCapacity;
    }
  }

  // password validator
  public String isPasswordValid(char[] password) {
    if (password.length < 12 || password.length > 32) {
      return "Password must be between 12 and 32 characters. ";
    }
    // required characters
    String passwordString = new String(password);
    if (!passwordString.matches(".*[!@#$%^*].*")) {
      return "Password must contain at least one of the following special characters: !, @, #, $, %, ^, or *. ";
    }

    // uppercase
    if (!passwordString.matches(".*[A-Z].*")) {
      return "Password must contain at least one uppercase letter. ";
    }

    // lowercase
    if (!passwordString.matches(".*[a-z].*")) {
      return "Password must contain at least one lowercase letter. ";
    }

    // forbidden substrings (defined in 'Constants')
    for (String substring : Constants.FORBIDDEN_SUBSTRINGS) {
      if (passwordString.contains(substring)) {
        return "Password contains forbidden group of characters. Please avoid using easily guessable words (\"password\", \"qwerty\", etc.), easily guessable numbers (123, 111, etc.), or any public personal information in your password. ";
      }
    }
    return null;
  }

  // change password
  public void changePassword(char[] password) {
    String passwordValid = isPasswordValid(password);
    if (passwordValid != null) {
      // prints reason for rejection of password
      System.out.println(passwordValid);
      return;
    }

    this.password = password;
    System.out.println("Password successfully changed.");
  }

  // check if username is taken
  private boolean isUsernameTaken(String username) {
    List<EmailAccount> existingAccounts = EmailApp.getAccountsFromJson();
    for (EmailAccount account : existingAccounts) {
      if (account.getUsername().equals(username)) {
        return true;
      }
    }
    return false;
  }

  // validate email format
  public String isEmailValid(String username) {
    if (!(username.matches("^(?!.*\\.\\.)(?!.*\\.$)(?!\\.)[a-zA-Z0-9.]{4,32}$"))) {
      return "Invalid email address, please try again using the following guidelines:\n- Between 4 and 32 characters\n- Contains only letters, numbers, and non-consecutive periods\n- Does not start or end with a period\n\nPlease enter a valid email:\n";
    }
    return null;
  }

  // change email
  public void changeUsername(String username) {
    String emailValid = isEmailValid(username);
    if (emailValid != null) {
      System.out.println(emailValid); // prints reason for invalidity & requests new email input
      changeUsername(this.scanner.next()); // recursive call with new user input
      return;
    }

    if (isUsernameTaken(username)) { // checking if username is taken
      System.out.println(
          "Sorry, this username is already in use\nIf you are the owner of this username, please contact your IT administrator\n\nIf you are not the owner, please use the following tips to create a new username:\n- Incorporate middle name\n- Abbreviate or initialize first, middle, and/or last names\n- Change order of name appearance\n\nExamples:\n John.B.Smith, J.Smith, John.Bob.Smith, John.Smith, Smith.John.B\n\nIf possible, use only characters from your name, and avoid adding numbers.\n Enter new username: ");
      changeUsername(this.scanner.next()); // call changeUsername with new user input
      return;
    }

    this.employeeUsername = username; // update username
    this.emailAddress = String.format("%s%s", this.employeeUsername, this.employeeDomain); // update email
  }

  // * getters
  public String getName() {
    return firstName + " " + lastName;
  }

  public String getEmail() {
    return emailAddress;
  }

  public String getDepartment() {
    return department;
  }

  public int getMailCapacity() {
    return mailboxCapacity;
  }

}