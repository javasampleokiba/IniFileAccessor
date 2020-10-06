package javasampleokiba.inifileaccessor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * INIファイル読み込みの各種オプションを設定するクラス
 * 
 * @author javasampleokiba
 */
public class IniFileReadOption {

    /**
     * 不明な行を検出したときの振る舞い列挙クラス
     */
    public enum UnknownLineBehavior {
        /** 例外をスローする */
        ERROR,
        /** 無視する */
        IGNORE,
    }

    /**
     * セクション名が重複したときの振る舞い列挙クラス
     */
    public enum DuplicateSectionBehavior {
        /** 例外をスローする */
        ERROR,
        /** 前のセクションを無視する */
        IGNORE_FORMER,
        /** 後のセクションを無視する */
        IGNORE_LATTER,
        /** マージする */
        MERGE,
    }

    /**
     * キーが重複したときの振る舞い列挙クラス
     */
    public enum DuplicateKeyBehavior {
        /** 例外をスローする */
        ERROR,
        /** 前のキーを無視する */
        IGNORE_FORMER,
        /** 後のキーを無視する */
        IGNORE_LATTER,
        /** 複数の値とみなす */
        MULTI_VALUES,
    }

    /*
     * 全体設定
     */
    /** 空白文字を無視するか */
    private boolean ignoreWhiteSpace_                               = false;
    /** 空白文字列を表す正規表現 */
    private String whiteSpaceRegex_                                 = "\\s+";
    /** 大文字小文字を無視するか */
    private boolean ignoreCase_                                     = true;
    /** 不明な行を検出したときの振る舞い */
    private UnknownLineBehavior unknownLineBehavior_                = UnknownLineBehavior.ERROR;

    /*
     * セクション・パラメータに関する設定
     */
    /** グローバルセクションの存在を許可するか */
    private boolean allowGlobalSection_                             = false;
    /** キーと値のデリミタ文字 */
    private final List<Character> delimiters_                       = new ArrayList<Character>();
    /** 複数行の値を許可するか */
    private boolean allowMultiLine_                                 = false;
    /** 複数行の値の改行コード */
    private LineSeparator multiLineSeparator_                       = LineSeparator.SYSTEM_DEFAULT;
    /** セクション名が重複したときの振る舞い */
    private DuplicateSectionBehavior duplicateSectionBehavior_      = DuplicateSectionBehavior.ERROR;
    /** キーが重複したときの振る舞い */
    private DuplicateKeyBehavior duplicateKeyBehavior_              = DuplicateKeyBehavior.ERROR;

    /*
     * コメントに関する設定
     */
    /** ヘッダコメントを持つINIファイルか */
    private boolean hasHeaderComment_                               = false;
    /** コメントの開始を表す文字 */
    private final List<Character> commentChars_                     = new ArrayList<Character>();
    /** 行の途中からのコメントを許可するか */
    private boolean allowMiddleOfLineComment_                       = false;

    /**
     * デフォルトの設定を持った{@code IniFileReadOption}オブジェクトを構築します。
     */
    public IniFileReadOption() {
        commentChars_.add(';');
        delimiters_.add('=');
    }

    /**
     * コメント行の先頭、セクション行の前後、キーおよび値の前後の空白文字を無視するか判定します。
     * デフォルトは、無視しません。
     * 
     * @return 空白文字を無視する場合は {@code true}
     */
    public boolean isIgnoreWhiteSpace() {
        return ignoreWhiteSpace_;
    }

    /**
     * コメント行の先頭、セクション行の前後、キーの前後の空白文字を無視するか設定します。
     * 
     * <p>[コメント行の例]</p>
     * 無視する場合、下記の1行目もコメント行とみなされます。
     * コメント文字の前の空白文字は取り除きません。
     * <pre>
     * 1:     ;comment line
     * 2: ;comment line
     * </pre>
     * 
     * <p>[セクション行の例]</p>
     * 無視する場合、下記の1行目のセクション行は2行目と同じとみなされます。
     * セクション名の前後の空白文字はセクション名の一部とみなされるため無視しません。
     * <pre>
     * 1:     [  section  ]    
     * 2: [  section  ]
     * </pre>
     * 
     * <p>[パラメータ行の例]</p>
     * 無視する場合、キーの前後の空白文字は取り除かれ、
     * 下記の1行目のキー名は2行目と同じとみなされ ます。
     * 値の前後の空白文字は取り除きません（空白文字も値の一部と認識します）。
     * <pre>
     * 1:     key    =    value    
     * 2: key=    value    
     * </pre>
     * 
     * @param ignoreWhiteSpace  空白文字を無視するかの真偽値
     * @return このオブジェクトへの参照
     */
    public IniFileReadOption setIgnoreWhiteSpace(boolean ignoreWhiteSpace) {
        ignoreWhiteSpace_ = ignoreWhiteSpace;
        return this;
    }

    /**
     * 空白文字列を表す正規表現を取得します。
     * デフォルトは、"\\s+"です。
     * 
     * @return 空白文字列を表す正規表現
     */
    public String getWhiteSpaceRegex() {
        return whiteSpaceRegex_;
    }

    /**
     * 空白文字列を表す正規表現を設定します。
     * 
     * @param whiteSpaceRegex  空白文字列を表す正規表現
     * @return このオブジェクトへの参照
     */
    public IniFileReadOption setWhiteSpaceRegex(String whiteSpaceRegex) {
        // 正規表現の構文チェック
        Pattern.compile(whiteSpaceRegex);
        whiteSpaceRegex_ = whiteSpaceRegex;
        return this;
    }

    /**
     * 大文字小文字を無視するか判定します。
     * デフォルトは、無視します。
     * 
     * @return 大文字小文字を無視する場合はtrue
     */
    public boolean isIgnoreCase() {
        return ignoreCase_;
    }

    /**
     * 大文字小文字を無視するか設定します。
     * 
     * <p>[セクション行の例]</p>
     * 無視する場合、下記の1行目と2行目のセクションは同じで、重複しているとみなされます。
     * <pre>
     * 1: [section]    
     * 2: [SECTION]
     * </pre>
     * 
     * <p>[パラメータ行の例]</p>
     * 無視する場合、下記の1行目と2行目のキーは同じで、重複しているとみなされます。
     * <pre>
     * 1: Key=1
     * 2: key=2
     * </pre>
     * 
     * @param ignoreCase  大文字小文字を無視するかの真偽値
     * @return このオブジェクトへの参照
     */
    public IniFileReadOption setIgnoreCase(boolean ignoreCase) {
        ignoreCase_ = ignoreCase;
        return this;
    }

    /**
     * 不明な行を検出したときの振る舞いを取得します。
     * デフォルトは、{@code ERROR}です。
     * 
     * @return 不明な行を検出したときの振る舞い
     */
    public UnknownLineBehavior getUnknownLineBehavior() {
        return unknownLineBehavior_;
    }

    /**
     * 不明な行を検出したときの振る舞いを設定します。
     * 不明な行とは、空白行、コメント行、セクション行、パラメータ行（複数行の値を許可する場合は値の行含む）
     * 以外の行を指します。
     * <ul>
     * <li>ERROR  : 例外（{@code IOException}）をスローします。</li>
     * <li>IGNORE : その行を無視します。</li>
     * </ul>
     * 
     * @param unknownLineBehavior  不明な行を検出したときの振る舞い
     * @return このオブジェクトへの参照
     * @throws NullPointerException  {@code unknownLineBehavior}が{@code null}の場合
     */
    public IniFileReadOption setUnknownLineBehavior(UnknownLineBehavior unknownLineBehavior) {
        if (unknownLineBehavior == null) throw new NullPointerException();
        unknownLineBehavior_ = unknownLineBehavior;
        return this;
    }

    /**
     * グローバルセクションの存在を許可するか判定します。
     * デフォルトは、許可しません。
     * 
     * @return グローバルセクションの存在を許可する場合は {@code true}
     */
    public boolean isAllowGlobalSection() {
        return allowGlobalSection_;
    }

    /**
     * グローバルセクションの存在を許可するか設定します。
     * グローバルセクションとは、いずれのセクションにも属さない、INIファイルの先頭に定義されたパラメータを持つ
     * 無名のセクションです。
     * 
     * <p>[例]</p>
     * 許可する場合は、key1、key2がグローバルセクションに属します。許可しない場合は、不明な行とみなされます。
     * <pre>
     * 1: key1=1
     * 2: key2=2
     * 3: [section]
     * 4: key3=3
     * </pre>
     * 
     * @param allowGlobalSection  グローバルセクションの存在を許可するかの真偽値
     * @return このオブジェクトへの参照
     */
    public IniFileReadOption setAllowGlobalSection(boolean allowGlobalSection) {
        allowGlobalSection_ = allowGlobalSection;
        return this;
    }

    /**
     * パラメータをキーと値に区切るデリミタ文字を取得します。
     * デフォルトは、'='です。
     * 
     * @return デリミタ文字のリスト
     */
    public List<Character> getDelimiters() {
        return delimiters_;
    }

    /**
     * パラメータをキーと値に区切るデリミタ文字を設定します。
     * 複数のデリミタ文字を混在させることも可能です。
     * 
     * @param delimiters  デリミタ文字
     * @return このオブジェクトへの参照
     */
    public IniFileReadOption setDelimiters(char... delimiters) {
        delimiters_.clear();
        for (char c : delimiters) {
            delimiters_.add(c);
        }
        return this;
    }

    /**
     * パラメータが複数行の値を持つことを許可するか判定します。
     * デフォルトは、許可しません。
     * 
     * @return パラメータが複数行の値を持つことを許可する場合は {@code true}
     */
    public boolean isAllowMultiLine() {
        return allowMultiLine_;
    }

    /**
     * パラメータが複数行の値を持つことを許可するか設定します。
     * パラメータ行の末尾に"\"を置くことで、値が複数行であることを示し、
     * 次の行全体が値の一部とみなされます。
     * 
     * <p>[例]</p>
     * 下記の場合、3行目までがkeyの値とみなされます。
     * <pre>
     * 1: key=123\
     * 2: ;456\
     * 3: [789]
     * </pre>
     * 
     * @param allowMultiLine  複数行の値を持つことを許可するかの真偽値
     * @return このオブジェクトへの参照
     */
    public IniFileReadOption setAllowMultiLine(boolean allowMultiLine) {
        allowMultiLine_ = allowMultiLine;
        return this;
    }

    /**
     * 複数行の値の改行コードを取得します。
     * デフォルトは、システムデフォルトの改行コードです。
     * 
     * @return 複数行の値の改行コード
     */
    public LineSeparator getMultiLineSeparator() {
        return multiLineSeparator_;
    }

    /**
     * 複数行の値の改行コードを設定します。
     * ※読み込んだ値に付加する改行コードで、ファイル内の実際の改行コードと一致している必要はありません。
     * 
     * @param multiLineSeparator  複数行の値の改行コード
     * @return このオブジェクトへの参照
     * @throws NullPointerException  {@code multiLineSeparator}が{@code null}の場合
     */
    public IniFileReadOption setMultiLineSeparator(LineSeparator multiLineSeparator) {
        if (multiLineSeparator == null) throw new NullPointerException();
        multiLineSeparator_ = multiLineSeparator;
        return this;
    }

    /**
     * セクション名が重複したときの振る舞いを取得します。
     * デフォルトは、{@code ERROR}です。
     * 
     * @return セクション名が重複したときの振る舞い
     */
    public DuplicateSectionBehavior getDuplicateSectionBehavior() {
        return duplicateSectionBehavior_;
    }

    /**
     * セクション名が重複したときの振る舞いを設定します。
     * 
     * <ul>
     * <li>ERROR         : 例外（{@code IOException}）をスローします。</li>
     * <li>IGNORE_FORMER : 先に検出したセクションを無視します。</li>
     * <li>IGNORE_LATTER : 後に検出したセクションを無視します。</li>
     * <li>MERGE         : 重複したセクションの持つ情報をマージします。</li>
     * </ul>
     * 
     * <p>[MERGEの例]</p>
     * 下記の場合、sectionはkey1、key2、key3、key4のパラメータを持ちます。
     * <pre>
     * 1: [section]
     * 2: key1=1
     * 3: key2=2
     * 4:
     * 5: [section]
     * 6: key3=3
     * 7: key4=4
     * </pre>
     * 
     * @param duplicateSectionBehavior  セクション名が重複したときの振る舞い
     * @return このオブジェクトへの参照
     * @throws NullPointerException  {@code duplicateSectionBehavior}が{@code null}の場合
     */
    public IniFileReadOption setDuplicateSectionBehavior(DuplicateSectionBehavior duplicateSectionBehavior) {
        if (duplicateSectionBehavior == null) throw new NullPointerException();
        duplicateSectionBehavior_ = duplicateSectionBehavior;
        return this;
    }

    /**
     * セクション内でキーが重複したときの振る舞いを取得します。
     * デフォルトは、{@code ERROR}です。
     * 
     * @return セクション内でキーが重複したときの振る舞い
     */
    public DuplicateKeyBehavior getDuplicateKeyBehavior() {
        return duplicateKeyBehavior_;
    }

    /**
     * セクション内でキーが重複したときの振る舞いを設定します。
     * 
     * <ul>
     * <li>ERROR         : 例外（{@code IOException}）をスローします。</li>
     * <li>IGNORE_FORMER : 先に検出したキーを無視します。</li>
     * <li>IGNORE_LATTER : 後に検出したキーを無視します。</li>
     * <li>MULTI_VALUES  : 複数の値を持つキーとみなします。</li>
     * </ul>
     * 
     * <p>[MULTI_VALUESの例]</p>
     * 下記の場合、key1は1と3の値を持ちます。
     * <pre>
     * 1: [section]
     * 2: key1=1
     * 3: key2=2
     * 4: key1=3
     * </pre>
     * 
     * @param duplicateKeyBehavior  セクション内でキーが重複したときの振る舞い
     * @return このオブジェクトへの参照
     * @throws NullPointerException  {@code duplicateKeyBehavior}が{@code null}の場合
     */
    public IniFileReadOption setDuplicateKeyBehavior(DuplicateKeyBehavior duplicateKeyBehavior) {
        if (duplicateKeyBehavior == null) throw new NullPointerException();
        duplicateKeyBehavior_ = duplicateKeyBehavior;
        return this;
    }

    /**
     * ヘッダコメントを持つINIファイルか判定します。
     * デフォルトは、持ちません。
     * 
     * @return ヘッダコメントを持つINIファイルの場合 {@code true}
     */
    public boolean hasHeaderComment() {
        return hasHeaderComment_;
    }

    /**
     * ヘッダコメントを持つINIファイルかを設定します。
     * {@code true}の場合、ファイルの先頭から続くコメント行をINIファイルのヘッダコメントとみなします。
     * 
     * <p>[例]</p>
     * {@code true}の場合、下記は1-2行目をINIファイルのヘッダコメントとみなします。
     * <pre>
     * 1: ;header comment 1
     * 2: ;header comment 2
     * 3: 
     * 4: ;section comment
     * 5: [section]
     * </pre>
     * 
     * @param hasHeaderComment  ヘッダコメントを持つINIファイルかどうかの真偽値
     * @return このオブジェクトへの参照
     */
    public IniFileReadOption setHasHeaderComment(boolean hasHeaderComment) {
        hasHeaderComment_ = hasHeaderComment;
        return this;
    }

    /**
     * コメントの開始を表す文字を取得します。
     * デフォルトは、';'です。
     * 
     * @return コメントの開始を表す文字のリスト
     */
    public List<Character> getCommentChars() {
        return commentChars_;
    }

    /**
     * コメントの開始を表す文字を設定します。
     * 複数の文字を混在させることも可能です。
     * 
     * @param commentChars  コメントの開始を表す文字
     * @return このオブジェクトへの参照
     */
    public IniFileReadOption setCommentChars(char... commentChars) {
        commentChars_.clear();
        for (char c : commentChars) {
            commentChars_.add(c);
        }
        return this;
    }

    /**
     * 行の途中からのコメントを許可するか判定します。
     * デフォルトは、許可しません。
     * 
     * @return 行の途中からのコメントを許可する場合は {@code true}
     */
    public boolean isAllowMiddleOfLineComment() {
        return allowMiddleOfLineComment_;
    }

    /**
     * 行の途中からのコメントを許可するか設定します。
     * 許可する場合、値などにコメント開始文字が使われていると、その位置からコメントとみなされるので注意が必要です。
     * 
     * <p>[例]</p>
     * 下記の場合、";section comment"がsectionのコメント、
     * ";parameter comment"がkey1のコメント、"abc"が値、
     * ";abc    ;parameter comment"がkey2のコメント、""が値になります。
     * <pre>
     * 1: [section]    ;section comment
     * 2: key1=abc     ;parameter comment
     * 3: key2=;abc    ;parameter comment
     * </pre>
     * 
     * @param allowMiddleOfLineComment  行の途中からのコメントを許可するかの真偽値
     * @return このオブジェクトへの参照
     */
    public IniFileReadOption setAllowMiddleOfLineComment(boolean allowMiddleOfLineComment) {
        allowMiddleOfLineComment_ = allowMiddleOfLineComment;
        return this;
    }
}