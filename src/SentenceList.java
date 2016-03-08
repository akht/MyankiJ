import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class SentenceList {

    private ArrayList<String[]> sentenceList;

    private String resPath = "res";
    private static String[] filelist;
    public String fileName;
    public String targetFile;

    public SentenceList() {
        filelist = makeFilelistInDir(resPath);
        targetFile = pickFileRandomly(filelist);
        sentenceList = makeSentenceList(targetFile);
    }

    public String[] getElem(int index) {
        return this.sentenceList.get(index);
    }


    // res/以下の全てのファイルのファイル名を配列に格納
    private String[] makeFilelistInDir(String dirPath) {
        File dir = new File(dirPath);
        return dir.list();
    }

    // res/からひとつランダムにファイルを選ぶ
    private String pickFileRandomly(String[] filelist) {
        int fileIndex = new Random().nextInt(filelist.length);
        fileName = filelist[fileIndex];
        return resPath + "/" + fileName;
    }

    // 日本語と英文が入ったsentenceListを作成
    // ([日,英], [日,英], [日,英], ...)となっている
    private ArrayList<String[]> makeSentenceList(String targetFile) {
        ArrayList<String[]> list = new ArrayList<>();
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

    public void makeSentenceListAgain() {
        targetFile = pickFileRandomly(filelist);
        sentenceList = makeSentenceList(targetFile);
    }
}
