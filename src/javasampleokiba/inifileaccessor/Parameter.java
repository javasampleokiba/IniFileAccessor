package javasampleokiba.inifileaccessor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * INIファイルの一つのパラメータ情報を格納するクラス.
 * 
 * <p>[特徴]</p>
 * <ul>
 * <li>複数の値に対応しています。</li>
 * <li>下にパラメータ行が続くコメント行をそのパラメータのコメントとして情報を持ちます。</li>
 * <li>本クラスはスレッドセーフではありません。
 * </ul>
 * 
 * 下記例の場合、keyは2-3行目のコメントと、[1, 2, 3]の複数の値を持ちます。<br>
 * 
 * <p>[例]</p>
 * <pre>
 * 1: [section]
 * 2: ;comment line 1
 * 3: ;comment line 2
 * 4: key=1
 * 5: key=2
 * 6: key=3
 * </pre>
 * 
 * @author javasampleokiba
 */
@SuppressWarnings("serial")
public class Parameter implements Serializable {

    private final String key_;
    private final List<String> comments_        = new ArrayList<String>();
    private final List<String> values_          = new ArrayList<String>();

    /**
     * 指定されたキーと値を持つ{@code Parameter}オブジェクトを構築します。
     * 
     * @param key    キー
     * @param value  値
     * @throws NullPointerException  {@code key}か{@code value}が{@code null}の場合
     */
    public Parameter(String key, String value) {
        if (key == null || value == null) throw new NullPointerException();
        key_ = key;
        values_.add(value);
    }

    /**
     * 指定されたキー、値、コメントを持つオブジェクトを構築します。
     * 
     * @param key      キー
     * @param value    値
     * @param comment  コメント
     * @throws NullPointerException  {@code key}か{@code value}か{@code comment}がnullの場合
     */
    public Parameter(String key, String value, String comment) {
        if (key == null || value == null || comment == null) throw new NullPointerException();
        key_ = key;
        values_.add(value);
        comments_.add(comment);
    }

    /**
     * キーを取得します。
     * 
     * @return キー
     */
    public String getKey() {
        return key_;
    }

    /**
     * 複数の値を持つか判定します。
     * 
     * @return 複数の値を持つ場合は {@code true}
     */
    public boolean hasMultiValues() {
        return 1 < values_.size();
    }

    /**
     * 値を取得します。
     * 複数の値を持つ場合は先頭の値を返します。
     * 
     * @return 値
     */
    public String getValue() {
        return values_.get(0);
    }

    /**
     * 複数の値すべてを取得します。
     * 
     * @return 値のリスト
     */
    public List<String> getValues() {
        return new ArrayList<String>(values_);
    }

    /**
     * 値を設定します。
     * 複数の値を持っていた場合は単一の値にリセットされます。
     * 
     * @param value  値
     * @throws NullPointerException  {@code value}が{@code null}の場合
     */
    public void setValue(String value) {
        if (value == null) throw new NullPointerException();
        values_.clear();
        values_.add(value);
    }

    /**
     * 値を追加します。
     * 
     * @param value  値
     * @throws NullPointerException  {@code value}が{@code null}の場合
     */
    public void addValue(String value) {
        if (value == null) throw new NullPointerException();
        values_.add(value);
    }

    /**
     * 値をクリア（空文字に設定）します。
     */
    public void clearValue() {
        values_.clear();
        values_.add("");
    }

    /**
     * コメントを持っているか判定します。
     * 
     * @return コメントを持つ場合は {@code true}
     */
    public boolean hasComment() {
        return !comments_.isEmpty();
    }

    /**
     * コメントを取得します。
     * 
     * @return コメント
     */
    public List<String> getComments() {
        return new ArrayList<String>(comments_);
    }

    /**
     * コメントを設定します。
     * 
     * @param comment  コメント
     * @throws NullPointerException  {@code comment}が{@code null}の場合
     */
    public void setComment(String comment) {
        if (comment == null) throw new NullPointerException();
        comments_.clear();
        comments_.add(comment);
    }

    /**
     * 現在保持しているコメントにコメントを追加します。
     * 複数回実行することで複数行のコメントになります。
     * 
     * @param comment  コメント
     * @throws NullPointerException  {@code comment}が{@code null}の場合
     */
    public void addComment(String comment) {
        if (comment == null) throw new NullPointerException();
        comments_.add(comment);
    }

    /**
     * コメントをクリアします。
     */
    public void clearComment() {
        comments_.clear();
    }

    @Override
    public String toString() {
        return "Parameter [key=" + key_ + ", comments=" + comments_ + ", values=" + values_ + "]";
    }
}