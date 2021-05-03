package com.udacity.jwdnd.course1.cloudstorage.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage extends DriverHelper {

    private final By notesTab = By.id("nav-notes-tab");
    private final By logoutBtn = By.id("logoutBtn");

    public HomePage(WebDriver driver) {
        super.driver = driver;
    }

    public void waitForPageElementsToBeClickable() {
        sleepThread(1);
        waitForElementToBeClickable(logoutBtn);
        waitForElementToBeClickable(notesTab);
    }

    public void clickLogout() {
        clickElement(logoutBtn);
    }
}
