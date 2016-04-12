package main;

import javax.swing.*;
import java.awt.*;

// 起動後まず表示されるパネル
public class WelcomePanel extends JPanel {

    private AppFrame appFrame;

    public WelcomePanel(AppFrame appFrame) {
        this.appFrame = appFrame;

        setSize(AppFrame.APP_WIDTH, AppFrame.APP_HEIGHT);
        setLayout(new BorderLayout());

        JButton startBtn = new JButton("Click to play");
        startBtn.setBorderPainted(false);
        startBtn.addActionListener(e -> appFrame.game.play());
        this.add(startBtn, BorderLayout.CENTER);
    }
}
