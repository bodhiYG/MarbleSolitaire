package cs3500.marblesolitaire;

import cs3500.marblesolitaire.controller.MarbleSolitaireGuiController;
import cs3500.marblesolitaire.model.MarbleSolitaireModel;
import cs3500.marblesolitaire.model.MarbleSolitaireModelState;
import cs3500.marblesolitaire.model.EnglishSolitaireModel;
import cs3500.marblesolitaire.view.SwingGuiView;

public class MainGUI {
  public static void main(String[] args) {
    MarbleSolitaireModel model = new EnglishSolitaireModel();  // Default settings
    MarbleSolitaireModelState state = model;
    SwingGuiView view = new SwingGuiView(state);
    new MarbleSolitaireGuiController(model, view);
  }
}
