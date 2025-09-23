package cs3500.marblesolitaire.model.hw02;

import java.util.ArrayList;

/**
 * Implementation of the Marble Solitaire game using a triangular board.
 */
public class TriangleSolitaireModel implements MarbleSolitaireModel {

  private final ArrayList<ArrayList<SlotState>> board;
  private final int dimensions;
  private int score;

  /**
   * Default constructor creates a 5-row game with empty slot at (0,0).
   */
  public TriangleSolitaireModel() {
    this(5, 0, 0);
  }

  /**
   * Constructor with custom dimensions.
   * @param dimensions number of marbles in the bottom row
   * @throws IllegalArgumentException if dimensions is not positive
   */
  public TriangleSolitaireModel(int dimensions) {
    this(dimensions, 0, 0);
  }

  /**
   * Constructor with custom empty slot position.
   * @param row row of the empty slot
   * @param col column of the empty slot
   * @throws IllegalArgumentException if position is invalid
   */
  public TriangleSolitaireModel(int row, int col) {
    this(5, row, col);
  }

  /**
   * Full constructor with all parameters.
   * @param dimensions number of marbles in the bottom row
   * @param row row of the empty slot
   * @param col column of the empty slot
   * @throws IllegalArgumentException if parameters are invalid
   */
  public TriangleSolitaireModel(int dimensions, int row, int col) {
    if (dimensions <= 0) {
      throw new IllegalArgumentException("Dimensions must be positive");
    }

    this.dimensions = dimensions;
    this.board = this.initializeBoard();

    if (row < 0 || row >= dimensions || col < 0 || col >= dimensions ||
        col > row || this.board.get(row).get(col) != SlotState.Marble) {
      throw new IllegalArgumentException("Invalid empty cell position (" + row + "," + col + ")");
    }

    this.board.get(row).set(col, SlotState.Empty);
    this.score = this.calculateInitialScore();
  }

  private ArrayList<ArrayList<SlotState>> initializeBoard() {
    ArrayList<ArrayList<SlotState>> board = new ArrayList<>();

    for (int row = 0; row < this.dimensions; row++) {
      ArrayList<SlotState> currentRow = new ArrayList<>();
      for (int col = 0; col < this.dimensions; col++) {
        if (col <= row) {
          currentRow.add(SlotState.Marble);
        } else {
          currentRow.add(SlotState.Invalid);
        }
      }
      board.add(currentRow);
    }
    return board;
  }

  private int calculateInitialScore() {
    return this.dimensions * (this.dimensions + 1) / 2 - 1;
  }

  @Override
  public void move(int fromRow, int fromCol, int toRow, int toCol) throws IllegalArgumentException {
    // Triangle-specific move validation
    if (!isValidMove(fromRow, fromCol, toRow, toCol)) {
      throw new IllegalArgumentException("Invalid move");
    }

    // Execute the move
    int midRow = (fromRow + toRow) / 2;
    int midCol = (fromCol + toCol) / 2;

    this.board.get(fromRow).set(fromCol, SlotState.Empty);
    this.board.get(toRow).set(toCol, SlotState.Marble);
    this.board.get(midRow).set(midCol, SlotState.Empty);
    this.score--;
  }

  private boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
    // Check basic bounds and states
    if (!isValidPosition(fromRow, fromCol) || !isValidPosition(toRow, toCol) ||
        getSlotAt(fromRow, fromCol) != SlotState.Marble ||
        getSlotAt(toRow, toCol) != SlotState.Empty) {
      return false;
    }

    // Check for proper distance and direction
    int rowDiff = Math.abs(fromRow - toRow);
    int colDiff = Math.abs(fromCol - toCol);

    // Valid moves are either:
    // 1. Two rows up/down and two columns left/right (diagonal)
    // 2. Two columns left/right in same row (horizontal)
    // 3. Two rows up/down in same column (vertical)
    if ((rowDiff == 2 && colDiff == 2) ||
        (rowDiff == 2 && colDiff == 0) ||
        (rowDiff == 0 && colDiff == 2)) {
      int midRow = (fromRow + toRow) / 2;
      int midCol = (fromCol + toCol) / 2;
      return getSlotAt(midRow, midCol) == SlotState.Marble;
    }

    return false;
  }

  private boolean isValidPosition(int row, int col) {
    return row >= 0 && row < this.dimensions &&
        col >= 0 && col < this.dimensions &&
        col <= row;
  }

  @Override
  public boolean isGameOver() {
    for (int row = 0; row < this.dimensions; row++) {
      for (int col = 0; col <= row; col++) {
        if (getSlotAt(row, col) == SlotState.Marble) {
          // Check all possible moves from this position
          if (isValidMove(row, col, row + 2, col) ||  // Down
              isValidMove(row, col, row - 2, col) ||  // Up
              isValidMove(row, col, row, col + 2) ||   // Right
              isValidMove(row, col, row, col - 2) ||   // Left
              isValidMove(row, col, row + 2, col + 2) || // Down-right
              isValidMove(row, col, row - 2, col - 2)) { // Up-left
            return false;
          }
        }
      }
    }
    return true;
  }

  @Override
  public int getBoardSize() {
    return this.dimensions;
  }

  @Override
  public SlotState getSlotAt(int row, int col) throws IllegalArgumentException {
    if (row < 0 || row >= this.dimensions || col < 0 || col >= this.dimensions) {
      throw new IllegalArgumentException("Invalid position (" + row + "," + col + ")");
    }
    return this.board.get(row).get(col);
  }

  @Override
  public int getScore() {
    return this.score;
  }
}