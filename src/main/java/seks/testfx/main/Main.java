package seks.testfx.main;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main extends Application {
  private static final Logger logger = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {
    launch();
  }

  @Override
  public void start(Stage stage) throws IOException {
    logger.info("Starting Main...");
    FXMLLoader fxmlLoader =
        new FXMLLoader(getClass().getResource("/seks/testfx/fxml/main-view.fxml"));
    Scene scene = new Scene(fxmlLoader.load());
    stage.setTitle("Pizza Maker");
    stage.setScene(scene);
    stage.show();
  }

  @Override
  public void stop() {
    logger.info("stop: Shutting down");
  }
}