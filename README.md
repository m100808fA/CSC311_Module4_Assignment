# Card24Game

Card24Game is a JavaFX desktop game where the player uses four drawn card values and arithmetic operations to form an expression that evaluates to 24.

## Game Rules

1. Four random cards are shown each round.
2. Use all four card values exactly once.
3. Allowed operators: `+`, `-`, `*`, `/`, and parentheses.
4. A valid answer must evaluate to 24.

## Card Value Mapping

| Card Rank | Value |
|---|---:|
| Ace (A) | 1 |
| 2-10 | Face value |
| Jack (J) | 11 |
| Queen (Q) | 12 |
| King (K) | 13 |

## Build and Run

### Run the app

```bash
mvn clean javafx:run
```

### Run tests

```bash
mvn test
```

## Project Structure

```text
src/
  main/
    java/com/example/card24/
      MainApp.java
      controller/GameController.java
      model/Card.java
      model/Deck.java
      logic/
        ExpressionTokenizer.java
        ExpressionEvaluator.java
        Game24Validator.java
        SolutionFinder.java
        InvalidExpressionException.java
    resources/
      com/example/card24/
        game-view.fxml
        style.css
      png/
        ...52 card images...
  test/
    java/com/example/card24/logic/
      Game24LogicTest.java
```

## Screenshot

Add a screenshot of the running game UI here.

`![Card24Game Screenshot](docs/screenshot-placeholder.png)`
