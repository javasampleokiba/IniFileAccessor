package test.javasampleokiba.inifileaccessor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import javasampleokiba.inifileaccessor.IniFile;
import javasampleokiba.inifileaccessor.IniFileReadOption;
import javasampleokiba.inifileaccessor.IniFileReadOption.DuplicateKeyBehavior;
import javasampleokiba.inifileaccessor.IniFileReadOption.DuplicateSectionBehavior;
import javasampleokiba.inifileaccessor.IniFileReadOption.UnknownLineBehavior;
import javasampleokiba.inifileaccessor.LineSeparator;
import javasampleokiba.inifileaccessor.Parameter;
import javasampleokiba.inifileaccessor.Section;

/**
 * JUnit5によるIniFileReadOptionクラスのテスト
 */
public class IniFileReadOptionTest extends TestBase {

    @Test
    public void testIsIgnoreWhiteSpace() throws IOException {
        assertFalse(new IniFileReadOption().isIgnoreWhiteSpace());
        assertTrue(new IniFileReadOption().setIgnoreWhiteSpace(true).isIgnoreWhiteSpace());
    }

    /*
     * 空白文字を無視しない
     */
    @Test
    public void testSetIgnoreWhiteSpaceFalse() throws IOException {
        String[] lines = {
                " ;comment line",
                "[section1]",
                "\t;comment line",
                "key1=1",
                " key2\t= 2",
                "\t\t key3 \t =\t\t3  \t",
                " [section2]",
                "[section3]\t",
        };
        IniFileReadOption option = new IniFileReadOption()
                .setUnknownLineBehavior(UnknownLineBehavior.IGNORE);
        IniFile ini = read(lines, option);
        Section section = ini.getSection("section1");

        // コメント文字の前に空白文字があるため、不明な行と認識され行が無視（読み捨て）される
        assertFalse(section.hasComment());
        assertFalse(section.getParameter("key1").hasComment());

        // セクション行の前後に空白文字があるため、不明な行と認識され行が無視（読み捨て）される
        assertNull(ini.getSection("section2"));
        assertNull(ini.getSection("section3"));

        // キーの前後の空白文字はキーの一部と認識される
        assertTrue(section.hasParameter(" key2\t"));
        assertTrue(section.hasParameter("\t\t key3 \t "));
        
        // 値の前後の空白文字は値の一部と認識される
        assertEquals(" 2", section.getParameter(" key2\t").getValue());
        assertEquals("\t\t3  \t", section.getParameter("\t\t key3 \t ").getValue());
    }

    /*
     * 空白文字を無視する
     */
    @Test
    public void testSetIgnoreWhiteSpaceTrue() throws IOException {
        String[] lines = {
                "\t \t; comment line  \t",
                "[section1]",
                " \t  ;\tcomment line\t\t ",
                "key1=1",
                " key2\t= 2",
                "\t\t key3 \t =\t\t3  \t",
                " [\t \t section2  \t\t]\t",
                "\t  \t[ section3\t] \t ",
        };
        IniFileReadOption option = new IniFileReadOption()
                .setIgnoreWhiteSpace(true);
        IniFile ini = read(lines, option);
        Section section = ini.getSection("section1");

        // コメント文字の前の空白文字は無視し、コメント行と認識される
        assertEquals("\t \t; comment line  \t", section.getComments().get(0));
        assertEquals(" \t  ;\tcomment line\t\t ", section.getParameter("key1").getComments().get(0));

        // セクション行の前後に空白文字は無視し、セクション行と認識される（セクション名の前後の空白文字はセクション名の一部と認識）
        assertTrue(ini.hasSection("\t \t section2  \t\t"));
        assertTrue(ini.hasSection(" section3\t"));

        // キーの前後の空白文字は無視（トリム）される
        assertTrue(section.hasParameter("key2"));
        assertTrue(section.hasParameter("key3"));

        // 値の前後の空白文字は値の一部と認識される
        assertEquals(" 2", section.getParameter("key2").getValue());
        assertEquals("\t\t3  \t", section.getParameter("key3").getValue());
    }

    @Test
    public void testGetWhiteSpaceRegex() throws IOException {
        assertEquals("\\s+", new IniFileReadOption().getWhiteSpaceRegex());
        assertEquals("foo", new IniFileReadOption().setWhiteSpaceRegex("foo").getWhiteSpaceRegex());
    }

    @Test
    public void testSetWhiteSpaceRegex() throws IOException {
        String[] lines = {
                "aaa; comment line",
                "bc[section1]ca",
                "abckey1cba=ab1c",
        };
        IniFileReadOption option = new IniFileReadOption()
                .setIgnoreWhiteSpace(true)
                .setWhiteSpaceRegex("[abc]+");
        IniFile ini = read(lines, option);

        assertTrue(ini.hasSection("section1"));
        Section section = ini.getSection("section1");

        assertEquals("aaa; comment line", section.getComments().get(0));
        assertTrue(section.hasParameter("key1"));
    }

    @Test
    public void testIsIgnoreCase() throws IOException {
        assertTrue(new IniFileReadOption().isIgnoreCase());
        assertFalse(new IniFileReadOption().setIgnoreCase(false).isIgnoreCase());
    }

    @Test
    public void testSetIgnoreCaseTrue() throws IOException {
        String[] lines = {
                "[section1]",
                "key=0",
                "[SECTION1]",
                "key1=1",
                "KEY1=2",
        };
        IniFileReadOption option = new IniFileReadOption()
                .setDuplicateSectionBehavior(DuplicateSectionBehavior.IGNORE_FORMER)
                .setDuplicateKeyBehavior(DuplicateKeyBehavior.IGNORE_FORMER);
        IniFile ini = read(lines, option);
        assertTrue(ini.hasSection("SECTION1"));
        assertFalse(ini.getSection("SECTION1").hasParameter("key"));
        assertEquals("2", ini.getSection("SECTION1").getParameter("KEY1").getValue());
    }

    @Test
    public void testSetIgnoreCaseFalse() throws IOException {
        String[] lines = {
                "[section1]",
                "[Section1]",
                "key1=1",
                "keY1=1",
        };
        IniFileReadOption option = new IniFileReadOption()
                .setIgnoreCase(false);
        IniFile ini = read(lines, option);
        assertTrue(ini.hasSection("section1"));
        assertTrue(ini.hasSection("Section1"));
        assertTrue(ini.getSection("Section1").hasParameter("key1"));
        assertTrue(ini.getSection("Section1").hasParameter("keY1"));
    }

    @Test
    public void testGetUnknownLineBehavior() throws IOException {
        assertEquals(UnknownLineBehavior.ERROR, new IniFileReadOption().getUnknownLineBehavior());
        assertEquals(UnknownLineBehavior.IGNORE, new IniFileReadOption()
                .setUnknownLineBehavior(UnknownLineBehavior.IGNORE).getUnknownLineBehavior());
    }

    @Test
    public void testSetUnknownLineBehaviorError() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=1",
                "key2=2",
                "key3",
                "key4=4",
        };
        assertThrows(IOException.class, () -> read(lines, new IniFileReadOption()));
    }

    @Test
    public void testSetUnknownLineBehaviorIgnore() throws IOException {
        String[] lines = {
                "#This is not a comment.",
                "[section1]",
                "key1=1",
                "key2=2",
                "key3",
                " ;This is not a comment.",
                " key4 =4",
                " [section2]",
                "[section3]\t",
        };
        IniFileReadOption option = new IniFileReadOption()
                .setUnknownLineBehavior(UnknownLineBehavior.IGNORE);
        IniFile ini = read(lines, option);
        Section section = ini.getSection("section1");
        assertFalse(section.hasComment());
        assertFalse(section.hasParameter("key3"));
        assertFalse(section.getParameter(" key4 ").hasComment());
        assertFalse(ini.hasSection("section2"));
        assertFalse(ini.hasSection("section3"));
    }

    @Test
    public void testIsAllowGlobalSection() throws IOException {
        assertFalse(new IniFileReadOption().isAllowGlobalSection());
        assertTrue(new IniFileReadOption().setAllowGlobalSection(true).isAllowGlobalSection());
    }

    @Test
    public void testSetAllowGlobalSectionFalse() throws IOException {
        String[] lines = {
                "key=0",
                "[section1]",
                "key1=1",
        };
        assertThrows(IOException.class, () -> read(lines, new IniFileReadOption()));
    }

    @Test
    public void testSetAllowGlobalSectionTrue() throws IOException {
        String[] lines = {
                "key1=1",
                "key2=2",
                "[section1]",
                "key1=1",
        };
        IniFileReadOption option = new IniFileReadOption()
                .setAllowGlobalSection(true);
        IniFile ini = read(lines, option);
        Section section = ini.getSection(null);
        assertTrue(section.isGlobalSection());
        assertTrue(section.hasParameter("key1"));
        assertTrue(section.hasParameter("key2"));
    }

    @Test
    public void testGetDelimiters() throws IOException {
        assertIterableEquals(Arrays.asList('='), new IniFileReadOption().getDelimiters());
        assertIterableEquals(Arrays.asList(':', '|'), new IniFileReadOption().setDelimiters(':', '|').getDelimiters());
    }

    @Test
    public void testSetDelimiters() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=1",
                "key2:2",
                "key3|3",
                "key4  4",
                "key5|: 5",
        };
        IniFileReadOption option = new IniFileReadOption()
                .setDelimiters(':', '|', ' ')
                .setUnknownLineBehavior(UnknownLineBehavior.IGNORE);
        IniFile ini = read(lines, option);
        Section section = ini.getSection("section1");
        assertFalse(section.hasParameter("key1"));
        assertTrue(section.hasParameter("key2"));
        assertTrue(section.hasParameter("key3"));
        assertTrue(section.hasParameter("key4"));
        assertEquals(" 4", section.getParameter("key4").getValue());
        assertTrue(section.hasParameter("key5"));
        assertEquals(": 5", section.getParameter("key5").getValue());
    }

    @Test
    public void testIsAllowMultiLine() throws IOException {
        assertFalse(new IniFileReadOption().isAllowMultiLine());
        assertTrue(new IniFileReadOption().setAllowMultiLine(true).isAllowMultiLine());
    }

    @Test
    public void testSetAllowMultiLineFalse() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=123\\",
                ";456\\",
                "\\",
                "[789]",
                ";0",
        };
        IniFileReadOption option = new IniFileReadOption()
                .setUnknownLineBehavior(UnknownLineBehavior.IGNORE);
        IniFile ini = read(lines, option);
        assertEquals("123\\", ini.getSection("section1").getParameter("key1").getValue());
    }

    @Test
    public void testSetAllowMultiLineTrue() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=123\\",
                ";456\\",
                "\\",
                "[789]",
                ";0",
        };
        IniFileReadOption option = new IniFileReadOption()
                .setAllowMultiLine(true);
        IniFile ini = read(lines, option);
        String lineSeparator = option.getMultiLineSeparator().toString();
        String expected = "123" + lineSeparator +
                ";456" + lineSeparator +
                "" + lineSeparator +
                "[789]";
        assertEquals(expected, ini.getSection("section1").getParameter("key1").getValue());
    }

    @Test
    public void testGetMultiLineSeparator() throws IOException {
        assertEquals(LineSeparator.SYSTEM_DEFAULT, new IniFileReadOption().getMultiLineSeparator());
        assertEquals(LineSeparator.CRLF, new IniFileReadOption()
                .setMultiLineSeparator(LineSeparator.CRLF).getMultiLineSeparator());
        assertEquals(LineSeparator.LF, new IniFileReadOption()
                .setMultiLineSeparator(LineSeparator.LF).getMultiLineSeparator());
    }

    @Test
    public void testSetMultiLineSeparatorCrLf() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=123\\",
                "456",
        };
        IniFileReadOption option = new IniFileReadOption()
                .setAllowMultiLine(true)
                .setMultiLineSeparator(LineSeparator.CRLF);
        IniFile ini = read(lines, option);
        assertEquals("123\r\n456", ini.getSection("section1").getParameter("key1").getValue());
    }

    @Test
    public void testSetMultiLineSeparatorLf() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=123\\",
                "456",
        };
        IniFileReadOption option = new IniFileReadOption()
                .setAllowMultiLine(true)
                .setMultiLineSeparator(LineSeparator.LF);
        IniFile ini = read(lines, option);
        assertEquals("123\n456", ini.getSection("section1").getParameter("key1").getValue());
    }

    @Test
    public void testGetDuplicateSectionBehavior() throws IOException {
        assertEquals(DuplicateSectionBehavior.ERROR, new IniFileReadOption().getDuplicateSectionBehavior());
        assertEquals(DuplicateSectionBehavior.IGNORE_FORMER, new IniFileReadOption()
                .setDuplicateSectionBehavior(DuplicateSectionBehavior.IGNORE_FORMER).getDuplicateSectionBehavior());
        assertEquals(DuplicateSectionBehavior.IGNORE_LATTER, new IniFileReadOption()
                .setDuplicateSectionBehavior(DuplicateSectionBehavior.IGNORE_LATTER).getDuplicateSectionBehavior());
        assertEquals(DuplicateSectionBehavior.MERGE, new IniFileReadOption()
                .setDuplicateSectionBehavior(DuplicateSectionBehavior.MERGE).getDuplicateSectionBehavior());
    }

    @Test
    public void testSetDuplicateSectionBehaviorError() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=1",
                "[section1]",
                "key2=2",
                "key3=3",
        };
        assertThrows(IOException.class, () -> read(lines, new IniFileReadOption()));
    }

    @Test
    public void testSetDuplicateSectionBehaviorIgnoreFormer() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=1",
                "[Section1]",
                "key2=2",
                "key3=3",
        };
        IniFileReadOption option = new IniFileReadOption()
                .setDuplicateSectionBehavior(DuplicateSectionBehavior.IGNORE_FORMER);
        IniFile ini = read(lines, option);
        Section section = ini.getSection("Section1");
        assertEquals("Section1", section.getName());
        assertFalse(section.hasParameter("key1"));
        assertTrue(section.hasParameter("key2"));
        assertTrue(section.hasParameter("key3"));
    }

    @Test
    public void testSetDuplicateSectionBehaviorIgnoreLatter() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=1",
                "[Section1]",
                "key2=2",
                "key3=3",
        };
        IniFileReadOption option = new IniFileReadOption()
                .setDuplicateSectionBehavior(DuplicateSectionBehavior.IGNORE_LATTER);
        IniFile ini = read(lines, option);
        Section section = ini.getSection("section1");
        assertEquals("section1", section.getName());
        assertTrue(section.hasParameter("key1"));
        assertFalse(section.hasParameter("key2"));
        assertFalse(section.hasParameter("key3"));
    }

    @Test
    public void testSetDuplicateSectionBehaviorMerge() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=1",
                "[Section1]",
                "key2=2",
                "key3=3",
        };
        IniFileReadOption option = new IniFileReadOption()
                .setDuplicateSectionBehavior(DuplicateSectionBehavior.MERGE);
        IniFile ini = read(lines, option);
        Section section = ini.getSection("section1");
        assertEquals("section1", section.getName());
        assertTrue(section.hasParameter("key1"));
        assertTrue(section.hasParameter("key2"));
        assertTrue(section.hasParameter("key3"));
    }

    @Test
    public void testGetDuplicateKeyBehavior() throws IOException {
        assertEquals(DuplicateKeyBehavior.ERROR, new IniFileReadOption().getDuplicateKeyBehavior());
        assertEquals(DuplicateKeyBehavior.IGNORE_FORMER, new IniFileReadOption()
                .setDuplicateKeyBehavior(DuplicateKeyBehavior.IGNORE_FORMER).getDuplicateKeyBehavior());
        assertEquals(DuplicateKeyBehavior.IGNORE_LATTER, new IniFileReadOption()
                .setDuplicateKeyBehavior(DuplicateKeyBehavior.IGNORE_LATTER).getDuplicateKeyBehavior());
        assertEquals(DuplicateKeyBehavior.MULTI_VALUES, new IniFileReadOption()
                .setDuplicateKeyBehavior(DuplicateKeyBehavior.MULTI_VALUES).getDuplicateKeyBehavior());
    }

    @Test
    public void testSetDuplicateKeyBehaviorError() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=1",
                "key2=2",
                "[section1]",
                "key2=2",
                "key3=3",
        };
        assertThrows(IOException.class, () -> read(lines, new IniFileReadOption()));
    }

    @Test
    public void testSetDuplicateKeyBehaviorIgnoreFormer() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=1",
                "KEY1=10",
                "key2=2",
                "[section1]",
                "KEY2=20",
                "key3=3",
                "key1=100",
        };
        IniFileReadOption option = new IniFileReadOption()
                .setDuplicateSectionBehavior(DuplicateSectionBehavior.MERGE)
                .setDuplicateKeyBehavior(DuplicateKeyBehavior.IGNORE_FORMER);
        IniFile ini = read(lines, option);
        Section section = ini.getSection("section1");
        {
            Parameter param = section.getParameter("key1");
            assertEquals("key1", param.getKey());
            assertFalse(param.hasMultiValues());
            assertEquals("100", param.getValue());
        }
        {
            Parameter param = section.getParameter("key2");
            assertEquals("KEY2", param.getKey());
            assertFalse(param.hasMultiValues());
            assertEquals("20", param.getValue());
        }
        {
            Parameter param = section.getParameter("key3");
            assertEquals("key3", param.getKey());
            assertFalse(param.hasMultiValues());
            assertEquals("3", param.getValue());
        }
    }

    @Test
    public void testSetDuplicateKeyBehaviorIgnoreLatter() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=1",
                "KEY1=10",
                "key2=2",
                "[section1]",
                "KEY2=20",
                "key3=3",
                "key1=100",
        };
        IniFileReadOption option = new IniFileReadOption()
                .setDuplicateSectionBehavior(DuplicateSectionBehavior.MERGE)
                .setDuplicateKeyBehavior(DuplicateKeyBehavior.IGNORE_LATTER);
        IniFile ini = read(lines, option);
        Section section = ini.getSection("section1");
        {
            Parameter param = section.getParameter("key1");
            assertEquals("key1", param.getKey());
            assertFalse(param.hasMultiValues());
            assertEquals("1", param.getValue());
        }
        {
            Parameter param = section.getParameter("key2");
            assertEquals("key2", param.getKey());
            assertFalse(param.hasMultiValues());
            assertEquals("2", param.getValue());
        }
        {
            Parameter param = section.getParameter("key3");
            assertEquals("key3", param.getKey());
            assertFalse(param.hasMultiValues());
            assertEquals("3", param.getValue());
        }
    }

    @Test
    public void testSetDuplicateKeyBehaviorMultiValues() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=1",
                "KEY1=10",
                "key2=2",
                "[section1]",
                "KEY2=20",
                "key3=3",
                "key1=100",
        };
        IniFileReadOption option = new IniFileReadOption()
                .setDuplicateSectionBehavior(DuplicateSectionBehavior.MERGE)
                .setDuplicateKeyBehavior(DuplicateKeyBehavior.MULTI_VALUES);
        IniFile ini = read(lines, option);
        Section section = ini.getSection("section1");
        {
            Parameter param = section.getParameter("key1");
            assertEquals("key1", param.getKey());
            assertTrue(param.hasMultiValues());
            assertIterableEquals(Arrays.asList("1", "10", "100"), param.getValues());
        }
        {
            Parameter param = section.getParameter("key2");
            assertEquals("key2", param.getKey());
            assertTrue(param.hasMultiValues());
            assertIterableEquals(Arrays.asList("2", "20"), param.getValues());
        }
        {
            Parameter param = section.getParameter("key3");
            assertEquals("key3", param.getKey());
            assertFalse(param.hasMultiValues());
            assertEquals("3", param.getValue());
        }
    }

    @Test
    public void testHasHeaderComment() throws IOException {
        assertFalse(new IniFileReadOption().hasHeaderComment());
        assertTrue(new IniFileReadOption().setHasHeaderComment(true).hasHeaderComment());
    }

    @Test
    public void testSetHasHeaderCommentFalse() throws IOException {
        String[] lines = {
                ";This is a section comment.",
                "[section1]",
                "key1=1",
        };
        IniFile ini = read(lines, new IniFileReadOption());
        assertFalse(ini.hasComment());
    }

    @Test
    public void testSetHasHeaderCommentTrue() throws IOException {
        String[] lines = {
                ";This is",
                ";a header comment.",
                "",
                ";This is a section comment.",
                "[section1]",
                "key1=1",
        };
        IniFileReadOption option = new IniFileReadOption()
                .setHasHeaderComment(true);
        IniFile ini = read(lines, option);
        assertIterableEquals(Arrays.asList(";This is", ";a header comment."), ini.getComments());
    }

    @Test
    public void testGetCommentChars() throws IOException {
        assertIterableEquals(Arrays.asList(';'), new IniFileReadOption().getCommentChars());
        assertIterableEquals(Arrays.asList('#', ';'), new IniFileReadOption().setCommentChars('#', ';').getCommentChars());
    }

    @Test
    public void testSetCommentChars() throws IOException {
        String[] lines = {
                "#This is a section comment.",
                "[section1]",
                "\'This is",
                "\'#a key comment.",
                ";Unknown line",
                "key1=1",
        };
        IniFileReadOption option = new IniFileReadOption()
                .setCommentChars('#', '\'')
                .setUnknownLineBehavior(UnknownLineBehavior.IGNORE);
        IniFile ini = read(lines, option);
        Section section = ini.getSection("section1");
        assertIterableEquals(Arrays.asList("#This is a section comment."), section.getComments());
        assertIterableEquals(Arrays.asList("\'This is", "\'#a key comment."), section.getParameter("key1").getComments());
    }

    @Test
    public void testIsAllowMiddleOfLineComment() throws IOException {
        assertFalse(new IniFileReadOption().isAllowMiddleOfLineComment());
        assertTrue(new IniFileReadOption().setAllowMiddleOfLineComment(true).isAllowMiddleOfLineComment());
    }

    @Test
    public void testSetAllowMiddleOfLineCommentFalse() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=1       ;This is a parameter comment.",
                "key2=;2      ;This is a parameter comment.",
                "key;3=3      ;This is a parameter comment.",
                "[section2]   ;This is a section comment.",
                "[section3]abc;This is a section comment.",
                "[sec;tion4]",
        };
        IniFileReadOption option = new IniFileReadOption()
                .setUnknownLineBehavior(UnknownLineBehavior.IGNORE);
        IniFile ini = read(lines, option);
        Section section = ini.getSection("section1");
        assertEquals("1       ;This is a parameter comment.", section.getParameter("key1").getValue());
        assertEquals(";2      ;This is a parameter comment.", section.getParameter("key2").getValue());
        assertEquals("3      ;This is a parameter comment.", section.getParameter("key;3").getValue());
        assertFalse(ini.hasSection("section2"));
        assertFalse(ini.hasSection("section3"));
        assertTrue(ini.hasSection("sec;tion4"));
    }

    @Test
    public void testSetAllowMiddleOfLineCommentTrue() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=1       #;This is a parameter comment.",
                "key2=;2      ;This is a parameter comment.",
                "key;3=3      ;This is a parameter comment.",
                "[section2]   ;This is a section comment.",
                "[section3]abc;This is a section comment.",
                "[sec;tion4]",
        };
        IniFileReadOption option = new IniFileReadOption()
                .setAllowMiddleOfLineComment(true)
                .setCommentChars(';', '#')
                .setUnknownLineBehavior(UnknownLineBehavior.IGNORE);
        IniFile ini = read(lines, option);
        Section section = ini.getSection("section1");
        assertEquals("1", section.getParameter("key1").getValue());
        assertEquals("#;This is a parameter comment.", section.getParameter("key1").getComments().get(0));
        assertEquals("", section.getParameter("key2").getValue());
        assertEquals(";2      ;This is a parameter comment.", section.getParameter("key2").getComments().get(0));
        assertFalse(section.hasParameter("key;3"));
        assertTrue(ini.hasSection("section2"));
        assertEquals(";This is a section comment.", ini.getSection("section2").getComments().get(0));
        assertFalse(ini.hasSection("section3"));
        assertFalse(ini.hasSection("sec;tion4"));
    }
}