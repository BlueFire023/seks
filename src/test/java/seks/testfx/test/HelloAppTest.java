package seks.testfx.test;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testfx.framework.junit5.ApplicationTest;
import seks.testfx.seks.HelloApplication;

public class HelloAppTest extends ApplicationTest {
  private static final Logger logger = LoggerFactory.getLogger(HelloAppTest.class);

  @Override
  public void start(Stage stage) throws Exception {
    logger.info("Starting HelloApplication...");
    new HelloApplication().start(stage);
  }

  @Test
  public void should_display_hello_message() {
    logger.info("should_display_hello_message");

    verifyThat("#welcomeText", hasText(""));

    // simulate a user interaction
    clickOn("#helloButton");

    // verify that the label's text has changed
    verifyThat("#welcomeText", hasText("Welcome to JavaFX Application!"));
  }
}