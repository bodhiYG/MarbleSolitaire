package cs3500.marblesolitaire.view;

import cs3500.marblesolitaire.controller.ControllerFeatures;
import cs3500.marblesolitaire.model.MarbleSolitaireModelState;
import java.io.IOException;

/**
 * Textual view for the Triangle Solitaire game, displaying the board in a triangular format.
 */
public class TriangleSolitaireTextView implements MarbleSolitaireView {
  private final MarbleSolitaireModelState model;
  private final Appendable destination;

  /**
   * Constructor that initializes the view with the given model state.
   * Uses System.out as the default destination.
   *
   * @param model the game model state
   * @throws IllegalArgumentException if the model is null
   */
  public TriangleSolitaireTextView(MarbleSolitaireModelState model)
      throws IllegalArgumentException {
    this(model, System.out);
  }

  /**
   * Constructor that initializes the view with the given model state and output destination.
   *
   * @param model the game model state
   * @param destination the output destination
   * @throws IllegalArgumentException if either parameter is null
   */
  public TriangleSolitaireTextView(MarbleSolitaireModelState model, Appendable destination)
      throws IllegalArgumentException {
    if (model == null || destination == null) {
      throw new IllegalArgumentException("Model and destination cannot be null");
    }
    this.model = model;
    this.destination = destination;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    int size = model.getBoardSize();

    for (int row = 0; row < size; row++) {
      // Add leading spaces for proper triangular indentation
      for (int space = 0; space < size - row - 1; space++) {
        sb.append(" ");
      }

      for (int col = 0; col <= row; col++) {
        MarbleSolitaireModelState.SlotState state = model.getSlotAt(row, col);
        switch (state) {
          case Marble:
            sb.append("O");
            break;
          case Empty:
            sb.append("_");
            break;
          case Invalid:
            // Shouldn't happen in valid positions of triangular board
            sb.append(" ");
            break;
          default:
            // Should never reach here
            break;
        }

        // Add space between marbles except the last one in the row
        if (col < row) {
          sb.append(" ");
        }
      }

      // Don't add newline after last row
      if (row < size - 1) {
        sb.append("\n");
      }
    }

    return sb.toString();
  }

  @Override
  public void renderBoard() throws IOException {
    try {
      destination.append(this.toString());
    } catch (IOException e) {
      throw new IOException("Failed to render board", e);
    }
  }

  @Override
  public void renderMessage(String message) throws IOException {
    try {
      destination.append(message);
    } catch (IOException e) {
      throw new IOException("Failed to render message", e);
    }
  }

  @Override
  public void refresh() {
    // No operation needed for text view
  }

  @Override
  public void setFeatures(ControllerFeatures features) {
    // Text view doesn't support controller features
    throw new UnsupportedOperationException(
        "TriangleSolitaireTextView doesn't support controller features");
  }
}