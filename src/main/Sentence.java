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

    // Sentence型のインスタンスを比べた結果に応じてEnum型で定義された値を返す
    // ふたつが等しいとみなせればDistance.CORRECT
    // 等しくないが近ければDistance.CLOSE
    // 等しくなく近くもなければDistance.FAR
    // 全く等しくなければDistance.INCORRECT を返す
    public Distance getDistanceFrom(Sentence sentence) {
        if (nearlyEquals(sentence)) return Distance.CORRECT;
        if (this.equals("")) return Distance.INCORRECT;
        
        String str = sentence.getFormatted().replaceAll(" ", "");
        int d = SentenceUtil.getDistance(this.formatted.replaceAll(" ", ""), str);
        if (d <= 3) {
            return Distance.CLOSE;
        } else if (d <= 6) {
            return Distance.FAR;
        } else {
            return Distance.INCORRECT;
        }
    }
}
