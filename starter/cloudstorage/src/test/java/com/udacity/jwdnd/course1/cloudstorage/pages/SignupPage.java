package com.udacity.jwdnd.course1.cloudstorage.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SignupPage extends DriverHelper {
    
    private final By backToLogin = By.id("backToLoginLink");
    private final By inputFirstname = By.id("inputFirstName");
    private final By inputLastname = By.id("inputLastName");
    private final By inputUsername = By.id("inputUsername");
    private final By inputPassword = By.id("inputPassword");
    private final By signupBtn = By.id("submit-button");
    private final By signUpSuccessMsg = By.id("success-msg");
    private final By signUpErrorMsg = By.id("error-msg");

    public SignupPage(WebDriver driver) {
        super.driver = driver;
    }

    public void signupUserAndSubmit(String firstname, String lastname, String username,
                                    String password) {
        textInput(inputFirstname, firstname);
        textInput(inputLastname, lastname);
        textInput(inputUsername, username);
        textInput(inputPassword, password);
        clickElement(signupBtn);
        sleepThread(1);
    }

    public boolean isSuccessMsgVisible() {
        waitForElementVisibility(signUpSuccessMsg);
        return driver.findElement(signUpSuccessMsg).isDisplayed();
    }

    public boolean isErrorMsgVisible() {
        waitForElementVisibility(signUpErrorMsg);
        return driver.findElement(signUpErrorMsg).isDisplayed();
    }

    public void backToLogin() {
        clickElement(backToLogin);
    }
}
