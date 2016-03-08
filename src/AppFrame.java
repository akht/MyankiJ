import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AppFrame extends JFrame {
    private  Container contentPane = this.getContentPane();

    public static final int APP_WIDTH = 650;
    public static final int APP_HEIGHT = 400;

    public WelcomePanel welcomePanel;
    public MyankiPanel myankiPanel;
    public ResultPanel resultPanel;
    public Game game;

    public AppFrame() {
        initComponents();
        startGame();
    }

    private void initComponents() {
        setTitle("Test AppFrame");
        setSize(APP_WIDTH, APP_HEIGHT);
        setLocationRelativeTo(null);
        addWindowListener(new MyWindowListener());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        welcomePanel = new WelcomePanel(this);
        myankiPanel = new MyankiPanel(this);
        resultPanel = new ResultPanel(this);
    }

    public void startGame() {
        contentPane.add(welcomePanel);
        game = new Game(this);
    }

    public void toMyankiPanel() {
        contentPane.removeAll();
        contentPane.add(myankiPanel);
        contentPane.revalidate();
        repaint();
        myankiPanel.inputField.requestFocus();
    }

    public void toResultPanel() {
        contentPane.removeAll();
        contentPane.add(resultPanel);
        contentPane.revalidate();
        repaint();
    }

    private class MyWindowListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            game.refreshLog();
        }
    }
}
