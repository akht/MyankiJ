import java.io.File;

public class Game {
    private AppFrame appFrame;
    private MyankiPanel myankiPanel;
    private ResultPanel resultPanel;

    private String logPath = "files/myankilog.txt";
    private File logFile;
    private MyankiLog log;

    private long start;
    private long end;

    public int index = 0;
    public SentenceList sentenceList;


    public Game(AppFrame appFrame) {
        this.appFrame = appFrame;
        this.myankiPanel = appFrame.myankiPanel;
        this.resultPanel = appFrame.resultPanel;
        initGame();
    }

    private void initGame() {
        sentenceList = new SentenceList();
        logFile = new File(logPath);
        log = new MyankiLog(logFile);
        setMyankiText();
    }

    public void replay() {
        index = 0;
        setMyankiText();
        startClock();
        appFrame.toMyankiPanel();
    }

    public void replayRandomly() {
        index = 0;
        sentenceList.makeSentenceListAgain();
        setMyankiText();
        startClock();
        appFrame.toMyankiPanel();
    }

    // 正誤判定
    public boolean checkAnswer() {
        EnglishSentence correct = new EnglishSentence();
        EnglishSentence user = new EnglishSentence();
        correct.sentence = sentenceList.getElem(index)[1];
        user.sentence = myankiPanel.inputField.getText();

        return user.isCorrect(correct);
    }

    public void nextQuestion() {
        index++;
        if (index < 10) {
            setMyankiText();
            myankiPanel.inputField.setText("");
            myankiPanel.inputField.requestFocus();
        } else {
            stopClock();
            log.update(sentenceList.fileName);
            myankiPanel.inputField.setText("");
            setResultText();
            appFrame.toResultPanel();
        }
    }

    public void setMyankiText() {
        String question = sentenceList.getElem(index)[0];
        String count = String.valueOf(index + 1) + "/10";
        String logText = log.getCount(sentenceList.fileName);

        myankiPanel.howManyTimesLabel.setText("(" + logText + ")" + sentenceList.fileName);
        myankiPanel.countLabel.setText(count);
        myankiPanel.questionLabel.setText(question);
    }

    public void setResultText() {
        resultPanel.resultLabel.setText(duration() + "秒でクリアしました");
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
        log.refresh(logFile);
    }
}
