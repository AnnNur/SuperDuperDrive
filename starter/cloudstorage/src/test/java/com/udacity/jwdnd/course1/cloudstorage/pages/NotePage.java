package com.udacity.jwdnd.course1.cloudstorage.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class NotePage extends DriverHelper {
    private final By notesTab = By.id("nav-notes-tab");
    private final By logoutBtn = By.id("logoutBtn");

    // notes
    private final By addNoteBtn = By.id("addNoteBtn");
    private final By noteTitle = By.cssSelector("#notesTable tbody tr td:nth-of-type(2)");
    private final By noteDescription = By.cssSelector("#notesTable tbody tr td:nth-of-type(3)");
    private final By noteEditBtn = By.id("noteEditBtn");
    private final By noteDeleteBtn = By.id("noteDeleteBtn");

    // note popup
    private final By noteLabel = By.id("noteModalLabel");
    private final By inputNoteTitle = By.id("note-title");
    private final By inputNoteDesc = By.id("note-description");
    private final By saveChanges = By.id("noteSaveChanges");

    // redirect message
    private final By homeIfSuccess = By.id("redirectHomeIfSuccess");

    public NotePage(WebDriver driver) {
        super.driver = driver;
    }

    //region Notes CRUD verification methods
    //-----------------------------------------------------------------------------------------------------
    public void addNote(String title, String description) {
        changeToNotesTab();
        clickElement(addNoteBtn);
        submitNote(title, description);
        clickHomeIfSuccess();
        changeToNotesTab();
        waitForElementToBeClickable(addNoteBtn);
    }

    public void updateNote(String title, String description) {
        clickElement(noteEditBtn);
        submitNote(title, description);
        clickHomeIfSuccess();
        changeToNotesTab();
        waitForElementToBeClickable(addNoteBtn);
    }

    public void deleteNote() {
        clickElement(noteDeleteBtn);
        waitForInvisibilityOfElement(addNoteBtn);
        sleepThread(1);
        clickHomeIfSuccess();
        changeToNotesTab();
        waitForElementToBeClickable(addNoteBtn);
    }
    //-----------------------------------------------------------------------------------------------------
    //endregion

    public void submitNote(String title, String description) {
        waitForElementVisibility(noteLabel);
        textInput(inputNoteTitle, title);
        textInput(inputNoteDesc, description);
        clickElement(saveChanges);
        waitForInvisibilityOfElement(addNoteBtn);
        sleepThread(1);
        waitForElementVisibility(homeIfSuccess);
    }

    public String getNoteTitle() {
        return getElementText(noteTitle);
    }

    public String getNoteDescription() {
        return getElementText(noteDescription);
    }

    public int getNoteElementsCount() {
        return driver.findElements(noteTitle).size();
    }

    public void changeToNotesTab() {
        clickElement(notesTab);
        waitForElementToBeClickable(addNoteBtn);
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

    public void logoutUser(){
        waitForElementToBeClickable(logoutBtn);
        clickElement(logoutBtn);
    }
}
