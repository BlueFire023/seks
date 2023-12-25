package seks.testfx.main;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainController {
  private static final Logger logger = LoggerFactory.getLogger(MainController.class);
  @FXML
  private Label welcomeLabel;
  @FXML
  private ListView<String> ingredientListView;
  @FXML
  private ListView<String> pizzaListView;
  @FXML
  private Button helloButton;

  public void initialize(){
    welcomeLabel.setText("");
  }

  @FXML
  public void onHelloButton(){
    welcomeLabel.setText("Welcome to the Pizza constructor!");
  }
}