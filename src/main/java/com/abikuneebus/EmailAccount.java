package com.abikuneebus;

public class EmailAccount {
  private String firstName;
  private String lastName;
  private String email;
  private int mailCapacity;
  private String department;
  private String username;
  private String hashedPassword;

  public EmailAccount(String firstName, String lastName, String email, int mailCapacity, String department,
      String hashedPassword) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.mailCapacity = mailCapacity;
    this.department = department;
    this.hashedPassword = hashedPassword;
  }

  // getters

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getDepartment() {
    return department;
  }

  public String getEmail() {
    return email;
  }

  public String getUsername() {
    return username;
  }

  public int getMailCapacity() {
    return mailCapacity;
  }

  public String getHashedPassword() {
    return hashedPassword;
  }

  // setters

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.hashedPassword = password;
  }

  public void setMailCapacity(int capacity) {
    this.mailCapacity = capacity;
  }

}