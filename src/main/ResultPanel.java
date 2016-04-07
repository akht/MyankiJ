package main;

import javax.swing.*;
import java.awt.*;

public class ResultPanel extends JPanel {
    private AppFrame appFrame;

    public JLabel resultLabel;

    public ResultPanel(AppFrame appFrame) {
        this.appFrame = appFrame;

        setSize(AppFrame.APP_WIDTH, AppFrame.APP_HEIGHT);
        setLayout(new BorderLayout());

        // ResultPanelの上にresultTopPanel,resultBottomPanelを配置
        // topのほうにはresultLabelを配置
        // bottomのほうにはさらに２枚のパネルを配置し
        // そのパネル上にラベルやボタンを配置する

        JPanel resultTopPanel = new JPanel();
        // クリアにかかった時間を表示するラベル
        resultLabel = new JLabel();
        resultLabel.setHorizontalAlignment(JLabel.CENTER);
        resultTopPanel.add(resultLabel);

        JPanel resultBottomPanel = new JPanel();
        resultBottomPanel.setLayout(new GridLayout(2,0));

        JPanel panelForLabel = new JPanel();
        JLabel replayLabel = new JLabel("----------- リプレイ -----------");
        replayLabel.setForeground(Color.GRAY);
        replayLabel.setHorizontalAlignment(JLabel.CENTER);
        panelForLabel.add(replayLabel);

        JPanel panelForBtns = new JPanel();
        // 同じ問題をもう一度
        JButton replayBtn = new JButton("同じ問題");
        replayBtn.addActionListener(e -> appFrame.game.replay());
        panelForBtns.add(replayBtn);

        // 違う問題へ
        JButton randomPlayBtn = new JButton("ランダム");
        randomPlayBtn.addActionListener(e -> appFrame.game.replayRandomly());
        panelForBtns.add(randomPlayBtn);

        resultBottomPanel.add(panelForLabel);
        resultBottomPanel.add(panelForBtns);

        this.add(resultTopPanel, BorderLayout.CENTER);
        this.add(resultBottomPanel, BorderLayout.SOUTH);
    }

    public void setResultText(String duration) {
        resultLabel.setText(duration + "秒でクリアしました");
    }

}
