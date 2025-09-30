package cs3500.marblesolitaire.model.hw02;

import java.util.ArrayList;

public class EuropeanSolitaireModel implements MarbleSolitaireModel {
  private final ArrayList<ArrayList<SlotState>> board;
  private final int armThickness;
  private int score;

  public EuropeanSolitaireModel() {
    this(3, 3, 3);
  }

  public EuropeanSolitaireModel(int armThickness) {
    this(armThickness, (armThickness * 3 - 2) / 2, (armThickness * 3 - 2) / 2);
  }

  public EuropeanSolitaireModel(int sRow, int sCol) {
    this(3, sRow, sCol);
  }

  public EuropeanSolitaireModel(int armThickness, int sRow, int sCol) {
    if (armThickness < 1 || armThickness % 2 == 0) {
      throw new IllegalArgumentException("Arm thickness must be a positive odd number");
    }

    this.armThickness = armThickness;
    this.board = this.initializeBoard();
    int size = this.getBoardSize();

    if (sRow < 0 || sRow >= size || sCol < 0 || sCol >= size ||
        this.board.get(sRow).get(sCol) != SlotState.Marble) {
      throw new IllegalArgumentException("Invalid empty cell position (" + sRow + "," + sCol + ")");
    }

    this.board.get(sRow).set(sCol, SlotState.Empty);
    this.score = this.calculateInitialScore();
  }

  private ArrayList<ArrayList<SlotState>> initializeBoard() {
    int size = this.getBoardSize();
    ArrayList<ArrayList<SlotState>> board = new ArrayList<>();

    for (int row = 0; row < size; row++) {
      ArrayList<SlotState> currentRow = new ArrayList<>();
      for (int col = 0; col < size; col++) {
        if (isValidPosition(row, col)) {
          currentRow.add(SlotState.Marble);
        } else {
          currentRow.add(SlotState.Invalid);
        }
      }
      board.add(currentRow);
    }
    return board;
  }

  private boolean isValidPosition(int row, int col) {
    int size = getBoardSize();
    int arm = armThickness;

    // Top section: rows 0 to arm-1
    if (row < arm) {
      int minCol = arm - 1 - row;
      int maxCol = size - arm + row;
      return col >= minCol && col <= maxCol;
    }
    // Middle section: rows arm to 2*arm-1
    else if (row < 2 * arm - 1) {
      return true; // All positions valid in middle section
    }
    // Bottom section: rows 2*arm-1 to size-1
    else {
      int offset = row - (2 * arm - 2);
      int minCol = offset;
      int maxCol = size - 1 - offset;
      return col >= minCol && col <= maxCol;
    }
  }

  private int calculateInitialScore() {
    int count = 0;
    for (ArrayList<SlotState> row : this.board) {
      for (SlotState state : row) {
        if (state == SlotState.Marble) {
          count++;
        }
      }
    }
    return count;
  }

  @Override
  public void move(int fromRow, int fromCol, int toRow, int toCol) throws IllegalArgumentException {
    if (!isValidMove(fromRow, fromCol, toRow, toCol)) {
      throw new IllegalArgumentException("Invalid move");
    }

    int midRow = (fromRow + toRow) / 2;
    int midCol = (fromCol + toCol) / 2;

    this.board.get(fromRow).set(fromCol, SlotState.Empty);
    this.board.get(toRow).set(toCol, SlotState.Marble);
    this.board.get(midRow).set(midCol, SlotState.Empty);
    this.score--;
  }

  private boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
    // Check bounds
    if (fromRow < 0 || fromCol < 0 || toRow < 0 || toCol < 0 ||
        fromRow >= getBoardSize() || fromCol >= getBoardSize() ||
        toRow >= getBoardSize() || toCol >= getBoardSize()) {
      return false;
    }

    // Check slot states
    if (getSlotAt(fromRow, fromCol) != SlotState.Marble ||
        getSlotAt(toRow, toCol) != SlotState.Empty) {
      return false;
    }

    // Check move distance and direction
    int rowDiff = Math.abs(fromRow - toRow);
    int colDiff = Math.abs(fromCol - toCol);

    // Valid moves are either:
    // 1. Two spaces in one direction (horizontal/vertical)
    // 2. Two spaces diagonally (for European version)
    if (!((rowDiff == 2 && colDiff == 0) ||  // Vertical
        (rowDiff == 0 && colDiff == 2) ||  // Horizontal
        (rowDiff == 2 && colDiff == 2))) {  // Diagonal
      return false;
    }

    // Check midpoint has marble
    int midRow = (fromRow + toRow) / 2;
    int midCol = (fromCol + toCol) / 2;
    return getSlotAt(midRow, midCol) == SlotState.Marble;
  }

  @Override
  public boolean isGameOver() {
    int size = getBoardSize();
    for (int row = 0; row < size; row++) {
      for (int col = 0; col < size; col++) {
        if (getSlotAt(row, col) == SlotState.Marble) {
          // Check all possible move directions
          int[][] directions = {
              {0, 2}, {0, -2},  // Horizontal
              {2, 0}, {-2, 0},   // Vertical
              {2, 2}, {2, -2},   // Diagonal
              {-2, 2}, {-2, -2}  // Diagonal
          };

          for (int[] dir : directions) {
            int toRow = row + dir[0];
            int toCol = col + dir[1];
            if (isValidMove(row, col, toRow, toCol)) {
              return false;
            }
          }
        }
      }
    }
    return true;
  }

  @Override
  public int getBoardSize() {
    return armThickness * 3 - 2;
  }

  @Override
  public SlotState getSlotAt(int row, int col) throws IllegalArgumentException {
    if (row < 0 || row >= getBoardSize() || col < 0 || col >= getBoardSize()) {
      throw new IllegalArgumentException(
          String.format("Position (%d,%d) is outside board boundaries", row, col));
    }
    return board.get(row).get(col);
  }

  @Override
  public int getScore() {
    return score;
  }
}