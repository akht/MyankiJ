package main;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

// Sentenceクラスで使用するユーティリティメソッドをまとめたクラス
public final class SentenceUtil {
    private static final Map<String, String[]> sfMap;

    static {
        sfMap = makeShortformsMap();
    }

    private SentenceUtil() {}

    public static String format(String str) {
        return SentenceUtil.removeDotAtEnd(str) // 末尾のドットを削除
                .toLowerCase()                  // 小文字へ
                .replaceAll("\\(.*?\\)", "")    // 丸括弧と丸括弧の中身を削除
                .replaceAll("\\-", " - ")       // ハイフンの前後に空白を追加
                .replaceAll("\\s{2,}", " ");    // 連続する空白をひとつの空白に
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

        String listItem = list.stream()
                .filter(s -> findShortformIn(s))
                .findFirst()
                .orElse("");

        list.remove(listItem);
        List<String> elems = Arrays.asList(listItem.split(" "));
        String key = elems.stream()
                .filter(s -> findShortformIn(s))
                .findFirst()
                .orElse("");

        String[] longforms = sfMap.get(key);
        int index = elems.indexOf(key);
        Arrays.stream(longforms)
                .forEach(s -> {
                    elems.set(index, s);
                    list.add(String.join(" ", elems));
                });

        return unfoldShortform(list);
    }

    // List<String>の要素の中に短縮形があればtrueを、なければfalseを返す
    public static boolean findShortformIn(List<String> list) {
        return list.stream()
                .anyMatch(SentenceUtil::findShortformIn);
    }

    // 文字列中に短縮形があればtrueを、なければfalseを返す
    public static boolean findShortformIn(String str) {
        // 文字列中にアポストロフィがなければ短縮形もない
        if (!str.contains("'")) return false;

        return Arrays.asList(str.split(" ")).stream()
                .anyMatch(sfMap::containsKey);
    }

    // shortforms.csvからshortformsMapを作る
    // 例)
    // key: she's
    // value: {she is, she has}
    private static Map<String, String[]> makeShortformsMap() {
        Map<String, String[]> map = new LinkedHashMap<>();
        Path path = Paths.get("files/shortforms.csv");
        try (Stream<String> stream = Files.lines(path, StandardCharsets.UTF_8)) {
            stream.forEach(line -> {
                String[] arr = line.split(",");
                // array配列の最初の要素以外をvalue配列にコピーする
                String[] value = Arrays.copyOfRange(arr, 1, arr.length);
                map.put(arr[0], value);
            });
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
