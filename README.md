# Quiz Application with Timer

A Java Swing-based multiple-choice quiz application developed as a Java Development internship project for CODSOFT.

## Features
- Displays one multiple-choice question at a time.
- Includes 10 Java programming questions.
- Gives 15 seconds to answer each question.
- Automatically records a question as unanswered when time expires.
- Allows users to select and submit an answer.
- Calculates the final score and percentage.
- Displays a review of every answer, including the correct answer.
- Allows the user to restart the quiz.

## Technologies Used
- Java
- Java Swing (GUI)
- Java AWT
- Object-Oriented Programming
- Swing Timer

## Project Structure
```text
CODSOFT_TASK2_Quiz_Application/
├── QuizApplication.java
└── README.md
```

## Requirements
- JDK 8 or later
- VS Code or any Java IDE

## How to Run
1. Open this folder in VS Code or a terminal.
2. Compile the source file:
   ```bash
   javac QuizApplication.java
   ```
3. Run the application:
   ```bash
   java QuizApplication
   ```

## How to Play
1. Launch the application.
2. Read the question and select one of the four options.
3. Click **Submit Answer** before the 15-second timer expires.
4. If time runs out, the question is recorded as unanswered.
5. After the final question, view your score and answer summary.
6. Click **Restart Quiz** to play again.

## Customization
- Change `QUESTION_TIME_SECONDS` to modify the time limit.
- Edit the `createQuestions()` method to add or modify questions and answers.

## Notes
This is an educational desktop application. Questions and scores are stored in memory and reset when the quiz is restarted or the application is closed.

## Internship
Developed as part of the Java Development Internship at CODSOFT.
