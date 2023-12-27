package seks.testfx.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testfx.api.FxAssert.verifyThat;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.stage.Stage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.ListViewMatchers;
import seks.testfx.main.Main;

public class AppTest extends ApplicationTest {
  private static final Logger logger = LoggerFactory.getLogger(AppTest.class);

  @Override
  public void start(Stage stage) throws Exception {
    logger.info("Starting Main...");
    new Main().start(stage);
  }

  @Test
  @DisplayName("Should clear pizza list when clear button is clicked")
  public void shouldClearPizzaListWhenClearButtonClicked() {
    logger.info("shouldClearPizzaListWhenClearButtonClicked");

    drag("Tomato").interact(() -> dropTo("#pizzaListView"));
    drag("Mushrooms").interact(() -> dropTo("#pizzaListView"));
    drag("Pineapple").interact(() -> dropTo("#pizzaListView"));
    drag("Salami").interact(() -> dropTo("#pizzaListView"));

    clickOn("#xButton");

    verifyThat("#pizzaListView", ListViewMatchers.hasItems(0));
    verifyThat("#ingredientListView", ListViewMatchers.hasItems(10));
  }

  @Test
  @DisplayName("Test buttons")
  public void testPressButtons() {
    logger.info("testPressButtons");
    clickOn("#size32RadioButton");
    clickOn("#cheeseCheckBox");

    verifyThat("#size32RadioButton", RadioButton::isSelected);
    verifyThat("#cheeseCheckBox", CheckBox::isSelected);

    clickOn("#doubleCheckBox");
    clickOn("#kebabCheckBox");

    verifyThat("#doubleCheckBox", CheckBox::isSelected);
    verifyThat("#kebabCheckBox", CheckBox::isSelected);

    clickOn("#size26RadioButton");

    verifyThat("#size26RadioButton", RadioButton::isSelected);
  }

  @Test
  @DisplayName("Should generate correct JSON output after selecting ingredients and options")
  public void shouldGenerateCorrectJsonOutputAfterSelections() {
    logger.info("shouldGenerateCorrectJsonOutputAfterSelections");

    drag("Pineapple").interact(() -> dropTo("#pizzaListView"));
    drag("Salami").interact(() -> dropTo("#pizzaListView"));

    clickOn("#size32RadioButton");
    clickOn("#cheeseCheckBox");

    String expectedJson = """
        {
          "zipcode": "",
          "house number": "",
          "size": "32cm",
          "street": "",
          "price": "16.00€",
          "double toppings": false,
          "kebab sauce": false,
          "name": "",
          "count": 1,
          "ingredients": [
            "Pineapple",
            "Salami"
          ],
          "cheese crust": true
        }""";

    assertEquals(expectedJson, lookup("#resultTextArea").queryAs(TextArea.class).getText());

    clickOn("#size20RadioButton");
    clickOn("#cheeseCheckBox");
    clickOn("#doubleCheckBox");
    clickOn("#kebabCheckBox");

    expectedJson = """
        {
          "zipcode": "",
          "house number": "",
          "size": "20cm",
          "street": "",
          "price": "8.00€",
          "double toppings": true,
          "kebab sauce": true,
          "name": "",
          "count": 1,
          "ingredients": [
            "Pineapple",
            "Salami"
          ],
          "cheese crust": false
        }""";

    assertEquals(expectedJson, lookup("#resultTextArea").queryAs(TextArea.class).getText());
  }

  @Test
  @DisplayName("Should copy generated JSON to clipboard")
  public void shouldCopyGeneratedJsonToClipboard() {
    logger.info("shouldCopyGeneratedJsonToClipboard");

    drag("Mushrooms").interact(() -> dropTo("#pizzaListView"));
    drag("Salami").interact(() -> dropTo("#pizzaListView"));

    clickOn("#cheeseCheckBox");
    clickOn("#size20RadioButton");
    clickOn("#doubleCheckBox");
    clickOn("#copyButton");

    //workaround for IllegalStateException: not in FX application thread
    interact(() -> {
      String clipboardText = Clipboard.getSystemClipboard().getString();
      assertEquals(lookup("#resultTextArea").queryAs(TextArea.class).getText(), clipboardText);
    });
  }

  @Test
  @DisplayName("Should move selected ingredients to pizza list and back to ingredient list")
  public void shouldMoveSelectedIngredientsBetweenLists() {
    logger.info("shouldMoveSelectedIngredientsBetweenLists");
    drag("Pineapple").interact(() -> dropTo("#pizzaListView"));
    drag("Salami").interact(() -> dropTo("#pizzaListView"));

    verifyThat("#pizzaListView", ListViewMatchers.hasItems(2));
    verifyThat("#ingredientListView", ListViewMatchers.hasItems(8));

    drag("Pineapple").interact(() -> dropTo("#ingredientListView"));
    drag("Salami").interact(() -> dropTo("#ingredientListView"));

    verifyThat("#pizzaListView", ListViewMatchers.hasItems(0));
    verifyThat("#ingredientListView", ListViewMatchers.hasItems(10));
  }

  @Test
  @DisplayName("Should not move ingredients when dragged to invalid targets")
  public void shouldNotMoveIngredientsToInvalidTargets() {
    logger.info("shouldNotMoveIngredientsToInvalidTargets");
    drag("Pineapple").interact(() -> dropTo("#xButton"));
    logger.info("dragged Pineapple to xButton");

    verifyThat("#pizzaListView", ListViewMatchers.hasItems(0));
    verifyThat("#ingredientListView", ListViewMatchers.hasItems(10));

    drag("Salami").interact(() -> dropTo("#orderButton"));
    logger.info("dragged Salami to orderButton");

    verifyThat("#pizzaListView", ListViewMatchers.hasItems(0));
    verifyThat("#ingredientListView", ListViewMatchers.hasItems(10));

    drag("Mushrooms").interact(() -> dropTo("#resultTextArea"));
    logger.info("dragged Mushrooms to resultTextArea");

    verifyThat("#pizzaListView", ListViewMatchers.hasItems(0));
    verifyThat("#ingredientListView", ListViewMatchers.hasItems(10));
  }

  @Test
  @DisplayName("Should give error message when order button is clicked without the required fields filled")
  public void shouldGiveErrorMessageWhenOrderButtonClickedWithoutRequiredFieldsFilled() {
    logger.info("shouldGiveErrorMessageWhenOrderButtonClickedWithoutRequiredFieldsFilled");
    clickOn("#orderButton");

    DialogPane dialogPane = lookup(".dialog-pane").queryAs(DialogPane.class);
    String alertText = dialogPane.getContentText();

    assertEquals("Please fill in all the fields!", alertText);
  }

  @Test
  @DisplayName("Should order pizza when order button is clicked with all the required fields filled")
  public void shouldOrderPizzaWhenOrderButtonClickedWithAllRequiredFieldsFilled() {
    logger.info("shouldOrderPizzaWhenOrderButtonClickedWithAllRequiredFieldsFilled");
    clickOn("#nameTextField").write("Test");
    clickOn("#zipcodeTextField").write("12345");
    clickOn("#streetTextField").write("Test");
    clickOn("#houseNumberTextField").write("1");
    clickOn("#orderButton");

    DialogPane dialogPane = lookup(".dialog-pane").queryAs(DialogPane.class);
    String alertText = dialogPane.getContentText();

    assertEquals("Your order has been placed!", alertText);
  }

  @Test
  @DisplayName("Should increase pizza count when spinner is clicked")
  public void shouldIncreasePizzaCountWhenSpinnerClicked() {
    logger.info("shouldIncreasePizzaCountWhenSpinnerClicked");
    int count = 6;

    // Get the node
    Node node = lookup("#countSpinner").query();

    // Get the screen coordinates of the top left corner of the node
    Point2D topRightCorner = node.localToScreen(node.getBoundsInLocal().getWidth(), 0);

    moveTo(topRightCorner);

    for (int i = 0; i < count - 1; i++) {
      clickOn();
    }

    assertEquals(count, lookup("#countSpinner").queryAs(Spinner.class).getValue());
  }
}