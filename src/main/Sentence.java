package main;

import java.util.ArrayList;
import java.util.List;

// 解答文またはユーザーからの入力文を表すクラス
public class Sentence {
    private String formatted;
    private List<String> list;

    public Sentence(String str) {
        this.formatted = SentenceUtil.format(str);
        this.list = makeList(formatted);
    }

    public String getFormatted() {
        return this.formatted;
    }

    public List<String> getList() {
        return this.list;
    }

    // Utilクラスのメソッドを使用し
    // 正誤判定に必要なList<String>を生成して返す
    public List<String> makeList(String str) {
        List<String> list = new ArrayList<>();
        list.add(str);

        // str文字列中にアポストロフィがなければ短縮形は存在ない
        // アポストロフィがあれば短縮形が存在する可能性がある
        if (str.contains("'")) {
            list = SentenceUtil.unfoldShortform(list);
        }
        return list;
    }

    // 自分自身のlistと、引数sentenceのlistに
    // ひとつでも共通する要素があればtrueを返す
    public boolean nearlyEquals(Sentence sentence) {
        return this.getList().stream()
                .anyMatch(sentence.getList()::contains);
    }

    public int distanceFrom(Sentence sentence) {
        String str = sentence.getFormatted();
        return SentenceUtil.levenshteinDistance(this.formatted, str);
    }
}
