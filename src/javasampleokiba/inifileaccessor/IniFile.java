package javasampleokiba.inifileaccessor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * INIファイルの内容を格納するクラス.
 * 
 * <p>[特徴]</p>
 * <ul>
 * <li>ファイルの先頭から続くコメント行をINIファイルの(ヘッダ)コメントとして情報を持ちます。</li>
 * <li>セクションを取得する際に、セクション名の大文字小文字を区別するか指定できます。</li>
 * <li>本クラスはスレッドセーフではありません。
 * </ul>
 * 
 * 下記例の場合、{@code IniFile}オブジェクトは1-2行目のヘッダコメントと、4-7行目と8-10行目の2つのセクション情報を持ちます。<br>
 * 
 * <p>[例]</p>
 * <pre>
 *  1: ;header comment 1
 *  2: ;header comment 2
 *  3: 
 *  4: ;section comment
 *  5: [section1]
 *  6: key1=1
 *  7: key2=1
 *  8: [section2]
 *  9: ;key comment
 * 10: key1=ABC
 * </pre>
 * 
 * @author javasampleokiba
 */
@SuppressWarnings("serial")
public class IniFile implements Serializable {

    private final List<String> comments_       = new ArrayList<String>();
    private final List<Section> sections_      = new ArrayList<Section>();
    private final boolean ignoreCase_;

    /**
     * 何も情報を持たない{@code IniFile}オブジェクトを構築します。
     * セクション名の大文字小文字は区別しません。
     */
    public IniFile() {
        this(true);
    }

    /**
     * 何も情報を持たないオブジェクトを構築します。
     * {@code ignoreCase}で、セクション名の大文字小文字を区別するか指定できます。
     * 
     * @param ignoreCase  大文字小文字を無視するかの真偽値
     */
    public IniFile(boolean ignoreCase) {
        ignoreCase_ = ignoreCase;
    }

    /**
     * ヘッダコメントを持っているか判定します。
     * 
     * @return ヘッダコメントを持つ場合は {@code true}
     */
    public boolean hasComment() {
        return !comments_.isEmpty();
    }

    /**
     * ヘッダコメントを取得します。
     * 
     * @return ヘッダコメント
     */
    public List<String> getComments() {
        return new ArrayList<String>(comments_);
    }

    /**
     * ヘッダコメントを設定します。
     * 
     * @param comment  ヘッダコメント
     * @throws NullPointerException  {@code comment}がnullの場合
     */
    public void setComment(String comment) {
        if (comment == null) throw new NullPointerException();
        comments_.clear();
        comments_.add(comment);
    }

    /**
     * 現在保持しているヘッダコメントにコメントを追加します。
     * 複数回実行することで複数行のヘッダコメントになります。
     * 
     * @param comment  追加するコメント
     * @throws NullPointerException  {@code comment}がnullの場合
     */
    public void addComment(String comment) {
        if (comment == null) throw new NullPointerException();
        comments_.add(comment);
    }

    /**
     * ヘッダコメントをクリアします。
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
     * 指定された名前のセクションが存在するか判定します。
     * グローバルセクションの存在判定の場合は{@code null}を指定します。
     * 
     * @param name  セクション名
     * @return セクションが存在する場合は {@code true}
     */
    public boolean hasSection(String name) {
        return -1 < indexOfSection(name);
    }

    /**
     * 指定された名前のセクションの位置インデックスを取得します。
     * グローバルセクションの場合は{@code null}を指定します（存在する場合は常に0を返します）。
     * セクションが存在しない場合は-1を返します。
     * 
     * @param name  セクション名
     * @return セクションの位置インデックス
     */
    public int indexOfSection(String name) {
        if (name == null) {
            for (int i = 0; i < sections_.size(); i++) {
                if (sections_.get(i).getName() == null) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < sections_.size(); i++) {
                String n = sections_.get(i).getName();
                if (ignoreCase_ ? name.equalsIgnoreCase(n) : name.equals(n)) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * 指定された名前のセクションを取得します。
     * グローバルセクションを取得する場合は{@code null}を指定します。
     * セクションが存在しない場合は{@code null}を返します。
     * 
     * @param name  セクション名
     * @return セクション
     */
    public Section getSection(String name) {
        int idx = indexOfSection(name);
        if (-1 < idx) {
            return sections_.get(idx);
        }
        return null;
    }

    /**
     * すべてのセクションを取得します。
     * 
     * @return 全セクションのリスト
     */
    public List<Section> getSections() {
        return new ArrayList<Section>(sections_);
    }

    /**
     * INIファイルの一番後ろにセクションを追加します。
     * 
     * @param section  セクション
     * @throws NullPointerException      {@code section}が{@code null}の場合
     *         IllegalArgumentException  すでに同じ名前のセクションが存在する場合
     */
    public void addSection(Section section) {
        addSection(sections_.size(), section);
    }

    /**
     * 指定された位置にセクションを追加します。
     * 
     * @param index    追加する位置
     * @param section  セクション
     * @throws NullPointerException      {@code section}が{@code null}の場合
     *         IllegalArgumentException  すでに同じ名前のセクションが存在する場合
     */
    public void addSection(int index, Section section) {
        if (section == null) {
            throw new NullPointerException();
        }
        if (hasSection(section.getName())) {
            throw new IllegalArgumentException("Same section already exists.(" + section.getName() + ")");
        }
        sections_.add(index, section);
    }

    /**
     * すでに存在するセクションを更新します。
     * セクションが存在しない場合は何もせず{@code null}を返します。
     * 
     * @param section  セクション
     * @return 更新する前のセクション。存在しない場合は{@code null}を返します。
     * @throws NullPointerException  {@code section}が{@code null}の場合
     */
    public Section setSection(Section section) {
        if (section == null) {
            throw new NullPointerException();
        }
        int idx = indexOfSection(section.getName());
        if (-1 < idx) {
            return sections_.set(idx, section);
        }
        return null;
    }

    /**
     * 指定された名前のセクションを削除します。
     * 
     * @param name  セクション名
     * @return 削除されたセクション。セクションが存在しない場合は{@code null}を返します。
     */
    public Section removeSection(String name) {
        int idx = indexOfSection(name);
        if (-1 < idx) {
            return sections_.remove(idx);
        }
        return null;
    }

    /**
     * すべてのセクションを削除します。
     */
    public void clearSection() {
        sections_.clear();
    }

    @Override
    public String toString() {
        return "IniFile [comments=" + comments_ + ", sections=" + sections_ + ", ignoreCase=" + ignoreCase_ + "]";
    }
}