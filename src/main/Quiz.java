package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    // res/以下の全てのファイルのファイル名を配列に格納
    public String[] makeFilelistInDir(String dirPath) {
        File dir = new File(dirPath);
        return dir.list();
    }

    // res/からひとつランダムにファイルを選ぶ
    public String pickFileRandomly(String[] filelist) {
        int fileIndex = new Random().nextInt(filelist.length);
        fileName = filelist[fileIndex];
        return resPath + "/" + fileName;
    }

    // 日本語と英文が入ったsentenceListを作成
    // ([日,英], [日,英], [日,英], ...)となっている
    public List<String[]> makeQuizList(String targetFile) {
        List<String[]> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(targetFile))) {
            String line = br.readLine();
            while (line != null) {
                list.add(line.split("\t"));
                line = br.readLine();
            }
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

