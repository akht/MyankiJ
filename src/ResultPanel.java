import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ResultPanel extends JPanel implements ActionListener {
    private AppFrame appFrame;

    public JLabel resultLabel;

    public ResultPanel(AppFrame appFrame) {
        this.appFrame = appFrame;

        setLayout(new BorderLayout());
        setSize(AppFrame.APP_WIDTH, AppFrame.APP_HEIGHT);

        // リザルト画面の上部パネル
        JPanel resultUpperPanel = new JPanel();

        // リザルト画面の下部パネル
        JPanel resultBottomPanel = new JPanel();
        resultBottomPanel.setLayout(new FlowLayout());

        // 実際の結果を表示するラベル
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


        resultUpperPanel.add(resultLabel);
        resultBottomPanel.add(replayBtn);
        resultBottomPanel.add(randomPlayBtn);

        this.add(resultUpperPanel, BorderLayout.CENTER);
        this.add(resultBottomPanel, BorderLayout.SOUTH);
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
