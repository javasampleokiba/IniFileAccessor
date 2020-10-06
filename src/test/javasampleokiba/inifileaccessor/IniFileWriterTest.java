package test.javasampleokiba.inifileaccessor;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import javasampleokiba.inifileaccessor.IniFile;
import javasampleokiba.inifileaccessor.IniFileWriteOption;
import javasampleokiba.inifileaccessor.IniFileWriter;
import javasampleokiba.inifileaccessor.Parameter;
import javasampleokiba.inifileaccessor.Section;

/**
 * JUnit5によるIniFileWriterクラスのテスト
 */
public class IniFileWriterTest extends TestBase {

    @Test
    public void testGetSetOption() throws IOException {
        IniFileWriteOption option = new IniFileWriteOption();
        IniFileWriter writer = new IniFileWriter();
        writer.setOption(option);
        assertSame(option, writer.getOption());

        assertThrows(NullPointerException.class, () -> writer.setOption(null));
    }

    @Test
    public void testWriteEmpty() throws IOException {
        IniFile ini = new IniFile();
        assertIterableEquals(Arrays.asList(), write(ini, new IniFileWriteOption()));
    }

    @Test
    public void testWriteOnlyHeaderComment() throws IOException {
        IniFile ini = new IniFile();
        ini.addComment(";This is");
        ini.addComment(";a header comment.");

        assertIterableEquals(Arrays.asList(
                ";This is",
                ";a header comment.",
                ""
                ), write(ini, new IniFileWriteOption()));
    }

    @Test
    public void testWriteOnlyGlobalSection() throws IOException {
        IniFile ini = new IniFile();
        Section section = new Section(null);
        section.setComment(";comment");     // グローバルセクションはコメントを持たない
        section.addParameter(new Parameter("key0", "0", ";key0 comment"));
        ini.addSection(section);

        assertIterableEquals(Arrays.asList(
                ";key0 comment",
                "key0=0"
                ), write(ini, new IniFileWriteOption()));
    }

    @Test
    public void testWritePattern1() throws IOException {
        IniFile ini = new IniFile();
        {
            Section section = new Section("section1");
            section.addParameter(new Parameter("key1", "1", ";key1 comment"));
            {
                Parameter param = new Parameter("key2", "2");
                param.addValue("ABC");
                param.addValue("abc");
                section.addParameter(param);
            }
            section.addParameter(new Parameter("key3", "\r\nABC\nabc"));
            ini.addSection(section);
        }
        {
            Section section = new Section("");
            ini.addSection(section);
        }
        {
            Section section = new Section(" \t\t ");
            ini.addSection(section);
        }
        {
            Section section = new Section(";abc");
            ini.addSection(section);
        }
        {
            Section section = new Section("key1=1");
            ini.addSection(section);
        }
        {
            Section section = new Section("[se[cti]on]");
            ini.addSection(section);
        }
        {
            Section section = new Section("section2");
            section.setComment("#section2 comment");    // コメント文字が誤っていても出力可。ただし、読み込み時には不明行判定になる
            section.addParameter(new Parameter("", ""));
            section.addParameter(new Parameter(" \t", ""));
            section.addParameter(new Parameter("key", "==="));
            section.addParameter(new Parameter("k;ey", ""));
            section.addParameter(new Parameter(" ;key", ""));
            section.addParameter(new Parameter("[key]", ""));
            ini.addSection(section);
        }

        assertIterableEquals(Arrays.asList(
                "[section1]",
                ";key1 comment",
                "key1=1",
                "key2=2",
                "key2=ABC",
                "key2=abc",
                "key3=\\",
                "ABC\\",
                "abc",
                "",
                "[]",
                "",
                "[ \t\t ]",
                "",
                "[;abc]",
                "",
                "[key1=1]",
                "",
                "[[se[cti]on]]",
                "",
                "#section2 comment",
                "[section2]",
                "=",
                " \t=",
                "key====",
                "k;ey=",
                " ;key=",
                "[key]="
                ), write(ini, new IniFileWriteOption()));
    }

    @Test
    public void testWritePattern2() throws IOException {
        IniFile ini = new IniFile();
        ini.addComment(";This is");
        ini.addComment(";a header comment.");
        {
            Section section = new Section(null);
            section.setComment(";comment");     // グローバルセクションはコメントを持たない
            section.addParameter(new Parameter("key0", "0", ";key0 comment"));
            ini.addSection(section);
        }
        {
            Section section = new Section("section1");
            section.setComment(";section1 comment");
            ini.addSection(section);
        }
        {
            Section section = new Section("section2");
            section.addParameter(new Parameter("key1", "1", ";key1 comment"));
            section.addParameter(new Parameter("key2", "2"));
            section.addParameter(new Parameter("key3", ""));
            ini.addSection(section);
        }

        assertIterableEquals(Arrays.asList(
                ";This is",
                ";a header comment.",
                "",
                ";key0 comment",
                "key0=0",
                "",
                ";section1 comment",
                "[section1]",
                "",
                "[section2]",
                ";key1 comment",
                "key1=1",
                "key2=2",
                "key3="
                ), write(ini, new IniFileWriteOption()));
    }
}