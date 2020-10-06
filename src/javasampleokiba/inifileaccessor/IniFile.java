package javasampleokiba.inifileaccessor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * INI�t�@�C���̓��e���i�[����N���X.
 * 
 * <p>[����]</p>
 * <ul>
 * <li>�t�@�C���̐擪���瑱���R�����g�s��INI�t�@�C����(�w�b�_)�R�����g�Ƃ��ď��������܂��B</li>
 * <li>�Z�N�V�������擾����ۂɁA�Z�N�V�������̑啶������������ʂ��邩�w��ł��܂��B</li>
 * <li>�{�N���X�̓X���b�h�Z�[�t�ł͂���܂���B
 * </ul>
 * 
 * ���L��̏ꍇ�A{@code IniFile}�I�u�W�F�N�g��1-2�s�ڂ̃w�b�_�R�����g�ƁA4-7�s�ڂ�8-10�s�ڂ�2�̃Z�N�V�������������܂��B<br>
 * 
 * <p>[��]</p>
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
     * �������������Ȃ�{@code IniFile}�I�u�W�F�N�g���\�z���܂��B
     * �Z�N�V�������̑啶���������͋�ʂ��܂���B
     */
    public IniFile() {
        this(true);
    }

    /**
     * �������������Ȃ��I�u�W�F�N�g���\�z���܂��B
     * {@code ignoreCase}�ŁA�Z�N�V�������̑啶������������ʂ��邩�w��ł��܂��B
     * 
     * @param ignoreCase  �啶���������𖳎����邩�̐^�U�l
     */
    public IniFile(boolean ignoreCase) {
        ignoreCase_ = ignoreCase;
    }

    /**
     * �w�b�_�R�����g�������Ă��邩���肵�܂��B
     * 
     * @return �w�b�_�R�����g�����ꍇ�� {@code true}
     */
    public boolean hasComment() {
        return !comments_.isEmpty();
    }

    /**
     * �w�b�_�R�����g���擾���܂��B
     * 
     * @return �w�b�_�R�����g
     */
    public List<String> getComments() {
        return new ArrayList<String>(comments_);
    }

    /**
     * �w�b�_�R�����g��ݒ肵�܂��B
     * 
     * @param comment  �w�b�_�R�����g
     * @throws NullPointerException  {@code comment}��null�̏ꍇ
     */
    public void setComment(String comment) {
        if (comment == null) throw new NullPointerException();
        comments_.clear();
        comments_.add(comment);
    }

    /**
     * ���ݕێ����Ă���w�b�_�R�����g�ɃR�����g��ǉ����܂��B
     * ��������s���邱�Ƃŕ����s�̃w�b�_�R�����g�ɂȂ�܂��B
     * 
     * @param comment  �ǉ�����R�����g
     * @throws NullPointerException  {@code comment}��null�̏ꍇ
     */
    public void addComment(String comment) {
        if (comment == null) throw new NullPointerException();
        comments_.add(comment);
    }

    /**
     * �w�b�_�R�����g���N���A���܂��B
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
     * �w�肳�ꂽ���O�̃Z�N�V���������݂��邩���肵�܂��B
     * �O���[�o���Z�N�V�����̑��ݔ���̏ꍇ��{@code null}���w�肵�܂��B
     * 
     * @param name  �Z�N�V������
     * @return �Z�N�V���������݂���ꍇ�� {@code true}
     */
    public boolean hasSection(String name) {
        return -1 < indexOfSection(name);
    }

    /**
     * �w�肳�ꂽ���O�̃Z�N�V�����̈ʒu�C���f�b�N�X���擾���܂��B
     * �O���[�o���Z�N�V�����̏ꍇ��{@code null}���w�肵�܂��i���݂���ꍇ�͏��0��Ԃ��܂��j�B
     * �Z�N�V���������݂��Ȃ��ꍇ��-1��Ԃ��܂��B
     * 
     * @param name  �Z�N�V������
     * @return �Z�N�V�����̈ʒu�C���f�b�N�X
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
     * �w�肳�ꂽ���O�̃Z�N�V�������擾���܂��B
     * �O���[�o���Z�N�V�������擾����ꍇ��{@code null}���w�肵�܂��B
     * �Z�N�V���������݂��Ȃ��ꍇ��{@code null}��Ԃ��܂��B
     * 
     * @param name  �Z�N�V������
     * @return �Z�N�V����
     */
    public Section getSection(String name) {
        int idx = indexOfSection(name);
        if (-1 < idx) {
            return sections_.get(idx);
        }
        return null;
    }

    /**
     * ���ׂẴZ�N�V�������擾���܂��B
     * 
     * @return �S�Z�N�V�����̃��X�g
     */
    public List<Section> getSections() {
        return new ArrayList<Section>(sections_);
    }

    /**
     * INI�t�@�C���̈�Ԍ��ɃZ�N�V������ǉ����܂��B
     * 
     * @param section  �Z�N�V����
     * @throws NullPointerException      {@code section}��{@code null}�̏ꍇ
     *         IllegalArgumentException  ���łɓ������O�̃Z�N�V���������݂���ꍇ
     */
    public void addSection(Section section) {
        addSection(sections_.size(), section);
    }

    /**
     * �w�肳�ꂽ�ʒu�ɃZ�N�V������ǉ����܂��B
     * 
     * @param index    �ǉ�����ʒu
     * @param section  �Z�N�V����
     * @throws NullPointerException      {@code section}��{@code null}�̏ꍇ
     *         IllegalArgumentException  ���łɓ������O�̃Z�N�V���������݂���ꍇ
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
     * ���łɑ��݂���Z�N�V�������X�V���܂��B
     * �Z�N�V���������݂��Ȃ��ꍇ�͉�������{@code null}��Ԃ��܂��B
     * 
     * @param section  �Z�N�V����
     * @return �X�V����O�̃Z�N�V�����B���݂��Ȃ��ꍇ��{@code null}��Ԃ��܂��B
     * @throws NullPointerException  {@code section}��{@code null}�̏ꍇ
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
     * �w�肳�ꂽ���O�̃Z�N�V�������폜���܂��B
     * 
     * @param name  �Z�N�V������
     * @return �폜���ꂽ�Z�N�V�����B�Z�N�V���������݂��Ȃ��ꍇ��{@code null}��Ԃ��܂��B
     */
    public Section removeSection(String name) {
        int idx = indexOfSection(name);
        if (-1 < idx) {
            return sections_.remove(idx);
        }
        return null;
    }

    /**
     * ���ׂẴZ�N�V�������폜���܂��B
     */
    public void clearSection() {
        sections_.clear();
    }

    @Override
    public String toString() {
        return "IniFile [comments=" + comments_ + ", sections=" + sections_ + ", ignoreCase=" + ignoreCase_ + "]";
    }
}