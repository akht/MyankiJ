MyankiJ
====
瞬間英作文を訓練するためのアプリ  

![myankij](https://cloud.githubusercontent.com/assets/6790930/14600842/bfd8f068-0599-11e6-92f5-913c9fafca43.gif)

## これは何か
[akht/myanki](https://github.com/akht/myanki) をGUIアプリ化したもの。

1. 日本語が表示される
2. それに対応する英語を打つ
3. 英作文力が鍛えられる

といったミニマリスティックなアプリです。

### 使いかた
- srcフォルダ内のソースコードをお好きなIDEなどにインポートし、jarファイルを作成
- 以下のようにfilesフォルダとresフォルダをjarファイルと同一階層に置く

```
.
├── files
│   ├── myankilog.txt
│   └── shortforms.txt
├── myanki.jar
└── res
    ├── 1.txt
    └── 2.txt
```
あとはjarファイルをダブルクリックで英作文力が鍛えられるようになります。

#### アプリで使う問題(日本語と英語のペア)について
ご自身で用意された問題はresフォルダ内に追加してください。問題ファイルの形式はタブ区切りの **.txtファイル** となります。

```
私は太郎です。<TAB>I am Taro.
あなたは誰ですか？<TAB>Who are you?
...
```

### モチベーション
瞬間英作文の訓練を声に出して行うのではなく、キーボードで打ち込んでやりたいという~~本末転倒気味な~~ニーズと、プログラミングやgitをはじめとしたその他もろもろの学習目的から作られました。

## 便利な機能
- 大文字小文字を区別しない (煩わしいシフトキー押下が不要)
- 短縮形でもその原形でも正解と判定 (I am = I'm, It's = It is など)
- 文末のピリオドは省略可 (?は省略不可)
- 日本語表示エリアをクリックしている間だけ答えを表示
- 上部のeditボタンにより問題文のtypoなどをその場で修正可能

## 必要なもの
- Java SE 8
- (重要) 出題に使う問題とその答え

## ライセンス
MIT
