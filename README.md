# Emotional Crusade — recovered Java Swing project

Originally developed in 2023 by Joshua Jeong and Arnav Kiran Vasishta. The recovered `GameHolder.java` retains the original source and attribution comments.

## Recovery status

The original kingdom and title artwork plus the original question file have now been recovered. The source references `passages.txt`, while the recovered question file was called `passage.txt`; both are included, with `passages.txt` an **identical copy**, not edited question data.

`EmotionalCrusadeHolder.java` is a **replacement launcher created during recovery**, not the original launcher. It starts the Swing interface and loads the original kingdom artwork. The recovered `ech_start_panel.png` is included, but the replacement launcher does not yet recreate the original title screen.

**Not confirmed fully playable.** The code expects 6 question records for each emotion/difficulty, but `passage.txt` contains only 2–4 in each section; selecting questions can trigger parsing/end-of-file errors. We have intentionally not invented or duplicated question content. Some original-game behavior may require further debugging.

## Compiling and launching

Install a Java JDK and from this folder run:

```sh
javac GameHolder.java EmotionalCrusadeHolder.java
java EmotionalCrusadeHolder
```

The GUI needs a graphical desktop. Launch from the project folder so relative image and question paths resolve. `results.txt` is generated locally and excluded by `.gitignore`.

## File notes

- `GameHolder.java`: extracted from the surviving 2023 code document.
- `EmotionalCrusadeHolder.java`: reconstructed launcher (2026), not original code.
- `ech_kingdom_panel.png`, `ech_start_panel.png`: recovered original artwork.
- `passage.txt`: original recovered question data.
- `passages.txt`: same content under the filename the Java code expects.

## Credits and sharing

Respect the original collaborator's attribution. Obtain permission from the other author before making this repository public. Verify the right to distribute the question material and artwork before public release.
