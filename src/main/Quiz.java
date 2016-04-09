package main;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class Quiz {
    private List<String[]> list;

    private String resPath = "res";
    private String[] filelist;
    public String fileName;
    public String targetFile;

    public Quiz() {
        this.filelist = makeFilelistInDir(resPath);
        this.targetFile = pickFileRandomly(filelist);
        this.list = makeQuizList(targetFile);
    }

    public String getQestion(int index) {
        return this.list.get(index)[0];
    }

    public String getAnswer(int index) {
        return this.list.get(index)[1];
    }

    // res/内の全てのファイルのファイル名を配列に格納
    public String[] makeFilelistInDir(String dirPath) {
        File dir = new File(dirPath);
        return dir.list();
    }

    // res/内のファイルをランダムにひとつ選び、パスを含めたファイル名を返す
    public String pickFileRandomly(String[] filelist) {
        int fileIndex = new Random().nextInt(filelist.length);
        fileName = filelist[fileIndex];
        return resPath + "/" + fileName;
    }

    // 日本語と英文が入ったArrayList<String[]>を作成
    // ([日,英], [日,英], [日,英], ...)となっている
    public List<String[]> makeQuizList(String targetFile) {
        List<String[]> list = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(targetFile), StandardCharsets.UTF_8)) {
            stream.forEach(line -> list.add(line.split("\t")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void makeQuizListAgain() {
        targetFile = pickFileRandomly(filelist);
        this.list = makeQuizList(targetFile);
    }
}

