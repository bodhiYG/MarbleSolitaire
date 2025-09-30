package cs3500.marblesolitaire.view;

import static org.junit.Assert.*;

import cs3500.marblesolitaire.model.EnglishSolitaireModel;
import cs3500.marblesolitaire.view.MarbleSolitaireTextView;
import cs3500.marblesolitaire.view.MarbleSolitaireView;
import org.junit.Before;
import org.junit.Test;

public class MarbleSolitaireTextViewTest {
  private MarbleSolitaireView model1;
  private MarbleSolitaireView model2;
  private MarbleSolitaireView model3;

  @Before
  public void setUp() {
    this.model1 = new MarbleSolitaireTextView(new EnglishSolitaireModel());

    EnglishSolitaireModel m = new EnglishSolitaireModel();
    m.move(3, 1, 3, 3);
    this.model3 = new MarbleSolitaireTextView(m);
  }

  // Test toString()
  @Test
  public void testToString() {
    assertEquals(
        "    O O O\n" +
            "    O O O\n" +
            "O O O O O O O\n" +
            "O O O _ O O O\n" +
            "O O O O O O O\n" +
            "    O O O\n" +
            "    O O O",
        this.model1.toString());
    assertEquals(
        "    O O O\n" +
            "    O O O\n" +
            "O O O O O O O\n" +
            "O _ _ O O O O\n" +
            "O O O O O O O\n" +
            "    O O O\n" +
            "    O O O",
        this.model3.toString());
  }
}