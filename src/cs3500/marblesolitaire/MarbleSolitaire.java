package cs3500.marblesolitaire;

import cs3500.marblesolitaire.controller.MarbleSolitaireController;
import cs3500.marblesolitaire.controller.MarbleSolitaireControllerImpl;
import cs3500.marblesolitaire.model.EnglishSolitaireModel;
import cs3500.marblesolitaire.model.EuropeanSolitaireModel;
import cs3500.marblesolitaire.model.MarbleSolitaireModel;
import cs3500.marblesolitaire.model.TriangleSolitaireModel;
import cs3500.marblesolitaire.view.MarbleSolitaireTextView;
import cs3500.marblesolitaire.view.MarbleSolitaireView;
import java.io.InputStreamReader;

/**
 * Entry point for the Marble Solitaire game, handling command-line arguments to configure the game.
 */
public final class MarbleSolitaire {
  public static void main(String[] args) {
    String boardType = null;
    Integer size = null;
    Integer holeRow = null;
    Integer holeCol = null;

    // Parse command line arguments
    for (int i = 0; i < args.length; i++) {
      switch (args[i].toLowerCase()) {
        case "english":
        case "european":
        case "triangular":
          boardType = args[i].toLowerCase();
          break;
        case "-size":
          if (i + 1 < args.length) {
            try {
              size = Integer.parseInt(args[++i]);
            } catch (NumberFormatException e) {
              // Ignore invalid size, will use default
            }
          }
          break;
        case "-hole":
          if (i + 2 < args.length) {
            try {
              holeRow = Integer.parseInt(args[++i]) - 1; // Convert to 0-based index
              holeCol = Integer.parseInt(args[++i]) - 1;
            } catch (NumberFormatException e) {
              // Ignore invalid hole position, will use default
            }
          }
          break;
        default:
          // Ignore unrecognized arguments
          break;
      }
    }

    // Default to english if no board type specified
    if (boardType == null) {
      boardType = "english";
    }

    // Create the appropriate model based on arguments
    MarbleSolitaireModel model;
    try {
      switch (boardType) {
        case "english":
          if (size != null && holeRow != null && holeCol != null) {
            model = new EnglishSolitaireModel(size, holeRow, holeCol);
          } else if (size != null) {
            model = new EnglishSolitaireModel(size);
          } else if (holeRow != null && holeCol != null) {
            model = new EnglishSolitaireModel(holeRow, holeCol);
          } else {
            model = new EnglishSolitaireModel();
          }
          break;
        case "european":
          if (size != null && holeRow != null && holeCol != null) {
            model = new EuropeanSolitaireModel(size, holeRow, holeCol);
          } else if (size != null) {
            model = new EuropeanSolitaireModel(size);
          } else if (holeRow != null && holeCol != null) {
            model = new EuropeanSolitaireModel(holeRow, holeCol);
          } else {
            model = new EuropeanSolitaireModel();
          }
          break;
        case "triangular":
          if (size != null && holeRow != null && holeCol != null) {
            model = new TriangleSolitaireModel(size, holeRow, holeCol);
          } else if (size != null) {
            model = new TriangleSolitaireModel(size);
          } else if (holeRow != null && holeCol != null) {
            model = new TriangleSolitaireModel(holeRow, holeCol);
          } else {
            model = new TriangleSolitaireModel();
          }
          break;
        default:
          // Shouldn't happen as we set default to english
          model = new EnglishSolitaireModel();
          break;
      }
    } catch (IllegalArgumentException e) {
      // If model creation fails due to invalid parameters, just use defaults
      model = createDefaultModel(boardType);
    }

    // Create view and controller
    MarbleSolitaireView view = new MarbleSolitaireTextView(model);
    MarbleSolitaireController controller = new MarbleSolitaireControllerImpl(
        model, view, new InputStreamReader(System.in));

    // Start the game
    controller.playGame();
  }

  private static MarbleSolitaireModel createDefaultModel(String boardType) {
    switch (boardType) {
      case "english":
        return new EnglishSolitaireModel();
      case "european":
        return new EuropeanSolitaireModel();
      case "triangular":
        return new TriangleSolitaireModel();
      default:
        return new EnglishSolitaireModel();
    }
  }
}