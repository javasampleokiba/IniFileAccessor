package example.javasampleokiba.inifileaccessor;

import java.io.IOException;

import javasampleokiba.inifileaccessor.IniFile;
import javasampleokiba.inifileaccessor.IniFileReadOption;
import javasampleokiba.inifileaccessor.IniFileReader;
import javasampleokiba.inifileaccessor.LineSeparator;
import javasampleokiba.inifileaccessor.Parameter;
import javasampleokiba.inifileaccessor.Section;
import javasampleokiba.inifileaccessor.IniFileReadOption.DuplicateKeyBehavior;
import javasampleokiba.inifileaccessor.IniFileReadOption.DuplicateSectionBehavior;
import javasampleokiba.inifileaccessor.IniFileReadOption.UnknownLineBehavior;

/**
 * IniFileAccessorの実行例2
 */
public class Example2 {

    public static void main(String[] args) {
        example2_1();
        example2_2();
        example2_3();
        example2_4();
        example2_5();
        example2_6();
        example2_7();
        example2_8();
        example2_9();
    }

    /*
     * デフォルトの空白文字に加えて全角スペースを空白文字とみなし、空白文字を無視する設定。
     * これにより行に空白文字が含まれていてもエラーとならずに無視されます。
     */
    private static void example2_1() {
        System.out.println("==========example2_1==========");
        try {
            IniFileReadOption option = new IniFileReadOption()
                    .setIgnoreWhiteSpace(true)
                    .setWhiteSpaceRegex("[\\s　]+");

            IniFileReader r = new IniFileReader(option);
            IniFile ini = r.read("examples/example1.ini");

            print(ini);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * 大文字小文字を区別する設定。
     * これにより大文字小文字の違いがあるセクションやキーは別ものとみなされます。
     */
    private static void example2_2() {
        System.out.println("==========example2_2==========");
        try {
            IniFileReadOption option = new IniFileReadOption()
                    .setIgnoreCase(false);

            IniFileReader r = new IniFileReader(option);
            IniFile ini = r.read("examples/example2.ini");

            print(ini);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * 解析できない不明な行を無視する設定。
     * この設定を行うと、不明な行を検出した場合もエラーとはならず単に無視されます。
     */
    private static void example2_3() {
        System.out.println("==========example2_3==========");
        try {
            IniFileReadOption option = new IniFileReadOption()
                    .setUnknownLineBehavior(UnknownLineBehavior.IGNORE);

            IniFileReader r = new IniFileReader(option);
            IniFile ini = r.read("examples/example3.ini");

            print(ini);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * グローバルセクションの存在を許可する設定と、キーと値のデリミタ文字を"="と":"にする設定。
     * グローバルセクションの存在を許可すると、example4.iniのglobalKeyのようないずれのセクション行よりも前に
     * 定義されたパラメータはグローバルセクションに所属します。
     * グローバルセクションは名前を持たないためセクション名はnullとなります。
     */
    private static void example2_4() {
        System.out.println("==========example2_4==========");
        try {
            IniFileReadOption option = new IniFileReadOption()
                    .setAllowGlobalSection(true)
                    .setDelimiters('=', ':');

            IniFileReader r = new IniFileReader(option);
            IniFile ini = r.read("examples/example4.ini");

            print(ini);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * パラメータが複数行の値を持つことを許可する設定と、複数行の改行コードをLFとする設定。
     * 値の末尾に"\"が置かれている場合に複数行とみなします。
     */
    private static void example2_5() {
        System.out.println("==========example2_5==========");
        try {
            IniFileReadOption option = new IniFileReadOption()
                    .setAllowMultiLine(true)
                    .setMultiLineSeparator(LineSeparator.LF);

            IniFileReader r = new IniFileReader(option);
            IniFile ini = r.read("examples/example5.ini");

            print(ini);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * セクションとキーが重複している場合に先に現れたものを無視する設定。
     */
    private static void example2_6() {
        System.out.println("==========example2_6==========");
        try {
            IniFileReadOption option = new IniFileReadOption()
                    .setDuplicateSectionBehavior(DuplicateSectionBehavior.IGNORE_FORMER)
                    .setDuplicateKeyBehavior(DuplicateKeyBehavior.IGNORE_FORMER);

            IniFileReader r = new IniFileReader(option);
            IniFile ini = r.read("examples/example6.ini");

            print(ini);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * セクションとキーが重複している場合に後に現れたものを無視する設定。
     */
    private static void example2_7() {
        System.out.println("==========example2_7==========");
        try {
            IniFileReadOption option = new IniFileReadOption()
                    .setDuplicateSectionBehavior(DuplicateSectionBehavior.IGNORE_LATTER)
                    .setDuplicateKeyBehavior(DuplicateKeyBehavior.IGNORE_LATTER);

            IniFileReader r = new IniFileReader(option);
            IniFile ini = r.read("examples/example6.ini");

            print(ini);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * セクションが重複している場合にはマージし、キーが重複している場合には複数の値を持つパラメータとする設定。
     */
    private static void example2_8() {
        System.out.println("==========example2_8==========");
        try {
            IniFileReadOption option = new IniFileReadOption()
                    .setDuplicateSectionBehavior(DuplicateSectionBehavior.MERGE)
                    .setDuplicateKeyBehavior(DuplicateKeyBehavior.MULTI_VALUES);

            IniFileReader r = new IniFileReader(option);
            IniFile ini = r.read("examples/example6.ini");

            print(ini);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * ヘッダコメントありの設定、コメント文字を";"と"#"にする設定、行の途中からのコメントを許可する設定。
     */
    private static void example2_9() {
        System.out.println("==========example2_9==========");
        try {
            IniFileReadOption option = new IniFileReadOption()
                    .setHasHeaderComment(true)
                    .setCommentChars(';', '#')
                    .setAllowMiddleOfLineComment(true);

            IniFileReader r = new IniFileReader(option);
            IniFile ini = r.read("examples/example7.ini");

            print(ini);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void print(IniFile ini) {
        // ヘッダコメントがあれば出力
        if (ini.hasComment()) {
            System.out.println("---HEADER COMMENT---");
            for (String c : ini.getComments()) {
                System.out.println(c);
            }
        }

        // すべてのセクションを出力
        for (Section s : ini.getSections()) {
            // セクションコメントがあれば出力
            if (s.hasComment()) {
                System.out.println("---SECTION COMMENT---");
                for (String c : s.getComments()) {
                    System.out.println(c);
                }
            }
            System.out.println("---SECTION NAME---");
            System.out.println(s.getName());

            // すべてのパラメータを出力
            for (Parameter p : s.getParameters()) {
                // パラメータコメントがあれば出力
                if (p.hasComment()) {
                    System.out.println("---PARAMETER COMMENT---");
                    for (String c : p.getComments()) {
                        System.out.println(c);
                    }
                }
                System.out.println("---PARAMETER---");
                // 複数の値を持つか？
                if (p.hasMultiValues()) {
                    System.out.println(p.getKey() + " = " + p.getValues());
                } else {
                    System.out.println(p.getKey() + " = " + p.getValue());
                }
            }
        }
    }
}