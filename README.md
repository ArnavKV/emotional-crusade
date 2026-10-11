# Emotional Crusade

A Java Swing strategy and educational game developed in 2023 by **Joshua Jeong and Arnav Kiran Vasishta**.

Players choose a difficulty level and one of four kingdoms, then answer emotion-based questions to manage resources and compete against computer-controlled kingdoms.

Originally developed in 2023, the game was reconstructed and updated in 2026 to restore its functionality.

## Features

- **Four Kingdoms:** Choose between Happy, Sad, Angry, and Fear.
- **Multiple Difficulty Levels:** Select from four standard difficulty settings or customize gameplay.
- **Interactive Questions:** Identify emotions represented in passages to influence resource management.
- **Resource Management:** Manage gold, resources, men, and land while competing against other kingdoms.
- **Timed Gameplay:** Answer questions under time constraints.
- **Random Events:** Experience randomized events that affect gameplay.
- **Graphical Interface:** Built using Java Swing with event-driven interactions.

## Technologies

- Java
- Java Swing
- Java AWT
- Object-Oriented Programming
- Event-Driven Programming
- File I/O

## My Contributions (Arnav)

During the original development, I worked on:

- Implementing difficulty selection interfaces and event handlers.
- Developing interactive question-and-answer interface components.
- Creating kingdom-switching functionality.
- Contributing to game timing and state-management features.
- Collaborating on the question-selection system and other gameplay components.

## Running the Game

Install a Java Development Kit (JDK), then open a terminal in the project directory.

Compile the project:

```bash
mkdir -p build
javac -d build GameHolder.java EmotionalCrusadeHolder.java QuestionBank.java
```

Launch the game:

```bash
java -cp build EmotionalCrusadeHolder
```

A graphical desktop environment is required. Keep the image assets and question data files in the project directory.

## Project Structure

- `GameHolder.java` — Original game logic and Swing components, with restoration fixes.
- `EmotionalCrusadeHolder.java` — Reconstructed title menu and application launcher.
- `QuestionBank.java` — Question loading and selection.
- `ech_start_panel.png` — Original title-screen artwork.
- `ech_kingdom_panel.png` — Original kingdom artwork.
- `passage.txt` — Original question data.
- `passages.txt` — Copy of the question data used by the game.

Compiled Java files are stored in `build/`. The game may also generate a local `results.txt` file.

## Development and Restoration

**Original Development:** 2023  
**Restoration:** 2026

The original game was developed collaboratively. In 2026, the surviving source code and assets were recovered, and the application was updated to restore functionality and compatibility with a modern Java environment.

Some restoration work was assisted by AI coding tools.

## Credits

**Original Developers:** Joshua Jeong and Arnav Kiran Vasishta

Developed as a collaborative Java programming project.
