package seks.testfx.seks;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloController {
  private static final Logger logger = LoggerFactory.getLogger(HelloController.class);
  @FXML
  private Label welcomeText;

  @FXML
  protected void onHelloButtonClick() {
    logger.info("HelloButton clicked!");
    welcomeText.setText("Welcome to JavaFX Application!");
  }
}