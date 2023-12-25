package seks.testfx.test;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testfx.framework.junit5.ApplicationTest;
import seks.testfx.main.Main;

public class AppTest extends ApplicationTest {
  private static final Logger logger = LoggerFactory.getLogger(AppTest.class);

  @Override
  public void start(Stage stage) throws Exception {
    logger.info("Starting Main...");
    new Main().start(stage);
  }

  @Test
  public void shouldDisplayWelcomeMessage() {
    logger.info("shouldDisplayWelcomeMessage");

    verifyThat("#welcomeLabel", hasText(""));

    // simulate a user interaction
    clickOn("#helloButton");

    // verify that the label's text has changed
    verifyThat("#welcomeLabel", hasText("Welcome to the Pizza constructor!"));
  }
}