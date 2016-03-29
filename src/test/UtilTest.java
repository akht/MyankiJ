package test;

import main.SentenceUtil;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Enclosed.class)
public class UtilTest {

    /**
     * SentenceUtil.formatString()のテスト
     */
    public static class formatStringTest {

        @Test
        public void 全て小文字に変換される() throws Exception {
            String original = "THE quiCK BrOwn FOX jumps oVeR The LAZy doG";
            String expected = "the quick brown fox jumps over the lazy dog";
            assertThat(SentenceUtil.formatString(original), is(expected));
        }

        @Test
        public void 末尾のピリオドは削除される() throws Exception {
            String original = "This sentence ends with a period.";
            String expected = "this sentence ends with a period";
            assertThat(SentenceUtil.formatString(original), is(expected));
        }

        @Test
        public void 末尾の空白は削除される() throws Exception {
            String original = "This sentence ends with a white-space   ";
            String expected = "this sentence ends with a white-space";
            assertThat(SentenceUtil.formatString(original), is(expected));
        }

        @Test
        public void 短縮形はREPLACEDに置き換えられる() throws Exception {
            String orignal = "It's fine today.";
            String expected = "REPLACED19 fine today";
            assertThat(SentenceUtil.formatString(orignal), is(expected));
        }

        @Test
        public void 短縮形にできるものはREPLACEDに置き換えらえる() throws Exception {
            String original = "It is fine today.";
            String expected = "REPLACED19 fine today";
            assertThat(SentenceUtil.formatString(original), is(expected));
        }

        @Test
        public void 短縮形や短縮形にできるものが連続してもREPLACEDに置き換えられる() throws Exception {
            String original = "It's, she is, they're, there's, isn't, it is not, mustn't";
            String expected = "REPLACED19, REPLACED18, REPLACED21, REPLACED59, REPLACED0, REPLACED19 not, REPLACED14";
            assertThat(SentenceUtil.formatString(original), is(expected));
        }

        @Test
        public void 丸括弧自身と丸括弧に挟まれた部分は削除される() throws Exception {
            String original = "This (hoge) sentence (foo) (bar) contains (huga) some parentheses (parenthesis).";
            String expected = "this sentence contains some parentheses";
            assertThat(SentenceUtil.formatString(original), is(expected));
        }

        @Test
        public void ふたつ以上の連続する空白はひとつにまとめられる() throws Exception {
            String original = "Replace   multiple  adjacent white-space  with a single white-space";
            String expected = "replace multiple adjacent white-space with a single white-space";
            assertThat(SentenceUtil.formatString(original), is(expected));
        }

        @Test
        public void 全てのテストケースを含む文章は期待通り整形される() throws Exception {
            String original = "UPPERCASE, (parenthesis) shortforms HE IS  (a) there is, (parenthesis) white-space    period.";
            String expected = "uppercase, shortforms REPLACED17 REPLACED59, white-space period";
            assertThat(SentenceUtil.formatString(original), is(expected));
        }
    }

    /**
     * SentenceUtil.removePeriodAtEnd()のテスト
     */
    public static class removePeriodAtEndTest {

        @Test
        public void 文字列の終端のピリオドは削除される() throws Exception {
            String actual = SentenceUtil.removePeriodAtEnd("This is a test.");
            String expected = "This is a test";
            assertThat(actual, is(expected));
        }
    }

    /**
     * SentenceUtil.countLines()のテスト
     */
    public static class countLinesTest {

        @Test
        public void 与えたファイルの行数を返す() {
            int actual = SentenceUtil.countLines("files/shortforms.txt");
            int expected = 60;
            assertThat(actual, is(expected));
        }
    }
}