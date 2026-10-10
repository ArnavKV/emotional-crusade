# Emotional Crusade

A Java Swing game created in 2023 by Joshua Jeong and Arnav Kiran Vasishta. Choose a difficulty and kingdom, then answer emotion-based questions.

## Run

Install a Java JDK, then run these commands from the project folder:

```sh
mkdir -p build
javac -d build GameHolder.java EmotionalCrusadeHolder.java QuestionBank.java
java -cp build EmotionalCrusadeHolder
```

The game requires a graphical desktop. Keep the images and question files in the project folder when launching.

## Files

- `GameHolder.java`: original game source, with recovery fixes.
- `EmotionalCrusadeHolder.java`: reconstructed title menu and launcher.
- `QuestionBank.java`: question loading and selection.
- `ech_start_panel.png` and `ech_kingdom_panel.png`: original artwork.
- `passage.txt`: original question data; `passages.txt` is the identical copy used by the game.

Compiled files go in `build/`. Java automatically uses `$` in compiled inner-class filenames; these generated files do not belong in the repository. The game also creates `results.txt` locally.
