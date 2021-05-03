package com.udacity.jwdnd.course1.cloudstorage.pages;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CredentialPage extends DriverHelper {

    private final By credentialsTab = By.id("nav-credentials-tab");
    private final By notesTab = By.id("nav-notes-tab");
    private final By logoutBtn = By.id("logoutBtn");

    // credentials
    private final By addNewCredBtn = By.id("addNewCredBtn");
    private final By editCredentialBtn = By.id("editCredentialBtn");
    private final By deleteCredentialBtn = By.id("deleteCredentialBtn");
    private final By credentialUrl = By.id("credentialUrl");
    private final By credentialUsername = By.id("credentialUsername");
    private final By credentialPassword = By.id("credentialPassword");

    // credentials popup
    private final By credentialLabel = By.id("credentialModalLabel");
    private final By inputCredentialUrl = By.id("credential-url");
    private final By inputCredentialUser = By.id("credential-username");
    private final By inputCredentialPassword = By.id("credential-password");
    private final By credentialSaveChangesBtn = By.id("credSaveChanges");

    // redirect message
    private final By homeIfSuccess = By.id("redirectHomeIfSuccess");

    public CredentialPage(WebDriver driver) {
        super.driver = driver;
    }

    //region Credentials CRUD verification methods
    //-----------------------------------------------------------------------------------------------------
    public void addCredential(String url, String username, String password) {
        changeToCredentialsTab();
        clickElement(addNewCredBtn);
        submitCredential(url, username, password);
        clickHomeIfSuccess();
        changeToCredentialsTab();//my changes
        waitForElementToBeClickable(addNewCredBtn);
    }

    public void updateCredential(String url, String username, String password) {
        clickElement(editCredentialBtn);
        submitCredential(url, username, password);
        clickHomeIfSuccess();
        changeToCredentialsTab();//my changes
        waitForElementToBeClickable(addNewCredBtn);
    }

    public void deleteCredential() {
        clickElement(deleteCredentialBtn);
        waitForInvisibilityOfElement(addNewCredBtn);
        sleepThread(1);
        waitForElementVisibility(homeIfSuccess);
        clickHomeIfSuccess();
        changeToCredentialsTab();//my changes
        waitForElementToBeClickable(addNewCredBtn);
    }
    //-----------------------------------------------------------------------------------------------------
    //endregion

    public void submitCredential(String url, String username, String password) {
        waitForElementVisibility(credentialLabel);
        textInput(inputCredentialUrl, url);
        textInput(inputCredentialUser, username);
        textInput(inputCredentialPassword, password);
        clickElement(credentialSaveChangesBtn);
        sleepThread(1);
        waitForElementVisibility(homeIfSuccess);
    }

    public String getCredentialUrl() {
        return getElementText(credentialUrl);
    }

    public String getCredentialUsername() {
        return getElementText(credentialUsername);
    }

    public String getCredentialPassword() {
        return getElementText(credentialPassword);
    }

    public int getCredentialElementsCount() {
        return driver.findElements(credentialUrl).size();
    }

    public void changeToCredentialsTab() {
        clickElement((credentialsTab));
        waitForElementToBeClickable(addNewCredBtn);
    }

    public void editFirstCredential() {
        clickElement(editCredentialBtn);
        waitForElementVisibility(credentialLabel);
    }

    public Credential getCredentialProperties() {
        Credential credential = new Credential();
        credential.setUrl(driver.findElement(inputCredentialUrl).getAttribute("value"));
        credential.setUsername(driver.findElement(inputCredentialUser).getAttribute("value"));
        credential.setPassword(driver.findElement(inputCredentialPassword).getAttribute("value"));
        return credential;
    }

    public void waitForPageElementsToBeClickable() {
        sleepThread(1);
        waitForElementToBeClickable(logoutBtn);
        waitForElementToBeClickable(notesTab);
    }

    public void clickHomeIfSuccess() {
        waitForElementVisibility(homeIfSuccess);
        clickElement(homeIfSuccess);
        sleepThread(1);
        waitForPageElementsToBeClickable();
    }
}
