package javasampleokiba.inifileaccessor;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * INI�t�@�C����ǂݍ��ރN���X.
 * 
 * <br>
 * �f�t�H���g�̓ǂݍ��݃A���S���Y�����ȉ��Ɏ����܂��i�ԍ����ɍs���肪�s���܂��j�B
 * ���̓���́A{@code IniFileReadOption}�N���X���g���ĕύX�ł��܂��B<br>
 * ------------------------------------------------------<br>
 * <p>1. �󔒍s</p>
 * ���K�\��"^\s+$"�Ƀ}�b�`����s���󔒍s�Ƃ݂Ȃ��������܂��B
 * 
 * <p>2. �R�����g�s</p>
 * �擪��';'�Ŏn�܂�s���R�����g�s�Ƃ݂Ȃ��܂��B
 * �s�̈ʒu�ɂ��A�Z�N�V�����A�p�����[�^�̂�����̃R�����g�ł��邩�����肵�܂��B
 * 
 * <p>3. �Z�N�V�����s</p>
 * �擪��'['�Ŏn�܂�']'�ŏI���s���Z�N�V�����s�Ƃ݂Ȃ��A
 * '['��']'�ň͂܂ꂽ0�����ȏ�̕�������Z�N�V�������Ƃ݂Ȃ��܂��B
 * ���̃Z�N�V�����s�����o����܂ŁA�ȍ~�Ɍ��o�����p�����[�^�͂��̃Z�N�V�����ɑ����܂��B
 * 
 * <p>4. �p�����[�^�s</p>
 * 1�ȏ��'='���܂ލs���p�����[�^�s�Ƃ݂Ȃ��A
 * �ŏ��Ɍ����'='����؂�Ƃ��č�����0�����ȏ�̕�������L�[�A�E����0�����ȏ�̕������l�Ƃ݂Ȃ��܂��B
 * �f�t�H���g�ł͋����܂��񂪁A�I�v�V�����ɂ�蕡���s�̒l��������ꍇ�A
 * �p�����[�^�s�̖�����'\'���u����Ă���ƁA���̍s�S�̂�l�̈ꕔ�Ƃ݂Ȃ��܂��B
 * ���̍s�ȍ~��������'\'���u����Ă���ꍇ�́A���̎��̍s��l�̈ꕔ�Ƃ݂Ȃ��܂��B
 * ���̂Ƃ�������'\'�͎����I�Ɏ�菜����܂��B
 * 
 * <p>5. �s���ȍs</p>
 * ��L�̂�����̍s�ɂ��Y�����Ȃ��s�͕s���ȍs�Ƃ݂Ȃ��A��O���X���[�������𒆎~���܂��B
 * <br>
 * ------------------------------------------------------<br>
 * 
 * ���̑��A�ȉ��̓���������܂��B
 * <ul>
 * <li>�Z�N�V�������A�L�[�����d�������ꍇ�͗�O���X���[���܂��B
 *   ���̂Ƃ��A�啶���������͋�ʂ��܂���B</li>
 * <li>�s�̓r������̃R�����g�͋����܂���B</li>
 * </ul>
 * 
 * @author javasampleokiba
 */
public class IniFileReader {

    private static final Pattern SECTION_PATTERN        = Pattern.compile("^\\[(.*)\\]$");
    private IniFileReadOption option_                   = new IniFileReadOption();

    /**
     * �f�t�H���g�̓ǂݍ��݃I�v�V������{@code IniFileReader}�I�u�W�F�N�g���\�z���܂��B
     */
    public IniFileReader() {
    }

    /**
     * �w�肳�ꂽ�ǂݍ��݃I�v�V������{@code IniFileReader}�I�u�W�F�N�g���\�z���܂��B
     * 
     * @param option  �ǂݍ��݃I�v�V����
     * @throws NullPointerException  {@code option}��{@code null}�̏ꍇ
     */
    public IniFileReader(IniFileReadOption option) {
        if (option == null) throw new NullPointerException();
        option_ = option;
    }

    /**
     * �ǂݍ��݃I�v�V�������擾���܂��B
     * 
     * @return �ǂݍ��݃I�v�V����
     */
    public IniFileReadOption getOption() {
        return option_;
    }

    /**
     * �ǂݍ��݃I�v�V������ݒ肵�܂��B
     * 
     * @param option  �ǂݍ��݃I�v�V����
     * @throws NullPointerException  {@code option}��{@code null}�̏ꍇ
     */
    public void setOption(IniFileReadOption option) {
        if (option == null) throw new NullPointerException();
        option_ = option;
    }

    /**
     * �f�t�H���g�̕����R�[�h�Ŏw�肳�ꂽINI�t�@�C����ǂݍ��݁A
     * INI�t�@�C���I�u�W�F�N�g��Ԃ��܂��B
     * 
     * @param path  �ǂݍ���INI�t�@�C���̃t�@�C���p�X
     * @return INI�t�@�C���I�u�W�F�N�g
     * @throws IOException  �ǂݍ��݃G���[�����������ꍇ
     */
    public IniFile read(String path) throws IOException {
        return read(path, Charset.defaultCharset());
    }

    /**
     * �w�肳�ꂽ�����R�[�h�Ŏw�肳�ꂽINI�t�@�C����ǂݍ��݁A
     * INI�t�@�C���I�u�W�F�N�g��Ԃ��܂��B
     * 
     * @param path  �ǂݍ���INI�t�@�C���̃p�X
     * @param cs    �����R�[�h
     * @return INI�t�@�C���I�u�W�F�N�g
     * @throws IOException  �ǂݍ��݃G���[�����������ꍇ
     */
    public IniFile read(String path, Charset cs) throws IOException {
        IniFile ini = new IniFile(option_.isIgnoreCase());
        List<String> lines = Files.readAllLines(Paths.get(path), cs);
        int num = 0;

        // �w�b�_�R�����g�擾
        if (option_.hasHeaderComment()) {
            List<String> header = new ArrayList<String>();
            for (; num < lines.size(); num++) {
                String line = lines.get(num);

                // �R�����g�s�ł͂Ȃ��ꍇ
                if (!isCommentLine(line)) {
                    break;
                }
                header.add(line);
            }
            for (String comment : header) {
                ini.addComment(comment);
            }
        }

        // �O���[�o���Z�N�V�����o�^
        Section section = new Section(null, option_.isIgnoreCase());
        ini.addSection(section);

        List<String> comments = new ArrayList<String>();
        Parameter multiLineParameter = null;
        for (; num < lines.size(); num++) {
            String line = lines.get(num);

            // �����s�p�����[�^���o�ς݂̏ꍇ
            if (multiLineParameter != null) {
                String value = multiLineParameter.getValue() + option_.getMultiLineSeparator();

                // ������"\"�̏ꍇ
                if (line.endsWith("\\")) {
                    multiLineParameter.setValue(value + line.substring(0, line.length() - 1));
                } else {
                    multiLineParameter.setValue(value + line);
                    multiLineParameter = null;
                }
                continue;
            }

            // ��s�̏ꍇ
            if (isBlankLine(line)) {
                continue;
            }

            // �R�����g�s�̏ꍇ
            if (isCommentLine(line)) {
                comments.add(line);
                continue;
            }

            Section nextSection = parseSectionLine(line);
            // �Z�N�V�����s�̏ꍇ
            if (nextSection != null) {
                section = addSection(ini, section, nextSection, comments);
                comments = new ArrayList<String>();
                continue;
            }

            Parameter parameter = parseParameterLine(line);
            // �p�����[�^�s�̏ꍇ
            if (parameter != null) {
                addParameter(section, parameter, comments);
                comments = new ArrayList<String>();

                // �����s�̒l��������ꍇ ���� ������"\"�̏ꍇ
                if (option_.isAllowMultiLine() &&
                        parameter.getValue().endsWith("\\")) {
                    String value = parameter.getValue();
                    parameter.setValue(value.substring(0, value.length() - 1));
                    multiLineParameter = parameter;
                }
                continue;
            }

            // �����ȍs�̑��݂��G���[�Ƃ݂Ȃ��ꍇ�́A��O���X���[���ďI��
            if (option_.getUnknownLineBehavior() == IniFileReadOption.UnknownLineBehavior.ERROR) {
                throw new IOException("It contains a invalid line.(" + (num + 1) + ")");
            }
        }

        // �O���[�o���Z�N�V�����ɃL�[�����݂��Ȃ��ꍇ�̓f�t�H���g�Z�N�V�����폜
        Section globalSection = ini.getSection(null);
        if (globalSection.getParameters().isEmpty()) {
            ini.removeSection(null);

        // �O���[�o���Z�N�V�����̑��݂������Ȃ��ꍇ
        } else if (!option_.isAllowGlobalSection()) {
            throw new IOException("Some parameter doesn't belong to any section.");
        }

        return ini;
    }

    private boolean isBlankLine(String line) {
        return trim(line).isEmpty();
    }

    private boolean isCommentLine(String line) {
        line = option_.isIgnoreWhiteSpace() ? trim(line) : line;
        for (char c : option_.getCommentChars()) {
            if (line.startsWith(String.valueOf(c))) {
                return true;
            }
        }
        return false;
    }

    private Section parseSectionLine(String line) {
        String validStr = extractValidStr(line);
        String comment = extractComment(line);

        validStr = option_.isIgnoreWhiteSpace() ? trim(validStr) : validStr;
        Matcher m = SECTION_PATTERN.matcher(validStr);
        if (m.find()) {
            Section section = new Section(m.group(1), option_.isIgnoreCase());
            if (comment != null) {
                section.setComment(comment);
            }
            return section;
        }
        return null;
    }

    private Parameter parseParameterLine(String line) {
        String validStr = extractValidStr(line);
        String comment = extractComment(line);

        int idx = indexOfDelimiter(validStr);
        if (-1 < idx) {
            String key = validStr.substring(0, idx);
            String value = validStr.substring(idx + 1);
            Parameter parameter = null;
            if (option_.isIgnoreWhiteSpace()) {
                parameter = new Parameter(trim(key), value);
            } else {
                parameter = new Parameter(key, value);
            }
            if (comment != null) {
                parameter.setComment(comment);
            }
            return parameter;
        }
        return null;
    }

    private int indexOfDelimiter(String str) {
        int index = -1;
        for (char c : option_.getDelimiters()) {
            int idx = str.indexOf(c);
            if (idx < 0) {
                continue;
            }
            if (index == -1 || idx < index) {
                index = idx;
            }
        }
        return index;
    }

    private Section addSection(IniFile ini, Section current, Section next, List<String> comments) throws IOException {
        // �Z�N�V�������d�����Ă���ꍇ
        if (ini.hasSection(next.getName())) {
            switch (option_.getDuplicateSectionBehavior()) {
                case ERROR:
                    throw new IOException(next.getName() + " section is duplicate.");
                case IGNORE_FORMER:
                    current = next;
                    setComment(current, comments);
                    ini.setSection(current);
                    break;
                case IGNORE_LATTER:
                    current = null;
                    break;
                case MERGE:
                    current = ini.getSection(next.getName());
                    for (String comment : comments) {
                        current.addComment(comment);
                    }
                    for (String comment : next.getComments()) {
                        current.addComment(comment);
                    }
                    break;
                default:
                    break;
            }
        } else {
            current = next;
            setComment(current, comments);
            ini.addSection(current);
        }
        return current;
    }

    private void addParameter(Section section, Parameter parameter, List<String> comments) throws IOException {
        // �����ȃZ�N�V�����̏ꍇ
        if (section == null) {
            return;
        }

        // �L�[���d�����Ă���ꍇ
        if (section.hasParameter(parameter.getKey())) {
            switch (option_.getDuplicateKeyBehavior()) {
                case ERROR:
                    throw new IOException(parameter.getKey() + " key is duplicate.");
                case IGNORE_FORMER:
                    setComment(parameter, comments);
                    section.setParameter(parameter);
                    break;
                case IGNORE_LATTER:
                    break;
                case MULTI_VALUES:
                    Parameter p = section.getParameter(parameter.getKey());
                    p.addValue(parameter.getValue());
                    for (String comment : comments) {
                        p.addComment(comment);
                    }
                    for (String comment : parameter.getComments()) {
                        p.addComment(comment);
                    }
                    break;
                default:
                    break;
            }
        } else {
            setComment(parameter, comments);
            section.addParameter(parameter);
        }
    }

    private String extractValidStr(String line) {
        if (!option_.isAllowMiddleOfLineComment()) {
            return line;
        }
        int idx = indexOfCommentChar(line);
        return -1 < idx ? trimRight(line.substring(0, idx)) : line;
    }

    private String extractComment(String line) {
        if (!option_.isAllowMiddleOfLineComment()) {
            return null;
        }
        int idx = indexOfCommentChar(line);
        return -1 < idx ? line.substring(idx) : null;
    }

    private int indexOfCommentChar(String str) {
        int index = -1;
        for (char c : option_.getCommentChars()) {
            int idx = str.indexOf(c);
            if (idx < 0) {
                continue;
            }
            if (index == -1 || idx < index) {
                index = idx;
            }
        }
        return index;
    }

    private void setComment(Section section, List<String> comments) {
        if (section.hasComment()) {
            comments.addAll(section.getComments());
            section.clearComment();
        }
        for (String comment : comments) {
            section.addComment(comment);
        }
    }

    private void setComment(Parameter parameter, List<String> comments) {
        if (parameter.hasComment()) {
            comments.addAll(parameter.getComments());
            parameter.clearComment();
        }
        for (String comment : comments) {
            parameter.addComment(comment);
        }
    }

    private String trim(String str) {
        return str.replaceAll("^" + option_.getWhiteSpaceRegex(), "")
                .replaceAll(option_.getWhiteSpaceRegex() + "$", "");
    }

    private String trimRight(String str) {
        return str.replaceAll(option_.getWhiteSpaceRegex() + "$", "");
    }
}