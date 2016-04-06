package main;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

// ログはファイルネームに依存している。
// 現状のファイルネームは1.txt、2.txt、となっている。
// ログはこの[ファイル名-1]がインデックスになっているため
// 現状では自由なファイル名には対応できない
// TODO: まとも化
public class GameLog extends ArrayList<String> {

    public GameLog(String logFile) {
        makeLogList(logFile);
    }

    // myankilog.txtの中身をそのままArrayListに格納
    public void makeLogList(String logFile) {
        try (Stream<String> stream = Files.lines(Paths.get(logFile), StandardCharsets.UTF_8)) {
            stream.forEach(this::add);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // この問題をやった回数を返す
    public String getCount(String fileName) {
        int index = toNumber(fileName) - 1;

        // 取り出した数字部分をインデックスにしログを取り出す
        // ログの数値によって返す値を決める
        // 初めてのファイルならNew
        // 2~4回目なら✔︎✔︎✔︎✔︎(回数に応じた数)
        // 5~n回なら✔︎n
        int count = Integer.parseInt(this.get(index));
        if (count == 0) {
            return "New";
        } else if (count > 1 && count < 5) {
            return new String(new char[count]).replaceAll("\0", "✔︎");
        } else {
            return "✔︎" + count;
        }
    }

    // ログを+1してアップデートし、logListを再生成する
    public void update(String fileName) {
        int index = toNumber(fileName) - 1;
        int count = Integer.parseInt(this.get(index));
        this.set(index, String.valueOf(++count));
    }

    // 正規表現を使ってファイル名から数字を取り出す
    public int toNumber(String fileName) {
        Pattern p = Pattern.compile("([0-9]+)");
        Matcher m = p.matcher(fileName);
        int fileNumber = 0;
        if (m.find()) {
            fileNumber = Integer.parseInt(m.group());
        }
        return fileNumber;
    }

    // update()されたログをmyankilog.txtに書き込みリフレッシュする
    public void refresh(String logFile) {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(logFile), StandardCharsets.UTF_8)) {
            for (String line : this) {
                bw.write(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
