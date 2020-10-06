package test.javasampleokiba.inifileaccessor;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import javasampleokiba.inifileaccessor.IniFile;
import javasampleokiba.inifileaccessor.IniFileReadOption;
import javasampleokiba.inifileaccessor.IniFileReadOption.UnknownLineBehavior;
import javasampleokiba.inifileaccessor.IniFileReader;
import javasampleokiba.inifileaccessor.Section;

/**
 * JUnit5によるIniFileReaderクラスのテスト
 */
public class IniFileReaderTest extends TestBase {

    @Test
    public void testGetSetOption() throws IOException {
        IniFileReadOption option = new IniFileReadOption();
        IniFileReader reader = new IniFileReader();
        reader.setOption(option);
        assertSame(option, reader.getOption());

        assertThrows(NullPointerException.class, () -> reader.setOption(null));
    }

    @Test
    public void testRead() throws IOException {
        String[] lines = {
                "[]",
                "[ \t\t ]",
                "[;abc]",
                "[key1=1]",
                "[[se[cti]on]]",
                "[section1]",
                "=",
                " \t=",
                "key====",
                "k;ey=",
                " ;key=",
                "[key]=",
        };
        IniFileReadOption option = new IniFileReadOption()
                .setUnknownLineBehavior(UnknownLineBehavior.IGNORE);
        IniFile ini = read(lines, option);
        assertTrue(ini.hasSection(""));
        assertTrue(ini.hasSection(" \t\t "));
        assertTrue(ini.hasSection(";abc"));
        assertTrue(ini.hasSection("key1=1"));
        assertTrue(ini.hasSection("[se[cti]on]"));
        Section section = ini.getSection("section1");
        assertTrue(section.hasParameter(""));
        assertTrue(section.hasParameter(" \t"));
        assertEquals("===", section.getParameter("key").getValue());
        assertTrue(section.hasParameter("k;ey"));
        assertTrue(section.hasParameter(" ;key"));
        assertTrue(section.hasParameter("[key]"));
    }
}