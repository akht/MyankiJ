package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public final class SentenceUtil {
    private static final Map<String, String[]> sfMap;

    static {
        sfMap = makeShortformsMap();
    }

    private SentenceUtil() {}

    public static String format(String str) {
        // 末尾のドットを削除し、すべて小文字へ
        str = SentenceUtil.removeDotAtEnd(str).toLowerCase();

        // 丸括弧と丸括弧の中身を削除
        str = str.replaceAll("\\(.*?\\)", "");

        // ハイフンの前後に空白を追加
        str = str.replaceAll("\\-", " - ");

        // 連続する空白をひとつの空白になおす
        str = str.replaceAll("\\s{2,}", " ");
        return str;
    }

    // 短縮形(I'm)に対してそれを開いたかたち(I am)を原形と呼ぶことにする
    // 短縮形を原形になおすことを展開と呼ぶことにする
    //
    // 引数listの中に短縮形を含む要素があればそれを展開した文字列をlistに格納し、
    // 展開に使った要素を削除する
    // 例1) you're => you are
    // old: [ "you're so cute" ]
    // new: [ "you are so cute" ]
    //
    // 原形が複数あるときは、すべての原形で文字列を作りlistに格納する
    // 例2) it's => it is, it has
    // old: [ "it's fine today" ]
    // new: [ "it is fine today",
    //        "it has fine today" ]
    //
    // 例3) she's => she is, she has
    //      he's => he is, he has
    // old: [ "she's tall just like he's" ]
    // new: [ "she is tall just like he is",
    //        "she is tall just like he has",
    //        "she has tall just like he is",
    //        "she has tall just like he has" ]
    public static List<String> unfoldShortform(List<String> list) {
        if (!findShortformIn(list)) return list;

        String listItem = "";
        for (String s : list) {
            if (!findShortformIn(s)) continue;
            listItem = s;
            break;
        }

        list.remove(listItem);
        List<String> elems = new ArrayList<>(Arrays.asList(listItem.split(" ")));
        String key = "";

        for (String s : elems) {
            if (!findShortformIn(s)) continue;
            key = s;
            break;
        }

        String[] longforms = sfMap.get(key);
        int index = elems.indexOf(key);
        for (String longform : longforms) {
            elems.set(index, longform);
            list.add(String.join(" ", elems));
        }

        return unfoldShortform(list);
    }

    // List<String>の要素の中に短縮形があればtrueを、なければfalseを返す
    public static boolean findShortformIn(List<String> list) {
        for (String s : list) {
            if (findShortformIn(s)) {
                return true;
            }
        }
        return false;
    }

    // 文字列中に短縮形があればtrueを、なければfalseを返す
    public static boolean findShortformIn(String str) {
        // 文字列中にアポストロフィがなければ短縮形もない
        if (!str.contains("'")) {
            return false;
        }

        String[] strings = str.split(" ");
        for (String key : strings) {
            if (sfMap.containsKey(key)) {
                return true;
            }
        }
        return false;
    }

    // shortforms.csvからshortformsMapを作る
    // 例)
    // key: she's
    // value: {she is, she has}
    private static Map<String, String[]> makeShortformsMap() {
        String filename = "files/shortforms.csv";
        Map<String, String[]> map = new LinkedHashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine();
            while (line != null) {
                String[] array = line.split(",");
                String[] value = new String[array.length - 1];
                System.arraycopy(array, 1, value, 0, array.length - 1);
                map.put(array[0], value);
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    // 受け取ったStringの末尾が.(ドット)で終わっていれば、
    // .(ドット)を削除して返す
    public static String removeDotAtEnd(String str) {
        if (str.endsWith(".")) {
            return str.substring(0, str.length() - 1);
        }
        return str;
    }
}
