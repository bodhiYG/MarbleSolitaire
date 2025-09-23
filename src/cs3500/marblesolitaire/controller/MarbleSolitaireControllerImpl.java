package cs3500.marblesolitaire.controller;

import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModel;
import cs3500.marblesolitaire.view.MarbleSolitaireView;
import java.io.IOException;
import java.util.Scanner;

/**
 * Implementation of the MarbleSolitaireController interface that handles user input
 * and coordinates between the model and view to play the game.
 */
public class MarbleSolitaireControllerImpl implements MarbleSolitaireController {

  private final MarbleSolitaireModel model;
  private final MarbleSolitaireView view;
  private final Readable input;

  public MarbleSolitaireControllerImpl(MarbleSolitaireModel model,
      MarbleSolitaireView view,
      Readable input) {
    if (model == null || view == null || input == null) {
      throw new IllegalArgumentException("Model, view, and input cannot be null");
    }
    this.model = model;
    this.view = view;
    this.input = input;
  }

  @Override
  public void playGame() throws IllegalStateException {
    Scanner scanner = new Scanner(input);
    boolean quit = false;

    while (!model.isGameOver() && !quit) {
      // Render game state
      try {
        view.renderBoard();
        view.renderMessage("\nScore: " + model.getScore() + "\n");
      } catch (IOException e) {
        throw new IllegalStateException("Failed to render game state");
      }

      // Get user input
      int[] moveCoords = new int[4];
      int coordIndex = 0;

      while (coordIndex < 4 && !quit) {
        if (!scanner.hasNext()) {
          throw new IllegalStateException("No more input available");
        }

        String token = scanner.next();

        // Check for quit command
        if (token.equalsIgnoreCase("q")) {
          quit = true;
          try {
            view.renderMessage("Game quit!\nState of game when quit:\n");
            view.renderBoard();
            view.renderMessage("\nScore: " + model.getScore() + "\n");
          } catch (IOException e) {
            throw new IllegalStateException("Failed to render quit message");
          }
          break;
        }

        // Parse number
        try {
          int num = Integer.parseInt(token);
          if (num <= 0) {
            try {
              view.renderMessage("Please enter a positive number (or 'q' to quit): ");
            } catch (IOException e) {
              throw new IllegalStateException("Failed to render error message");
            }
            continue;
          }
          moveCoords[coordIndex] = num - 1; // Convert to 0-based index
          coordIndex++;
        } catch (NumberFormatException e) {
          try {
            view.renderMessage("Please enter a valid number (or 'q' to quit): ");
          } catch (IOException ex) {
            throw new IllegalStateException("Failed to render error message");
          }
        }
      }

      // Attempt move if we have all coordinates
      if (!quit && coordIndex == 4) {
        try {
          model.move(moveCoords[0], moveCoords[1], moveCoords[2], moveCoords[3]);
        } catch (IllegalArgumentException e) {
          try {
            view.renderMessage("Invalid move. Play again. " + e.getMessage() + "\n");
          } catch (IOException ex) {
            throw new IllegalStateException("Failed to render error message");
          }
        }
      }
    }

    // Game over sequence
    if (!quit && model.isGameOver()) {
      try {
        view.renderMessage("Game over!\n");
        view.renderBoard();
        view.renderMessage("\nScore: " + model.getScore() + "\n");
      } catch (IOException e) {
        throw new IllegalStateException("Failed to render game over message");
      }
    }
  }
}
