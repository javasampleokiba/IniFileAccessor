package example.javasampleokiba.inifileaccessor;

import java.io.IOException;
import java.nio.file.StandardOpenOption;

import javasampleokiba.inifileaccessor.IniFile;
import javasampleokiba.inifileaccessor.IniFileReadOption;
import javasampleokiba.inifileaccessor.IniFileReader;
import javasampleokiba.inifileaccessor.IniFileWriteOption;
import javasampleokiba.inifileaccessor.IniFileWriter;
import javasampleokiba.inifileaccessor.Parameter;
import javasampleokiba.inifileaccessor.Section;

/**
 * IniFileAccessorの実行例3
 */
public class Example3 {

    public static void main(String[] args) throws IOException {
        example3_1();
    }

    /*
     * before.iniを読み込んで、色々と編集を行った後にafter.iniに書き込む。
     */
    private static void example3_1() {
        try {
            // INIファイル読み込み
            IniFileReadOption ro = new IniFileReadOption()
                    .setHasHeaderComment(true)
                    .setAllowGlobalSection(true)
                    .setAllowMultiLine(true);
            IniFileReader r = new IniFileReader(ro);
            IniFile ini = r.read("examples/before.ini");

            // グローバルセクション作成
            Section globalSection = new Section();
            globalSection.addParameter(new Parameter("globalKey", "0", ";This is a global key!"));
            ini.addSection(globalSection);

            // [Section_1]セクションの先頭に新しいパラメータを追加
            Section section1 = ini.getSection("Section_1");
            section1.addParameter(0, new Parameter("newKey", "1"));

            // [Section_2]セクションのパラメータをすべて削除
            Section section2 = ini.getSection("Section_2");
            section2.clearParameter();

            // [Section_3]セクションを追加する
            Section section3 = new Section("Section_3");
            section3.addParameter(new Parameter("newKey", "あ\r\nい\r\nう\r\nえ\r\nお", ";複数行の値を持つパラメータ"));
            ini.addSection(section3);

            // キーと値のデリミタをデフォルトの'='から':'に変更。
            // セクション間に3行分の空行を挿入。
            // キー間に1行分の空行を挿入。
            IniFileWriteOption wo = new IniFileWriteOption()
                    .setDelimiter(':')
                    .setBlankLineBetweenSection(3)
                    .setBlankLineBetweenParameter(1);

            // INIファイル書き込み
            IniFileWriter w = new IniFileWriter(wo);
            w.write("examples/after.ini", ini, StandardOpenOption.CREATE);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}