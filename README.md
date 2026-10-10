# Emotional Crusade — recovered Java Swing project

Originally developed in 2023 by Joshua Jeong and Arnav Kiran Vasishta. The recovered `GameHolder.java` retains the original source and attribution comments.

## Recovery status

The original kingdom and title artwork plus the original question file have now been recovered. The source references `passages.txt`, while the recovered question file was called `passage.txt`; both are included, with `passages.txt` an **identical copy**, not edited question data.

`EmotionalCrusadeHolder.java` is a **replacement launcher created during recovery**, not the original launcher. It shows a title menu using `ech_start_panel.png`; Play opens the original difficulty and kingdom selection screens.

Questions are parsed by section and record markers, using the actual number of valid records. Malformed records are skipped with diagnostics. Questions do not repeat until the enabled pool is exhausted (custom passage mode still allows repeats). Then the existing pool is reused so the original game limits can continue to work; no question content is invented or added to the data files.

Both win and lose screens have a **Restart Game** button. Restart stops the timer, closes file handles, and rebuilds the game with default settings, fresh resources, score, question history, and kingdom selection. It returns to difficulty selection.

## Compiling and launching

Install a Java JDK and from this folder run:

```sh
javac GameHolder.java EmotionalCrusadeHolder.java QuestionBank.java
java EmotionalCrusadeHolder
```

The GUI needs a graphical desktop. Launch from the project folder so relative image and question paths resolve. `results.txt` is generated locally.

## File notes

- `GameHolder.java`: extracted from the surviving 2023 code document.
- `EmotionalCrusadeHolder.java`: reconstructed launcher (2026), not original code.
- `ech_kingdom_panel.png`, `ech_start_panel.png`: recovered original artwork.
- `passage.txt`: original recovered question data.
- `passages.txt`: same content under the filename the Java code expects.

## Credits and sharing

Respect the original collaborator's attribution. Obtain permission from the other author before making this repository public. Verify the right to distribute the question material and artwork before public release.
