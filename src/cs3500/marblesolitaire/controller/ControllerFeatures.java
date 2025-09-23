package cs3500.marblesolitaire.controller;

/**
 * Represents controller operations for the Marble Solitaire GUI.
 */
public interface ControllerFeatures {
  /**
   * Handle a click on the cell at the given row and column.
   *
   * @param row the row of the clicked cell (0-based)
   * @param col the column of the clicked cell (0-based)
   */
  void handleCellClick(int row, int col);
}