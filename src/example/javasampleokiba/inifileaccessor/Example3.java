package example.javasampleokiba.inifileaccessor;

import java.io.IOException;
import java.nio.file.StandardOpenOption;

import javasampleokiba.inifileaccessor.IniFile;
import javasampleokiba.inifileaccessor.IniFileReadOption;
import javasampleokiba.inifileaccessor.IniFileReader;
import javasampleokiba.inifileaccessor.IniFileWriteOption;
import javasampleokiba.inifileaccessor.IniFileWriter;
import javasampleokiba.inifileaccessor.Parameter;
import javasampleokiba.inifileaccessor.Section;

/**
 * IniFileAccessor�̎��s��3
 */
public class Example3 {

    public static void main(String[] args) throws IOException {
        example3_1();
    }

    /*
     * before.ini��ǂݍ���ŁA�F�X�ƕҏW���s�������after.ini�ɏ������ށB
     */
    private static void example3_1() {
        try {
            // INI�t�@�C���ǂݍ���
            IniFileReadOption ro = new IniFileReadOption()
                    .setHasHeaderComment(true)
                    .setAllowGlobalSection(true)
                    .setAllowMultiLine(true);
            IniFileReader r = new IniFileReader(ro);
            IniFile ini = r.read("examples/before.ini");

            // �O���[�o���Z�N�V�����쐬
            Section globalSection = new Section();
            globalSection.addParameter(new Parameter("globalKey", "0", ";This is a global key!"));
            ini.addSection(globalSection);

            // [Section_1]�Z�N�V�����̐擪�ɐV�����p�����[�^��ǉ�
            Section section1 = ini.getSection("Section_1");
            section1.addParameter(0, new Parameter("newKey", "1"));

            // [Section_2]�Z�N�V�����̃p�����[�^�����ׂč폜
            Section section2 = ini.getSection("Section_2");
            section2.clearParameter();

            // [Section_3]�Z�N�V������ǉ�����
            Section section3 = new Section("Section_3");
            section3.addParameter(new Parameter("newKey", "��\r\n��\r\n��\r\n��\r\n��", ";�����s�̒l�����p�����[�^"));
            ini.addSection(section3);

            // �L�[�ƒl�̃f���~�^���f�t�H���g��'='����':'�ɕύX�B
            // �Z�N�V�����Ԃ�3�s���̋�s��}���B
            // �L�[�Ԃ�1�s���̋�s��}���B
            IniFileWriteOption wo = new IniFileWriteOption()
                    .setDelimiter(':')
                    .setBlankLineBetweenSection(3)
                    .setBlankLineBetweenParameter(1);

            // INI�t�@�C����������
            IniFileWriter w = new IniFileWriter(wo);
            w.write("examples/after.ini", ini, StandardOpenOption.CREATE);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}