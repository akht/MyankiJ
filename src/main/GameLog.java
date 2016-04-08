package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class GameLog extends LinkedHashMap<String, String> {

    public GameLog(String logFile) {
        initGameLog(logFile);
        searchNewFile("res/");
    }

    public void initGameLog(String logFile) {
        Path path = Paths.get(logFile);
        // ログファイルが存在しなければ、ログファイルを作成
        if (Files.notExists(path)) {
            try {
                Files.createFile(path, PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rwxr-xr-x")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        makeLogMap(logFile);
    }

    // myankilog.txtの中身を自身(HashMap)に格納
    // key: ファイル名(1列目)
    // value: 回数(2列目)
    public void makeLogMap(String logFile) {
        try (Stream<String> stream = Files.lines(Paths.get(logFile), StandardCharsets.UTF_8)) {
            stream.map(line -> line.split(","))
                    .forEach(a -> this.put(a[0], a[1]));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // res/にあるファイルをすべて読み込んで、ログmapのキーに存在しないファイル名があれば
    // ファイル名,0をログファイルに追加
    public void searchNewFile(String dirPath) {
        File dir = new File(dirPath);
        List<String> files = Arrays.asList(dir.list());
        files.stream()
                .filter(s -> !this.containsKey(s))
                .forEach(s -> this.put(s, "0"));
    }

    // 指定された問題(ファイル名)をやった回数を返す
    public String getCount(String fileName) {
        // 初めてのファイルならNew
        // 2~4回目なら✔︎✔︎✔︎✔︎(回数に応じた数)
        // 5~n回なら✔︎n
        int count = Integer.parseInt(this.get(fileName));
        if (count == 0) {
            return "New";
        } else if (count > 1 && count < 5) {
            return new String(new char[count]).replaceAll("\0", "✔︎");
        } else {
            return "✔︎" + count;
        }
    }

    // ログを+1してアップデートする
    public void update(String fileName) {
        int count = Integer.parseInt(this.get(fileName));
        this.put(fileName, String.valueOf(++count));
    }

    // update()されたログをmyankilog.txtに書き込みリフレッシュする
    public void refresh(String logFile) {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(logFile), StandardCharsets.UTF_8)) {
            for (Map.Entry<String, String> entry : this.entrySet()) {
                bw.write(entry.getKey() + "," + entry.getValue() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
