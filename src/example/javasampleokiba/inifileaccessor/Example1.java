package example.javasampleokiba.inifileaccessor;

import java.io.IOException;

import javasampleokiba.inifileaccessor.IniFile;
import javasampleokiba.inifileaccessor.IniFileReader;
import javasampleokiba.inifileaccessor.Parameter;
import javasampleokiba.inifileaccessor.ParameterAccessor;
import javasampleokiba.inifileaccessor.Section;

/**
 * IniFileAccessorの実行例1
 */
public class Example1 {

    public static void main(String[] args) throws IOException {
        example1_1();
        example1_2();
    }

    /*
     * INIファイルを読み込み、内容を出力する例
     */
    private static void example1_1() {
        System.out.println("==========example1_1==========");
        try {
            IniFileReader r = new IniFileReader();
            IniFile ini = r.read("examples/example.ini");

            // すべてのセクションを出力
            for (Section s : ini.getSections()) {
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
                    if (p.hasComment()) {
                        System.out.println("---PARAMETER COMMENT---");
                        for (String c : p.getComments()) {
                            System.out.println(c);
                        }
                    }
                    System.out.println("---PARAMETER---");
                    System.out.println(p.getKey() + " = " + p.getValue());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * INIファイルを読み込み、ParameterAccessorオブジェクトを介して内容を取得する例
     */
    private static void example1_2() throws IOException {
        System.out.println("==========example1_2==========");
        try {
            IniFileReader r = new IniFileReader();
            IniFile ini = r.read("examples/example.ini");
            ParameterAccessor pa = new ParameterAccessor(ini);

            System.out.println("すべてのセクション名: " + pa.getSectionNames());
            System.out.println("section1のすべてのキー: " + pa.getKeys("section1"));
            System.out.println("section1のfooの値: " + pa.getInt("section1", "foo"));
            System.out.println("section1のbarの値: " + pa.get("section1", "bar"));
            System.out.println("section1のbazの値: " + pa.getDouble("section1", "baz"));
            System.out.println("section2のすべてのパラメータ: " + pa.getParameterMap("section2"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}