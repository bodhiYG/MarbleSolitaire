package cs3500.marblesolitaire.model;
import java.util.*;

public class EnglishSolitaireModel implements MarbleSolitaireModel {
  // Enum for board creation
  public enum RowType {ARM, BODY}

  // Fields
  ArrayList<ArrayList<SlotState>>  board = new ArrayList<>();
  int armLength;

  // ====================================== Constructors ======================================
  /**
   * Default constructor
   * Arm length 3
   * Start point at 3,3
   */
  public EnglishSolitaireModel() {
    this(3, 3, 3);
  }

  // Constructor with custom row length; proportional plus sign
  /**
   * @param armLength the length of the arm of the board (standard is 3)
   * @throws IllegalArgumentException if armLength is odd
   */
  public EnglishSolitaireModel(int armLength) {
    this(armLength, (armLength + 4) / 2, (armLength + 4) / 2);
  }

  // Constructor with custom location of starting point
  /**
   * @throws IllegalArgumentException if
   *    * row or collum is outside the bounds of the board
   *    * row and collum is an invalid point
   */
  public EnglishSolitaireModel(int sRow, int sCol) {
    this(3, sRow, sCol);
  }

  // Constructor with custom arm thickness and location of starting point
  /**
   *
   * @param armLength the length of the arm of the board (standard is 3)
   * @throws IllegalArgumentException if
   * armLength is odd
   * row or collum is outside the bounds of the board
   * row and collum is an invalid point
   */
  public EnglishSolitaireModel(int armLength, int sRow, int sCol) {
    if (armLength < 2 || armLength % 2 == 0) {
      throw new IllegalArgumentException("Invalid arm length: " + armLength);
    }
    this.armLength = armLength;
    this.board = makeBody();
    int size = getBoardSize();

    // still validate both bounds *and* that you’re not on an Invalid slot
    if (sRow < 0 || sRow >= size
        || sCol < 0 || sCol >= size
        || this.board.get(sRow).get(sCol) == SlotState.Invalid) {
      throw new IllegalArgumentException("Invalid row/col: " + sRow + "/" + sCol);
    }

    // **only here** do we carve out the one empty slot
    this.board.get(sRow).set(sCol, SlotState.Empty);
  }

  // ======================================== Helper =========================================

  /**
   * Builds a 3×armLength board:
   * – total dimension = 3*armLength
   * – central square = side armLength
   * – four arms: any cell where row or col is in the central armLength‐thick band
   */
  private ArrayList<ArrayList<SlotState>> makeBody() {
    int size  = this.armLength + 4;
    int start = 2;
    int end   = this.getBoardSize() - 2;

    ArrayList<ArrayList<SlotState>> b = new ArrayList<>();
    for (int i = 0; i < size; i++) {
      ArrayList<SlotState> row = new ArrayList<>();
      for (int j = 0; j < size; j++) {
        if ((i >= start && i < end) || (j >= start && j < end)) {
          row.add(SlotState.Marble);
        } else {
          row.add(SlotState.Invalid);
        }
      }
      b.add(row);
    }
    return b;
  }

  // ======================================== Gameplay ========================================
  /**
   * Move a single marble from a given position to another given position.
   * A move is valid only if the from and to positions are valid. Specific
   * implementations may place additional constraints on the validity of a move.
   *
   * @param fromRow the row number of the position to be moved from
   *                (starts at 0)
   * @param fromCol the column number of the position to be moved from
   *                (starts at 0)
   * @param toRow   the row number of the position to be moved to
   *                (starts at 0)
   * @param toCol   the column number of the position to be moved to
   *                (starts at 0)
   * @throws IllegalArgumentException if the move is not possible
   */
  public void move(int fromRow, int fromCol, int toRow, int toCol)
      throws IllegalArgumentException {

    if(!this.canMove(fromRow, fromCol, toRow, toCol))
      throw new IllegalArgumentException("Invalid row/col: " + fromRow + "/" + fromCol + ", " +
          toRow + "/" + toCol);

    int midRow = (fromRow + toRow) / 2;
    int midCol = (fromCol + toCol) / 2;

    if (this.board.get(midRow).get(midCol) != SlotState.Marble)
      throw new IllegalArgumentException("Illegal move; There must be a marble "
          + "between the start and end points");

    this.board.get(fromRow).set(fromCol, SlotState.Empty);
    this.board.get(toRow).set(toCol, SlotState.Marble);
    this.board.get(midRow).set(midCol, SlotState.Empty);
  }

  /**
   * Determines whether a move from the given starting position to the given
   * destination is valid based on the board rules.
   * <p>
   * A move is valid only if:
   * <ul>
   *   <li>All indices are within board bounds</li>
   *   <li>The from-slot contains a marble</li>
   *   <li>The to-slot is empty</li>
   *   <li>The move is exactly two positions away in a straight line (either row or column)</li>
   *   <li>There is a marble in the slot between from and to positions</li>
   * </ul>
   *
   * @param fromRow the row number of the starting position (0-based)
   * @param fromCol the column number of the starting position (0-based)
   * @param toRow   the row number of the destination position (0-based)
   * @param toCol   the column number of the destination position (0-based)
   * @return {@code true} if the move is valid according to the game rules,
   *         {@code false} otherwise
   */
  private boolean canMove(int fromRow, int fromCol, int toRow, int toCol) {
    // Errors
    if (fromRow < 0 || fromCol < 0 || toRow < 0 || toCol < 0 || fromRow >= this.getBoardSize() ||
        toRow >= this.getBoardSize() || fromCol >= this.getBoardSize() || toCol >= this.getBoardSize())
      return false;

    if (this.board.get(fromRow).get(fromCol) != SlotState.Marble)
      return false;

    if (this.board.get(toRow).get(toCol) != SlotState.Empty)
      return false;

    if (!((fromCol == toCol && Math.abs(fromRow - toRow) == 2) ||
        (fromRow == toRow && Math.abs(fromCol - toCol) == 2)))
      return false;

    int midRow = (fromRow + toRow) / 2;
    int midCol = (fromCol + toCol) / 2;

    return this.board.get(midRow).get(midCol) == SlotState.Marble;
  }

  /**
   * Determine and return if the game is over or not. A game is over if no
   * more moves can be made.
   *
   * @return true if the game is over, false otherwise
   */
  public boolean isGameOver() {
    for (int i = 0; i < this.board.size(); i++)
      for (int j = 0; j < this.board.size(); j++)
        if (this.canMove(i, j, i + 2, j) || this.canMove(i, j, i - 2, j) ||
            this.canMove(i, j, i, j + 2) || this.canMove(i, j, i, j - 2))
          return false;
    return true;
  }

  /**
   * Return the size of this board. The size is roughly the longest dimension of a board
   *
   * @return the size as an integer
   */
  public int getBoardSize() {
    return this.armLength + 4;
  }

  /**
   * Get the state of the slot at a given position on the board.
   *
   * @param row the row of the position sought, starting at 0
   * @param col the column of the position sought, starting at 0
   * @return the state of the slot at the given row and column
   * @throws IllegalArgumentException if the row or the column are beyond
   *         the dimensions of the board
   */
  public SlotState getSlotAt(int row, int col) {
    if (row < 0 || row >= this.board.get(row).size() ||
        col < 0 || col >= this.board.get(row).size()) {
      throw new IllegalArgumentException("Invalid row/col: " + row + "/" + col);
    }
    return this.board.get(row).get(col);
  }

  /**
   * Return the number of marbles currently on the board.
   *
   * @return the number of marbles currently on the board
   */
  public int getScore() {
    int score = 0;

    for(int i = 0; i < this.board.size(); i++) {
      for(int j = 0; j < this.board.get(i).size(); j++) {
        if (getSlotAt(i, j) == SlotState.Marble) {
          score++;
        }
      }
    }
    return score;
  }
}
