package main;

import java.io.File;

public class Game {
    private AppFrame appFrame;
    private MyankiPanel myankiPanel;
    private ResultPanel resultPanel;

    private String logPath = "files/myankilog.txt";
    private File logFile;
    public GameLog gameLog;

    private long start;
    private long end;

    public int index = 0;
    public Quiz quiz;

    public Game(AppFrame appFrame) {
        this.appFrame = appFrame;
        this.myankiPanel = appFrame.myankiPanel;
        this.resultPanel = appFrame.resultPanel;
        initGame();
    }

    // QuizとGameLogの生成
    private void initGame() {
        quiz = new Quiz();
        logFile = new File(logPath);
        gameLog = new GameLog(logFile);
        myankiPanel.setMyankiText(quiz, index, gameLog);
    }

    public void replay() {
        index = 0;
        myankiPanel.setMyankiText(quiz, index, gameLog);
        startClock();
        appFrame.toMyankiPanel();
    }

    public void replayRandomly() {
        index = 0;
        quiz.makeQuizListAgain();
        myankiPanel.setMyankiText(quiz, index, gameLog);
        startClock();
        appFrame.toMyankiPanel();
    }

    // 正誤判定
    public boolean checkAnswer() {
        String userInput = SentenceUtil.formatString(myankiPanel.inputField.getText());
        String correct = SentenceUtil.formatString(quiz.getAnswer(index));
        return userInput.equals(correct);
    }

    public void nextQuestion() {
        index++;
        if (index < 10) {
            myankiPanel.setMyankiText(quiz, index, gameLog);
            myankiPanel.inputField.setText("");
            myankiPanel.inputField.requestFocus();
        } else {
            stopClock();
            gameLog.update(quiz.fileName);
            myankiPanel.inputField.setText("");
            resultPanel.setResultText(duration());
            appFrame.toResultPanel();
        }
    }

    public void startClock() {
        start = System.currentTimeMillis();
    }

    private void stopClock() {
        end = System.currentTimeMillis();
    }

    private String duration() {
        return String.valueOf((end - start) / 1000);
    }

    public void refreshLog() {
        gameLog.refresh(logFile);
    }
}
