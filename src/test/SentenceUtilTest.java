package test;

import main.SentenceUtil;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.util.*;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

@RunWith(Enclosed.class)
public class SentenceUtilTest {

    public static class testFormat {
        @Test
        public void テキストは整形される() {
            String str = "IS THIS (parenthesis) A  TEST  STRING?-YES, IT IS.";
            String expected = "is this a test string? - yes, it is";

            assertThat(SentenceUtil.format(str), is(expected));
        }
    }

    public static class testGetIndexOfIndefiniteArticle {
        @Test
        public void 不定冠詞がaの場合はインデックスとして正の数を返す() {
            String s1 = "this is a delicious apple";
            String s2 = "this is an apple";

            assertThat(SentenceUtil.getIndexOfIndefiniteArticle(s1), is(2));
            assertThat(SentenceUtil.getIndexOfIndefiniteArticle(s2), is(2));
        }
        
        @Test
        public void 不定冠詞がない場合は0を返す() {
            String s3 = "this sentence has no Indefinite articles";

            assertThat(SentenceUtil.getIndexOfIndefiniteArticle(s3), is(0));
        }
    }

    public static class testGetDistance {
        @Test
        public void ふたつの文字列の編集距離を返す() {
            String str1 = "this is a test string".replaceAll(" ", "");
            String str2 = "this is not a test string".replaceAll(" ", "");
            int distance = SentenceUtil.getDistance(str1, str2);

            assertThat(distance, is(3));
        }
    }

    public static class testUnfoldShortform {
        @Test
        public void 短縮形は元の形に展開される() {
            List<String> actual = new ArrayList<>();
            actual.add("she's tall just like he's");
            actual = SentenceUtil.unfoldShortform(actual);

            String[] array = {
                    "she is tall just like he is",
                    "she is tall just like he has",
                    "she has tall just like he is",
                    "she has tall just like he has"
            };
            List<String> expected = Arrays.asList(array);

            // リスト内の要素の順番を無視してactual.equals(expected)が成り立つかを調べる
            // expectedの全要素でactual.removeでき、かつその後actualが空のリストになっていれば
            // ふたつのリストは完全に同じ要素からできていることになる
            // この方法だと、他方の要素をすべて含んでいるが等しくない場合や
            // 重複する要素を持っていた場合に対応できる
            boolean canRemoveAllElements = true;
            for (String s : expected) {
                if (!actual.remove(s)) {
                    canRemoveAllElements = false;
                    break;
                }
            }

            assertThat(actual.isEmpty(), is(true));
            assertThat(canRemoveAllElements, is(true));
        }

        @Test
        public void 短縮形を展開した後のリスト内には重複する要素は存在しない() {
            String str = "it's you're she's i'll isn't what's";
            List<String> list = new ArrayList<>();
            list.add(str);
            list = SentenceUtil.unfoldShortform(list);

            Set<String> set = new HashSet<>(list);
            // setの要素数の方が少なければlist内に重複要素があったということ
            boolean actual = set.size() < list.size();

            assertThat(actual, is(false));
        }
    }

    public static class testFindShortformIn {
        @Test
        public void 短縮形がある場合() {
            String[] array = {
                    "it's fine today",
                    "it is fine today",
                    "this is not a pen"
            };
            List<String> list = Arrays.asList(array);

            assertThat(SentenceUtil.findShortformIn(list), is(true));
        }

        @Test
        public void 短縮形がない場合() {
            String[] array = {
                    "it is fine today",
                    "this is a pen",
                    "he is not a pen"
            };
            List<String> list = Arrays.asList(array);

            assertThat(SentenceUtil.findShortformIn(list), is(false));
        }
    }

    public static class testRemoveDotAtEnd {
        @Test
        public void 末尾のドットは削除される() {
            String str = "This sentence contains dot at the end.";
            String expected = "This sentence contains dot at the end";

            assertThat(SentenceUtil.removeDotAtEnd(str), is(expected));
        }

        @Test
        public void ドット以外は削除されない() {
            String str = "Is this sentence contains Question-mark at the end?";
            String expected = "Is this sentence contains Question-mark at the end";

            assertThat(SentenceUtil.removeDotAtEnd(str), is(not(expected)));
        }
    }

}