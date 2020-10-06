package javasampleokiba.inifileaccessor;

/**
 * 改行コード列挙クラス
 * 
 * @author javasampleokiba
 */
public enum LineSeparator {
    SYSTEM_DEFAULT	(System.lineSeparator()),
    CRLF			("\r\n"),
    LF				("\n"),
    ;

    private final String str_;

    private LineSeparator(String str) {
        str_ = str;
    }

    @Override
    public String toString() {
        return str_;
    }
}