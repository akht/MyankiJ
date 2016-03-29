package main;

import java.util.ArrayList;
import java.util.List;

public class Sentence {
    private List<String> list;

    public Sentence(String str) {
        this.list = makeList(str);
    }

    public List<String> getList() {
        return this.list;
    }

    // Utilクラスのメソッドを使用し
    // 正誤判定に必要なList<String>を生成して返す
    public List<String> makeList(String str) {
        // 末尾のピリオドを削除し、すべて小文字へ
        str = SentenceUtil.removeDotAtEnd(str).toLowerCase();
        // 丸括弧と丸括弧の中身を削除
        str = str.replaceAll("\\(.*?\\)", "");
        // 連続する空白をひとつの空白になおす
        str = str.replaceAll("\\s{2,}", " ");

        List<String> list = new ArrayList<>();
        list.add(str);

        // str文字列中にアポストロフィがなければ短縮型は存在ない
        // アポストロフィがあれば短縮型が存在する可能性がある
        if (str.contains("'")) {
            list = SentenceUtil.unfoldShortform(list);
        }
        return list;
    }

    // 自分自身のlistと、引数sentenceのlistに
    // ひとつでも共通する要素があればtrueを返す
    public boolean nearlyEquals(Sentence sentence) {
        for (String s : this.getList()) {
            if (sentence.getList().contains(s)) {
                return true;
            }
        }
        return false;
    }
}
