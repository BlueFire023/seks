package seks.testfx.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
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
  @DisplayName("Test welcome message")
  public void shouldDisplayWelcomeMessage() {
    logger.info("shouldDisplayWelcomeMessage");

    verifyThat("#welcomeLabel", hasText(""));

    clickOn("#helloButton");

    verifyThat("#welcomeLabel", hasText("Welcome to the Pizza constructor!"));
  }

  @Test
  @DisplayName("Test clear list")
  public void testClearList() {
    logger.info("testClearList");

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
  @DisplayName("Test JSON output")
  public void testJsonOutput() {
    logger.info("testJsonOutput");

    drag("Pineapple").interact(() -> dropTo("#pizzaListView"));
    drag("Salami").interact(() -> dropTo("#pizzaListView"));

    clickOn("#size32RadioButton");
    clickOn("#cheeseCheckBox");

    String expectedJson = """
        {
          "size": "32cm",
          "double toppings": false,
          "kebab sauce": false,
          "ingredients": [
            "Pineapple",
            "Salami"
          ],
          "cheese crust": true
        }""";

    assertEquals(expectedJson, lookup("#resultTextArea").queryAs(TextArea.class).getText());

    clickOn("#size32RadioButton");
    clickOn("#cheeseCheckBox");
    clickOn("#doubleCheckBox");
    clickOn("#kebabCheckBox");

    expectedJson = """
        {
          "size": "32cm",
          "double toppings": true,
          "kebab sauce": true,
          "ingredients": [
            "Pineapple",
            "Salami"
          ],
          "cheese crust": true
        }""";

    assertEquals(expectedJson, lookup("#resultTextArea").queryAs(TextArea.class).getText());
  }

  @Test
  @DisplayName("Test copy button")
  public void testCopyButton() {
    logger.info("testCopyButton");

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
  @DisplayName("Test moving ingredients back")
  public void testMoveIngredientsBack() {
    logger.info("testMoveIngredientsBack");
    drag("Pineapple").interact(() -> dropTo("#pizzaListView"));
    drag("Salami").interact(() -> dropTo("#pizzaListView"));

    drag("Pineapple").interact(() -> dropTo("#ingredientListView"));
    drag("Salami").interact(() -> dropTo("#ingredientListView"));

    verifyThat("#pizzaListView", ListViewMatchers.hasItems(0));
    verifyThat("#ingredientListView", ListViewMatchers.hasItems(10));
  }

  @Test
  @DisplayName("should not move ingredients when target is invalid")
  public void shouldNotMoveIngredientsWhenTargetIsInvalid() {
    logger.info("shouldNotMoveIngredientsWhenTargetIsInvalid");
    drag("Pineapple").interact(() -> dropTo("#xButton"));
    logger.info("dragged Pineapple to xButton");

    verifyThat("#pizzaListView", ListViewMatchers.hasItems(0));
    verifyThat("#ingredientListView", ListViewMatchers.hasItems(10));

    drag("Salami").interact(() -> dropTo("#helloButton"));
    logger.info("dragged Salami to helloButton");

    verifyThat("#pizzaListView", ListViewMatchers.hasItems(0));
    verifyThat("#ingredientListView", ListViewMatchers.hasItems(10));

    drag("Mushrooms").interact(() -> dropTo("#resultTextArea"));
    logger.info("dragged Mushrooms to resultTextArea");

    verifyThat("#pizzaListView", ListViewMatchers.hasItems(0));
    verifyThat("#ingredientListView", ListViewMatchers.hasItems(10));

    drag("Ham").interact(() -> dropTo(".root"));
    logger.info("dragged Ham to root");

    verifyThat("#pizzaListView", ListViewMatchers.hasItems(0));
    verifyThat("#ingredientListView", ListViewMatchers.hasItems(10));
  }

  @Test
  @DisplayName("Should move all ingredients to pizza list")
  public void shouldMoveAllIngredientsToPizzaList() {
    logger.info("shouldMoveAllIngredientsToPizzaList");

    drag("Tomato").interact(() -> dropTo("#pizzaListView"));
    drag("Mushrooms").interact(() -> dropTo("#pizzaListView"));
    drag("Pepperoni").interact(() -> dropTo("#pizzaListView"));
    drag("Onion").interact(() -> dropTo("#pizzaListView"));
    drag("Bacon").interact(() -> dropTo("#pizzaListView"));
    drag("Salami").interact(() -> dropTo("#pizzaListView"));
    drag("Olives").interact(() -> dropTo("#pizzaListView"));
    drag("Pineapple").interact(() -> dropTo("#pizzaListView"));
    drag("Cheese").interact(() -> dropTo("#pizzaListView"));
    drag("Ham").interact(() -> dropTo("#pizzaListView"));

    verifyThat("#pizzaListView", ListViewMatchers.hasItems(10));
    verifyThat("#ingredientListView", ListViewMatchers.hasItems(0));
  }
}