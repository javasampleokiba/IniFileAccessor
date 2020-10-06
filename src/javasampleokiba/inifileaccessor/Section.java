package javasampleokiba.inifileaccessor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * INI�t�@�C���̈�̃Z�N�V���������i�[����N���X.
 * 
 * <p>[����]</p>
 * <ul>
 * <li>���ɃZ�N�V�����s�������R�����g�s�����̃Z�N�V�����̃R�����g�Ƃ��ď��������܂��B</li>
 * <li>�p�����[�^���擾����ۂɁA�L�[�̑啶������������ʂ��邩�w��ł��܂��B</li>
 * <li>�{�N���X�̓X���b�h�Z�[�t�ł͂���܂���B
 * </ul>
 * 
 * ���L��̏ꍇ�Asection��1-2�s�ڂ̃R�����g�ƁAkey1�Akey2�̃p�����[�^�������܂��B
 * 
 * <p>[��]</p>
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
     * �O���[�o���Z�N�V������\��{@code Section}�I�u�W�F�N�g���\�z���܂��B
     * �L�[�̑啶���������͋�ʂ��܂���B
     */
    public Section() {
        this(null);
    }

    /**
     * �w�肳�ꂽ�Z�N�V������������{@code Section}�I�u�W�F�N�g���\�z���܂��B
     * {@code name}��{@code null}���w�肷��ƃO���[�o���Z�N�V�����ƂȂ�܂��B
     * �L�[�̑啶���������͋�ʂ��܂���B
     * 
     * @param name  �Z�N�V������
     */
    public Section(String name) {
        this(name, true);
    }

    /**
     * �w�肳�ꂽ�Z�N�V������������{@code Section}�I�u�W�F�N�g���\�z���܂��B
     * {@code name}��{@code null}���w�肷��ƃO���[�o���Z�N�V�����ƂȂ�܂��B
     * {@code ignoreCase}�ŁA�L�[�̑啶������������ʂ��邩�w��ł��܂��B
     * 
     * @param name        �Z�N�V������
     * @param ignoreCase  �啶���������𖳎����邩�̐^�U�l
     */
    public Section(String name, boolean ignoreCase) {
        name_ = name;
        ignoreCase_ = ignoreCase;
    }

    /**
     * �w�肳�ꂽ�Z�N�V�������A�R�����g������{@code Section}�I�u�W�F�N�g���\�z���܂��B
     * {@code name}��{@code null}���w�肷��ƃO���[�o���Z�N�V�����ƂȂ�܂��B
     * �L�[�̑啶���������͋�ʂ��܂���B
     * 
     * @param name     �Z�N�V������
     * @param comment  �R�����g
     * @throws NullPointerException  {@code comment}��{@code null}�̏ꍇ
     */
    public Section(String name, String comment) {
        this(name, comment, true);
    }

    /**
     * �w�肳�ꂽ�Z�N�V�������A�R�����g������{@code Section}�I�u�W�F�N�g���\�z���܂��B
     * {@code name}��{@code null}���w�肷��ƃO���[�o���Z�N�V�����ƂȂ�܂��B
     * {@code ignoreCase}�ŁA�L�[�̑啶������������ʂ��邩�w��ł��܂��B
     * 
     * @param name        �Z�N�V������
     * @param comment     �R�����g
     * @param ignoreCase  �啶���������𖳎����邩�̐^�U�l
     * @throws NullPointerException  {@code comment}��{@code null}�̏ꍇ
     */
    public Section(String name, String comment, boolean ignoreCase) {
        if (comment == null) throw new NullPointerException();
        name_ = name;
        comments_.add(comment);
        ignoreCase_ = ignoreCase;
    }

    /**
     * ���̃Z�N�V�������O���[�o���Z�N�V�����ł��邩���肵�܂��B
     * 
     * @return �O���[�o���Z�N�V�����̏ꍇ�� {@code true}
     */
    public boolean isGlobalSection() {
        return name_ == null;
    }

    /**
     * �Z�N�V���������擾���܂��B
     * 
     * @return �Z�N�V������
     */
    public String getName() {
        return name_;
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

    /**
     * �啶���������𖳎����邩���肵�܂��B
     * 
     * @return �啶���������𖳎�����ꍇ�� {@code true}
     */
    public boolean isIgnoreCase() {
        return ignoreCase_;
    }

    /**
     * ���̃Z�N�V���������p�����[�^�̐����擾���܂��B
     * 
     * @return �p�����[�^�̐�
     */
    public int countParameters() {
        return parameters_.size();
    }

    /**
     * �w�肳�ꂽ�L�[�̃p�����[�^�����݂��邩���肵�܂��B
     * 
     * @param key  �L�[
     * @return �p�����[�^�����݂���ꍇ�� {@code true}
     */
    public boolean hasParameter(String key) {
        return -1 < indexOfParameter(key);
    }

    /**
     * �w�肳�ꂽ�L�[�̃p�����[�^�̈ʒu�C���f�b�N�X���擾���܂��B
     * �p�����[�^�����݂��Ȃ��ꍇ��-1��Ԃ��܂��B
     * 
     * @param key  �L�[
     * @return �p�����[�^�̈ʒu�C���f�b�N�X
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
     * �w�肳�ꂽ�L�[�̃p�����[�^���擾���܂��B
     * �p�����[�^�����݂��Ȃ��ꍇ��{@code null}��Ԃ��܂��B
     * 
     * @param key  �L�[
     * @return �p�����[�^
     */
    public Parameter getParameter(String key) {
        int idx = indexOfParameter(key);
        if (-1 < idx) {
            return parameters_.get(idx);
        }
        return null;
    }

    /**
     * ���̃Z�N�V�����������ׂẴp�����[�^���擾���܂��B
     * 
     * @return �S�p�����[�^�̃��X�g
     */
    public List<Parameter> getParameters() {
        return new ArrayList<Parameter>(parameters_);
    }

    /**
     * �Z�N�V�����̈�Ԍ��Ƀp�����[�^��ǉ����܂��B
     * 
     * @param parameter  �p�����[�^
     * @throws NullPointerException      {@code parameter}��{@code null}�̏ꍇ
     *         IllegalArgumentException  ���łɓ����L�[�̃p�����[�^�����݂���ꍇ
     */
    public void addParameter(Parameter parameter) {
        addParameter(parameters_.size(), parameter);
    }

    /**
     * �w�肳�ꂽ�ʒu�Ƀp�����[�^��ǉ����܂��B
     * 
     * @param index      �ǉ�����ʒu
     * @param parameter  �p�����[�^
     * @throws NullPointerException      {@code parameter}��{@code null}�̏ꍇ
     *         IllegalArgumentException  ���łɓ����L�[�̃p�����[�^�����݂���ꍇ
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
     * ���łɑ��݂���p�����[�^���X�V���܂��B
     * �p�����[�^�����݂��Ȃ��ꍇ�͉�������{@code null}��Ԃ��܂��B
     * 
     * @param parameter  �p�����[�^
     * @return �X�V����O�̃p�����[�^�B���݂��Ȃ��ꍇ��{@code null}��Ԃ��܂��B
     * @throws NullPointerException  {@code parameter}��{@code null}�̏ꍇ
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
     * �w�肳�ꂽ�L�[�̃p�����[�^���폜���܂��B
     * 
     * @param key  �L�[
     * @return �폜���ꂽ�p�����[�^�B�p�����[�^�����݂��Ȃ��ꍇ��{@code null}��Ԃ��܂��B
     */
    public Parameter removeParameter(String key) {
        int idx = indexOfParameter(key);
        if (-1 < idx) {
            return parameters_.remove(idx);
        }
        return null;
    }

    /**
     * ���̃Z�N�V�����������ׂẴp�����[�^���폜���܂��B
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