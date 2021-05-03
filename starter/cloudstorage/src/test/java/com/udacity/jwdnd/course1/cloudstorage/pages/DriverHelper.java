package com.udacity.jwdnd.course1.cloudstorage.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class DriverHelper {

    protected WebDriver driver;
    
    protected void clickElement(By element) {
        waitForElementToBeClickable(element);
        driver.findElement(element).click();
    }

    protected String getElementText(By element) {
        return driver.findElement(element).getText();
    }

    protected void textInput(By element, String text) {
        waitForElementVisibility(element);
        waitForElementToBeClickable(element);
        driver.findElement(element).clear();
        driver.findElement(element).sendKeys(text);
    }

    public void sleepThread(int sec) {
        try {
            Thread.sleep(sec * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void waitForElementVisibility(By element) {
        WebDriverWait wait = new WebDriverWait(driver, 15);
        wait.until(ExpectedConditions.visibilityOfElementLocated(element));
    }

    protected void waitForElementToBeClickable(By element) {
        WebDriverWait wait = new WebDriverWait(driver, 15);
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    protected void waitForInvisibilityOfElement(By element) {
        WebDriverWait wait = new WebDriverWait(driver, 15);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(element));
    }
}
