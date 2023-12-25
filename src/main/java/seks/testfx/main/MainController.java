package seks.testfx.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
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
  private RadioButton size32RadioButton;
  @FXML
  private RadioButton size26RadioButton;
  @FXML
  private RadioButton size20RadioButton;
  ToggleGroup toggleGroup = new ToggleGroup();

  @FXML
  private CheckBox cheeseCheckBox;
  @FXML
  private CheckBox doubleCheckBox;
  @FXML
  private CheckBox kebabCheckBox;
  @FXML
  private TextArea resultTextArea;


  public void initialize() {
    welcomeLabel.setText("");
    //add draggable items to the ingredient list
    ingredientListView.getItems()
        .addAll("Tomato", "Cheese", "Ham", "Mushrooms", "Pineapple", "Pepperoni", "Onion", "Bacon",
            "Salami", "Olives");
    // Set up drag and drop for ingredientListView
    ingredientListView.setOnDragDetected(event -> {
      String ingredient = ingredientListView.getSelectionModel().getSelectedItem();
      if (ingredient != null) {
        Dragboard dragboard = ingredientListView.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent content = new ClipboardContent();
        content.putString(ingredient);
        dragboard.setContent(content);
        event.consume();
      }
      generateJSON();
    });

    ingredientListView.setOnDragOver(event -> {
      if (event.getGestureSource() != ingredientListView && event.getDragboard().hasString()) {
        event.acceptTransferModes(TransferMode.MOVE);
      }
      generateJSON();
      event.consume();
    });

    ingredientListView.setOnDragDropped(event -> {
      Dragboard dragboard = event.getDragboard();
      if (dragboard.hasString()) {
        ingredientListView.getItems().add(dragboard.getString());
        event.setDropCompleted(true);
      } else {
        event.setDropCompleted(false);
      }
      generateJSON();
      event.consume();
    });

    ingredientListView.setOnDragDone(event -> {
      if (event.getTransferMode() == TransferMode.MOVE) {
        ingredientListView.getItems()
            .remove(ingredientListView.getSelectionModel().getSelectedItem());
      }
      generateJSON();
    });

    // Set up drag and drop for pizzaListView
    pizzaListView.setOnDragDetected(event -> {
      String ingredient = pizzaListView.getSelectionModel().getSelectedItem();
      if (ingredient != null) {
        Dragboard dragboard = pizzaListView.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent content = new ClipboardContent();
        content.putString(ingredient);
        dragboard.setContent(content);
        event.consume();
      }
      generateJSON();
    });

    pizzaListView.setOnDragOver(event -> {
      if (event.getGestureSource() != pizzaListView && event.getDragboard().hasString()) {
        event.acceptTransferModes(TransferMode.MOVE);
      }
      generateJSON();
      event.consume();
    });

    pizzaListView.setOnDragDropped(event -> {
      Dragboard dragboard = event.getDragboard();
      if (dragboard.hasString()) {
        pizzaListView.getItems().add(dragboard.getString());
        event.setDropCompleted(true);
      } else {
        event.setDropCompleted(false);
      }
      generateJSON();
      event.consume();
    });

    pizzaListView.setOnDragDone(event -> {
      if (event.getTransferMode() == TransferMode.MOVE) {
        pizzaListView.getItems().remove(pizzaListView.getSelectionModel().getSelectedItem());
      }
      generateJSON();
    });

    // Set up radio buttons
    size32RadioButton.setToggleGroup(toggleGroup);
    size26RadioButton.setToggleGroup(toggleGroup);
    size20RadioButton.setToggleGroup(toggleGroup);

    size26RadioButton.setSelected(true);

    size26RadioButton.setOnAction(event -> generateJSON());
    size32RadioButton.setOnAction(event -> generateJSON());
    size20RadioButton.setOnAction(event -> generateJSON());

    cheeseCheckBox.setOnAction(event -> generateJSON());
    doubleCheckBox.setOnAction(event -> generateJSON());
    kebabCheckBox.setOnAction(event -> generateJSON());
    generateJSON();
  }


  @FXML
  public void onHelloButton() {
    logger.info("onHelloButton");
    welcomeLabel.setText("Welcome to the Pizza constructor!");
  }

  @FXML
  public void onXButton() {
    logger.info("onXButton");
    ingredientListView.getItems().addAll(pizzaListView.getItems());
    pizzaListView.getItems().clear();
    generateJSON();
  }

  @FXML
  public void onCopyButton() {
    logger.info("onCopyButton");
    String json = resultTextArea.getText();
    Clipboard clipboard = Clipboard.getSystemClipboard();
    ClipboardContent content = new ClipboardContent();
    content.putString(json);
    clipboard.setContent(content);
  }

  public void generateJSON() {
    logger.info("onGenerateButton");
    Map<String, Object> pizza = new HashMap<>();

    // Add the size to the Map
    if (size32RadioButton.isSelected()) {
      pizza.put("size", "32cm");
    } else if (size26RadioButton.isSelected()) {
      pizza.put("size", "26cm");
    } else if (size20RadioButton.isSelected()) {
      pizza.put("size", "20cm");
    }

    // Add the cheese, double cheese, and kebab options to the Map
    pizza.put("cheese", cheeseCheckBox.isSelected());
    pizza.put("doubleCheese", doubleCheckBox.isSelected());
    pizza.put("kebab", kebabCheckBox.isSelected());

    // Add the ingredients from the pizzaListView to the Map
    pizza.put("ingredients", new ArrayList<>(pizzaListView.getItems()));

    // Create a Gson object
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // Convert the Map to a JSON string
    String json = gson.toJson(pizza);

    // Set the text of the resultTextArea to the JSON string
    resultTextArea.setText(json);
  }
}