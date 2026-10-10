import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

// Reads the recovered format without assuming a question count or line count.
class QuestionBank {
    static class Question {
        final int difficulty, emotion, answer;
        final String passage, correct, wrong;
        boolean used;

        Question(int difficulty, int emotion, String passage, int answer,
                 String correct, String wrong) {
            this.difficulty = difficulty;
            this.emotion = emotion;
            this.passage = passage;
            this.answer = answer;
            this.correct = correct;
            this.wrong = wrong;
        }
    }

    private final List<Question> questions = new ArrayList<>();

    QuestionBank(File file) {
        try {
            List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
            int difficulty = -1, emotion = -1;
            String[] difficulties = {"Easy", "Adequate", "Prohibitive", "Macabre"};
            String[] emotions = {"Happy", "Sad", "Angry", "Fear"};
            for (int i = 0; i < lines.size();) {
                String line = lines.get(i).trim();
                if (line.startsWith("@@@@")) {
                    difficulty = indexOf(difficulties, line.substring(4).trim());
                    emotion = -1;
                    i++;
                } else if (line.startsWith("%%")) {
                    emotion = indexOf(emotions, line.substring(2).trim());
                    i++;
                } else if (isStart(lines, i)) {
                    int start = i++;
                    int answer = Integer.parseInt(lines.get(i++).trim());
                    int end = i;
                    while (end < lines.size() && !isBoundary(lines, end)) end++;
                    int amp = -1, bang = -1;
                    for (int j = i; j < end; j++) {
                        String marker = lines.get(j).trim();
                        if (marker.equals("&") && amp < 0) amp = j;
                        if (marker.equals("!") && bang < 0) bang = j;
                    }
                    if (difficulty >= 0 && emotion >= 0 && amp >= 0 && bang > amp) {
                        questions.add(new Question(difficulty, emotion, lines.get(start), answer,
                            join(lines, amp + 1, bang), join(lines, bang + 1, end)));
                    } else {
                        System.err.println("Skipping malformed question at line " + (start + 1));
                    }
                    i = end;
                } else {
                    if (!line.isEmpty())
                        System.err.println("Ignoring unrecognized question data at line " + (i + 1));
                    i++;
                }
            }
        } catch (IOException e) {
            System.err.println("Unable to read question data: " + e.getMessage());
        }
    }

    private static boolean isStart(List<String> lines, int i) {
        if (i + 1 >= lines.size()) return false;
        String passage = lines.get(i).trim();
        return (passage.startsWith("\"") || passage.startsWith("“"))
            && lines.get(i + 1).trim().matches("[0-3]");
    }

    private static boolean isBoundary(List<String> lines, int i) {
        String line = lines.get(i).trim();
        // A quoted passage also bounds a malformed record with a bad answer.
        return line.startsWith("@@@@") || line.startsWith("%%")
            || line.startsWith("\"") || line.startsWith("“");
    }

    private static int indexOf(String[] values, String name) {
        for (int i = 0; i < values.length; i++) if (values[i].equals(name)) return i;
        return -1;
    }

    private static String join(List<String> lines, int start, int end) {
        return String.join("\n", lines.subList(start, end)).trim();
    }

    Question choose(int difficulty, boolean[] enabled, boolean allowRepeats) {
        List<Question> available = new ArrayList<>();
        List<Question> unused = new ArrayList<>();
        for (Question q : questions) {
            if (q.difficulty == difficulty && enabled[q.emotion]) {
                available.add(q);
                if (!q.used) unused.add(q);
            }
        }
        if (available.isEmpty()) return null;
        if (!allowRepeats && unused.isEmpty()) {
            // The recovered pool is smaller than the game's total question allowance.
            for (Question q : available) q.used = false;
            unused.addAll(available);
        }
        List<Question> pool = allowRepeats ? available : unused;
        // Keep the original equal chance of choosing each available emotion.
        List<Integer> emotions = new ArrayList<>();
        for (Question q : pool) if (!emotions.contains(q.emotion)) emotions.add(q.emotion);
        int emotion = emotions.get((int) (Math.random() * emotions.size()));
        List<Question> matching = new ArrayList<>();
        for (Question q : pool) if (q.emotion == emotion) matching.add(q);
        Question chosen = matching.get((int) (Math.random() * matching.size()));
        chosen.used = true;
        return chosen;
    }
}
