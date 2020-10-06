package example.javasampleokiba.inifileaccessor;

import java.io.IOException;

import javasampleokiba.inifileaccessor.IniFile;
import javasampleokiba.inifileaccessor.IniFileReadOption;
import javasampleokiba.inifileaccessor.IniFileReader;
import javasampleokiba.inifileaccessor.LineSeparator;
import javasampleokiba.inifileaccessor.Parameter;
import javasampleokiba.inifileaccessor.Section;
import javasampleokiba.inifileaccessor.IniFileReadOption.DuplicateKeyBehavior;
import javasampleokiba.inifileaccessor.IniFileReadOption.DuplicateSectionBehavior;
import javasampleokiba.inifileaccessor.IniFileReadOption.UnknownLineBehavior;

/**
 * IniFileAccessor�̎��s��2
 */
public class Example2 {

    public static void main(String[] args) {
        example2_1();
        example2_2();
        example2_3();
        example2_4();
        example2_5();
        example2_6();
        example2_7();
        example2_8();
        example2_9();
    }

    /*
     * �f�t�H���g�̋󔒕����ɉ����đS�p�X�y�[�X���󔒕����Ƃ݂Ȃ��A�󔒕����𖳎�����ݒ�B
     * ����ɂ��s�ɋ󔒕������܂܂�Ă��Ă��G���[�ƂȂ炸�ɖ�������܂��B
     */
    private static void example2_1() {
        System.out.println("==========example2_1==========");
        try {
            IniFileReadOption option = new IniFileReadOption()
                    .setIgnoreWhiteSpace(true)
                    .setWhiteSpaceRegex("[\\s�@]+");

            IniFileReader r = new IniFileReader(option);
            IniFile ini = r.read("examples/example1.ini");

            print(ini);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * �啶������������ʂ���ݒ�B
     * ����ɂ��啶���������̈Ⴂ������Z�N�V������L�[�͕ʂ��̂Ƃ݂Ȃ���܂��B
     */
    private static void example2_2() {
        System.out.println("==========example2_2==========");
        try {
            IniFileReadOption option = new IniFileReadOption()
                    .setIgnoreCase(false);

            IniFileReader r = new IniFileReader(option);
            IniFile ini = r.read("examples/example2.ini");

            print(ini);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * ��͂ł��Ȃ��s���ȍs�𖳎�����ݒ�B
     * ���̐ݒ���s���ƁA�s���ȍs�����o�����ꍇ���G���[�Ƃ͂Ȃ炸�P�ɖ�������܂��B
     */
    private static void example2_3() {
        System.out.println("==========example2_3==========");
        try {
            IniFileReadOption option = new IniFileReadOption()
                    .setUnknownLineBehavior(UnknownLineBehavior.IGNORE);

            IniFileReader r = new IniFileReader(option);
            IniFile ini = r.read("examples/example3.ini");

            print(ini);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * �O���[�o���Z�N�V�����̑��݂�������ݒ�ƁA�L�[�ƒl�̃f���~�^������"="��":"�ɂ���ݒ�B
     * �O���[�o���Z�N�V�����̑��݂�������ƁAexample4.ini��globalKey�̂悤�Ȃ�����̃Z�N�V�����s�����O��
     * ��`���ꂽ�p�����[�^�̓O���[�o���Z�N�V�����ɏ������܂��B
     * �O���[�o���Z�N�V�����͖��O�������Ȃ����߃Z�N�V��������null�ƂȂ�܂��B
     */
    private static void example2_4() {
        System.out.println("==========example2_4==========");
        try {
            IniFileReadOption option = new IniFileReadOption()
                    .setAllowGlobalSection(true)
                    .setDelimiters('=', ':');

            IniFileReader r = new IniFileReader(option);
            IniFile ini = r.read("examples/example4.ini");

            print(ini);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * �p�����[�^�������s�̒l�������Ƃ�������ݒ�ƁA�����s�̉��s�R�[�h��LF�Ƃ���ݒ�B
     * �l�̖�����"\"���u����Ă���ꍇ�ɕ����s�Ƃ݂Ȃ��܂��B
     */
    private static void example2_5() {
        System.out.println("==========example2_5==========");
        try {
            IniFileReadOption option = new IniFileReadOption()
                    .setAllowMultiLine(true)
                    .setMultiLineSeparator(LineSeparator.LF);

            IniFileReader r = new IniFileReader(option);
            IniFile ini = r.read("examples/example5.ini");

            print(ini);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * �Z�N�V�����ƃL�[���d�����Ă���ꍇ�ɐ�Ɍ��ꂽ���̂𖳎�����ݒ�B
     */
    private static void example2_6() {
        System.out.println("==========example2_6==========");
        try {
            IniFileReadOption option = new IniFileReadOption()
                    .setDuplicateSectionBehavior(DuplicateSectionBehavior.IGNORE_FORMER)
                    .setDuplicateKeyBehavior(DuplicateKeyBehavior.IGNORE_FORMER);

            IniFileReader r = new IniFileReader(option);
            IniFile ini = r.read("examples/example6.ini");

            print(ini);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * �Z�N�V�����ƃL�[���d�����Ă���ꍇ�Ɍ�Ɍ��ꂽ���̂𖳎�����ݒ�B
     */
    private static void example2_7() {
        System.out.println("==========example2_7==========");
        try {
            IniFileReadOption option = new IniFileReadOption()
                    .setDuplicateSectionBehavior(DuplicateSectionBehavior.IGNORE_LATTER)
                    .setDuplicateKeyBehavior(DuplicateKeyBehavior.IGNORE_LATTER);

            IniFileReader r = new IniFileReader(option);
            IniFile ini = r.read("examples/example6.ini");

            print(ini);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * �Z�N�V�������d�����Ă���ꍇ�ɂ̓}�[�W���A�L�[���d�����Ă���ꍇ�ɂ͕����̒l�����p�����[�^�Ƃ���ݒ�B
     */
    private static void example2_8() {
        System.out.println("==========example2_8==========");
        try {
            IniFileReadOption option = new IniFileReadOption()
                    .setDuplicateSectionBehavior(DuplicateSectionBehavior.MERGE)
                    .setDuplicateKeyBehavior(DuplicateKeyBehavior.MULTI_VALUES);

            IniFileReader r = new IniFileReader(option);
            IniFile ini = r.read("examples/example6.ini");

            print(ini);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * �w�b�_�R�����g����̐ݒ�A�R�����g������";"��"#"�ɂ���ݒ�A�s�̓r������̃R�����g��������ݒ�B
     */
    private static void example2_9() {
        System.out.println("==========example2_9==========");
        try {
            IniFileReadOption option = new IniFileReadOption()
                    .setHasHeaderComment(true)
                    .setCommentChars(';', '#')
                    .setAllowMiddleOfLineComment(true);

            IniFileReader r = new IniFileReader(option);
            IniFile ini = r.read("examples/example7.ini");

            print(ini);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void print(IniFile ini) {
        // �w�b�_�R�����g������Ώo��
        if (ini.hasComment()) {
            System.out.println("---HEADER COMMENT---");
            for (String c : ini.getComments()) {
                System.out.println(c);
            }
        }

        // ���ׂẴZ�N�V�������o��
        for (Section s : ini.getSections()) {
            // �Z�N�V�����R�����g������Ώo��
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
                // �p�����[�^�R�����g������Ώo��
                if (p.hasComment()) {
                    System.out.println("---PARAMETER COMMENT---");
                    for (String c : p.getComments()) {
                        System.out.println(c);
                    }
                }
                System.out.println("---PARAMETER---");
                // �����̒l�������H
                if (p.hasMultiValues()) {
                    System.out.println(p.getKey() + " = " + p.getValues());
                } else {
                    System.out.println(p.getKey() + " = " + p.getValue());
                }
            }
        }
    }
}