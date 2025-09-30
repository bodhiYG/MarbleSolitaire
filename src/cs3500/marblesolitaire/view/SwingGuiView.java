package cs3500.marblesolitaire.view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.*;

import cs3500.marblesolitaire.controller.ControllerFeatures;
import cs3500.marblesolitaire.model.MarbleSolitaireModelState;

/**
 * This class represents a GUI view that is implemented using Java
 * Swing.
 */
public class SwingGuiView extends JFrame implements MarbleSolitaireGuiView {

  //the custom panel on which the board will be drawn
  private final BoardPanel boardPanel;

  //the model state
  private MarbleSolitaireModelState modelState;
  //a label to display the score
  private JLabel scoreLabel;
  //a label to display any messages to the user
  private JLabel messageLabel;

  public SwingGuiView(MarbleSolitaireModelState state) {
    super("Marble solitaire");
    this.modelState = state;
    /* main frame uses a border layout. Read about it here:
    *
    * */

    this.setLayout(new BorderLayout());
    //initialize the custom board with the model state
    boardPanel = new BoardPanel(this.modelState);
    //add custom board to the center of the frame
    this.add(boardPanel,BorderLayout.CENTER);
    //create the score label
    this.scoreLabel = new JLabel();
    //create the message label
    this.messageLabel = new JLabel();
    //put them both in a panel. This is done mostly to arrange them properly
    JPanel panel = new JPanel();
    /*
    the panel uses a grid layout with two columns. The gridlayout
    will stretch the labels so that they are exactly half of the width
    of this panel.

    Since we mention that we want a grid of 2 columns, and we
    add exactly two things to it, it will use one row.
     */

    panel.setLayout(new GridLayout(0,2));
    panel.add(scoreLabel);
    panel.add(messageLabel);
    //add this panel to the bottom of the frame
    this.add(panel,BorderLayout.PAGE_END);
    pack();
    setVisible(true);
  }

  @Override
  public void setFeatures(ControllerFeatures features) {
    this.boardPanel.addFeatures(features);
  }

  public void refresh() {
    //refresh the score
    this.scoreLabel.setText("Score: "+modelState.getScore());
    //this repaints the frame, which cascades to everything added
    //in the frame
    this.repaint();
  }

  @Override
  public void renderMessage(String message) {
    this.messageLabel.setText(message);
  }

  /**
   * Add controller features to handle user interaction.
   * @param features the controller features
   */
  public void addFeatures(ControllerFeatures features) {
    this.boardPanel.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        int col = (e.getX() - boardPanel.getOriginX()) / boardPanel.getCellDimension();
        int row = (e.getY() - boardPanel.getOriginY()) / boardPanel.getCellDimension();
        features.handleCellClick(row, col);
      }
    });
  }
}

