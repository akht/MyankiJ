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

    // レーベンシュタイン距離を利用し、２つの文字列の編集距離を返す
    public static int getDistance(String s1, String s2) {
        // (s1の文字数+1)x(s2の文字数+1)の行列を用意
        // +1は空文字("")のぶん
        int row = s1.length() + 1;
        int column = s2.length() + 1;
        int[][] matrix = new int[row][column];

        // matrixの1列目を埋める
        for (int i = 0; i < row; i++) {
            matrix[i][0] = i;
        }
        // matrixの1行目を埋める
        for (int i = 0; i < column; i++) {
            matrix[0][i] = i;
        }

        char[] s1Arr = s1.toCharArray();
        char[] s2Arr = s2.toCharArray();
        for (int i = 1; i < row; i++) {
            for (int j = 1; j < column; j++) {
                int x;
                if (s1Arr[i-1] == s2Arr[j-1]) {
                    x = 0;
                } else {
                    x = 1;
                }
                matrix[i][j] = Math.min(Math.min(matrix[i-1][j] + 1, matrix[i][j-1] + 1),
                                        matrix[i-1][j-1] + x);
            }
        }
        return matrix[row-1][column-1];
    }

    // 短縮形を含む英文文字列を、短縮形を含まない英文文字列に変換するメソッド。
    //
    // 引数listの中に短縮形を含む要素があれば、
    // その要素中の短縮形を元の形に置き換えた文字列を引数listに格納し、
    // 元の短縮形を含んだ要素を引数listから削除する。
    // ロジックの複雑化を避けるため、すべての短縮形を一度に処理することはせずに
    // 最初に見つかった短縮形を元の形に置き換える処理を短縮形がなくなるまで再帰的に呼び出す。
    //
    // 例1) you're => you are
    // param:  [ "you're so cute" ]
    // return: [ "you are so cute" ]
    //
    // 原形が複数あるときは、すべての原形で文字列を作りlistに格納する
    // 例2) it's => it is, it has
    // param:  [ "it's fine today" ]
    // return: [ "it is fine today",
    //           "it has fine today" ]
    //
    // 例3) she's => she is, she has
    //      he's => he is, he has
    // param:  [ "she's tall just like he's" ]
    // return: [ "she is tall just like he is",
    //           "she is tall just like he has",
    //           "she has tall just like he is",
    //           "she has tall just like he has" ]
    public static List<String> unfoldShortform(List<String> list) {
        if (!findShortformIn(list)) return list;

        String listItem = list.stream()
                .filter(SentenceUtil::findShortformIn)
                .findFirst()
                .orElse("");

        list.remove(listItem);
        List<String> elems = Arrays.asList(listItem.split(" "));
        String key = elems.stream()
                .filter(SentenceUtil::findShortformIn)
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
