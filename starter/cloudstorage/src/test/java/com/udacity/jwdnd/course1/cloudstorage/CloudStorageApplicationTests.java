package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.pages.*;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

    @Autowired
    UserService userService;

    @Autowired
    NoteService noteService;

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private LoginPage loginPage;
    private SignupPage signupPage;
    private HomePage homePage;
    private NotePage notePage;
    private CredentialPage credentialPage;
    public static final String TEST_FIRSTNAME = "Abc";
    public static final String TEST_LASTNAME = "Last Abc";
    private static final String TEST_USERNAME = "testUserName";
    public static final String TEST_PASSWORD = "1234";
    public static final String TEST_NOTE_TITLE = "NoteTitle";
    public static final String TEST_NOTE_DESCRIPTION = "NoteDescription";
    public static final String TEST_NOTE_TITLE_EDITED = "NoteTitle edited";
    public static final String TEST_NOTE_DESCRIPTION_EDITED = "NoteDescription edited";
    public static final String TEST_CREDENTIAL_URL = "http://www.example.com";
    public static final String TEST_CREDENTIAL_URL_UPDATED = "http://www.ireland.ie";
    public static final String TEST_CREDENTIAL_USERNAME_EDITED = "testUserName edited";
    public static final String TEST_CREDENTIAL_PASSWORD_EDITED = "1234 edited";

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
        loginPage = new LoginPage(driver);
        signupPage = new SignupPage(driver);
        homePage = new HomePage(driver);
        notePage = new NotePage(driver);
        credentialPage = new CredentialPage(driver);
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    @Test
    public void verifyUnauthorizedAccessToHome() {
        driver.get("http://localhost:" + this.port + "/home");
        Assertions.assertNotEquals("Home", driver.getTitle());
    }

    @Test
    public void should_EnableSignupAndLogin_ForNewUser_WithCorrectCredentials() {
        //region Signup
        //-----------------------------------------------------------------------------------------------------
        driver.get("http://localhost:" + this.port + "/signup");
        signupPage.signupUserAndSubmit(TEST_FIRSTNAME, TEST_LASTNAME, TEST_USERNAME, TEST_PASSWORD);
        Assertions.assertTrue(signupPage.isSuccessMsgVisible());
        //-----------------------------------------------------------------------------------------------------
        //endregion

        //region login
        //-----------------------------------------------------------------------------------------------------

        //verify if username is valid
        loginPage.loginUserAndSubmit(RandomStringUtils.randomAlphabetic(3), TEST_PASSWORD);
        Assertions.assertTrue(loginPage.isErrorMsgVisible());

        loginPage.loginUserAndSubmit(TEST_USERNAME, TEST_PASSWORD);
        homePage.waitForPageElementsToBeClickable();
        Assertions.assertEquals("Home", driver.getTitle());
        //-----------------------------------------------------------------------------------------------------
        //endregion

        //region logout
        //-----------------------------------------------------------------------------------------------------
        homePage.clickLogout();
        loginPage.waitForPageElementsToBeVisible();
        Assertions.assertEquals("Login", driver.getTitle());
        //-----------------------------------------------------------------------------------------------------
        //endregion

        // region home no longer accessible
        //-----------------------------------------------------------------------------------------------------
        driver.get("http://localhost:" + this.port + "/home");
        Assertions.assertNotEquals("Home", driver.getTitle());
        //-----------------------------------------------------------------------------------------------------
        //endregion
    }

    //region NOTES
    //-----------------------------------------------------------------------------------------------------
    @Test
    public void should_AddNoteToPage_IfSubmittedTitleAndDesription() {
        loginAndSubmitTestUser();
        notePage.addNote(TEST_NOTE_TITLE, TEST_NOTE_DESCRIPTION);
        Assertions.assertEquals(TEST_NOTE_TITLE, notePage.getNoteTitle());
        Assertions.assertEquals(TEST_NOTE_DESCRIPTION, notePage.getNoteDescription());
    }

    @Test
    public void should_EditNote_IfUpdatedNoteProperties() {
        loginUserWithNoteAdded();
        notePage.changeToNotesTab();
        notePage.updateNote(TEST_NOTE_TITLE_EDITED, TEST_NOTE_DESCRIPTION_EDITED);
        Assertions.assertEquals(TEST_NOTE_TITLE_EDITED, notePage.getNoteTitle());
        Assertions.assertEquals(TEST_NOTE_DESCRIPTION_EDITED, notePage.getNoteDescription());
    }

    @Test
    public void should_DeleteNoteFromPage_IfDeleteNoteBtnClicked() {
        loginUserWithNoteAdded();
        notePage.changeToNotesTab();
        notePage.deleteNote();
        notePage.changeToNotesTab();
        Assertions.assertEquals(0, notePage.getNoteElementsCount());
        Assertions.assertThrows(NoSuchElementException.class, () -> notePage.getNoteTitle());
    }

    private void loginUserWithNoteAdded() {
        User user = addTestUser();
        loginAndSubmitTestUser(user);
        notePage.changeToNotesTab();
        notePage.addNote(TEST_NOTE_TITLE, TEST_NOTE_DESCRIPTION);
        notePage.logoutUser();
        loginAndSubmitTestUser(user);
    }
    //-----------------------------------------------------------------------------------------------------
    //endregion

    //region CREDENTIALS
    //-----------------------------------------------------------------------------------------------------
    @Test
    public void should_AddCredentialToPage_IfSubmittedCredentials() {
        loginAndSubmitTestUser();
        credentialPage.addCredential(TEST_CREDENTIAL_URL, TEST_USERNAME, TEST_PASSWORD);
        Assertions.assertEquals(TEST_CREDENTIAL_URL, credentialPage.getCredentialUrl());
        Assertions.assertEquals(TEST_USERNAME, credentialPage.getCredentialUsername());
        Assertions.assertNotEquals(TEST_PASSWORD, credentialPage.getCredentialPassword());
        credentialPage.editFirstCredential();
        Credential credentialPopUpProperties = credentialPage.getCredentialProperties();
        Assertions.assertEquals(TEST_CREDENTIAL_URL, credentialPopUpProperties.getUrl());
        Assertions.assertEquals(TEST_USERNAME, credentialPopUpProperties.getUsername());
        Assertions.assertEquals(TEST_PASSWORD, credentialPopUpProperties.getPassword());
    }

    @Test
    public void should_EditCredential_IfUpdatedCredentialProperties() {
        loginUserWithCredentialAdded();
        credentialPage.changeToCredentialsTab();
        credentialPage.updateCredential(TEST_CREDENTIAL_URL_UPDATED, TEST_CREDENTIAL_USERNAME_EDITED, TEST_CREDENTIAL_PASSWORD_EDITED);
        Assertions.assertEquals(TEST_CREDENTIAL_URL_UPDATED, credentialPage.getCredentialUrl());
        Assertions.assertEquals(TEST_CREDENTIAL_USERNAME_EDITED, credentialPage.getCredentialUsername());
        Assertions.assertNotEquals(TEST_CREDENTIAL_PASSWORD_EDITED, credentialPage.getCredentialPassword());
        credentialPage.editFirstCredential();
        Credential credFromModal = credentialPage.getCredentialProperties();
        Assertions.assertEquals(TEST_CREDENTIAL_URL_UPDATED, credFromModal.getUrl());
        Assertions.assertEquals(TEST_CREDENTIAL_USERNAME_EDITED, credFromModal.getUsername());
    }

    @Test
    public void should_DeleteCredentialFromPage_IfDeleteCredentialBtnClicked() {
        loginUserWithCredentialAdded();
        credentialPage.changeToCredentialsTab();
        credentialPage.deleteCredential();
        Assertions.assertEquals(0, credentialPage.getCredentialElementsCount());
        Assertions.assertThrows(NoSuchElementException.class, () -> credentialPage.getCredentialUrl());
    }

    private void loginUserWithCredentialAdded() {
        User user = addTestUser();
        loginAndSubmitTestUser(user);
        credentialPage.changeToCredentialsTab();
        credentialPage.addCredential(TEST_CREDENTIAL_URL, TEST_USERNAME, TEST_PASSWORD);
        credentialPage.logoutUser();
        loginAndSubmitTestUser(user);
    }
    //-----------------------------------------------------------------------------------------------------
    //endregion

    private User addTestUser() {
        String username = RandomStringUtils.random(4, true, false);
        String password = RandomStringUtils.random(4, true, true);
        User user = new User();
        user.setUserId(1);
        user.setFirstName(TEST_FIRSTNAME);
        user.setLastName(TEST_LASTNAME);
        user.setUsername(username);
        user.setPassword(password);
        userService.createUser(user);
        user.setPassword(password);//reset password
        return user;
    }

    private void loginAndSubmitTestUser(User... users) {
        User user;
        user = users.length == 0 ? addTestUser() : users[0];

        driver.get("http://localhost:" + this.port + "/login");
        loginPage.loginUserAndSubmit(user.getUsername(), user.getPassword());
    }
}
