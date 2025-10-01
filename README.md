# Marble Solitaire

A Java implementation of the classic **Marble Solitaire** game with support for multiple board variations and a GUI interface.

**This repository was re-created after being mistakenly pushed to the wrong GitHub account.  
As a result, the full commit history from the original repository is not reflected here.**

---

## Features

- **Board Variants**
  - English Solitaire
  - European Solitaire (with diagonal moves supported)
  - Triangle Solitaire

- **Game Functionality**
  - Fully playable from start to finish
  - Valid move checking (orthogonal and diagonal depending on board type)
  - Score tracking (number of marbles left)
  - Win/loss detection

- **Graphical User Interface**
  - Built with Java Swing
  - Click-based move input
  - Visual rendering of marbles, empty slots, and invalid positions
  - Automatic board centering and scaling

---

## How to Play

1. **Start the program** with a desired board type.
2. **Click on a marble** to select it, then click on an empty slot to move.
   - English Solitaire: moves in the 4 cardinal directions
   - European Solitaire: moves in the 4 cardinal + 4 diagonal directions
   - Triangle Solitaire: moves valid within triangular grid rules
3. **Game ends** when no more valid moves are possible.

---

## Requirements

- Java 17+  
- No external libraries required

---

## Running the Project

### Compile
```bash
javac cs3500/marblesolitaire/**/*.java
java cs3500.marblesolitaire.MarbleSolitaireGUI European
java cs3500.marblesolitaire.MarbleSolitaire English

    O O O
    O O O
O O O O O O O
O O O _ O O O
O O O O O O O
    O O O
    O O O

