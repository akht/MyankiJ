package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

// 問題、入力欄などを持つメインのパネル
public class MyankiPanel extends JPanel {
    private AppFrame appFrame;
    private Timer delayTimer;
    private Timer blinkTimer;

    public JLabel howManyTimesLabel;
    public JLabel countLabel;
    public JLabel questionLabel;
    public final JLabel responseLabel;
    public JTextField inputField;

    public MyankiPanel(AppFrame appFrame) {
        this.appFrame = appFrame;

        setSize(AppFrame.APP_WIDTH, AppFrame.APP_HEIGHT);
        setLayout(new BorderLayout());

        // MyankiPanelの上にtopPanel,centerPanel,bottomPanelを配置し
        // その３枚のパネルの上に各コンポーネントを配置する
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        topPanel.setPreferredSize(new Dimension(getWidth(), getHeight() / 3));

        JPanel centerPanel = new JPanel();
        centerPanel.setPreferredSize(new Dimension(getWidth(), getHeight() / 3));

        JPanel bottomPanel = new JPanel();

        // この問題ファイルが何回目かを表示するラベル
        howManyTimesLabel = new JLabel("22");

        // 今やっている問題ファイルをエディタで開くボタン
        JButton editBtn = new JButton("edit");
        editBtn.addActionListener(e -> openInEditor());

        // 今何問目かを表示するラベル
        countLabel = new JLabel("1/10");

        // 問題文用のラベル
        questionLabel = new JLabel();
        questionLabel.setOpaque(true);
        questionLabel.setFont(new Font("sanserif", Font.PLAIN, 13));
        questionLabel.setBackground(new Color(250, 250, 255));
        questionLabel.setPreferredSize(new Dimension(getWidth(), 80));
        questionLabel.setHorizontalAlignment(JLabel.CENTER);
        // クリックしている間だけ答えを表示させるためのリスナ
        questionLabel.addMouseListener(new MyMouseListener());

        // 入力に対するレスポンスを表示させるラベル
        responseLabel = new JLabel();
        responseLabel.setFont(new Font("sanserif", Font.PLAIN, 20));
        responseLabel.setForeground(new Color(80, 80, 80));
        responseLabel.setHorizontalAlignment(JLabel.CENTER);
        
        // ユーザーが入力するテキストフィールド
        inputField = new JTextField();
        inputField.setFont(new Font("sanserif", Font.PLAIN, 16));
        inputField.setCaretColor(Color.GRAY);
        inputField.addActionListener(e -> checkEvent());
        inputField.setPreferredSize(new Dimension(500, 45));
        inputField.setHorizontalAlignment(JTextField.CENTER);

        // 次へボタン
        JButton nextBtn = new JButton();
        nextBtn.setText("skip");
        nextBtn.addActionListener(e -> appFrame.game.nextQuestion());
        nextBtn.setPreferredSize(new Dimension(45, 45));

        topPanel.add(howManyTimesLabel);
        topPanel.add(editBtn);
        topPanel.add(countLabel);
        centerPanel.add(questionLabel);
        centerPanel.add(responseLabel);
        bottomPanel.add(inputField);
        bottomPanel.add(nextBtn);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void setMyankiText(Quiz quiz, int index, GameLog gameLog) {
        String question = quiz.getQestion(index);
        String count = String.valueOf(index + 1) + "/10";
        String logText = gameLog.getCount(quiz.fileName);

        howManyTimesLabel.setText("(" + logText + ")" + quiz.fileName);
        countLabel.setText(count);
        questionLabel.setText(question);
    }

    // 入力が不正解だと示す
    // 答えからの近さに応じて反応を変える
    private void showResponse(Distance distance) {
        if (distance == Distance.ARTICLE) {
            int articleIndex = appFrame.game.userInput.getIndexOfIndefiniteArticles();
                String[] arr = inputField.getText().split(" ");
                int count = 0;
                for (int i = 0; i < articleIndex; i++) {
                    count += arr[i].length();
                }
                int from = count + articleIndex;
                int to = from + arr[articleIndex].length();
                inputField.select(from, to);
        }

        final boolean[] state = new boolean[1];
        blinkTimer = new Timer(distance.getMilliseconds(), e -> {
            state[0] = !state[0];
            if (state[0]) {
                questionLabel.setBackground(distance.getFirstColor());
            } else {
                questionLabel.setBackground(distance.getSecondColor());
            }
        });
        blinkTimer.setRepeats(true);
        blinkTimer.setInitialDelay(0);
        blinkTimer.start();
        responseLabel.setText(distance.getKaomoji());
    }

    // 今やっている問題をテキストエディターで開く
    private void openInEditor() {
        File file = new File(appFrame.game.quiz.targetFile);
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.edit(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Timerを使用してアクションリスナの実行を150ms遅らせる
    // その間に、正誤判定に応じたレスポンスを返す
    private void checkEvent() {
        Distance d = appFrame.game.checkAnswer();
        showResponse(d);
        if (d == Distance.CORRECT) {
            delayTimer = new Timer(500, new TimerListener("next"));
        } else {
            delayTimer = new Timer(500, new TimerListener("stay"));
        }
        delayTimer.start();
    }

    // Timerによって発生を150ms遅延させる
    private class TimerListener implements ActionListener {
        String action;

        public TimerListener(String action) {
            this.action = action;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            blinkTimer.stop();
            delayTimer.stop();
            responseLabel.setText("");
            questionLabel.setFont(new Font("sanserif", Font.PLAIN, 13));
            questionLabel.setBackground(new Color(250, 250, 255));

            if (action.equals("next")) {
                appFrame.game.nextQuestion();
            }
        }
    }

    // questionLabelをマウスでクリックし続けている(mousePressed)間は、答えを表示する
    // 離される(mouseReleased)と、元の表示に戻る
    private class MyMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            questionLabel.setText(appFrame.game.quiz.getAnswer(appFrame.game.index));
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            questionLabel.setText(appFrame.game.quiz.getQestion(appFrame.game.index));
        }
    }
}
