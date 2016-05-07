package main;

import java.util.ArrayList;
import java.util.List;

// 解答文またはユーザーからの入力文を表すクラス
public class Sentence {
    private String formatted;
    private List<String> list;
    private int indexOfIndefiniteArticles;

    public Sentence(String str) {
        formatted = SentenceUtil.format(str);
        list = makeList(formatted);
    }

    public String getFormatted() {
        return formatted;
    }

    public List<String> getList() {
        return list;
    }

    public int getIndexOfIndefiniteArticles() {
        return indexOfIndefiniteArticles;
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
        return list.stream()
                .anyMatch(sentence.getList()::contains);
    }

    // Sentence型のインスタンスを比べた結果に応じてEnum型で定義された値を返す
    // ふたつが等しいとみなせればDistance.CORRECT
    // 不定冠詞だけの違いならばDistance.ARTICLE
    // 等しくないが近ければDistance.CLOSE
    // 等しくなく近くもなければDistance.FAR
    // 全く等しくなければDistance.INCORRECT を返す
    public Distance getDistanceFrom(Sentence sentence) {
        if (nearlyEquals(sentence)) return Distance.CORRECT;
        if (formatted.equals("")) return Distance.INCORRECT;

        // 短縮形を含み、かつ不定冠詞のみが違う場合を判定
        // 不定冠詞aとanを入れ替えてみてnearlyEquals()からtrueが返ってくれば
        // ふたつの文字列は不定冠詞だけの違いとみなせる
        if (formatted.contains(" a ") ^ formatted.contains(" an ")) {   // XOR
            // aとanのどちらか一方のみを含んでいるとき
            List<String> pseudoList = SentenceUtil.replaceIndefiniteArticles(formatted);
            boolean anyMatch = SentenceUtil.unfoldShortform(pseudoList).stream()
                                        .anyMatch(sentence.getList()::contains);
            if (anyMatch) {
                indexOfIndefiniteArticles = SentenceUtil.getIndexOfIndefiniteArticle(formatted);
                return Distance.ARTICLE;
            }
        }

        String str = sentence.getFormatted().replaceAll(" ", "");
        int d = SentenceUtil.getDistance(formatted.replaceAll(" ", ""), str);
        if (d <= 3) {
            return Distance.CLOSE;
        } else if (d <= 6) {
            return Distance.FAR;
        } else {
            return Distance.INCORRECT;
        }
    }
}
