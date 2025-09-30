package cs3500.marblesolitaire.view;

import cs3500.marblesolitaire.controller.ControllerFeatures;
import cs3500.marblesolitaire.model.*;
import java.io.IOException;

public class MarbleSolitaireTextView implements MarbleSolitaireView{
  private final MarbleSolitaireModelState game;
  private final Appendable destination;

  // Constructor
  public MarbleSolitaireTextView(MarbleSolitaireModelState game) {
    if (game == null) {
      throw new IllegalArgumentException("game cannot be null");
    }
    this.game = game;
    this.destination = System.out;
  }

  public MarbleSolitaireTextView(MarbleSolitaireModelState game, Appendable destination) {
    if (game == null || destination == null)
      throw new IllegalArgumentException("model and destination cannot be null");
    this.game = game;
    this.destination = destination;
  }

  /**
   * Return a string that represents the current state of the board. The
   * string should have one line per row of the game board. Each slot on the
   * game board is a single character (O, _ or space for a marble, empty and
   * invalid position respectively). Slots in a row should be separated by a
   * space. Each row has no space before the first slot and after the last slot.
   * @return the game state as a string
   */
  @Override
  public String toString() {
    StringBuilder output = new StringBuilder();
    int size = game.getBoardSize();

    for (int i = 0; i < size; i++) {
      // Leading spaces for indentation
      for (int j = 0; j < size; j++) {
        if (game.getSlotAt(i, j) != MarbleSolitaireModelState.SlotState.Invalid) {
          break;
        }
        output.append("  ");
      }

      // Board contents
      for (int j = 0; j < size; j++) {
        MarbleSolitaireModelState.SlotState slot = game.getSlotAt(i, j);
        if (slot == MarbleSolitaireModelState.SlotState.Invalid) {
          continue;
        }

        if (slot == MarbleSolitaireModelState.SlotState.Marble) {
          output.append("O");
        } else if (slot == MarbleSolitaireModelState.SlotState.Empty) {
          output.append("_");
        }

        // Only add a space if the next slot is valid and not the end of the row
        if (j < size - 1) {
          boolean nextIsValid = game.getSlotAt(i, j + 1) !=
              MarbleSolitaireModelState.SlotState.Invalid;
          if (nextIsValid) {
            output.append(" ");
          }
        }
      }

      // Don't add newline after the last row
      if (i < size - 1) {
        output.append("\n");
      }
    }

    return output.toString();
  }

  @Override
  public void renderBoard() throws IOException {
    this.destination.append(this.toString());
  }

  @Override
  public void renderMessage(String message) throws IOException {
    this.destination.append(message);
  }

  @Override
  public void refresh() {
    // For text view, refreshing doesn't need to do anything special
    try {
      renderBoard();
    } catch (IOException e) {
      // Ignore or handle as needed for your implementation
    }
  }

  @Override
  public void setFeatures(ControllerFeatures features) {
    // Text view doesn't need controller features as it's not interactive
    throw new UnsupportedOperationException("Text view doesn't support controller features");
  }
}
