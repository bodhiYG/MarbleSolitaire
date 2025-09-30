package cs3500.marblesolitaire.model.hw02;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModelState.SlotState;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class TriangleSolitaireModelTest {
  private TriangleSolitaireModel model1;
  private TriangleSolitaireModel model2;
  private TriangleSolitaireModel model3;
  private TriangleSolitaireModel model4;
  private TriangleSolitaireModel moveModel;

  @Before
  public void setUp() {
    this.model1 = new TriangleSolitaireModel();
    this.model2 = new TriangleSolitaireModel(7);
    this.model3 = new TriangleSolitaireModel(2, 1);
    this.model4 = new TriangleSolitaireModel(7, 3, 2);
    this.moveModel = new TriangleSolitaireModel();
  }

  // ---------------------- Constructor Tests ----------------------

  @Test
  public void testDefaultConstructor() {
    assertEquals(5, model1.getBoardSize());
    assertEquals(SlotState.Empty, model1.getSlotAt(0, 0));
  }

  @Test
  public void testDimensionConstructor() {
    assertEquals(7, model2.getBoardSize());
    assertEquals(SlotState.Empty, model2.getSlotAt(0, 0));
  }

  @Test
  public void testEmptySlotConstructor() {
    assertEquals(5, model3.getBoardSize());
    assertEquals(SlotState.Empty, model3.getSlotAt(2, 1));
  }

  @Test
  public void testFullConstructor() {
    assertEquals(7, model4.getBoardSize());
    assertEquals(SlotState.Empty, model4.getSlotAt(3, 2));
  }

  // ---------------------- Invalid Constructor Arguments ----------------------

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidDimension() {
    new TriangleSolitaireModel(0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidEmptySlotNegative() {
    new TriangleSolitaireModel(3, -1, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidEmptySlotOutsideTriangle() {
    new TriangleSolitaireModel(5, 1, 2); // Column > row
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidEmptySlotTooLarge() {
    new TriangleSolitaireModel(5, 6, 0);
  }

  // ---------------------- Reflection Test for initializeBoard() ----------------------

  @Test
  @SuppressWarnings("unchecked")
  public void testInitializeBoard() throws Exception {
    Method initializeBoard = TriangleSolitaireModel.class.getDeclaredMethod("initializeBoard");
    initializeBoard.setAccessible(true);

    ArrayList<ArrayList<SlotState>> b1 =
        (ArrayList<ArrayList<SlotState>>) initializeBoard.invoke(model1);
    ArrayList<ArrayList<SlotState>> b2 =
        (ArrayList<ArrayList<SlotState>>) initializeBoard.invoke(model2);

    assertEquals(5, b1.size());
    assertEquals(7, b2.size());
    assertEquals(SlotState.Marble, b1.get(0).get(0));
    assertEquals(SlotState.Marble, b1.get(4).get(4));
    assertEquals(SlotState.Invalid, b1.get(0).get(1));
  }

  // ---------------------- Move Tests ----------------------

  @Test
  public void testValidMoves() {
    // Test right move
    moveModel.move(2, 0, 0, 0);
    assertEquals(SlotState.Empty, moveModel.getSlotAt(2, 0));
    assertEquals(SlotState.Empty, moveModel.getSlotAt(1, 0));
    assertEquals(SlotState.Marble, moveModel.getSlotAt(0, 0));

    // Test left move
    TriangleSolitaireModel leftMoveModel = new TriangleSolitaireModel(5, 2, 2);
    leftMoveModel.move(0, 0, 2, 2);
    assertEquals(SlotState.Empty, leftMoveModel.getSlotAt(0, 0));
    assertEquals(SlotState.Empty, leftMoveModel.getSlotAt(1, 1));
    assertEquals(SlotState.Marble, leftMoveModel.getSlotAt(2, 2));

    // Test diagonal down-right move
    TriangleSolitaireModel diagModel = new TriangleSolitaireModel(5, 2, 0);
    diagModel.move(0, 0, 2, 0);
    assertEquals(SlotState.Empty, diagModel.getSlotAt(0, 0));
    assertEquals(SlotState.Empty, diagModel.getSlotAt(1, 0));
    assertEquals(SlotState.Marble, diagModel.getSlotAt(2, 0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidVerticalMove() {
    // Vertical moves aren't allowed in triangle version
    moveModel.move(3, 1, 1, 1);
  }

  // ---------------------- Game State Tests ----------------------

  @Test
  public void testGetScore() {
    assertEquals(14, model1.getScore()); // (5*6)/2 - 1
    assertEquals(27, model2.getScore()); // (7*8)/2 - 1

    moveModel.move(2, 0, 0, 0);
    assertEquals(13, moveModel.getScore());
  }

  @Test
  public void testGetSlotAt() {
    assertEquals(SlotState.Empty, model1.getSlotAt(0, 0));
    assertEquals(SlotState.Marble, model1.getSlotAt(4, 4));
    assertEquals(SlotState.Invalid, model1.getSlotAt(0, 1));
  }

  @Test
  public void testGetBoardSize() {
    assertEquals(5, model1.getBoardSize());
    assertEquals(7, model2.getBoardSize());
    assertEquals(5, model3.getBoardSize());
    assertEquals(7, model4.getBoardSize());
  }
}