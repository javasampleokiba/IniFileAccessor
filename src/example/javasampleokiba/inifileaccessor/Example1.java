package example.javasampleokiba.inifileaccessor;

import java.io.IOException;

import javasampleokiba.inifileaccessor.IniFile;
import javasampleokiba.inifileaccessor.IniFileReader;
import javasampleokiba.inifileaccessor.Parameter;
import javasampleokiba.inifileaccessor.ParameterAccessor;
import javasampleokiba.inifileaccessor.Section;

/**
 * IniFileAccessor�̎��s��1
 */
public class Example1 {

    public static void main(String[] args) throws IOException {
        example1_1();
        example1_2();
    }

    /*
     * INI�t�@�C����ǂݍ��݁A���e���o�͂����
     */
    private static void example1_1() {
        System.out.println("==========example1_1==========");
        try {
            IniFileReader r = new IniFileReader();
            IniFile ini = r.read("examples/example.ini");

            // ���ׂẴZ�N�V�������o��
            for (Section s : ini.getSections()) {
                if (s.hasComment()) {
                    System.out.println("---SECTION COMMENT---");
                    for (String c : s.getComments()) {
                        System.out.println(c);
                    }
                }
                System.out.println("---SECTION NAME---");
                System.out.println(s.getName());

                // ���ׂẴp�����[�^���o��
                for (Parameter p : s.getParameters()) {
                    if (p.hasComment()) {
                        System.out.println("---PARAMETER COMMENT---");
                        for (String c : p.getComments()) {
                            System.out.println(c);
                        }
                    }
                    System.out.println("---PARAMETER---");
                    System.out.println(p.getKey() + " = " + p.getValue());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * INI�t�@�C����ǂݍ��݁AParameterAccessor�I�u�W�F�N�g����ē��e���擾�����
     */
    private static void example1_2() throws IOException {
        System.out.println("==========example1_2==========");
        try {
            IniFileReader r = new IniFileReader();
            IniFile ini = r.read("examples/example.ini");
            ParameterAccessor pa = new ParameterAccessor(ini);

            System.out.println("���ׂẴZ�N�V������: " + pa.getSectionNames());
            System.out.println("section1�̂��ׂẴL�[: " + pa.getKeys("section1"));
            System.out.println("section1��foo�̒l: " + pa.getInt("section1", "foo"));
            System.out.println("section1��bar�̒l: " + pa.get("section1", "bar"));
            System.out.println("section1��baz�̒l: " + pa.getDouble("section1", "baz"));
            System.out.println("section2�̂��ׂẴp�����[�^: " + pa.getParameterMap("section2"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}