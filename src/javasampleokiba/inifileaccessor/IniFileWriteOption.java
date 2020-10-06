package javasampleokiba.inifileaccessor;

/**
 * INI�t�@�C���������݂̊e��I�v�V������ݒ肷��N���X
 * 
 * @author javasampleokiba
 */
public class IniFileWriteOption {

    /** �L�[�ƒl�̃f���~�^���� */
    private char delimiter_                     = '=';
    /** �e�Z�N�V�����Ԃɑ}�������s�� */
    private int blankLineBetweenSection_        = 1;
    /** �e�p�����[�^�Ԃɑ}�������s�� */
    private int blankLineBetweenParameter_      = 0;

    /**
     * �L�[�ƒl����؂�f���~�^�������擾���܂��B
     * �f�t�H���g�́A'='�ł��B
     * 
     * @return �f���~�^����
     */
    public char getDelimiter() {
        return delimiter_;
    }

    /**
     * �L�[�ƒl����؂�f���~�^������ݒ肵�܂��B
     * 
     * @param delimiter  �f���~�^����
     * @return ���̃I�u�W�F�N�g�ւ̎Q��
     */
    public IniFileWriteOption setDelimiter(char delimiter) {
        delimiter_ = delimiter;
        return this;
    }

    /**
     * �e�Z�N�V�����Ԃɑ}�������s�����擾���܂��B
     * �f�t�H���g�́A1�ł��B
     * 
     * @return �e�Z�N�V�����Ԃɑ}�������s��
     */
    public int getBlankLineBetweenSection() {
        return blankLineBetweenSection_;
    }

    /**
     * �e�Z�N�V�����Ԃɑ}�������s����ݒ肵�܂��B
     * 
     * @param blankLineBetweenSection  ��s��
     * @return ���̃I�u�W�F�N�g�ւ̎Q��
     * @throws IllegalArgumentException  {@code blankLineBetweenSection}��0�����̏ꍇ
     */
    public IniFileWriteOption setBlankLineBetweenSection(int blankLineBetweenSection) {
        if (blankLineBetweenSection < 0) {
            throw new IllegalArgumentException("blankLineBetweenSection < 0");
        }
        blankLineBetweenSection_ = blankLineBetweenSection;
        return this;
    }

    /**
     * �e�p�����[�^�Ԃɑ}�������s�����擾���܂��B
     * �f�t�H���g�́A0�ł��B
     * 
     * @return �e�p�����[�^�Ԃɑ}�������s��
     */
    public int getBlankLineBetweenParameter() {
        return blankLineBetweenParameter_;
    }

    /**
     * �e�p�����[�^�Ԃɑ}�������s����ݒ肵�܂��B
     * 
     * @param blankLineBetweenParameter  ��s��
     * @return ���̃I�u�W�F�N�g�ւ̎Q��
     * @throws IllegalArgumentException  {@code blankLineBetweenParameter}��0�����̏ꍇ
     */
    public IniFileWriteOption setBlankLineBetweenParameter(int blankLineBetweenParameter) {
        if (blankLineBetweenParameter < 0) {
            throw new IllegalArgumentException("blankLineBetweenParameter < 0");
        }
        blankLineBetweenParameter_ = blankLineBetweenParameter;
        return this;
    }
}