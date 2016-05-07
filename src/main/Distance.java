package main;

import java.awt.*;

public enum Distance {
    CORRECT  ("(⋈◍＞◡＜◍)。✧♡", new Color(179, 220, 253), new Color(216, 238,255), 70),
    ARTICLE  ("(๑•̀ㅂ•́)و おしい!", new Color(217, 255, 234), new Color(237, 255, 250), 80),
    CLOSE    ("(๑•̀ㅂ•́)و おしい!", new Color(217, 255, 234), new Color(237, 255, 250), 80),
    FAR      ("( •́ㅿ•̀ )うーん", new Color(255, 254, 222), new Color(250, 255, 239), 90),
    INCORRECT(" (ಠ_ಠ).｡oஇ", new Color(255 , 179, 196), new Color(255 , 226, 234), 100);

    private final String kaomoji;
    private final Color first;
    private final Color second;
    private final int milliseconds;

    // コンストラクタ
    Distance(String kaomoji, Color first, Color second, int milliseconds) {
        this.kaomoji = kaomoji;
        this.first = first;
        this.second = second;
        this.milliseconds = milliseconds;
    }

    public String getKaomoji() { return kaomoji; }
    public Color getFirstColor() { return first; }
    public Color getSecondColor() { return second; }
    public int getMilliseconds() { return milliseconds; }
}
