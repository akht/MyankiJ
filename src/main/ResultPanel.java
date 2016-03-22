package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ResultPanel extends JPanel implements ActionListener {
    private AppFrame appFrame;

    public JLabel resultLabel;

    public ResultPanel(AppFrame appFrame) {
        this.appFrame = appFrame;

        setSize(AppFrame.APP_WIDTH, AppFrame.APP_HEIGHT);
        setLayout(new BorderLayout());

        // ResultPanelの上にresultTopPanel,resultBottomPanelを配置し
        // その２枚のパネルの上に各コンポーネントを配置する
        JPanel resultTopPanel = new JPanel();

        JPanel resultBottomPanel = new JPanel();
        resultBottomPanel.setLayout(new FlowLayout());

        // クリアにかかった時間を表示するラベル
        resultLabel = new JLabel();
        resultLabel.setHorizontalAlignment(JLabel.CENTER);

        // 同じ問題をもう一度
        JButton replayBtn = new JButton("同じ問題");
        replayBtn.addActionListener(this);
        replayBtn.setActionCommand("REPLAY");

        // 違う問題へ
        JButton randomPlayBtn = new JButton("違う問題");
        randomPlayBtn.addActionListener(this);
        randomPlayBtn.setActionCommand("REPLAY_RANDOMLY");


        resultTopPanel.add(resultLabel);
        resultBottomPanel.add(replayBtn);
        resultBottomPanel.add(randomPlayBtn);

        this.add(resultTopPanel, BorderLayout.CENTER);
        this.add(resultBottomPanel, BorderLayout.SOUTH);
    }

    public void setResultText(String duration) {
        resultLabel.setText(duration + "秒でクリアしました");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("REPLAY")) {
            appFrame.game.replay();
        }

        if (e.getActionCommand().equals("REPLAY_RANDOMLY")) {
            appFrame.game.replayRandomly();
        }
    }
}
