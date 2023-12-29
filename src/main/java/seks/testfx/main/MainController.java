package seks.testfx.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainController {
  private static final Logger logger = LoggerFactory.getLogger(MainController.class);
  ToggleGroup toggleGroup = new ToggleGroup();
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
  @FXML
  private CheckBox cheeseCheckBox;
  @FXML
  private CheckBox doubleCheckBox;
  @FXML
  private CheckBox kebabCheckBox;
  @FXML
  private TextArea resultTextArea;
  @FXML
  private TextField nameTextField;
  @FXML
  private TextField zipcodeTextField;
  @FXML
  private TextField houseNumberTextField;
  @FXML
  private TextField streetTextField;
  @FXML
  private Spinner<Integer> countSpinner;
  @FXML
  private Label priceLabel;
  @FXML
  private Button xButton;

  public void initialize() {
    //add draggable items to the ingredient list
    ingredientListView.getItems()
        .addAll("Tomato", "Cheese", "Ham", "Mushrooms", "Pineapple", "Pepperoni", "Onion", "Bacon",
            "Salami", "Olives");
    // Set up drag and drop for ingredientListView
    ingredientListView.setOnDragDetected(event -> {
      logger.info("ingredientListView.setOnDragDetected");
      String ingredient = ingredientListView.getSelectionModel().getSelectedItem();
      if (ingredient != null) {
        logger.info("Selected item {} from ingredientListView", ingredient);
        Dragboard dragboard = ingredientListView.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent content = new ClipboardContent();
        content.putString(ingredient);
        dragboard.setContent(content);
        event.consume();
      }
    });

    ingredientListView.setOnDragOver(event -> {
      if (event.getGestureSource() != ingredientListView && event.getDragboard().hasString()) {
        event.acceptTransferModes(TransferMode.MOVE);
      }
      event.consume();
    });

    ingredientListView.setOnDragDropped(event -> {
      logger.info("ingredientListView.setOnDragDropped");
      Dragboard dragboard = event.getDragboard();
      if (dragboard.hasString()) {
        logger.info("Dragged item: {}", dragboard.getString());
        ingredientListView.getItems().add(dragboard.getString());
        event.setDropCompleted(true);
      } else {
        event.setDropCompleted(false);
      }
      event.consume();
    });

    ingredientListView.setOnDragDone(event -> {
      logger.info("ingredientListView.setOnDragDone");
      if (event.getTransferMode() == TransferMode.MOVE) {
        ingredientListView.getItems()
            .remove(ingredientListView.getSelectionModel().getSelectedItem());
      }
      updatePriceLabel();
      generatePizzaJson();
    });

    // Set up drag and drop for pizzaListView
    pizzaListView.setOnDragDetected(event -> {
      logger.info("pizzaListView.setOnDragDetected");
      String ingredient = pizzaListView.getSelectionModel().getSelectedItem();
      if (ingredient != null) {
        logger.info("Selected item {} from pizzaListView", ingredient);
        Dragboard dragboard = pizzaListView.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent content = new ClipboardContent();
        content.putString(ingredient);
        dragboard.setContent(content);
        event.consume();
      }
    });

    pizzaListView.setOnDragOver(event -> {
      if (event.getGestureSource() != pizzaListView && event.getDragboard().hasString()) {
        event.acceptTransferModes(TransferMode.MOVE);
      }
      event.consume();
    });

    pizzaListView.setOnDragDropped(event -> {
      logger.info("pizzaListView.setOnDragDropped");
      Dragboard dragboard = event.getDragboard();
      if (dragboard.hasString()) {
        logger.info("Dragged item: {}", dragboard.getString());
        pizzaListView.getItems().add(dragboard.getString());
        event.setDropCompleted(true);
      } else {
        event.setDropCompleted(false);
      }
      event.consume();
    });

    pizzaListView.setOnDragDone(event -> {
      logger.info("pizzaListView.setOnDragDone");
      if (event.getTransferMode() == TransferMode.MOVE) {
        pizzaListView.getItems().remove(pizzaListView.getSelectionModel().getSelectedItem());
      }
      updatePriceLabel();
      generatePizzaJson();
    });

    // Set up radio buttons
    size32RadioButton.setToggleGroup(toggleGroup);
    size26RadioButton.setToggleGroup(toggleGroup);
    size20RadioButton.setToggleGroup(toggleGroup);

    size26RadioButton.setSelected(true);

    size26RadioButton.setOnAction(event -> {
      logger.info("size26RadioButton pressed");
      updatePriceLabel();
      generatePizzaJson();
    });
    size32RadioButton.setOnAction(event -> {
      logger.info("size32RadioButton pressed");
      updatePriceLabel();
      generatePizzaJson();
    });
    size20RadioButton.setOnAction(event -> {
      logger.info("size20RadioButton pressed");
      updatePriceLabel();
      generatePizzaJson();
    });

    cheeseCheckBox.setOnAction(event -> {
      logger.info("cheeseCheckBox pressed");
      updatePriceLabel();
      generatePizzaJson();
    });
    doubleCheckBox.setOnAction(event -> {
      logger.info("doubleCheckBox pressed");
      updatePriceLabel();
      generatePizzaJson();
    });
    kebabCheckBox.setOnAction(event -> {
      logger.info("kebabCheckBox pressed");
      updatePriceLabel();
      generatePizzaJson();
    });

    resultTextArea.setEditable(false);

    countSpinner.setValueFactory(
        new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1, 1));

    countSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
      logger.info("countSpinner value changed");
      updatePriceLabel();
      generatePizzaJson();
    });

    // Set up price label
    priceLabel.setText("7.50€");

    // Set up text fields
    nameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
      logger.info("nameTextField text changed");
      generatePizzaJson();
    });

    zipcodeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
      logger.info("zipcodeTextField text changed");
      generatePizzaJson();
    });

    houseNumberTextField.textProperty().addListener((observable, oldValue, newValue) -> {
      logger.info("houseNumberTextField text changed");
      generatePizzaJson();
    });

    streetTextField.textProperty().addListener((observable, oldValue, newValue) -> {
      logger.info("streetTextField text changed");
      generatePizzaJson();
    });

    FontIcon icon = new FontIcon(FontAwesomeSolid.TIMES);
    icon.setIconSize(18);
    icon.setIconColor(Color.RED);
    xButton.setGraphic(icon);

    updatePriceLabel();
    generatePizzaJson();
  }

  @FXML
  public void onXButton() {
    logger.info("XButton clicked");
    if (!pizzaListView.getItems().isEmpty()) {
      ingredientListView.getItems().addAll(pizzaListView.getItems());
      pizzaListView.getItems().clear();
      updatePriceLabel();
      generatePizzaJson();
    }
  }

  @FXML
  public void onCopyButton() {
    logger.info("CopyButton clicked");
    String json = resultTextArea.getText();
    Clipboard clipboard = Clipboard.getSystemClipboard();
    ClipboardContent content = new ClipboardContent();
    content.putString(json);
    clipboard.setContent(content);
  }

  @FXML
  public void onOrderButton() {
    logger.info("OrderButton clicked");
    if (nameTextField.getText().isEmpty() || zipcodeTextField.getText().isEmpty() ||
        houseNumberTextField.getText().isEmpty() || streetTextField.getText().isEmpty()) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText("Error");
      alert.setContentText("Please fill in all the fields!");
      alert.showAndWait();
    } else {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Order");
      alert.setHeaderText("Order placed");
      alert.setContentText("Your order has been placed!");
      alert.showAndWait();
    }
  }

  private void generatePizzaJson() {
    logger.info("generatePizzaJson: no params");
    Map<String, Object> pizza = new HashMap<>();

    // Add the size to the Map
    if (size32RadioButton.isSelected()) {
      pizza.put("size", "32cm");
    } else if (size26RadioButton.isSelected()) {
      pizza.put("size", "26cm");
    } else if (size20RadioButton.isSelected()) {
      pizza.put("size", "20cm");
    }

    // Add the cheese, double toppings, and kebab options to the Map
    pizza.put("cheese crust", cheeseCheckBox.isSelected());
    pizza.put("double toppings", doubleCheckBox.isSelected());
    pizza.put("kebab sauce", kebabCheckBox.isSelected());

    // Add the ingredients from the pizzaListView to the Map
    pizza.put("ingredients", new ArrayList<>(pizzaListView.getItems()));

    // Add the name, zipcode, house number, and street to the Map
    pizza.put("name", nameTextField.getText());
    pizza.put("zipcode", zipcodeTextField.getText());
    pizza.put("house number", houseNumberTextField.getText());
    pizza.put("street", streetTextField.getText());

    // Add the count to the Map
    pizza.put("count", countSpinner.getValue());

    // Add the price to the Map
    pizza.put("price", priceLabel.getText());

    // Create a Gson object
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // Convert the Map to a JSON string
    String json = gson.toJson(pizza);

    // Set the text of the resultTextArea to the JSON string
    resultTextArea.setText(json);
  }

  private void updatePriceLabel() {
    logger.info("updatePriceLabel: no params");
    double price = 7.50;
    double ingrendientMultiplier = 1.00;
    if (size32RadioButton.isSelected()) {
      ingrendientMultiplier = 1.50;
      price += 2.50;
    } else if (size20RadioButton.isSelected()) {
      ingrendientMultiplier = 0.50;
      price -= 2.50;
    }
    if (doubleCheckBox.isSelected()) {
      price += 2.00 * ingrendientMultiplier;
    }
    if (kebabCheckBox.isSelected()) {
      price += 2.00 * ingrendientMultiplier;
    }
    if (cheeseCheckBox.isSelected()) {
      price += 2.00 * ingrendientMultiplier;
    }
    price += pizzaListView.getItems().size() * ingrendientMultiplier;
    price *= countSpinner.getValue();
    priceLabel.setText(String.format("%.2f€", price));
  }
}