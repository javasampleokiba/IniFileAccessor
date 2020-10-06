package javasampleokiba.inifileaccessor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * INI�t�@�C���ǂݍ��݂̊e��I�v�V������ݒ肷��N���X
 * 
 * @author javasampleokiba
 */
public class IniFileReadOption {

    /**
     * �s���ȍs�����o�����Ƃ��̐U�镑���񋓃N���X
     */
    public enum UnknownLineBehavior {
        /** ��O���X���[���� */
        ERROR,
        /** �������� */
        IGNORE,
    }

    /**
     * �Z�N�V���������d�������Ƃ��̐U�镑���񋓃N���X
     */
    public enum DuplicateSectionBehavior {
        /** ��O���X���[���� */
        ERROR,
        /** �O�̃Z�N�V�����𖳎����� */
        IGNORE_FORMER,
        /** ��̃Z�N�V�����𖳎����� */
        IGNORE_LATTER,
        /** �}�[�W���� */
        MERGE,
    }

    /**
     * �L�[���d�������Ƃ��̐U�镑���񋓃N���X
     */
    public enum DuplicateKeyBehavior {
        /** ��O���X���[���� */
        ERROR,
        /** �O�̃L�[�𖳎����� */
        IGNORE_FORMER,
        /** ��̃L�[�𖳎����� */
        IGNORE_LATTER,
        /** �����̒l�Ƃ݂Ȃ� */
        MULTI_VALUES,
    }

    /*
     * �S�̐ݒ�
     */
    /** �󔒕����𖳎����邩 */
    private boolean ignoreWhiteSpace_                               = false;
    /** �󔒕������\�����K�\�� */
    private String whiteSpaceRegex_                                 = "\\s+";
    /** �啶���������𖳎����邩 */
    private boolean ignoreCase_                                     = true;
    /** �s���ȍs�����o�����Ƃ��̐U�镑�� */
    private UnknownLineBehavior unknownLineBehavior_                = UnknownLineBehavior.ERROR;

    /*
     * �Z�N�V�����E�p�����[�^�Ɋւ���ݒ�
     */
    /** �O���[�o���Z�N�V�����̑��݂������邩 */
    private boolean allowGlobalSection_                             = false;
    /** �L�[�ƒl�̃f���~�^���� */
    private final List<Character> delimiters_                       = new ArrayList<Character>();
    /** �����s�̒l�������邩 */
    private boolean allowMultiLine_                                 = false;
    /** �����s�̒l�̉��s�R�[�h */
    private LineSeparator multiLineSeparator_                       = LineSeparator.SYSTEM_DEFAULT;
    /** �Z�N�V���������d�������Ƃ��̐U�镑�� */
    private DuplicateSectionBehavior duplicateSectionBehavior_      = DuplicateSectionBehavior.ERROR;
    /** �L�[���d�������Ƃ��̐U�镑�� */
    private DuplicateKeyBehavior duplicateKeyBehavior_              = DuplicateKeyBehavior.ERROR;

    /*
     * �R�����g�Ɋւ���ݒ�
     */
    /** �w�b�_�R�����g������INI�t�@�C���� */
    private boolean hasHeaderComment_                               = false;
    /** �R�����g�̊J�n��\������ */
    private final List<Character> commentChars_                     = new ArrayList<Character>();
    /** �s�̓r������̃R�����g�������邩 */
    private boolean allowMiddleOfLineComment_                       = false;

    /**
     * �f�t�H���g�̐ݒ��������{@code IniFileReadOption}�I�u�W�F�N�g���\�z���܂��B
     */
    public IniFileReadOption() {
        commentChars_.add(';');
        delimiters_.add('=');
    }

    /**
     * �R�����g�s�̐擪�A�Z�N�V�����s�̑O��A�L�[����ђl�̑O��̋󔒕����𖳎����邩���肵�܂��B
     * �f�t�H���g�́A�������܂���B
     * 
     * @return �󔒕����𖳎�����ꍇ�� {@code true}
     */
    public boolean isIgnoreWhiteSpace() {
        return ignoreWhiteSpace_;
    }

    /**
     * �R�����g�s�̐擪�A�Z�N�V�����s�̑O��A�L�[�̑O��̋󔒕����𖳎����邩�ݒ肵�܂��B
     * 
     * <p>[�R�����g�s�̗�]</p>
     * ��������ꍇ�A���L��1�s�ڂ��R�����g�s�Ƃ݂Ȃ���܂��B
     * �R�����g�����̑O�̋󔒕����͎�菜���܂���B
     * <pre>
     * 1:     ;comment line
     * 2: ;comment line
     * </pre>
     * 
     * <p>[�Z�N�V�����s�̗�]</p>
     * ��������ꍇ�A���L��1�s�ڂ̃Z�N�V�����s��2�s�ڂƓ����Ƃ݂Ȃ���܂��B
     * �Z�N�V�������̑O��̋󔒕����̓Z�N�V�������̈ꕔ�Ƃ݂Ȃ���邽�ߖ������܂���B
     * <pre>
     * 1:     [  section  ]    
     * 2: [  section  ]
     * </pre>
     * 
     * <p>[�p�����[�^�s�̗�]</p>
     * ��������ꍇ�A�L�[�̑O��̋󔒕����͎�菜����A
     * ���L��1�s�ڂ̃L�[����2�s�ڂƓ����Ƃ݂Ȃ��� �܂��B
     * �l�̑O��̋󔒕����͎�菜���܂���i�󔒕������l�̈ꕔ�ƔF�����܂��j�B
     * <pre>
     * 1:     key    =    value    
     * 2: key=    value    
     * </pre>
     * 
     * @param ignoreWhiteSpace  �󔒕����𖳎����邩�̐^�U�l
     * @return ���̃I�u�W�F�N�g�ւ̎Q��
     */
    public IniFileReadOption setIgnoreWhiteSpace(boolean ignoreWhiteSpace) {
        ignoreWhiteSpace_ = ignoreWhiteSpace;
        return this;
    }

    /**
     * �󔒕������\�����K�\�����擾���܂��B
     * �f�t�H���g�́A"\\s+"�ł��B
     * 
     * @return �󔒕������\�����K�\��
     */
    public String getWhiteSpaceRegex() {
        return whiteSpaceRegex_;
    }

    /**
     * �󔒕������\�����K�\����ݒ肵�܂��B
     * 
     * @param whiteSpaceRegex  �󔒕������\�����K�\��
     * @return ���̃I�u�W�F�N�g�ւ̎Q��
     */
    public IniFileReadOption setWhiteSpaceRegex(String whiteSpaceRegex) {
        // ���K�\���̍\���`�F�b�N
        Pattern.compile(whiteSpaceRegex);
        whiteSpaceRegex_ = whiteSpaceRegex;
        return this;
    }

    /**
     * �啶���������𖳎����邩���肵�܂��B
     * �f�t�H���g�́A�������܂��B
     * 
     * @return �啶���������𖳎�����ꍇ��true
     */
    public boolean isIgnoreCase() {
        return ignoreCase_;
    }

    /**
     * �啶���������𖳎����邩�ݒ肵�܂��B
     * 
     * <p>[�Z�N�V�����s�̗�]</p>
     * ��������ꍇ�A���L��1�s�ڂ�2�s�ڂ̃Z�N�V�����͓����ŁA�d�����Ă���Ƃ݂Ȃ���܂��B
     * <pre>
     * 1: [section]    
     * 2: [SECTION]
     * </pre>
     * 
     * <p>[�p�����[�^�s�̗�]</p>
     * ��������ꍇ�A���L��1�s�ڂ�2�s�ڂ̃L�[�͓����ŁA�d�����Ă���Ƃ݂Ȃ���܂��B
     * <pre>
     * 1: Key=1
     * 2: key=2
     * </pre>
     * 
     * @param ignoreCase  �啶���������𖳎����邩�̐^�U�l
     * @return ���̃I�u�W�F�N�g�ւ̎Q��
     */
    public IniFileReadOption setIgnoreCase(boolean ignoreCase) {
        ignoreCase_ = ignoreCase;
        return this;
    }

    /**
     * �s���ȍs�����o�����Ƃ��̐U�镑�����擾���܂��B
     * �f�t�H���g�́A{@code ERROR}�ł��B
     * 
     * @return �s���ȍs�����o�����Ƃ��̐U�镑��
     */
    public UnknownLineBehavior getUnknownLineBehavior() {
        return unknownLineBehavior_;
    }

    /**
     * �s���ȍs�����o�����Ƃ��̐U�镑����ݒ肵�܂��B
     * �s���ȍs�Ƃ́A�󔒍s�A�R�����g�s�A�Z�N�V�����s�A�p�����[�^�s�i�����s�̒l��������ꍇ�͒l�̍s�܂ށj
     * �ȊO�̍s���w���܂��B
     * <ul>
     * <li>ERROR  : ��O�i{@code IOException}�j���X���[���܂��B</li>
     * <li>IGNORE : ���̍s�𖳎����܂��B</li>
     * </ul>
     * 
     * @param unknownLineBehavior  �s���ȍs�����o�����Ƃ��̐U�镑��
     * @return ���̃I�u�W�F�N�g�ւ̎Q��
     * @throws NullPointerException  {@code unknownLineBehavior}��{@code null}�̏ꍇ
     */
    public IniFileReadOption setUnknownLineBehavior(UnknownLineBehavior unknownLineBehavior) {
        if (unknownLineBehavior == null) throw new NullPointerException();
        unknownLineBehavior_ = unknownLineBehavior;
        return this;
    }

    /**
     * �O���[�o���Z�N�V�����̑��݂������邩���肵�܂��B
     * �f�t�H���g�́A�����܂���B
     * 
     * @return �O���[�o���Z�N�V�����̑��݂�������ꍇ�� {@code true}
     */
    public boolean isAllowGlobalSection() {
        return allowGlobalSection_;
    }

    /**
     * �O���[�o���Z�N�V�����̑��݂������邩�ݒ肵�܂��B
     * �O���[�o���Z�N�V�����Ƃ́A������̃Z�N�V�����ɂ������Ȃ��AINI�t�@�C���̐擪�ɒ�`���ꂽ�p�����[�^������
     * �����̃Z�N�V�����ł��B
     * 
     * <p>[��]</p>
     * ������ꍇ�́Akey1�Akey2���O���[�o���Z�N�V�����ɑ����܂��B�����Ȃ��ꍇ�́A�s���ȍs�Ƃ݂Ȃ���܂��B
     * <pre>
     * 1: key1=1
     * 2: key2=2
     * 3: [section]
     * 4: key3=3
     * </pre>
     * 
     * @param allowGlobalSection  �O���[�o���Z�N�V�����̑��݂������邩�̐^�U�l
     * @return ���̃I�u�W�F�N�g�ւ̎Q��
     */
    public IniFileReadOption setAllowGlobalSection(boolean allowGlobalSection) {
        allowGlobalSection_ = allowGlobalSection;
        return this;
    }

    /**
     * �p�����[�^���L�[�ƒl�ɋ�؂�f���~�^�������擾���܂��B
     * �f�t�H���g�́A'='�ł��B
     * 
     * @return �f���~�^�����̃��X�g
     */
    public List<Character> getDelimiters() {
        return delimiters_;
    }

    /**
     * �p�����[�^���L�[�ƒl�ɋ�؂�f���~�^������ݒ肵�܂��B
     * �����̃f���~�^���������݂����邱�Ƃ��\�ł��B
     * 
     * @param delimiters  �f���~�^����
     * @return ���̃I�u�W�F�N�g�ւ̎Q��
     */
    public IniFileReadOption setDelimiters(char... delimiters) {
        delimiters_.clear();
        for (char c : delimiters) {
            delimiters_.add(c);
        }
        return this;
    }

    /**
     * �p�����[�^�������s�̒l�������Ƃ������邩���肵�܂��B
     * �f�t�H���g�́A�����܂���B
     * 
     * @return �p�����[�^�������s�̒l�������Ƃ�������ꍇ�� {@code true}
     */
    public boolean isAllowMultiLine() {
        return allowMultiLine_;
    }

    /**
     * �p�����[�^�������s�̒l�������Ƃ������邩�ݒ肵�܂��B
     * �p�����[�^�s�̖�����"\"��u�����ƂŁA�l�������s�ł��邱�Ƃ������A
     * ���̍s�S�̂��l�̈ꕔ�Ƃ݂Ȃ���܂��B
     * 
     * <p>[��]</p>
     * ���L�̏ꍇ�A3�s�ڂ܂ł�key�̒l�Ƃ݂Ȃ���܂��B
     * <pre>
     * 1: key=123\
     * 2: ;456\
     * 3: [789]
     * </pre>
     * 
     * @param allowMultiLine  �����s�̒l�������Ƃ������邩�̐^�U�l
     * @return ���̃I�u�W�F�N�g�ւ̎Q��
     */
    public IniFileReadOption setAllowMultiLine(boolean allowMultiLine) {
        allowMultiLine_ = allowMultiLine;
        return this;
    }

    /**
     * �����s�̒l�̉��s�R�[�h���擾���܂��B
     * �f�t�H���g�́A�V�X�e���f�t�H���g�̉��s�R�[�h�ł��B
     * 
     * @return �����s�̒l�̉��s�R�[�h
     */
    public LineSeparator getMultiLineSeparator() {
        return multiLineSeparator_;
    }

    /**
     * �����s�̒l�̉��s�R�[�h��ݒ肵�܂��B
     * ���ǂݍ��񂾒l�ɕt��������s�R�[�h�ŁA�t�@�C�����̎��ۂ̉��s�R�[�h�ƈ�v���Ă���K�v�͂���܂���B
     * 
     * @param multiLineSeparator  �����s�̒l�̉��s�R�[�h
     * @return ���̃I�u�W�F�N�g�ւ̎Q��
     * @throws NullPointerException  {@code multiLineSeparator}��{@code null}�̏ꍇ
     */
    public IniFileReadOption setMultiLineSeparator(LineSeparator multiLineSeparator) {
        if (multiLineSeparator == null) throw new NullPointerException();
        multiLineSeparator_ = multiLineSeparator;
        return this;
    }

    /**
     * �Z�N�V���������d�������Ƃ��̐U�镑�����擾���܂��B
     * �f�t�H���g�́A{@code ERROR}�ł��B
     * 
     * @return �Z�N�V���������d�������Ƃ��̐U�镑��
     */
    public DuplicateSectionBehavior getDuplicateSectionBehavior() {
        return duplicateSectionBehavior_;
    }

    /**
     * �Z�N�V���������d�������Ƃ��̐U�镑����ݒ肵�܂��B
     * 
     * <ul>
     * <li>ERROR         : ��O�i{@code IOException}�j���X���[���܂��B</li>
     * <li>IGNORE_FORMER : ��Ɍ��o�����Z�N�V�����𖳎����܂��B</li>
     * <li>IGNORE_LATTER : ��Ɍ��o�����Z�N�V�����𖳎����܂��B</li>
     * <li>MERGE         : �d�������Z�N�V�����̎������}�[�W���܂��B</li>
     * </ul>
     * 
     * <p>[MERGE�̗�]</p>
     * ���L�̏ꍇ�Asection��key1�Akey2�Akey3�Akey4�̃p�����[�^�������܂��B
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
     * @param duplicateSectionBehavior  �Z�N�V���������d�������Ƃ��̐U�镑��
     * @return ���̃I�u�W�F�N�g�ւ̎Q��
     * @throws NullPointerException  {@code duplicateSectionBehavior}��{@code null}�̏ꍇ
     */
    public IniFileReadOption setDuplicateSectionBehavior(DuplicateSectionBehavior duplicateSectionBehavior) {
        if (duplicateSectionBehavior == null) throw new NullPointerException();
        duplicateSectionBehavior_ = duplicateSectionBehavior;
        return this;
    }

    /**
     * �Z�N�V�������ŃL�[���d�������Ƃ��̐U�镑�����擾���܂��B
     * �f�t�H���g�́A{@code ERROR}�ł��B
     * 
     * @return �Z�N�V�������ŃL�[���d�������Ƃ��̐U�镑��
     */
    public DuplicateKeyBehavior getDuplicateKeyBehavior() {
        return duplicateKeyBehavior_;
    }

    /**
     * �Z�N�V�������ŃL�[���d�������Ƃ��̐U�镑����ݒ肵�܂��B
     * 
     * <ul>
     * <li>ERROR         : ��O�i{@code IOException}�j���X���[���܂��B</li>
     * <li>IGNORE_FORMER : ��Ɍ��o�����L�[�𖳎����܂��B</li>
     * <li>IGNORE_LATTER : ��Ɍ��o�����L�[�𖳎����܂��B</li>
     * <li>MULTI_VALUES  : �����̒l�����L�[�Ƃ݂Ȃ��܂��B</li>
     * </ul>
     * 
     * <p>[MULTI_VALUES�̗�]</p>
     * ���L�̏ꍇ�Akey1��1��3�̒l�������܂��B
     * <pre>
     * 1: [section]
     * 2: key1=1
     * 3: key2=2
     * 4: key1=3
     * </pre>
     * 
     * @param duplicateKeyBehavior  �Z�N�V�������ŃL�[���d�������Ƃ��̐U�镑��
     * @return ���̃I�u�W�F�N�g�ւ̎Q��
     * @throws NullPointerException  {@code duplicateKeyBehavior}��{@code null}�̏ꍇ
     */
    public IniFileReadOption setDuplicateKeyBehavior(DuplicateKeyBehavior duplicateKeyBehavior) {
        if (duplicateKeyBehavior == null) throw new NullPointerException();
        duplicateKeyBehavior_ = duplicateKeyBehavior;
        return this;
    }

    /**
     * �w�b�_�R�����g������INI�t�@�C�������肵�܂��B
     * �f�t�H���g�́A�����܂���B
     * 
     * @return �w�b�_�R�����g������INI�t�@�C���̏ꍇ {@code true}
     */
    public boolean hasHeaderComment() {
        return hasHeaderComment_;
    }

    /**
     * �w�b�_�R�����g������INI�t�@�C������ݒ肵�܂��B
     * {@code true}�̏ꍇ�A�t�@�C���̐擪���瑱���R�����g�s��INI�t�@�C���̃w�b�_�R�����g�Ƃ݂Ȃ��܂��B
     * 
     * <p>[��]</p>
     * {@code true}�̏ꍇ�A���L��1-2�s�ڂ�INI�t�@�C���̃w�b�_�R�����g�Ƃ݂Ȃ��܂��B
     * <pre>
     * 1: ;header comment 1
     * 2: ;header comment 2
     * 3: 
     * 4: ;section comment
     * 5: [section]
     * </pre>
     * 
     * @param hasHeaderComment  �w�b�_�R�����g������INI�t�@�C�����ǂ����̐^�U�l
     * @return ���̃I�u�W�F�N�g�ւ̎Q��
     */
    public IniFileReadOption setHasHeaderComment(boolean hasHeaderComment) {
        hasHeaderComment_ = hasHeaderComment;
        return this;
    }

    /**
     * �R�����g�̊J�n��\���������擾���܂��B
     * �f�t�H���g�́A';'�ł��B
     * 
     * @return �R�����g�̊J�n��\�������̃��X�g
     */
    public List<Character> getCommentChars() {
        return commentChars_;
    }

    /**
     * �R�����g�̊J�n��\��������ݒ肵�܂��B
     * �����̕��������݂����邱�Ƃ��\�ł��B
     * 
     * @param commentChars  �R�����g�̊J�n��\������
     * @return ���̃I�u�W�F�N�g�ւ̎Q��
     */
    public IniFileReadOption setCommentChars(char... commentChars) {
        commentChars_.clear();
        for (char c : commentChars) {
            commentChars_.add(c);
        }
        return this;
    }

    /**
     * �s�̓r������̃R�����g�������邩���肵�܂��B
     * �f�t�H���g�́A�����܂���B
     * 
     * @return �s�̓r������̃R�����g��������ꍇ�� {@code true}
     */
    public boolean isAllowMiddleOfLineComment() {
        return allowMiddleOfLineComment_;
    }

    /**
     * �s�̓r������̃R�����g�������邩�ݒ肵�܂��B
     * ������ꍇ�A�l�ȂǂɃR�����g�J�n�������g���Ă���ƁA���̈ʒu����R�����g�Ƃ݂Ȃ����̂Œ��ӂ��K�v�ł��B
     * 
     * <p>[��]</p>
     * ���L�̏ꍇ�A";section comment"��section�̃R�����g�A
     * ";parameter comment"��key1�̃R�����g�A"abc"���l�A
     * ";abc    ;parameter comment"��key2�̃R�����g�A""���l�ɂȂ�܂��B
     * <pre>
     * 1: [section]    ;section comment
     * 2: key1=abc     ;parameter comment
     * 3: key2=;abc    ;parameter comment
     * </pre>
     * 
     * @param allowMiddleOfLineComment  �s�̓r������̃R�����g�������邩�̐^�U�l
     * @return ���̃I�u�W�F�N�g�ւ̎Q��
     */
    public IniFileReadOption setAllowMiddleOfLineComment(boolean allowMiddleOfLineComment) {
        allowMiddleOfLineComment_ = allowMiddleOfLineComment;
        return this;
    }
}