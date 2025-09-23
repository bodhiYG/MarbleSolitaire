package cs3500.marblesolitaire.controller;

import static org.junit.Assert.*;

import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModel;
import cs3500.marblesolitaire.view.MarbleSolitaireView;
import org.junit.Before;
import org.junit.Test;
import java.io.StringReader;
import java.io.IOException;

public class MarbleSolitaireControllerImplTest {

  private MarbleSolitaireModel mockModel;
  private StringBuilder mockOutput;
  private MarbleSolitaireView mockView;

  @Before
  public void setUp() {
    mockModel = new MockModel();
    mockOutput = new StringBuilder();
    mockView = new MockView(mockOutput);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNullModel() {
    new MarbleSolitaireControllerImpl(null, mockView, new StringReader(""));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNullView() {
    new MarbleSolitaireControllerImpl(mockModel, null, new StringReader(""));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNullInput() {
    new MarbleSolitaireControllerImpl(mockModel, mockView, null);
  }

  @Test
  public void testQuitImmediately() throws IOException {
    String input = "q";
    MarbleSolitaireController controller = new MarbleSolitaireControllerImpl(
        mockModel, mockView, new StringReader(input));
    controller.playGame();
    assertTrue(mockOutput.toString().contains("Game quit!"));
  }

  @Test
  public void testQuitAfterInvalidInput() throws IOException {
    String input = "invalid q";
    MarbleSolitaireController controller = new MarbleSolitaireControllerImpl(
        mockModel, mockView, new StringReader(input));
    controller.playGame();
    assertTrue(mockOutput.toString().contains("Game quit!"));
  }

  @Test
  public void testValidMoveSequence() throws IOException {
    String input = "4 2 4 4 q";
    MarbleSolitaireController controller = new MarbleSolitaireControllerImpl(
        mockModel, mockView, new StringReader(input));
    controller.playGame();
    assertTrue(mockOutput.toString().contains("Score: "));
  }

  @Test
  public void testInvalidMoveThenValidMove() throws IOException {
    String input = "1 1 1 1 4 2 4 4 q";
    StringBuilder output = new StringBuilder();
    MarbleSolitaireView view = new MockView(output);

    // Mock model that throws on first move, accepts second
    MarbleSolitaireModel mockModel = new MockModel() {
      private int moveCount = 0;

      @Override
      public void move(int fromRow, int fromCol, int toRow, int toCol) {
        if (moveCount++ == 0) {
          throw new IllegalArgumentException("Can't move to same position");
        }
        // Accept second move
      }
    };

    MarbleSolitaireController controller = new MarbleSolitaireControllerImpl(
        mockModel, view, new StringReader(input));
    controller.playGame();

    String result = output.toString();
    assertTrue(result.contains("Invalid move. Play again. Can't move to same position"));
    assertTrue(result.contains("Score: "));  // Should show score after valid move
    assertTrue(result.contains("Game quit!")); // Should quit only after 'q'
  }

  @Test(expected = IllegalStateException.class)
  public void testInputFailure() throws IOException {
    // Mock Readable that throws IOException
    Readable badInput = new Readable() {
      public int read(java.nio.CharBuffer cb) throws IOException {
        throw new IOException("Mock input failure");
      }
    };
    MarbleSolitaireController controller = new MarbleSolitaireControllerImpl(
        mockModel, mockView, badInput);
    controller.playGame();
  }

  @Test(expected = IllegalStateException.class)
  public void testOutputFailure() throws IOException {
    // Mock View that throws IOException
    MarbleSolitaireView badView = new MarbleSolitaireView() {
      public void renderBoard() throws IOException { throw new IOException(); }
      public void renderMessage(String message) throws IOException { throw new IOException(); }

      @Override
      public void refresh() {

      }

      @Override
      public void setFeatures(ControllerFeatures features) {

      }

      public String toString() { return ""; }
    };
    MarbleSolitaireController controller = new MarbleSolitaireControllerImpl(
        mockModel, badView, new StringReader(""));
    controller.playGame();
  }

  @Test
  public void testGameCompletion() throws IOException {
    // Mock model that reports game over immediately
    MarbleSolitaireModel completedModel = new MockModel() {
      @Override
      public boolean isGameOver() { return true; }
    };
    MarbleSolitaireController controller = new MarbleSolitaireControllerImpl(
        completedModel, mockView, new StringReader(""));
    controller.playGame();
    assertTrue(mockOutput.toString().contains("Game over!"));
  }
}

// Mock implementations for testing
class MockModel implements MarbleSolitaireModel {
  public void move(int fromRow, int fromCol, int toRow, int toCol) {}
  public boolean isGameOver() { return false; }
  public int getBoardSize() { return 7; }
  public SlotState getSlotAt(int row, int col) { return SlotState.Marble; }
  public int getScore() { return 32; }
}

class MockView implements MarbleSolitaireView {
  private final Appendable output;

  public MockView(Appendable output) {
    this.output = output;
  }

  public void renderBoard() throws IOException {
    output.append("Mock Board\n");
  }

  public void renderMessage(String message) throws IOException {
    output.append(message);
  }

  @Override
  public void refresh() {

  }

  @Override
  public void setFeatures(ControllerFeatures features) {

  }

  public String toString() { return ""; }
}