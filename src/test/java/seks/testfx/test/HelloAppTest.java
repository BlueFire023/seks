package seks.testfx.test;

import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import seks.testfx.seks.HelloApplication;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

public class HelloAppTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        new HelloApplication().start(stage);
    }

    @Test
    public void should_display_hello_message() {
        // simulate a user interaction
        clickOn("#helloButton");

        // verify that the label's text has changed
        verifyThat("#welcomeText", hasText("Welcome to JavaFX Application!"));
    }
}