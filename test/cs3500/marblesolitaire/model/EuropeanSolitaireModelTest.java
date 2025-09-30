package cs3500.marblesolitaire.model.hw02;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModelState.SlotState;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class EuropeanSolitaireModelTest {
  private EuropeanSolitaireModel model1;
  private EuropeanSolitaireModel model2;
  private EuropeanSolitaireModel model3;
  private EuropeanSolitaireModel model4;
  private EuropeanSolitaireModel moveModel;

  @Before
  public void setUp() {
    this.model1 = new EuropeanSolitaireModel();
    this.model2 = new EuropeanSolitaireModel(5);
    this.model3 = new EuropeanSolitaireModel(2, 4);
    this.model4 = new EuropeanSolitaireModel(5, 4, 5);
    this.moveModel = new EuropeanSolitaireModel();
  }

  // ---------------------- Constructor Tests ----------------------

  @Test
  public void testDefaultConstructor() {
    assertEquals(7, model1.getBoardSize());
    assertEquals(SlotState.Empty, model1.getSlotAt(3, 3));
  }

  @Test
  public void testArmLengthConstructor() {
    assertEquals(13, model2.getBoardSize()); // 5 * 3 - 2 = 13
    int center = 6; // (13 - 1) / 2 = 6
    assertEquals(SlotState.Empty, model2.getSlotAt(center, center));
  }

  @Test
  public void testEmptySlotConstructor() {
    assertEquals(7, model3.getBoardSize());
    assertEquals(SlotState.Empty, model3.getSlotAt(2, 4));
  }

  @Test
  public void testFullConstructor() {
    assertEquals(13, model4.getBoardSize()); // 5 * 3 - 2 = 13
    assertEquals(SlotState.Empty, model4.getSlotAt(4, 5));
  }

  // ---------------------- Invalid Constructor Arguments ----------------------

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidArmLengthLow() {
    new EuropeanSolitaireModel(0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidArmLengthEven() {
    new EuropeanSolitaireModel(4);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidEmptySlotNegative() {
    new EuropeanSolitaireModel(3, -1, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidEmptySlotTooLarge() {
    new EuropeanSolitaireModel(3, 3, 9);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidEmptySlotOutsideOctagon() {
    new EuropeanSolitaireModel(3, 0, 0);
  }

  // ---------------------- Reflection Test for initializeBoard() ----------------------

  @Test
  public void testInitializeBoard() throws Exception {
    Method initializeBoard = EuropeanSolitaireModel.class.getDeclaredMethod("initializeBoard");
    initializeBoard.setAccessible(true);

    ArrayList<ArrayList<SlotState>> b1 =
        (ArrayList<ArrayList<SlotState>>) initializeBoard.invoke(model1);
    ArrayList<ArrayList<SlotState>> b2 =
        (ArrayList<ArrayList<SlotState>>) initializeBoard.invoke(model2);

    assertEquals(7, b1.size());
    assertEquals(13, b2.size()); // 5 * 3 - 2 = 13
    assertEquals(SlotState.Marble, b1.get(3).get(3));
    assertEquals(SlotState.Marble, b1.get(0).get(3));
    assertEquals(SlotState.Marble, b1.get(3).get(0));
  }

  // ---------------------- Move Tests ----------------------

  @Test
  public void testValidMoves() {
    // Test vertical move
    moveModel.move(1, 3, 3, 3);
    assertEquals(SlotState.Empty, moveModel.getSlotAt(1, 3));
    assertEquals(SlotState.Empty, moveModel.getSlotAt(2, 3));
    assertEquals(SlotState.Marble, moveModel.getSlotAt(3, 3));

    // Reset for next test
    moveModel = new EuropeanSolitaireModel();

    // Test horizontal move
    moveModel.move(3, 1, 3, 3);
    assertEquals(SlotState.Empty, moveModel.getSlotAt(3, 1));
    assertEquals(SlotState.Empty, moveModel.getSlotAt(3, 2));
    assertEquals(SlotState.Marble, moveModel.getSlotAt(3, 3));

    // Test diagonal move (unique to European)
    EuropeanSolitaireModel diagonalModel = new EuropeanSolitaireModel(3, 3, 3);
    diagonalModel.move(1, 1, 3, 3);
    assertEquals(SlotState.Empty, diagonalModel.getSlotAt(1, 1));
    assertEquals(SlotState.Empty, diagonalModel.getSlotAt(2, 2));
    assertEquals(SlotState.Marble, diagonalModel.getSlotAt(3, 3));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidMove() {
    // Should fail because there's no marble to jump over
    moveModel.move(0, 3, 2, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidMoveDistance() {
    // Should fail because move distance is not 2
    moveModel.move(3, 1, 3, 2);
  }

  // ---------------------- Game State Tests ----------------------

  @Test
  public void testIsGameOver() {
    assertFalse(model1.isGameOver());

    // For a more comprehensive test, we'd need to play through more moves
    // This is a basic test to ensure the method works
  }

  @Test
  public void testGetScore() {
    assertEquals(36, model1.getScore()); // Standard 37 positions - 1 empty = 36
    assertEquals(128, model2.getScore()); // Arm thickness 5 has 128 marbles + 1 empty = 129 total
  }

  @Test
  public void testGetSlotAt() {
    assertEquals(SlotState.Marble, model1.getSlotAt(0, 2));
    assertEquals(SlotState.Empty, model1.getSlotAt(3, 3));
    assertEquals(SlotState.Invalid, model1.getSlotAt(0, 0));
    assertEquals(SlotState.Marble, model1.getSlotAt(1, 1)); // This should be valid in European
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetSlotAtInvalidPosition() {
    model1.getSlotAt(-1, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetSlotAtOutOfBounds() {
    model1.getSlotAt(7, 7);
  }

  @Test
  public void testScoreDecrementsOnMove() {
    int initialScore = moveModel.getScore();
    moveModel.move(1, 3, 3, 3);
    assertEquals(initialScore - 1, moveModel.getScore());
  }
}