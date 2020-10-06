package javasampleokiba.inifileaccessor;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * INIファイルを書き込むクラス.
 * 
 * IniFileクラスのデータ構造に従ってINIファイルを書き込みます。
 * IniFileReadOptionクラスにより書き込み動作のオプションを設定変更できます。
 * 以下の特徴があります。
 * <ul>
 * <li>改行コードはシステムデフォルトの改行コードが使われます。</li>
 * <li>複数行の値を持つパラメータの場合、自動的に末尾に'\'を付加して出力します。</li>
 * </ul>
 * 
 * @author javasampleokiba
 */
public class IniFileWriter {

    private IniFileWriteOption option_      = new IniFileWriteOption();

    /**
     * デフォルトの書き込みオプションで{@code IniFileWriter}オブジェクトを構築します。
     */
    public IniFileWriter() {
    }

    /**
     * 指定された書き込みオプションで{@code IniFileWriter}オブジェクトを構築します。
     * 
     * @param option  書き込みオプション
     * @throws NullPointerException  {@code option}が{@code null}の場合
     */
    public IniFileWriter(IniFileWriteOption option) {
        if (option == null) throw new NullPointerException();
        option_ = option;
    }

    /**
     * 書き込みオプションを取得します。
     * 
     * @return 書き込みオプション
     */
    public IniFileWriteOption getOption() {
        return option_;
    }

    /**
     * 書き込みオプションを設定します。
     * 
     * @param option  書き込みオプション
     * @throws NullPointerException  {@code option}が{@code null}の場合
     */
    public void setOption(IniFileWriteOption option) {
        if (option == null) throw new NullPointerException();
        option_ = option;
    }

    /**
     * デフォルトの文字コードでINIファイルを書き込みます。
     * 
     * @param path     書き込むINIファイルのファイルパス
     * @param ini      INIファイルオブジェクト
     * @param options  ファイルを開く方法を指定するオプション
     * @throws IOException  読み込みエラーが発生した場合
     */
    public void write(String path, IniFile ini, OpenOption... options) throws IOException {
        write(path, ini, Charset.defaultCharset());
    }

    /**
     * 指定された文字コードでINIファイルを書き込みます。
     * 
     * @param path     書き込むINIファイルのファイルパス
     * @param ini      INIファイルオブジェクト
     * @param cs       文字コード
     * @param options  ファイルを開く方法を指定するオプション
     * @throws IOException  読み込みエラーが発生した場合
     */
    public void write(String path, IniFile ini, Charset cs, OpenOption... options) throws IOException {
        List<String> content = new ArrayList<String>();

        // ヘッダコメント書き込み
        if (ini.hasComment()) {
            for (String comment : ini.getComments()) {
                addLine(content, comment);
            }
            addBlankLine(content, 1);
        }

        boolean firstSection = true;

        // グローバルセクション書き込み
        for (Section s : ini.getSections()) {
            if (!s.isGlobalSection()) {
                continue;
            }

            // パラメータコメント、パラメータ書き込み
            List<Parameter> parameters = s.getParameters();
            for (int j = 0; j < parameters.size(); j++) {
                if (j != 0) {
                    addBlankLine(content, option_.getBlankLineBetweenParameter());
                }
                addParameter(content, parameters.get(j));
            }
            firstSection = false;
            break;
        }

        // グローバルセクション以外のすべてのセクション書き込み
        for (Section s : ini.getSections()) {
            if (s.isGlobalSection()) {
                continue;
            }

            if (!firstSection) {
                addBlankLine(content, option_.getBlankLineBetweenSection());
            }
            firstSection = false;

            // セクションコメント、セクション行書き込み
            addSection(content, s);

            // パラメータコメント、パラメータ書き込み
            List<Parameter> parameters = s.getParameters();
            for (int i = 0; i < parameters.size(); i++) {
                if (i != 0) {
                    addBlankLine(content, option_.getBlankLineBetweenParameter());
                }
                addParameter(content, parameters.get(i));
            }
        }

        Files.write(Paths.get(path), content, cs, options);
    }

    private void addLine(List<String> content, String line) {
        content.add(line);
    }

    private void addBlankLine(List<String> content, int count) {
        for (int i = 0; i < count; i++) {
            content.add("");
        }
    }

    private void addSection(List<String> content, Section section) {
        String name = section.getName();

        if (section.hasComment()) {
            for (String comment : section.getComments()) {
                addLine(content, comment);
            }
        }
        addLine(content, "[" + name + "]");
    }

    private void addParameter(List<String> content, Parameter parameter) {
        String key = parameter.getKey();

        if (parameter.hasComment()) {
            for (String comment : parameter.getComments()) {
                addLine(content, comment);
            }
        }
        for (String v : parameter.getValues()) {
            String value = v.replaceAll("(?=\r\n)|((?<=[^\r])(?=\n))", "\\\\");
            addLine(content, key + option_.getDelimiter() + value);
        }
    }
}