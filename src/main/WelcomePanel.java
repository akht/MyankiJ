package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WelcomePanel extends JPanel implements ActionListener {

    private AppFrame appFrame;

    public WelcomePanel(AppFrame appFrame) {
        this.appFrame = appFrame;

        setSize(AppFrame.APP_WIDTH, AppFrame.APP_HEIGHT);
        setLayout(new BorderLayout());

        JButton startBtn = new JButton("Click to play");
        startBtn.setBorderPainted(false);
        startBtn.addActionListener(this);
        this.add(startBtn, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        appFrame.toMyankiPanel();
        appFrame.game.startClock();
        appFrame.myankiPanel.inputField.requestFocus();
    }
}
