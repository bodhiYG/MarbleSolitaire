package cs3500.marblesolitaire;

import cs3500.marblesolitaire.controller.MarbleSolitaireGuiController;
import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModel;
import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModelState;
import cs3500.marblesolitaire.model.hw02.EnglishSolitaireModel;
import cs3500.marblesolitaire.view.SwingGuiView;

public class MainGUI {
  public static void main(String[] args) {
    MarbleSolitaireModel model = new EnglishSolitaireModel();  // Default settings
    MarbleSolitaireModelState state = model;
    SwingGuiView view = new SwingGuiView(state);
    new MarbleSolitaireGuiController(model, view);
  }
}
