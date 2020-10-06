package javasampleokiba.inifileaccessor;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * INI�t�@�C�����������ރN���X.
 * 
 * IniFile�N���X�̃f�[�^�\���ɏ]����INI�t�@�C�����������݂܂��B
 * IniFileReadOption�N���X�ɂ�菑�����ݓ���̃I�v�V������ݒ�ύX�ł��܂��B
 * �ȉ��̓���������܂��B
 * <ul>
 * <li>���s�R�[�h�̓V�X�e���f�t�H���g�̉��s�R�[�h���g���܂��B</li>
 * <li>�����s�̒l�����p�����[�^�̏ꍇ�A�����I�ɖ�����'\'��t�����ďo�͂��܂��B</li>
 * </ul>
 * 
 * @author javasampleokiba
 */
public class IniFileWriter {

    private IniFileWriteOption option_      = new IniFileWriteOption();

    /**
     * �f�t�H���g�̏������݃I�v�V������{@code IniFileWriter}�I�u�W�F�N�g���\�z���܂��B
     */
    public IniFileWriter() {
    }

    /**
     * �w�肳�ꂽ�������݃I�v�V������{@code IniFileWriter}�I�u�W�F�N�g���\�z���܂��B
     * 
     * @param option  �������݃I�v�V����
     * @throws NullPointerException  {@code option}��{@code null}�̏ꍇ
     */
    public IniFileWriter(IniFileWriteOption option) {
        if (option == null) throw new NullPointerException();
        option_ = option;
    }

    /**
     * �������݃I�v�V�������擾���܂��B
     * 
     * @return �������݃I�v�V����
     */
    public IniFileWriteOption getOption() {
        return option_;
    }

    /**
     * �������݃I�v�V������ݒ肵�܂��B
     * 
     * @param option  �������݃I�v�V����
     * @throws NullPointerException  {@code option}��{@code null}�̏ꍇ
     */
    public void setOption(IniFileWriteOption option) {
        if (option == null) throw new NullPointerException();
        option_ = option;
    }

    /**
     * �f�t�H���g�̕����R�[�h��INI�t�@�C�����������݂܂��B
     * 
     * @param path     ��������INI�t�@�C���̃t�@�C���p�X
     * @param ini      INI�t�@�C���I�u�W�F�N�g
     * @param options  �t�@�C�����J�����@���w�肷��I�v�V����
     * @throws IOException  �ǂݍ��݃G���[�����������ꍇ
     */
    public void write(String path, IniFile ini, OpenOption... options) throws IOException {
        write(path, ini, Charset.defaultCharset());
    }

    /**
     * �w�肳�ꂽ�����R�[�h��INI�t�@�C�����������݂܂��B
     * 
     * @param path     ��������INI�t�@�C���̃t�@�C���p�X
     * @param ini      INI�t�@�C���I�u�W�F�N�g
     * @param cs       �����R�[�h
     * @param options  �t�@�C�����J�����@���w�肷��I�v�V����
     * @throws IOException  �ǂݍ��݃G���[�����������ꍇ
     */
    public void write(String path, IniFile ini, Charset cs, OpenOption... options) throws IOException {
        List<String> content = new ArrayList<String>();

        // �w�b�_�R�����g��������
        if (ini.hasComment()) {
            for (String comment : ini.getComments()) {
                addLine(content, comment);
            }
            addBlankLine(content, 1);
        }

        boolean firstSection = true;

        // �O���[�o���Z�N�V������������
        for (Section s : ini.getSections()) {
            if (!s.isGlobalSection()) {
                continue;
            }

            // �p�����[�^�R�����g�A�p�����[�^��������
            List<Parameter> parameters = s.getParameters();
            for (int j = 0; j < parameters.size(); j++) {
                if (j != 0) {
                    addBlankLine(content, option_.getBlankLineBetweenParameter());
                }
                addParameter(content, parameters.get(j));
            }
            firstSection = false;
            break;
        }

        // �O���[�o���Z�N�V�����ȊO�̂��ׂẴZ�N�V������������
        for (Section s : ini.getSections()) {
            if (s.isGlobalSection()) {
                continue;
            }

            if (!firstSection) {
                addBlankLine(content, option_.getBlankLineBetweenSection());
            }
            firstSection = false;

            // �Z�N�V�����R�����g�A�Z�N�V�����s��������
            addSection(content, s);

            // �p�����[�^�R�����g�A�p�����[�^��������
            List<Parameter> parameters = s.getParameters();
            for (int i = 0; i < parameters.size(); i++) {
                if (i != 0) {
                    addBlankLine(content, option_.getBlankLineBetweenParameter());
                }
                addParameter(content, parameters.get(i));
            }
        }

        Files.write(Paths.get(path), content, cs, options);
    }

    private void addLine(List<String> content, String line) {
        content.add(line);
    }

    private void addBlankLine(List<String> content, int count) {
        for (int i = 0; i < count; i++) {
            content.add("");
        }
    }

    private void addSection(List<String> content, Section section) {
        String name = section.getName();

        if (section.hasComment()) {
            for (String comment : section.getComments()) {
                addLine(content, comment);
            }
        }
        addLine(content, "[" + name + "]");
    }

    private void addParameter(List<String> content, Parameter parameter) {
        String key = parameter.getKey();

        if (parameter.hasComment()) {
            for (String comment : parameter.getComments()) {
                addLine(content, comment);
            }
        }
        for (String v : parameter.getValues()) {
            String value = v.replaceAll("(?=\r\n)|((?<=[^\r])(?=\n))", "\\\\");
            addLine(content, key + option_.getDelimiter() + value);
        }
    }
}