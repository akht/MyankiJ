package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class SentenceUtil {
    private static final Map<String, String[]> sfMap;

    static {
        sfMap = makeShortformsMap("files/shortforms.csv");
    }

    private SentenceUtil() {}


    // 短縮型(I'm)に対してそれを開いたかたち(I am)を原型と呼ぶことにする
    // 短縮型を原型になおすことを展開と呼ぶことにする
    //
    // 引数listの中に短縮型を含む要素があればそれを展開した文字列をlistに格納し、
    // 展開に使った要素を削除する
    // 例1) you're => you are
    // old: [ "you're so cute" ]
    // new: [ "you are so cute" ]
    //
    // 原型が複数あるときは、すべての原型で文字列を作りlistに格納する
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
        while (findShortformIn(list)) {
            for (int i = 0; i < list.size(); i++) {
                String listItem = list.get(i);
                if (findShortformIn(listItem)) {
                    list.remove(i);
                    String[] elems = listItem.split(" ");
                    for (int j = 0; j < elems.length; j++) {
                        String key = elems[j];
                        if (sfMap.containsKey(key)) {
                            String[] longforms = sfMap.get(key);
                            for (String longform : longforms) {
                                elems[j] = longform;
                                list.add(String.join(" ", elems));
                            }
                            break;
                        }
                    }
                }
            }
        }
        return list;
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
    public static Map<String, String[]> makeShortformsMap(String filename) {
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

    // 受け取ったStringの末尾が.(ピリオド)で終わっていれば、
    // .(ピリオド)を削除して返す
    public static String removeDotAtEnd(String str) {
        if (str.endsWith(".")) {
            return str.substring(0, str.length() - 1);
        }
        return str;
    }
}
