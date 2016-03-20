import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    private Util() {}

    // 5つのステップでテキストを整形する
    // Step1. テキストの末尾がピリオド(.)なら、ピリオドを除去する
    // Step2. 引数で受け取ったテキストをloweCaseにする
    // Step3. テキスト中に丸括弧()があれば、()自身と()で囲まれた部分を除去する
    // Step4. Step3でできたテキストを整形(空白文字が2つ以上あれば1つに置き換える)
    // Step5. 短縮形とその原形をREPLACED(n)という文字列に置き換える
    // Step6. 末尾に空白文字があれば空白文字を削除する
    // TODO: Step3の動作を変える。丸括弧の中身と直前の単語のどちらでも正解と判定できるように
    // FIXME: It is not などの場合、It's not と It isn'tがありうるが、どちらで判定されるかは運
    public static String formatString(String str) {
        // Step1, Step2
        String returnStr = removePeriodAtEnd(str).toLowerCase();

        // Step3
        String pRegex = "\\(.*?\\)";
        returnStr = returnStr.replaceAll(pRegex, "");

        // Step4
        String sRegex = "\\s{2,}";
        returnStr = returnStr.replaceAll(sRegex, " ");

        // Step5
        // HashMapのキー(配列になっている)から正規表現オブジェクトを作り、
        // それにマッチした部分をキーに対応する値(REPLACED(n))に置換する
        String shortformsFile = "files/shortforms.txt";
        Map<String[], String> shortforms = makeShortformsMap(shortformsFile);
        for (Map.Entry<String[], String> entry : shortforms.entrySet()) {
            String[] key = entry.getKey();
            String value = entry.getValue();
            for (String shortf : key) {
                Pattern p = Pattern.compile(shortf);
                Matcher m = p.matcher(returnStr);
                if (m.find()) {
                    returnStr = m.replaceAll(value);
                }
            }
        }
        // Step6
        return returnStr.trim();
    }

    // shortforms.txtからshortformsMapを作る
    // shortformsMapは次のような構造になっている
    // key: ["It's", "It is"]
    // value: "REPLACED1"
    public static Map<String[], String> makeShortformsMap(String filename) {
        Map<String[], String> map = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine();
            while (line != null) {
                for (int i = 0; i < Util.countLines(filename); i++) {
                    String[] key = line.split("\t");
                    String value = "REPLACED" + i;
                    map.put(key, value);
                    line = br.readLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }


    // 受け取ったStringの末尾が.(ピリオド)で終わっていれば、
    // .(ピリオド)を削除して返す
    public static String removePeriodAtEnd(String str) {
        if (str.endsWith(".")) {
            return str.substring(0, str.length() - 1);
        }
        return str;
    }

    // テキストファイルの行数を返す
    public static int countLines(String filename) {
        int counts = 0;
        try (LineNumberReader lnr = new LineNumberReader(new FileReader(filename))) {
            lnr.skip(Integer.MAX_VALUE);
            counts = lnr.getLineNumber() + 1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return counts;
    }
}
