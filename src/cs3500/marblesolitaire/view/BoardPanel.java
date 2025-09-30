package cs3500.marblesolitaire.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import cs3500.marblesolitaire.controller.ControllerFeatures;
import cs3500.marblesolitaire.model.MarbleSolitaireModelState;

public class BoardPanel extends JPanel implements BoardPanelFeatures {
  private MarbleSolitaireModelState modelState;
  private Image emptySlot, marbleSlot, blankSlot;
  private final int cellDimension;
  private int originX, originY;
  private ControllerFeatures features;

  public BoardPanel(MarbleSolitaireModelState state) throws IllegalStateException {
    super();
    this.modelState = state;
    this.setBackground(Color.WHITE);
    this.cellDimension = 50;
    try {
      emptySlot = ImageIO.read(new FileInputStream("res/empty.png"));
      emptySlot = emptySlot.getScaledInstance(cellDimension, cellDimension, Image.SCALE_DEFAULT);

      marbleSlot = ImageIO.read(new FileInputStream("res/marble.png"));
      marbleSlot = marbleSlot.getScaledInstance(cellDimension, cellDimension, Image.SCALE_DEFAULT);

      blankSlot = ImageIO.read(new FileInputStream("res/blank.png"));
      blankSlot = blankSlot.getScaledInstance(cellDimension, cellDimension, Image.SCALE_DEFAULT);

      this.setPreferredSize(
          new Dimension((this.modelState.getBoardSize() + 4) * cellDimension
              , (this.modelState.getBoardSize() + 4) * cellDimension));
    } catch (IOException e) {
      throw new IllegalStateException("Icons not found!");
    }
  }

  @Override
  public void addFeatures(ControllerFeatures features) {
    this.features = features;
    this.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        int col = (e.getX() - originX) / cellDimension;
        int row = (e.getY() - originY) / cellDimension;
        if (row >= 0 && row < modelState.getBoardSize() &&
            col >= 0 && col < modelState.getBoardSize()) {
          features.handleCellClick(row, col);
        }
      }
    });
  }

  public int getOriginX() {
    return this.originX;
  }

  public int getOriginY() {
    return this.originY;
  }

  public int getCellDimension() {
    return this.cellDimension;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    // Calculate origin for centering the board
    originX = (int) (this.getPreferredSize().getWidth() / 2
        - this.modelState.getBoardSize() * cellDimension / 2);
    originY = (int) (this.getPreferredSize().getHeight() / 2
        - this.modelState.getBoardSize() * cellDimension / 2);

    for (int row = 0; row < modelState.getBoardSize(); row++) {
      for (int col = 0; col < modelState.getBoardSize(); col++) {
        MarbleSolitaireModelState.SlotState slot = modelState.getSlotAt(row, col);

        int x = originX + col * cellDimension;
        int y = originY + row * cellDimension;

        switch (slot) {
          case Marble:
            g.drawImage(marbleSlot, x, y, this);
            break;
          case Empty:
            g.drawImage(emptySlot, x, y, this);
            break;
          case Invalid:
            g.drawImage(blankSlot, x, y, this);
            break;
        }
      }
    }
  }
}
