package com.udacity.jwdnd.course1.cloudstorage.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends DriverHelper {

    private final By headerLogin = By.id("headerLogin");
    private final By inputUsername = By.id("inputUsername");
    private final By inputPassword = By.id("inputPassword");
    private final By submitBtn = By.id("submit-button");
    private final By signUpErrorMsg = By.id("error-msg");

    public LoginPage(WebDriver driver) {
        super.driver = driver;
    }

    public void loginUserAndSubmit(String username, String password) {
        waitForPageElementsToBeVisible();
        textInput(inputUsername, username);
        textInput(inputPassword, password);
        clickElement(submitBtn);
        sleepThread(1);
    }

    public void waitForPageElementsToBeVisible() {
        sleepThread(1);
        waitForElementVisibility(headerLogin);
        waitForElementVisibility(inputUsername);
        waitForElementVisibility(inputPassword);
        waitForElementToBeClickable(submitBtn);
    }

    public boolean isErrorMsgVisible() {
        waitForElementVisibility(signUpErrorMsg);
        return driver.findElement(signUpErrorMsg).isDisplayed();
    }
}
