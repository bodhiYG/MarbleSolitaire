package cs3500.marblesolitaire.controller;

/**
 * This interface represents a controller for the Marble Solitaire game. It handles user
 * input and coordinates between the model and view to play the game.
 */
public interface MarbleSolitaireController {

  /**
   * Plays a new game of Marble Solitaire.
   * <p>
   * The method should:
   * <ul>
   *   <li>Handle user input to make moves</li>
   *   <li>Render the game state after each move</li>
   *   <li>Handle game completion</li>
   * </ul>
   *
   * @throws IllegalStateException if the controller is unable to successfully read input
   *         or transmit output. This could happen if the input source becomes unavailable
   *         or the output destination cannot be written to.
   */
  void playGame() throws IllegalStateException;
}