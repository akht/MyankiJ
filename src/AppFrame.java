import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

@SuppressWarnings("serial")
public class AppFrame extends JFrame implements ActionListener {

    private Container contentPane = getContentPane();

    private JPanel topPanel;
    private JPanel centerPanel;
    private JPanel bottomPanel;
    private JPanel resultPanel;
    private JPanel resultBottomPanel;

    private JLabel questionLabel;
    private JLabel howManyTimesLabel;
    private JLabel countLabel;
    private JLabel resultLabel;

    private JButton btn;
    private JButton editBtn;
    private JButton nextBtn;
    private JButton replayBtn;
    private JButton randomPlayBtn;

    private JTextField inputField;

    private long start;
    private long end;

    private String resPath = "res";
    private String[] filelist;
    private String fileName;
    private String targetFile;
    private ArrayList<String[]> sentenceList;
    private int index = 0;

    private File logFile;
    private MyankiLog log;

    public AppFrame() {
        initComponents();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AppFrame().setVisible(true));
    }

    // スタート画面を構築する
    private void initComponents() {
        setTitle("Myanki");
        setSize(650, 400);
        setLocationRelativeTo(null);
        addWindowListener(new MyWindowListener());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // スタート画面のパネル
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // スタートボタン
        // TODO: アニメーションをつける
        btn = new JButton("Click To Start");
        btn.setBorderPainted(false);
        btn.addActionListener(this);

        panel.add(btn, BorderLayout.CENTER);
        contentPane.add(panel);


        // Myanki用のパネルを用意
        initMyankiPanel();

        // リザルト画面用のパネルを用意
        resultPanel();
    }

    // Myanki用のパネルを生成
    private void initMyankiPanel() {
        // ログを生成
        logFile = new File("files/myankilog.txt");
        log = new MyankiLog(logFile);

        // sentenceListを生成
        filelist = makeFilelistInDir(resPath);
        targetFile = pickFileRandomly(filelist);
        sentenceList = makeSentenceList(targetFile);


        // BorderLayout.NORTHに配置するパネル
        topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        topPanel.setPreferredSize(new Dimension(getWidth(), getHeight() / 3));

        // BorderLayout.CENTERに配置するパネル
        centerPanel = new JPanel();
        centerPanel.setPreferredSize(new Dimension(getWidth(), getHeight() / 3));

        // BorderLayout.SOUTHに配置するパネル
        bottomPanel = new JPanel();


        // この問題ファイルが何回目かを表示するラベル
        howManyTimesLabel = new JLabel();
        howManyTimesLabel.setHorizontalAlignment(JLabel.LEFT);

        // 今やっている問題ファイルをエディタで開くボタン
        editBtn = new JButton("edit");
        editBtn.addActionListener(this);

        // 今何問目かを表示するラベル
        countLabel = new JLabel();
        countLabel.setHorizontalAlignment(JLabel.RIGHT);

        // 問題文用のラベル
        questionLabel = new JLabel();
        questionLabel.setOpaque(true);
        questionLabel.setBackground(new Color(250, 250, 255));
        questionLabel.setPreferredSize(new Dimension(getWidth(), 80));
        questionLabel.addMouseListener(new MyMouseListener());
        questionLabel.setHorizontalAlignment(JLabel.CENTER);

        // ユーザーが入力するテキストフィールド
        inputField = new JTextField();
        Font font = new Font("sanserif", Font.PLAIN, 16);
        inputField.setFont(font);
        inputField.setCaretColor(Color.GRAY);
        inputField.addActionListener(this);
        inputField.setPreferredSize(new Dimension(500, 45));
        inputField.setHorizontalAlignment(JTextField.CENTER);

        // 次へボタン
        nextBtn = new JButton();
        nextBtn.addActionListener(this);
        nextBtn.setText("skip");
        nextBtn.setPreferredSize(new Dimension(45, 45));


        topPanel.add(howManyTimesLabel);
        topPanel.add(editBtn);
        topPanel.add(countLabel);
        centerPanel.add(questionLabel);
        bottomPanel.add(inputField);
        bottomPanel.add(nextBtn);

    }

    private void setMyankiPanels() {
        contentPane.add(topPanel, BorderLayout.NORTH);
        contentPane.add(centerPanel, BorderLayout.CENTER);
        contentPane.add(bottomPanel, BorderLayout.SOUTH);
    }

    // リザルト画面用のパネル
    private void resultPanel() {
        // リザルト画面の上部パネル
        resultPanel = new JPanel();

        // リザルト画面の下部パネル
        resultBottomPanel = new JPanel();
        resultBottomPanel.setLayout(new FlowLayout());

        // 実際の結果を表示するラベル
        resultLabel = new JLabel();
        resultLabel.setHorizontalAlignment(JLabel.CENTER);

        // 同じ問題をもう一度
        replayBtn = new JButton("同じ問題");
        replayBtn.addActionListener(this);

        // 違う問題へ
        randomPlayBtn = new JButton("違う問題");
        randomPlayBtn.addActionListener(this);


        resultPanel.add(resultLabel);
        resultBottomPanel.add(replayBtn);
        resultBottomPanel.add(randomPlayBtn);
    }

    private void setResultPanels() {
        contentPane.add(resultPanel, BorderLayout.CENTER);
        contentPane.add(resultBottomPanel, BorderLayout.SOUTH);
    }

    // リザルト画面を表示
    private void showResult() {
        contentPane.removeAll();
        setResultPanels();
        setResultText();
        contentPane.repaint();
    }

    // res/以下の全てのファイルのファイル名を配列に格納
    private String[] makeFilelistInDir(String dirPath) {
        File dir = new File(dirPath);
        return dir.list();
    }

    // res/からひとつランダムにファイルを選ぶ
    private String pickFileRandomly(String[] filelist) {
        int fileIndex = new Random().nextInt(filelist.length);
        fileName = filelist[fileIndex];
        return resPath + "/" + fileName;
    }

    // 日本語と英文が入ったsentenceListを作成
    // ([日,英], [日,英], [日,英], ...)となっている
    private ArrayList<String[]> makeSentenceList(String targetFile) {
        ArrayList<String[]> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(targetFile))) {
            String line = br.readLine();
            while (line != null) {
                list.add(line.split("\t"));
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    private void makeSentenceListAgain() {
        targetFile = pickFileRandomly(filelist);
        sentenceList = makeSentenceList(targetFile);
    }


    // myankiPanel上のラベルにテキストをセットする
    private void setMyankiText() {
        String questionText = sentenceList.get(index)[0];
        String countText = String.valueOf(index + 1);
        String logText = log.getCount(targetFile);
        questionLabel.setText(questionText);
        howManyTimesLabel.setText("(" + logText + ")" + fileName);
        countLabel.setText(countText + "/10");
    }

    // resultPanel上のラベルにテキストをセットする
    private void setResultText() {
        long duration = end - start;
        String time = String.valueOf(duration / 1000);
        resultLabel.setText(time + "秒でクリアしました");
    }

    // inputFieldでイベントが発生したら実行
    private void checkEvent() {
        String userInput = inputField.getText();
        // 入力が"n"なら次の問題へ
        if (userInput.equals("n")) {
            nextQestion();
        }

        // 正誤判定
        // 正解なら次の問題へ、間違いなら入力文字を全選択
        if (checkAnswer()) {
            nextQestion();
        } else {
            inputField.selectAll();
        }
    }

    // 正誤判定
    private boolean checkAnswer() {
        EnglishSentence correct = new EnglishSentence();
        EnglishSentence user = new EnglishSentence();
        correct.sentence = sentenceList.get(index)[1];
        user.sentence = inputField.getText();

        return user.isCorrect(correct);
    }

    // スタート画面のパネルを削除し、Myanki用のパネルを表示
    private void startMyanki() {
        contentPane.removeAll();
        setMyankiPanels();
        setMyankiText();
        contentPane.repaint();
        inputField.requestFocus();
        start = System.currentTimeMillis();
    }


    private void nextQestion() {
        index++;
        if (index < 10) {
            setMyankiText();
            inputField.setText("");
            inputField.requestFocus();
        } else {
            log.update(fileName);
            inputField.setText("");
            end = System.currentTimeMillis();
            showResult();
        }
    }

    private void openInEditor() {
        File file = new File(targetFile);
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.open(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn) {
            startMyanki();
        }

        if (e.getSource() == inputField) {
            checkEvent();
        }

        if (e.getSource() == nextBtn) {
            nextQestion();
        }

        if (e.getSource() == editBtn) {
            openInEditor();
        }

        if (e.getSource() == replayBtn) {
            index = 0;
            startMyanki();
        }

        if (e.getSource() == randomPlayBtn) {
            index = 0;
            makeSentenceListAgain();
            startMyanki();
        }
    }

    private class MyMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getSource() == questionLabel) {
                questionLabel.setText(sentenceList.get(index)[1]);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getSource() == questionLabel) {
                questionLabel.setText(sentenceList.get(index)[0]);
            }
        }
    }

    private class MyWindowListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            log.refresh(logFile);
        }
    }

}
