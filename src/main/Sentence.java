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
        str = SentenceUtil.format(str);
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
        for (String s : this.getList()) {
            if (sentence.getList().contains(s)) {
                return true;
            }
        }
        return false;
    }
}
