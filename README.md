# IniFileAccessor
IniFileAccessorは、INIファイルの読み込み/書き込みを行うためのJavaライブラリです。

## Features
下記の通り、細かな読み込み/書き込みオプションを設定できます。

◆**読み込みオプション**

| オプション                             | デフォルト設定   | その他の設定                                                 |
| -------------------------------------- | ---------------- | ------------------------------------------------------------ |
| 空白文字を無視するか                   | False            | True                                                         |
| 空白文字列を表す正規表現               | \\s+             | 任意                                                         |
| 大文字小文字を無視するか               | True             | False                                                        |
| 不明な行を検出したときの振る舞い       | 例外をスローする | 無視する                                                     |
| グローバルセクションの存在を許可するか | False            | True                                                         |
| キーと値のデリミタ文字                 | =                | 任意                                                         |
| 複数行の値を許可するか                 | False            | True                                                         |
| 複数行の値の改行コード                 | システム依存     | CRLF / LF                                                    |
| セクション名が重複したときの振る舞い   | 例外をスローする | 前のセクションを無視する /<br/>後のセクションを無視する /<br/>マージする |
| キーが重複したときの振る舞い           | 例外をスローする | 前のキーを無視する /<br/>後のキーを無視する /<br/>複数の値とみなす |
| ヘッダコメントを持つか                 | False            | True                                                         |
| コメントの開始を表す文字               | ;                | 任意                                                         |
| 行の途中からのコメントを許可するか     | False            | True                                                         |

◆**書き込みオプション**

| オプション                     | デフォルト設定 | その他の設定 |
| ------------------------------ | -------------- | ------------ |
| キーと値のデリミタ文字         | =              | 任意         |
| 各セクション間に挿入する空行数 | 1              | 任意         |
| 各パラメータ間に挿入する空行数 | 0              | 任意         |

## Requirements
Java 5 or later<br>
（テストを実行したい場合は、Java 8 or later, JUnit5）

## Usage
src.exampleフォルダ配下の各クラスの実行例を参照してください。<br>
または、下記ブログを参照してください。<br>
[[Java][サンプルコード] INIファイル読み書きライブラリを自作してみた](http://javasampleokiba.blog.fc2.com/blog-entry-27.html)<br>
[[Java][サンプルコード] INIファイル読み書きライブラリを自作してみた (その2)](http://javasampleokiba.blog.fc2.com/blog-entry-28.html)<br>
[[Java][サンプルコード] INIファイル読み書きライブラリを自作してみた (その3)](http://javasampleokiba.blog.fc2.com/blog-entry-29.html)<br>
[[Java][サンプルコード] INIファイル読み書きライブラリを自作してみた (その4)](http://javasampleokiba.blog.fc2.com/blog-entry-30.html)

## License
[LICENSE](https://github.com/javasampleokiba/IniFileAccessor/blob/master/LICENSE)を参照してください。
