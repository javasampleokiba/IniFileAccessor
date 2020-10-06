package javasampleokiba.inifileaccessor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * INIファイルの一つのセクション情報を格納するクラス.
 * 
 * <p>[特徴]</p>
 * <ul>
 * <li>下にセクション行が続くコメント行をそのセクションのコメントとして情報を持ちます。</li>
 * <li>パラメータを取得する際に、キーの大文字小文字を区別するか指定できます。</li>
 * <li>本クラスはスレッドセーフではありません。
 * </ul>
 * 
 * 下記例の場合、sectionは1-2行目のコメントと、key1、key2のパラメータを持ちます。
 * 
 * <p>[例]</p>
 * <pre>
 * 1: ;comment line 1
 * 2: ;comment line 2
 * 3: [section]
 * 4: key1=1
 * 5: key2=1
 * </pre>
 * 
 * @author javasampleokiba
 */
@SuppressWarnings("serial")
public class Section implements Serializable {

    private final String name_;
    private final List<String> comments_            = new ArrayList<String>();
    private final List<Parameter> parameters_       = new ArrayList<Parameter>();
    private final boolean ignoreCase_;

    /**
     * グローバルセクションを表す{@code Section}オブジェクトを構築します。
     * キーの大文字小文字は区別しません。
     */
    public Section() {
        this(null);
    }

    /**
     * 指定されたセクション名を持つ{@code Section}オブジェクトを構築します。
     * {@code name}に{@code null}を指定するとグローバルセクションとなります。
     * キーの大文字小文字は区別しません。
     * 
     * @param name  セクション名
     */
    public Section(String name) {
        this(name, true);
    }

    /**
     * 指定されたセクション名を持つ{@code Section}オブジェクトを構築します。
     * {@code name}に{@code null}を指定するとグローバルセクションとなります。
     * {@code ignoreCase}で、キーの大文字小文字を区別するか指定できます。
     * 
     * @param name        セクション名
     * @param ignoreCase  大文字小文字を無視するかの真偽値
     */
    public Section(String name, boolean ignoreCase) {
        name_ = name;
        ignoreCase_ = ignoreCase;
    }

    /**
     * 指定されたセクション名、コメントを持つ{@code Section}オブジェクトを構築します。
     * {@code name}に{@code null}を指定するとグローバルセクションとなります。
     * キーの大文字小文字は区別しません。
     * 
     * @param name     セクション名
     * @param comment  コメント
     * @throws NullPointerException  {@code comment}が{@code null}の場合
     */
    public Section(String name, String comment) {
        this(name, comment, true);
    }

    /**
     * 指定されたセクション名、コメントを持つ{@code Section}オブジェクトを構築します。
     * {@code name}に{@code null}を指定するとグローバルセクションとなります。
     * {@code ignoreCase}で、キーの大文字小文字を区別するか指定できます。
     * 
     * @param name        セクション名
     * @param comment     コメント
     * @param ignoreCase  大文字小文字を無視するかの真偽値
     * @throws NullPointerException  {@code comment}が{@code null}の場合
     */
    public Section(String name, String comment, boolean ignoreCase) {
        if (comment == null) throw new NullPointerException();
        name_ = name;
        comments_.add(comment);
        ignoreCase_ = ignoreCase;
    }

    /**
     * このセクションがグローバルセクションであるか判定します。
     * 
     * @return グローバルセクションの場合は {@code true}
     */
    public boolean isGlobalSection() {
        return name_ == null;
    }

    /**
     * セクション名を取得します。
     * 
     * @return セクション名
     */
    public String getName() {
        return name_;
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

    /**
     * 大文字小文字を無視するか判定します。
     * 
     * @return 大文字小文字を無視する場合は {@code true}
     */
    public boolean isIgnoreCase() {
        return ignoreCase_;
    }

    /**
     * このセクションが持つパラメータの数を取得します。
     * 
     * @return パラメータの数
     */
    public int countParameters() {
        return parameters_.size();
    }

    /**
     * 指定されたキーのパラメータが存在するか判定します。
     * 
     * @param key  キー
     * @return パラメータが存在する場合は {@code true}
     */
    public boolean hasParameter(String key) {
        return -1 < indexOfParameter(key);
    }

    /**
     * 指定されたキーのパラメータの位置インデックスを取得します。
     * パラメータが存在しない場合は-1を返します。
     * 
     * @param key  キー
     * @return パラメータの位置インデックス
     */
    public int indexOfParameter(String key) {
        for (int i = 0; i < parameters_.size(); i++) {
            String k = parameters_.get(i).getKey();
            if (ignoreCase_ ? k.equalsIgnoreCase(key) : k.equals(key)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 指定されたキーのパラメータを取得します。
     * パラメータが存在しない場合は{@code null}を返します。
     * 
     * @param key  キー
     * @return パラメータ
     */
    public Parameter getParameter(String key) {
        int idx = indexOfParameter(key);
        if (-1 < idx) {
            return parameters_.get(idx);
        }
        return null;
    }

    /**
     * このセクションが持つすべてのパラメータを取得します。
     * 
     * @return 全パラメータのリスト
     */
    public List<Parameter> getParameters() {
        return new ArrayList<Parameter>(parameters_);
    }

    /**
     * セクションの一番後ろにパラメータを追加します。
     * 
     * @param parameter  パラメータ
     * @throws NullPointerException      {@code parameter}が{@code null}の場合
     *         IllegalArgumentException  すでに同じキーのパラメータが存在する場合
     */
    public void addParameter(Parameter parameter) {
        addParameter(parameters_.size(), parameter);
    }

    /**
     * 指定された位置にパラメータを追加します。
     * 
     * @param index      追加する位置
     * @param parameter  パラメータ
     * @throws NullPointerException      {@code parameter}が{@code null}の場合
     *         IllegalArgumentException  すでに同じキーのパラメータが存在する場合
     */
    public void addParameter(int index, Parameter parameter) {
        if (parameter == null) {
            throw new NullPointerException();
        }
        if (hasParameter(parameter.getKey())) {
            throw new IllegalArgumentException("Same parameter already exists.(" + parameter.getKey() + ")");
        }
        parameters_.add(index, parameter);
    }

    /**
     * すでに存在するパラメータを更新します。
     * パラメータが存在しない場合は何もせず{@code null}を返します。
     * 
     * @param parameter  パラメータ
     * @return 更新する前のパラメータ。存在しない場合は{@code null}を返します。
     * @throws NullPointerException  {@code parameter}が{@code null}の場合
     */
    public Parameter setParameter(Parameter parameter) {
        if (parameter == null) {
            throw new NullPointerException();
        }
        int idx = indexOfParameter(parameter.getKey());
        if (-1 < idx) {
            return parameters_.set(idx, parameter);
        }
        return null;
    }

    /**
     * 指定されたキーのパラメータを削除します。
     * 
     * @param key  キー
     * @return 削除されたパラメータ。パラメータが存在しない場合は{@code null}を返します。
     */
    public Parameter removeParameter(String key) {
        int idx = indexOfParameter(key);
        if (-1 < idx) {
            return parameters_.remove(idx);
        }
        return null;
    }

    /**
     * このセクションが持つすべてのパラメータを削除します。
     */
    public void clearParameter() {
        parameters_.clear();
    }

    @Override
    public String toString() {
        return "Section [name=" + name_ + ", comments=" + comments_ + ", parameters=" + parameters_
                + ", ignoreCase=" + ignoreCase_ + "]";
    }
}