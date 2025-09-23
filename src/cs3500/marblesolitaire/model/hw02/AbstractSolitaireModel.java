package cs3500.marblesolitaire.model.hw02;

import java.util.ArrayList;

/**
 * Abstract base class for Marble Solitaire models that handles common functionality.
 */
public abstract class AbstractSolitaireModel implements MarbleSolitaireModel {
  protected ArrayList<ArrayList<SlotState>> board;
  protected int dimensions;
  protected int score;

  /**
   * Initializes the board with the given dimensions and empty slot position.
   *
   * @param dimensions the size parameter for the board
   * @param sRow       the row of the empty slot
   * @param sCol       the column of the empty slot
   * @throws IllegalArgumentException if parameters are invalid
   */
  protected AbstractSolitaireModel(int dimensions, int sRow, int sCol)
      throws IllegalArgumentException {
    if (!isValidDimensions(dimensions)) {
      throw new IllegalArgumentException("Invalid dimensions");
    }
    this.dimensions = dimensions;
    this.board = initializeBoard();

    if (!isValidPosition(sRow, sCol) ||
        getSlotAt(sRow, sCol) != SlotState.Marble) {
      throw new IllegalArgumentException(
          String.format("Invalid empty cell position (%d,%d)", sRow, sCol));
    }

    this.board.get(sRow).set(sCol, SlotState.Empty);
    this.score = calculateInitialScore();
  }

  protected abstract boolean isValidDimensions(int dimensions);
  protected abstract ArrayList<ArrayList<SlotState>> initializeBoard();
  protected abstract boolean isValidPosition(int row, int col);

  protected int calculateInitialScore() {
    int count = 0;
    for (ArrayList<SlotState> row : board) {
      for (SlotState state : row) {
        if (state == SlotState.Marble) {
          count++;
        }
      }
    }
    return count;
  }

  @Override
  public void move(int fromRow, int fromCol, int toRow, int toCol)
      throws IllegalArgumentException {
    if (!isValidMove(fromRow, fromCol, toRow, toCol)) {
      throw new IllegalArgumentException("Invalid move");
    }

    int midRow = (fromRow + toRow) / 2;
    int midCol = (fromCol + toCol) / 2;

    board.get(fromRow).set(fromCol, SlotState.Empty);
    board.get(toRow).set(toCol, SlotState.Marble);
    board.get(midRow).set(midCol, SlotState.Empty);
    score--;
  }

  protected boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
    if (!isValidPosition(fromRow, fromCol) || !isValidPosition(toRow, toCol)) {
      return false;
    }

    if (getSlotAt(fromRow, fromCol) != SlotState.Marble ||
        getSlotAt(toRow, toCol) != SlotState.Empty) {
      return false;
    }

    int rowDiff = Math.abs(fromRow - toRow);
    int colDiff = Math.abs(fromCol - toCol);

    if (!isValidMoveDistance(rowDiff, colDiff)) {
      return false;
    }

    int midRow = (fromRow + toRow) / 2;
    int midCol = (fromCol + toCol) / 2;

    return getSlotAt(midRow, midCol) == SlotState.Marble;
  }

  protected abstract boolean isValidMoveDistance(int rowDiff, int colDiff);

  @Override
  public boolean isGameOver() {
    for (int row = 0; row < getBoardSize(); row++) {
      for (int col = 0; col < getBoardSize(); col++) {
        if (getSlotAt(row, col) == SlotState.Marble) {
          if (hasValidMoveFrom(row, col)) {
            return false;
          }
        }
      }
    }
    return true;
  }

  protected boolean hasValidMoveFrom(int row, int col) {
    int[][] directions = getValidMoveDirections();
    for (int[] dir : directions) {
      int toRow = row + dir[0];
      int toCol = col + dir[1];
      int midRow = row + dir[0]/2;
      int midCol = col + dir[1]/2;

      if (isValidPosition(toRow, toCol) &&
          isValidPosition(midRow, midCol) &&
          getSlotAt(toRow, toCol) == SlotState.Empty &&
          getSlotAt(midRow, midCol) == SlotState.Marble) {
        return true;
      }
    }
    return false;
  }

  protected abstract int[][] getValidMoveDirections();

  @Override
  public SlotState getSlotAt(int row, int col) throws IllegalArgumentException {
    if (row < 0 || row >= getBoardSize() || col < 0 || col >= getBoardSize()) {
      throw new IllegalArgumentException(
          String.format("Position (%d,%d) is invalid", row, col));
    }
    return board.get(row).get(col);
  }

  @Override
  public int getScore() {
    return score;
  }
}