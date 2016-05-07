package main;

public class Game {
    private AppFrame appFrame;
    private MyankiPanel myankiPanel;
    private ResultPanel resultPanel;

    private String logFile = "files/myankilog.txt";
    public GameLog gameLog;

    public Sentence userInput;
    private long start;
    private long end;

    public int index;
    public Quiz quiz;

    public Game(AppFrame appFrame) {
        this.appFrame = appFrame;
        myankiPanel = appFrame.myankiPanel;
        resultPanel = appFrame.resultPanel;
        initGame();
    }

    // QuizとGameLogの生成
    // myankiPanelの各ラベルに値を設定
    private void initGame() {
        quiz = new Quiz();
        gameLog = new GameLog(logFile);
    }

    public void play() {
        index = 0;
        myankiPanel.setMyankiText(quiz, index, gameLog);
        appFrame.toMyankiPanel();
        startClock();
    }

    public void replayRandomly() {
        quiz.makeQuizListAgain();
        play();
    }

    // 正誤判定
    public Distance checkAnswer() {
        Sentence correctAnswer = new Sentence(quiz.getAnswer(index));
        userInput = new Sentence(myankiPanel.inputField.getText());
        return userInput.getDistanceFrom(correctAnswer);
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
