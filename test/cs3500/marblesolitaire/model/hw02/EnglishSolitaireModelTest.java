package cs3500.marblesolitaire.model;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import cs3500.marblesolitaire.model.EnglishSolitaireModel;
import cs3500.marblesolitaire.model.MarbleSolitaireModel;
import java.lang.reflect.Method;

import cs3500.marblesolitaire.model.MarbleSolitaireModelState.SlotState;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cs3500.marblesolitaire.model.EnglishSolitaireModel.*;
import cs3500.marblesolitaire.view.MarbleSolitaireTextView;
import java.util.*;

public class EnglishSolitaireModelTest {
  private MarbleSolitaireModel model1;
  private MarbleSolitaireModel model2;
  private MarbleSolitaireModel model3;
  private MarbleSolitaireModel model4;
  private MarbleSolitaireModel moveModel;
  private MarbleSolitaireModel model;

  @Before
  public void setUp() {
    this.model1 = new EnglishSolitaireModel();
    this.model2 = new EnglishSolitaireModel(5);
    this.model3 = new EnglishSolitaireModel(5, 4);
    this.model4 = new EnglishSolitaireModel(3, 2, 4);
    this.moveModel = new EnglishSolitaireModel();
    this.model = new EnglishSolitaireModel();

    model.move(1, 3, 3, 3);
    model.move(4, 3, 2, 3);
    model.move(6, 3, 4, 3);
    model.move(3, 1, 3, 3);
    model.move(3, 4, 3, 2);
    model.move(3, 6, 3, 4);
  }
  // ---------------------- Constructor Tests ----------------------

  @Test
  public void testDefaultConstructor() {
    EnglishSolitaireModel m = new EnglishSolitaireModel();
    // board size = 3 × armLength = 3×3 = 9
    Assert.assertEquals(7, m.getBoardSize());
    // default empty comes from the (3,3,3) call
    Assert.assertEquals(SlotState.Empty, m.getSlotAt(3, 3));
  }

  @Test
  public void testArmLengthConstructor() {
    EnglishSolitaireModel m = new EnglishSolitaireModel(5);
    // board size = 3 × 5 = 15
    Assert.assertEquals(9, m.getBoardSize());
    // empty slot is at the center: (armLength*3/2, armLength*3/2)
    int center = 9 / 2;
    Assert.assertEquals(SlotState.Empty, m.getSlotAt(center, center));
  }

  @Test
  public void testEmptySlotConstructor() {
    // two-arg constructor = (sRow, sCol) on a default armLength=3 board
    EnglishSolitaireModel m = new EnglishSolitaireModel(2, 4);
    Assert.assertEquals(7, m.getBoardSize());
    // the only empty slot is exactly where we asked for it
    Assert.assertEquals(SlotState.Empty, m.getSlotAt(2, 4));
  }

  @Test
  public void testFullConstructor() {
    // three-arg constructor = (armLength, sRow, sCol)
    EnglishSolitaireModel m = new EnglishSolitaireModel(5, 4, 5);
    // board size = 3 × 5 = 15
    Assert.assertEquals(9, m.getBoardSize());
    Assert.assertEquals(SlotState.Empty, m.getSlotAt(4, 5));
  }

  // ---------------------- Invalid Constructor Arguments ----------------------

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidArmLengthLow() {
    new EnglishSolitaireModel(0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidArmLengthEven() {
    new EnglishSolitaireModel(4);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidEmptySlotNegative() {
    // row < 0
    new EnglishSolitaireModel(3, -1, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidEmptySlotTooLarge() {
    // col >= size
    new EnglishSolitaireModel(3, 3, 9);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidEmptySlotInArm() {
    // position falls in an invalid corner region
    // e.g. for armLength=3, any of (0,0), (0,1), (1,0), (1,1) are invalid
    new EnglishSolitaireModel(3, 1, 1);
  }

  // ---------------------- Reflection Test for makeBody() ----------------------

  @Test
  @SuppressWarnings("unchecked")
  public void testMakeBody() throws Exception {
    EnglishSolitaireModel m1 = new EnglishSolitaireModel();
    EnglishSolitaireModel m2 = new EnglishSolitaireModel(5);
    Method makeBody = EnglishSolitaireModel.class.getDeclaredMethod("makeBody");
    makeBody.setAccessible(true);

    ArrayList<ArrayList<SlotState>> b1 =
        (ArrayList<ArrayList<SlotState>>) makeBody.invoke(m1);
    ArrayList<ArrayList<SlotState>> b2 =
        (ArrayList<ArrayList<SlotState>>) makeBody.invoke(m2);

    // board dimension = 3 × armLength
    Assert.assertEquals(3 + 4, b1.size());
    Assert.assertEquals(5 + 4, b2.size());
    Assert.assertEquals(3 + 4, b1.get(0).size());
    Assert.assertEquals(5 + 4, b2.get(0).size());

    // check that center cell is Empty and an arm cell is Marble
    int c1 = b1.size() / 2;
    Assert.assertEquals(SlotState.Marble, b1.get(3).get(3));
    Assert.assertEquals(SlotState.Marble, b1.get(c1).get(0));
  }

  // Test move()
  @Test
  public void testMove() {
    // Out of bounds
    assertThrows("Invalid row/col: 100/3, 2/6", IllegalArgumentException.class,
        () -> this.model1.move(100, 3, 2, 6));
    assertThrows("Invalid row/col: 2/-1, 2/6", IllegalArgumentException.class,
        () -> this.model1.move(2, -1, 2, 6));
    assertThrows("Invalid row/col: 2/3, 9/6", IllegalArgumentException.class,
        () -> this.model1.move(2, 3, 9, 6));
    assertThrows("Invalid row/col: 2/3, 2/-1", IllegalArgumentException.class,
        () -> this.model1.move(2, 3, 2, -1));

    // No Marble at from
    assertThrows("Invalid row/col: 4/4", IllegalArgumentException.class,
        () -> this.model1.move(3, 3, 1, 3));

    // Marble at to
    assertThrows("Invalid row/col: 1/4", IllegalArgumentException.class,
        () -> this.model1.move(3, 4, 1, 4));

    // No Marble between
    assertThrows("Illegal move; There must be a marble between the start and end points",
        IllegalArgumentException.class, () -> this.model1.move(3, 4, 5, 4));

    // Move diagonal
    assertThrows("Illegal move", IllegalArgumentException.class,
        () -> this.model2.move(5, 5, 7, 7));
    // Move too far
    assertThrows("Illegal move", IllegalArgumentException.class,
        () -> this.model1.move(1, 4, 4, 4));

    // Testing movement
    Assert.assertEquals(SlotState.Marble, this.moveModel.getSlotAt(1, 3));
    Assert.assertEquals(SlotState.Marble, this.moveModel.getSlotAt(2, 3));
    Assert.assertEquals(SlotState.Empty, this.moveModel.getSlotAt(3, 3));
    this.moveModel.move(1, 3, 3, 3);
    Assert.assertEquals(SlotState.Empty, this.moveModel.getSlotAt(1, 3));
    Assert.assertEquals(SlotState.Empty, this.moveModel.getSlotAt(2, 3));
    Assert.assertEquals(SlotState.Marble, this.moveModel.getSlotAt(3, 3));
  }

  // Test canMove()
  @Test
  public void testCanMove() throws Exception {
    Method canMove = EnglishSolitaireModel.class
        .getDeclaredMethod("canMove", int.class, int.class, int.class, int.class);
    canMove.setAccessible(true);


    // Out of bounds
    assertFalse((Boolean) canMove.invoke(model1, -1, 3,  1, 3));
    assertFalse((Boolean) canMove.invoke(model1,  3, -1, 3, 1));
    assertFalse((Boolean) canMove.invoke(model1,  3,  3, 9, 3));
    assertFalse((Boolean) canMove.invoke(model1,  3,  3, 3, 9));

    assertFalse((Boolean) canMove.invoke(model1, 4, 4, 2, 4));
    assertFalse((Boolean) canMove.invoke(model1, 3, 4, 1, 4));
    assertFalse((Boolean) canMove.invoke(model1, 3, 4, 5, 4));
    assertFalse((Boolean) canMove.invoke(model1, 3, 4, 4, 5));
    assertFalse((Boolean) canMove.invoke(model1, 2, 4, 6, 4));
    assertTrue((Boolean) canMove.invoke(model1, 1, 3, 3, 3));
    assertTrue((Boolean) canMove.invoke(model1, 3, 5, 3, 3));
    assertTrue((Boolean) canMove.invoke(model2, 4, 2, 4, 4));
  }


  // Test isGameOver()
  @Test
  public void testGameOver() {
    assertFalse(this.model1.isGameOver());
    assertTrue(this.model.isGameOver());
  }

  // Test getBoardSize()
  @Test
  public void testGetBoardSize() {
    Assert.assertEquals(7, this.model1.getBoardSize());
    Assert.assertEquals(9, this.model2.getBoardSize());
    Assert.assertEquals(7, this.model3.getBoardSize());
    Assert.assertEquals(7, this.model4.getBoardSize());
  }

  // Test getSlotAt()
  @Test
  public void testGetSlotAt() {
    Assert.assertEquals(SlotState.Invalid, this.model1.getSlotAt(0, 0));
    Assert.assertEquals(SlotState.Marble, this.model1.getSlotAt(0, 4));
    Assert.assertEquals(SlotState.Empty, this.model1.getSlotAt(3, 3));
  }

  // Test getScore()
  @Test
  public void testGetScore() {
    Assert.assertEquals(32, this.model1.getScore());
    Assert.assertEquals(64, this.model2.getScore());
    Assert.assertEquals(26, this.model.getScore());
  }
}