package cs3500.marblesolitaire.controller;

import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModel;
import cs3500.marblesolitaire.view.MarbleSolitaireGuiView;

/**
 * A controller for the GUI version of Marble Solitaire.
 */
public class MarbleSolitaireGuiController implements ControllerFeatures {
  private final MarbleSolitaireModel model;
  private final MarbleSolitaireGuiView view;
  private int fromRow = -1;
  private int fromCol = -1;

  public MarbleSolitaireGuiController(MarbleSolitaireModel model, MarbleSolitaireGuiView view) {
    this.model = model;
    this.view = view;
    this.view.setFeatures(this);
    this.view.refresh();
  }

  @Override
  public void handleCellClick(int row, int col) {
    if (fromRow == -1) {
      // First selection
      fromRow = row;
      fromCol = col;
      view.renderMessage("Selected (" + (row + 1) + "," + (col + 1) + "). Now select destination.");
    } else {
      // Second selection - attempt move
      try {
        model.move(fromRow, fromCol, row, col);
        view.renderMessage("Move successful!");
        fromRow = -1;
        fromCol = -1;
      } catch (IllegalArgumentException e) {
        view.renderMessage("Invalid move: " + e.getMessage());
        fromRow = -1;
        fromCol = -1;
      }
      view.refresh();
    }
  }
}
