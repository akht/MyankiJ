import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class MyankiPanel extends JPanel implements ActionListener {
    private AppFrame appFrame;

    public JLabel howManyTimesLabel;
    public JLabel countLabel;
    public JLabel questionLabel;
    public JTextField inputField;

    public MyankiPanel(AppFrame appFrame) {
        this.appFrame = appFrame;

        setSize(AppFrame.APP_WIDTH, AppFrame.APP_HEIGHT);
        setLayout(new BorderLayout());


        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        topPanel.setPreferredSize(new Dimension(getWidth(), getHeight() / 3));

        JPanel centerPanel = new JPanel();
        centerPanel.setPreferredSize(new Dimension(getWidth(), getHeight() / 3));

        JPanel bottomPanel = new JPanel();


        // この問題ファイルが何回目かを表示するラベル
        howManyTimesLabel = new JLabel("22");
        howManyTimesLabel.setHorizontalAlignment(JLabel.LEFT);

        // 今やっている問題ファイルをエディタで開くボタン
        JButton editBtn = new JButton("edit");
        editBtn.addActionListener(this);
        editBtn.setActionCommand("OPEN_EDITOR");

        // 今何問目かを表示するラベル
        countLabel = new JLabel("1/10");
        countLabel.setHorizontalAlignment(JLabel.RIGHT);

        // 問題文用のラベル
        questionLabel = new JLabel("this is a question label");
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
        inputField.setActionCommand("RETURN");
        inputField.setPreferredSize(new Dimension(500, 45));
        inputField.setHorizontalAlignment(JTextField.CENTER);

        // 次へボタン
        JButton nextBtn = new JButton();
        nextBtn.setText("skip");
        nextBtn.addActionListener(this);
        nextBtn.setActionCommand("SKIP");
        nextBtn.setPreferredSize(new Dimension(45, 45));

        topPanel.add(howManyTimesLabel);
        topPanel.add(editBtn);
        topPanel.add(countLabel);
        centerPanel.add(questionLabel);
        bottomPanel.add(inputField);
        bottomPanel.add(nextBtn);

        this.add(topPanel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("OPEN_EDITOR")) {
            File file = new File(appFrame.game.sentenceList.targetFile);
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.edit(file);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        if (e.getActionCommand().equals("SKIP")) {
            appFrame.game.nextQuestion();
        }

        if (e.getActionCommand().equals("RETURN")) {
            if (appFrame.game.checkAnswer()) {
                appFrame.game.nextQuestion();
            } else {
                inputField.selectAll();
            }
        }
    }

    private class MyMouseListener extends MouseAdapter {
//        int index = appFrame.game.index;

        @Override
        public void mousePressed(MouseEvent e) {
            questionLabel.setText(appFrame.game.sentenceList.getElem(appFrame.game.index)[1]);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            questionLabel.setText(appFrame.game.sentenceList.getElem(appFrame.game.index)[0]);
        }
    }

}
