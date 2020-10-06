package javasampleokiba.inifileaccessor;

/**
 * INIファイル書き込みの各種オプションを設定するクラス
 * 
 * @author javasampleokiba
 */
public class IniFileWriteOption {

    /** キーと値のデリミタ文字 */
    private char delimiter_                     = '=';
    /** 各セクション間に挿入する空行数 */
    private int blankLineBetweenSection_        = 1;
    /** 各パラメータ間に挿入する空行数 */
    private int blankLineBetweenParameter_      = 0;

    /**
     * キーと値を区切るデリミタ文字を取得します。
     * デフォルトは、'='です。
     * 
     * @return デリミタ文字
     */
    public char getDelimiter() {
        return delimiter_;
    }

    /**
     * キーと値を区切るデリミタ文字を設定します。
     * 
     * @param delimiter  デリミタ文字
     * @return このオブジェクトへの参照
     */
    public IniFileWriteOption setDelimiter(char delimiter) {
        delimiter_ = delimiter;
        return this;
    }

    /**
     * 各セクション間に挿入する空行数を取得します。
     * デフォルトは、1です。
     * 
     * @return 各セクション間に挿入する空行数
     */
    public int getBlankLineBetweenSection() {
        return blankLineBetweenSection_;
    }

    /**
     * 各セクション間に挿入する空行数を設定します。
     * 
     * @param blankLineBetweenSection  空行数
     * @return このオブジェクトへの参照
     * @throws IllegalArgumentException  {@code blankLineBetweenSection}が0未満の場合
     */
    public IniFileWriteOption setBlankLineBetweenSection(int blankLineBetweenSection) {
        if (blankLineBetweenSection < 0) {
            throw new IllegalArgumentException("blankLineBetweenSection < 0");
        }
        blankLineBetweenSection_ = blankLineBetweenSection;
        return this;
    }

    /**
     * 各パラメータ間に挿入する空行数を取得します。
     * デフォルトは、0です。
     * 
     * @return 各パラメータ間に挿入する空行数
     */
    public int getBlankLineBetweenParameter() {
        return blankLineBetweenParameter_;
    }

    /**
     * 各パラメータ間に挿入する空行数を設定します。
     * 
     * @param blankLineBetweenParameter  空行数
     * @return このオブジェクトへの参照
     * @throws IllegalArgumentException  {@code blankLineBetweenParameter}が0未満の場合
     */
    public IniFileWriteOption setBlankLineBetweenParameter(int blankLineBetweenParameter) {
        if (blankLineBetweenParameter < 0) {
            throw new IllegalArgumentException("blankLineBetweenParameter < 0");
        }
        blankLineBetweenParameter_ = blankLineBetweenParameter;
        return this;
    }
}