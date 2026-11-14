import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class QuizApplication extends JFrame {
    private static final int QUESTION_TIME_SECONDS = 15;

    private final List<Question> questions = createQuestions();
    private final List<String> userAnswers = new ArrayList<>();
    private int currentQuestion = 0, score = 0, timeLeft;
    private boolean answerSubmitted = false;

    private JLabel progressLabel, timerLabel, questionLabel, feedbackLabel;
    private JProgressBar timerBar;
    private JRadioButton[] options;
    private ButtonGroup optionGroup;
    private JButton actionButton;
    private Timer countdownTimer;

    public QuizApplication() {
        setTitle("Java Quiz Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(760, 550);
        setMinimumSize(new Dimension(650, 480));
        setLocationRelativeTo(null);
        setContentPane(buildQuizPanel());
        showQuestion();
    }

    private JPanel buildQuizPanel() {
        JPanel main = new JPanel(new BorderLayout(18, 18));
        main.setBackground(new Color(242, 246, 252));
        main.setBorder(new EmptyBorder(24, 30, 24, 30));

        JLabel title = new JLabel("JAVA QUIZ CHALLENGE");
        title.setFont(new Font("Arial", Font.BOLD, 25));
        title.setForeground(new Color(29, 47, 76));
        progressLabel = new JLabel();
        progressLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(title, BorderLayout.WEST);
        header.add(progressLabel, BorderLayout.EAST);

        timerLabel = new JLabel();
        timerLabel.setFont(new Font("Arial", Font.BOLD, 15));
        timerBar = new JProgressBar(0, QUESTION_TIME_SECONDS);
        JPanel timerPanel = new JPanel(new BorderLayout(10, 7));
        timerPanel.setOpaque(false);
        timerPanel.add(timerLabel, BorderLayout.WEST);
        timerPanel.add(timerBar, BorderLayout.CENTER);

        JPanel top = new JPanel(new BorderLayout(10, 14));
        top.setOpaque(false);
        top.add(header, BorderLayout.NORTH);
        top.add(timerPanel, BorderLayout.SOUTH);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(28, 30, 28, 30));

        questionLabel = new JLabel();
        questionLabel.setFont(new Font("Arial", Font.BOLD, 20));
        questionLabel.setForeground(new Color(35, 48, 68));
        questionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(questionLabel);
        card.add(Box.createVerticalStrut(22));

        options = new JRadioButton[4];
        optionGroup = new ButtonGroup();
        for (int i = 0; i < options.length; i++) {
            options[i] = new JRadioButton();
            options[i].setFont(new Font("Arial", Font.PLAIN, 16));
            options[i].setBackground(Color.WHITE);
            options[i].setFocusPainted(false);
            options[i].setBorder(new EmptyBorder(10, 8, 10, 8));
            options[i].setAlignmentX(Component.LEFT_ALIGNMENT);
            optionGroup.add(options[i]);
            card.add(options[i]);
        }

        feedbackLabel = new JLabel(" ");
        feedbackLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        feedbackLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        actionButton = new JButton("Submit Answer");
        actionButton.setFont(new Font("Arial", Font.BOLD, 15));
        actionButton.setForeground(Color.WHITE);
        actionButton.setBackground(new Color(40, 105, 190));
        actionButton.setFocusPainted(false);
        actionButton.setBorder(new EmptyBorder(12, 24, 12, 24));
        actionButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        actionButton.addActionListener(e -> {
            if (!answerSubmitted) submitAnswer(false);
            else {
                currentQuestion++;
                showQuestion();
            }
        });

        card.add(Box.createVerticalStrut(12));
        card.add(feedbackLabel);
        card.add(Box.createVerticalStrut(12));
        card.add(actionButton);

        main.add(top, BorderLayout.NORTH);
        main.add(card, BorderLayout.CENTER);
        return main;
    }

    private void showQuestion() {
        if (currentQuestion >= questions.size()) {
            showResults();
            return;
        }

        Question q = questions.get(currentQuestion);
        progressLabel.setText("Question " + (currentQuestion + 1) + " of " + questions.size());
        questionLabel.setText("<html><div style='width:620px'>"
                + (currentQuestion + 1) + ". " + q.question + "</div></html>");

        for (int i = 0; i < 4; i++) {
            options[i].setText(q.options[i]);
            options[i].setSelected(false);
            options[i].setEnabled(true);
        }

        feedbackLabel.setText(" ");
        feedbackLabel.setForeground(new Color(190, 70, 55));
        actionButton.setText("Submit Answer");
        actionButton.setEnabled(true);
        answerSubmitted = false;
        startTimer();
    }

    private void startTimer() {
        if (countdownTimer != null) countdownTimer.stop();
        timeLeft = QUESTION_TIME_SECONDS;
        updateTimer();

        countdownTimer = new Timer(1000, e -> {
            timeLeft--;
            updateTimer();
            if (timeLeft <= 0) {
                countdownTimer.stop();
                submitAnswer(true);
            }
        });
        countdownTimer.start();
    }

    private void updateTimer() {
        timerLabel.setText("Time left: " + timeLeft + " seconds");
        timerBar.setValue(timeLeft);
        timerLabel.setForeground(timeLeft <= 5
                ? new Color(200, 55, 55) : new Color(35, 90, 160));
    }

    private void submitAnswer(boolean timedOut) {
        if (answerSubmitted) return;
        answerSubmitted = true;
        if (countdownTimer != null) countdownTimer.stop();

        int selected = -1;
        for (int i = 0; i < options.length; i++) {
            if (options[i].isSelected()) selected = i;
            options[i].setEnabled(false);
        }

        Question q = questions.get(currentQuestion);
        String answer = (timedOut || selected == -1)
                ? "Not Answered" : q.options[selected];
        userAnswers.add(answer);

        if (!timedOut && selected == q.correctIndex) {
            score++;
            feedbackLabel.setForeground(new Color(35, 145, 85));
            feedbackLabel.setText("Correct answer!");
        } else {
            feedbackLabel.setForeground(new Color(190, 70, 55));
            String reason = timedOut ? "Time is up! " :
                    (selected == -1 ? "No option selected. " : "Incorrect. ");
            feedbackLabel.setText(reason + "Correct answer: " + q.options[q.correctIndex]);
        }

        actionButton.setText(currentQuestion == questions.size() - 1
                ? "View Results" : "Next Question");
    }

    private void showResults() {
        if (countdownTimer != null) countdownTimer.stop();

        int percent = (int) Math.round(score * 100.0 / questions.size());
        JLabel heading = new JLabel("QUIZ COMPLETED!", SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 27));
        JLabel scoreText = new JLabel("Your Score: " + score + " / "
                + questions.size() + " (" + percent + "%)", SwingConstants.CENTER);
        scoreText.setFont(new Font("Arial", Font.BOLD, 21));
        scoreText.setForeground(new Color(35, 105, 170));

        JPanel review = new JPanel();
        review.setLayout(new BoxLayout(review, BoxLayout.Y_AXIS));
        review.setBackground(Color.WHITE);
        review.setBorder(new EmptyBorder(18, 22, 18, 22));

        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            String correct = q.options[q.correctIndex];
            boolean isCorrect = userAnswers.get(i).equals(correct);
            JLabel item = new JLabel("<html><div style='width:620px'>"
                    + (i + 1) + ". " + q.question
                    + "<br>Your answer: " + userAnswers.get(i)
                    + "<br>Correct answer: " + correct
                    + "<br>Result: " + (isCorrect ? "Correct" : "Incorrect")
                    + "</div></html>");
            item.setFont(new Font("Arial", Font.PLAIN, 14));
            item.setForeground(isCorrect
                    ? new Color(35, 125, 75) : new Color(175, 65, 55));
            item.setBorder(new EmptyBorder(8, 0, 12, 0));
            review.add(item);
        }

        JButton restart = new JButton("Restart Quiz");
        restart.addActionListener(e -> {
            currentQuestion = 0;
            score = 0;
            userAnswers.clear();
            setContentPane(buildQuizPanel());
            showQuestion();
            revalidate();
            repaint();
        });

        JPanel top = new JPanel(new GridLayout(2, 1, 8, 8));
        top.setOpaque(false);
        top.add(heading);
        top.add(scoreText);

        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        bottom.add(restart);

        JPanel result = new JPanel(new BorderLayout(12, 12));
        result.setBackground(new Color(242, 246, 252));
        result.setBorder(new EmptyBorder(25, 30, 25, 30));
        result.add(top, BorderLayout.NORTH);
        result.add(new JScrollPane(review), BorderLayout.CENTER);
        result.add(bottom, BorderLayout.SOUTH);

        setContentPane(result);
        revalidate();
        repaint();
    }

    private static List<Question> createQuestions() {
        List<Question> list = new ArrayList<>();
        list.add(new Question("Which keyword is used to create a class in Java?",
                new String[]{"class", "method", "new", "void"}, 0));
        list.add(new Question("Which method is the entry point of a standard Java application?",
                new String[]{"start()", "main()", "run()", "init()"}, 1));
        list.add(new Question("Which data type is used to store a whole number?",
                new String[]{"double", "String", "int", "boolean"}, 2));
        list.add(new Question("Which concept allows a class to acquire properties of another class?",
                new String[]{"Encapsulation", "Inheritance", "Polymorphism", "Abstraction"}, 1));
        list.add(new Question("Which symbol is used to end most Java statements?",
                new String[]{":", ".", ";", ","}, 2));
        list.add(new Question("Which collection does not allow duplicate elements?",
                new String[]{"ArrayList", "HashSet", "LinkedList", "Vector"}, 1));
        list.add(new Question("Which keyword is used to create an object in Java?",
                new String[]{"this", "class", "new", "static"}, 2));
        list.add(new Question("Which block is commonly used to handle exceptions?",
                new String[]{"check-catch", "try-catch", "do-catch", "test-catch"}, 1));
        list.add(new Question("What is the default value of an instance boolean variable?",
                new String[]{"true", "false", "0", "null"}, 1));
        list.add(new Question("Which package contains the Swing GUI components?",
                new String[]{"java.io", "java.net", "javax.swing", "java.sql"}, 2));
        return list;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new QuizApplication().setVisible(true));
    }
}

class Question {
    final String question;
    final String[] options;
    final int correctIndex;

    Question(String question, String[] options, int correctIndex) {
        this.question = question;
        this.options = options;
        this.correctIndex = correctIndex;
    }
}
