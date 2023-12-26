package seks.testfx.main;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;
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
    stage.setTitle("Pizza constructor");
    stage.setScene(scene);
    stage.show();
  }
}