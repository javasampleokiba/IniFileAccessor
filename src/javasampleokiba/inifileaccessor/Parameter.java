package javasampleokiba.inifileaccessor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * INI�t�@�C���̈�̃p�����[�^�����i�[����N���X.
 * 
 * <p>[����]</p>
 * <ul>
 * <li>�����̒l�ɑΉ����Ă��܂��B</li>
 * <li>���Ƀp�����[�^�s�������R�����g�s�����̃p�����[�^�̃R�����g�Ƃ��ď��������܂��B</li>
 * <li>�{�N���X�̓X���b�h�Z�[�t�ł͂���܂���B
 * </ul>
 * 
 * ���L��̏ꍇ�Akey��2-3�s�ڂ̃R�����g�ƁA[1, 2, 3]�̕����̒l�������܂��B<br>
 * 
 * <p>[��]</p>
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
     * �w�肳�ꂽ�L�[�ƒl������{@code Parameter}�I�u�W�F�N�g���\�z���܂��B
     * 
     * @param key    �L�[
     * @param value  �l
     * @throws NullPointerException  {@code key}��{@code value}��{@code null}�̏ꍇ
     */
    public Parameter(String key, String value) {
        if (key == null || value == null) throw new NullPointerException();
        key_ = key;
        values_.add(value);
    }

    /**
     * �w�肳�ꂽ�L�[�A�l�A�R�����g�����I�u�W�F�N�g���\�z���܂��B
     * 
     * @param key      �L�[
     * @param value    �l
     * @param comment  �R�����g
     * @throws NullPointerException  {@code key}��{@code value}��{@code comment}��null�̏ꍇ
     */
    public Parameter(String key, String value, String comment) {
        if (key == null || value == null || comment == null) throw new NullPointerException();
        key_ = key;
        values_.add(value);
        comments_.add(comment);
    }

    /**
     * �L�[���擾���܂��B
     * 
     * @return �L�[
     */
    public String getKey() {
        return key_;
    }

    /**
     * �����̒l���������肵�܂��B
     * 
     * @return �����̒l�����ꍇ�� {@code true}
     */
    public boolean hasMultiValues() {
        return 1 < values_.size();
    }

    /**
     * �l���擾���܂��B
     * �����̒l�����ꍇ�͐擪�̒l��Ԃ��܂��B
     * 
     * @return �l
     */
    public String getValue() {
        return values_.get(0);
    }

    /**
     * �����̒l���ׂĂ��擾���܂��B
     * 
     * @return �l�̃��X�g
     */
    public List<String> getValues() {
        return new ArrayList<String>(values_);
    }

    /**
     * �l��ݒ肵�܂��B
     * �����̒l�������Ă����ꍇ�͒P��̒l�Ƀ��Z�b�g����܂��B
     * 
     * @param value  �l
     * @throws NullPointerException  {@code value}��{@code null}�̏ꍇ
     */
    public void setValue(String value) {
        if (value == null) throw new NullPointerException();
        values_.clear();
        values_.add(value);
    }

    /**
     * �l��ǉ����܂��B
     * 
     * @param value  �l
     * @throws NullPointerException  {@code value}��{@code null}�̏ꍇ
     */
    public void addValue(String value) {
        if (value == null) throw new NullPointerException();
        values_.add(value);
    }

    /**
     * �l���N���A�i�󕶎��ɐݒ�j���܂��B
     */
    public void clearValue() {
        values_.clear();
        values_.add("");
    }

    /**
     * �R�����g�������Ă��邩���肵�܂��B
     * 
     * @return �R�����g�����ꍇ�� {@code true}
     */
    public boolean hasComment() {
        return !comments_.isEmpty();
    }

    /**
     * �R�����g���擾���܂��B
     * 
     * @return �R�����g
     */
    public List<String> getComments() {
        return new ArrayList<String>(comments_);
    }

    /**
     * �R�����g��ݒ肵�܂��B
     * 
     * @param comment  �R�����g
     * @throws NullPointerException  {@code comment}��{@code null}�̏ꍇ
     */
    public void setComment(String comment) {
        if (comment == null) throw new NullPointerException();
        comments_.clear();
        comments_.add(comment);
    }

    /**
     * ���ݕێ����Ă���R�����g�ɃR�����g��ǉ����܂��B
     * ��������s���邱�Ƃŕ����s�̃R�����g�ɂȂ�܂��B
     * 
     * @param comment  �R�����g
     * @throws NullPointerException  {@code comment}��{@code null}�̏ꍇ
     */
    public void addComment(String comment) {
        if (comment == null) throw new NullPointerException();
        comments_.add(comment);
    }

    /**
     * �R�����g���N���A���܂��B
     */
    public void clearComment() {
        comments_.clear();
    }

    @Override
    public String toString() {
        return "Parameter [key=" + key_ + ", comments=" + comments_ + ", values=" + values_ + "]";
    }
}