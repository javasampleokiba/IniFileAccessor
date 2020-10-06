package javasampleokiba.inifileaccessor;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * INIファイルを読み込むクラス.
 * 
 * <br>
 * デフォルトの読み込みアルゴリズムを以下に示します（番号順に行判定が行われます）。
 * この動作は、{@code IniFileReadOption}クラスを使って変更できます。<br>
 * ------------------------------------------------------<br>
 * <p>1. 空白行</p>
 * 正規表現"^\s+$"にマッチする行を空白行とみなし無視します。
 * 
 * <p>2. コメント行</p>
 * 先頭が';'で始まる行をコメント行とみなします。
 * 行の位置により、セクション、パラメータのいずれのコメントであるかが決定します。
 * 
 * <p>3. セクション行</p>
 * 先頭が'['で始まり']'で終わる行をセクション行とみなし、
 * '['と']'で囲まれた0文字以上の文字列をセクション名とみなします。
 * 次のセクション行を検出するまで、以降に検出したパラメータはこのセクションに属します。
 * 
 * <p>4. パラメータ行</p>
 * 1つ以上の'='を含む行をパラメータ行とみなし、
 * 最初に現れる'='を区切りとして左側の0文字以上の文字列をキー、右側の0文字以上の文字列を値とみなします。
 * デフォルトでは許可しませんが、オプションにより複数行の値を許可する場合、
 * パラメータ行の末尾に'\'が置かれていると、次の行全体を値の一部とみなします。
 * 次の行以降も末尾に'\'が置かれている場合は、その次の行を値の一部とみなします。
 * このとき末尾の'\'は自動的に取り除かれます。
 * 
 * <p>5. 不明な行</p>
 * 上記のいずれの行にも該当しない行は不明な行とみなし、例外をスローし処理を中止します。
 * <br>
 * ------------------------------------------------------<br>
 * 
 * その他、以下の特徴があります。
 * <ul>
 * <li>セクション名、キー名が重複した場合は例外をスローします。
 *   このとき、大文字小文字は区別しません。</li>
 * <li>行の途中からのコメントは許可しません。</li>
 * </ul>
 * 
 * @author javasampleokiba
 */
public class IniFileReader {

    private static final Pattern SECTION_PATTERN        = Pattern.compile("^\\[(.*)\\]$");
    private IniFileReadOption option_                   = new IniFileReadOption();

    /**
     * デフォルトの読み込みオプションで{@code IniFileReader}オブジェクトを構築します。
     */
    public IniFileReader() {
    }

    /**
     * 指定された読み込みオプションで{@code IniFileReader}オブジェクトを構築します。
     * 
     * @param option  読み込みオプション
     * @throws NullPointerException  {@code option}が{@code null}の場合
     */
    public IniFileReader(IniFileReadOption option) {
        if (option == null) throw new NullPointerException();
        option_ = option;
    }

    /**
     * 読み込みオプションを取得します。
     * 
     * @return 読み込みオプション
     */
    public IniFileReadOption getOption() {
        return option_;
    }

    /**
     * 読み込みオプションを設定します。
     * 
     * @param option  読み込みオプション
     * @throws NullPointerException  {@code option}が{@code null}の場合
     */
    public void setOption(IniFileReadOption option) {
        if (option == null) throw new NullPointerException();
        option_ = option;
    }

    /**
     * デフォルトの文字コードで指定されたINIファイルを読み込み、
     * INIファイルオブジェクトを返します。
     * 
     * @param path  読み込むINIファイルのファイルパス
     * @return INIファイルオブジェクト
     * @throws IOException  読み込みエラーが発生した場合
     */
    public IniFile read(String path) throws IOException {
        return read(path, Charset.defaultCharset());
    }

    /**
     * 指定された文字コードで指定されたINIファイルを読み込み、
     * INIファイルオブジェクトを返します。
     * 
     * @param path  読み込むINIファイルのパス
     * @param cs    文字コード
     * @return INIファイルオブジェクト
     * @throws IOException  読み込みエラーが発生した場合
     */
    public IniFile read(String path, Charset cs) throws IOException {
        IniFile ini = new IniFile(option_.isIgnoreCase());
        List<String> lines = Files.readAllLines(Paths.get(path), cs);
        int num = 0;

        // ヘッダコメント取得
        if (option_.hasHeaderComment()) {
            List<String> header = new ArrayList<String>();
            for (; num < lines.size(); num++) {
                String line = lines.get(num);

                // コメント行ではない場合
                if (!isCommentLine(line)) {
                    break;
                }
                header.add(line);
            }
            for (String comment : header) {
                ini.addComment(comment);
            }
        }

        // グローバルセクション登録
        Section section = new Section(null, option_.isIgnoreCase());
        ini.addSection(section);

        List<String> comments = new ArrayList<String>();
        Parameter multiLineParameter = null;
        for (; num < lines.size(); num++) {
            String line = lines.get(num);

            // 複数行パラメータ検出済みの場合
            if (multiLineParameter != null) {
                String value = multiLineParameter.getValue() + option_.getMultiLineSeparator();

                // 文末が"\"の場合
                if (line.endsWith("\\")) {
                    multiLineParameter.setValue(value + line.substring(0, line.length() - 1));
                } else {
                    multiLineParameter.setValue(value + line);
                    multiLineParameter = null;
                }
                continue;
            }

            // 空行の場合
            if (isBlankLine(line)) {
                continue;
            }

            // コメント行の場合
            if (isCommentLine(line)) {
                comments.add(line);
                continue;
            }

            Section nextSection = parseSectionLine(line);
            // セクション行の場合
            if (nextSection != null) {
                section = addSection(ini, section, nextSection, comments);
                comments = new ArrayList<String>();
                continue;
            }

            Parameter parameter = parseParameterLine(line);
            // パラメータ行の場合
            if (parameter != null) {
                addParameter(section, parameter, comments);
                comments = new ArrayList<String>();

                // 複数行の値を許可する場合 かつ 文末が"\"の場合
                if (option_.isAllowMultiLine() &&
                        parameter.getValue().endsWith("\\")) {
                    String value = parameter.getValue();
                    parameter.setValue(value.substring(0, value.length() - 1));
                    multiLineParameter = parameter;
                }
                continue;
            }

            // 無効な行の存在をエラーとみなす場合は、例外をスローして終了
            if (option_.getUnknownLineBehavior() == IniFileReadOption.UnknownLineBehavior.ERROR) {
                throw new IOException("It contains a invalid line.(" + (num + 1) + ")");
            }
        }

        // グローバルセクションにキーが存在しない場合はデフォルトセクション削除
        Section globalSection = ini.getSection(null);
        if (globalSection.getParameters().isEmpty()) {
            ini.removeSection(null);

        // グローバルセクションの存在を許可しない場合
        } else if (!option_.isAllowGlobalSection()) {
            throw new IOException("Some parameter doesn't belong to any section.");
        }

        return ini;
    }

    private boolean isBlankLine(String line) {
        return trim(line).isEmpty();
    }

    private boolean isCommentLine(String line) {
        line = option_.isIgnoreWhiteSpace() ? trim(line) : line;
        for (char c : option_.getCommentChars()) {
            if (line.startsWith(String.valueOf(c))) {
                return true;
            }
        }
        return false;
    }

    private Section parseSectionLine(String line) {
        String validStr = extractValidStr(line);
        String comment = extractComment(line);

        validStr = option_.isIgnoreWhiteSpace() ? trim(validStr) : validStr;
        Matcher m = SECTION_PATTERN.matcher(validStr);
        if (m.find()) {
            Section section = new Section(m.group(1), option_.isIgnoreCase());
            if (comment != null) {
                section.setComment(comment);
            }
            return section;
        }
        return null;
    }

    private Parameter parseParameterLine(String line) {
        String validStr = extractValidStr(line);
        String comment = extractComment(line);

        int idx = indexOfDelimiter(validStr);
        if (-1 < idx) {
            String key = validStr.substring(0, idx);
            String value = validStr.substring(idx + 1);
            Parameter parameter = null;
            if (option_.isIgnoreWhiteSpace()) {
                parameter = new Parameter(trim(key), value);
            } else {
                parameter = new Parameter(key, value);
            }
            if (comment != null) {
                parameter.setComment(comment);
            }
            return parameter;
        }
        return null;
    }

    private int indexOfDelimiter(String str) {
        int index = -1;
        for (char c : option_.getDelimiters()) {
            int idx = str.indexOf(c);
            if (idx < 0) {
                continue;
            }
            if (index == -1 || idx < index) {
                index = idx;
            }
        }
        return index;
    }

    private Section addSection(IniFile ini, Section current, Section next, List<String> comments) throws IOException {
        // セクションが重複している場合
        if (ini.hasSection(next.getName())) {
            switch (option_.getDuplicateSectionBehavior()) {
                case ERROR:
                    throw new IOException(next.getName() + " section is duplicate.");
                case IGNORE_FORMER:
                    current = next;
                    setComment(current, comments);
                    ini.setSection(current);
                    break;
                case IGNORE_LATTER:
                    current = null;
                    break;
                case MERGE:
                    current = ini.getSection(next.getName());
                    for (String comment : comments) {
                        current.addComment(comment);
                    }
                    for (String comment : next.getComments()) {
                        current.addComment(comment);
                    }
                    break;
                default:
                    break;
            }
        } else {
            current = next;
            setComment(current, comments);
            ini.addSection(current);
        }
        return current;
    }

    private void addParameter(Section section, Parameter parameter, List<String> comments) throws IOException {
        // 無効なセクションの場合
        if (section == null) {
            return;
        }

        // キーが重複している場合
        if (section.hasParameter(parameter.getKey())) {
            switch (option_.getDuplicateKeyBehavior()) {
                case ERROR:
                    throw new IOException(parameter.getKey() + " key is duplicate.");
                case IGNORE_FORMER:
                    setComment(parameter, comments);
                    section.setParameter(parameter);
                    break;
                case IGNORE_LATTER:
                    break;
                case MULTI_VALUES:
                    Parameter p = section.getParameter(parameter.getKey());
                    p.addValue(parameter.getValue());
                    for (String comment : comments) {
                        p.addComment(comment);
                    }
                    for (String comment : parameter.getComments()) {
                        p.addComment(comment);
                    }
                    break;
                default:
                    break;
            }
        } else {
            setComment(parameter, comments);
            section.addParameter(parameter);
        }
    }

    private String extractValidStr(String line) {
        if (!option_.isAllowMiddleOfLineComment()) {
            return line;
        }
        int idx = indexOfCommentChar(line);
        return -1 < idx ? trimRight(line.substring(0, idx)) : line;
    }

    private String extractComment(String line) {
        if (!option_.isAllowMiddleOfLineComment()) {
            return null;
        }
        int idx = indexOfCommentChar(line);
        return -1 < idx ? line.substring(idx) : null;
    }

    private int indexOfCommentChar(String str) {
        int index = -1;
        for (char c : option_.getCommentChars()) {
            int idx = str.indexOf(c);
            if (idx < 0) {
                continue;
            }
            if (index == -1 || idx < index) {
                index = idx;
            }
        }
        return index;
    }

    private void setComment(Section section, List<String> comments) {
        if (section.hasComment()) {
            comments.addAll(section.getComments());
            section.clearComment();
        }
        for (String comment : comments) {
            section.addComment(comment);
        }
    }

    private void setComment(Parameter parameter, List<String> comments) {
        if (parameter.hasComment()) {
            comments.addAll(parameter.getComments());
            parameter.clearComment();
        }
        for (String comment : comments) {
            parameter.addComment(comment);
        }
    }

    private String trim(String str) {
        return str.replaceAll("^" + option_.getWhiteSpaceRegex(), "")
                .replaceAll(option_.getWhiteSpaceRegex() + "$", "");
    }

    private String trimRight(String str) {
        return str.replaceAll(option_.getWhiteSpaceRegex() + "$", "");
    }
}