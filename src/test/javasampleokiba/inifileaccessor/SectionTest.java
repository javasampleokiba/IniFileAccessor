package test.javasampleokiba.inifileaccessor;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import javasampleokiba.inifileaccessor.IniFile;
import javasampleokiba.inifileaccessor.IniFileReadOption;
import javasampleokiba.inifileaccessor.Parameter;
import javasampleokiba.inifileaccessor.Section;

/**
 * JUnit5によるSectionクラスのテスト
 */
public class SectionTest extends TestBase {

    @Test
    public void testIsGlobalSection() throws IOException {
        String[] lines = {
                "key=1",
                "[section1]",
                "key1=2",
        };
        IniFile ini = read(lines);
        assertEquals(true, ini.getSection(null).isGlobalSection());
        assertEquals(false, ini.getSection("section1").isGlobalSection());
    }

    @Test
    public void testGetName() throws IOException {
        String[] lines = {
                "key=1",
                "[section1]",
                "key1=2",
                "[  section2  ]",
                "[]",
        };
        IniFile ini = read(lines);
        assertTrue(ini.hasSection(null));
        assertTrue(ini.hasSection("section1"));
        assertTrue(ini.hasSection("  section2  "));
        assertTrue(ini.hasSection(""));
    }

    @Test
    public void testHasComment() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=1",
                ";This is a section comment.",
                "[section2]",
                "key2=2",
        };
        IniFile ini = read(lines);
        assertEquals(false, ini.getSection("section1").hasComment());
        assertEquals(true, ini.getSection("section2").hasComment());
    }

    @Test
    public void testGetComments() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=1",
                ";This is",
                ";a section comment.",
                "[section2]",
                "key2=2",
        };
        IniFile ini = read(lines);
        assertIterableEquals(Arrays.asList(), ini.getSection("section1").getComments());
        assertIterableEquals(Arrays.asList(";This is", ";a section comment."), ini.getSection("section2").getComments());
    }

    @Test
    public void testSetComment() throws IOException {
        String[] lines = {
                "",
                ";This is",
                ";a section comment.",
                "[section1]",
                "key1=1",
        };
        IniFile ini = read(lines);
        Section section = ini.getSection("section1");
        section.setComment(";This is a section comment.");
        assertIterableEquals(Arrays.asList(";This is a section comment."), section.getComments());

        assertThrows(NullPointerException.class, () -> section.setComment(null));
    }

    @Test
    public void testAddComment() throws IOException {
        String[] lines = {
                "",
                ";This is",
                ";a section comment.",
                "[section1]",
                "key1=1",
        };
        IniFile ini = read(lines);
        Section section = ini.getSection("section1");
        section.addComment(";Added comment1.");
        section.addComment(";Added comment2.");
        assertIterableEquals(Arrays.asList(";This is", ";a section comment.", ";Added comment1.", ";Added comment2."),
                section.getComments());

        assertThrows(NullPointerException.class, () -> section.addComment(null));
    }

    @Test
    public void testClearComment() throws IOException {
        String[] lines = {
                "",
                ";This is",
                ";a section comment.",
                "[section1]",
                "key1=1",
        };
        IniFile ini = read(lines);
        Section section = ini.getSection("section1");
        section.clearComment();
        assertIterableEquals(Arrays.asList(), section.getComments());
    }

    @Test
    public void testIsIgnoreCase() throws IOException {
        String[] lines = {
                "",
                ";This is",
                ";a section comment.",
                "[section1]",
                "key1=1",
        };
        {
            IniFile ini = read(lines);
            assertEquals(true, ini.getSection("section1").isIgnoreCase());
        }
        {
            IniFileReadOption option = new IniFileReadOption()
                    .setIgnoreCase(false);
            IniFile ini = read(lines, option);
            assertEquals(false, ini.getSection("section1").isIgnoreCase());
        }
    }

    @Test
    public void testCountParameters() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=1",
                "key2=2",
                "key3=3",
                "[section2]",
        };
        IniFile ini = read(lines);
        assertEquals(3, ini.getSection("section1").countParameters());
        assertEquals(0, ini.getSection("section2").countParameters());
    }

    @Test
    public void testHasParameter() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=1",
                "key2=2",
                "key3=3",
        };
        IniFile ini = read(lines);
        Section section = ini.getSection("section1");
        assertEquals(true, section.hasParameter("key1"));
        assertEquals(false, section.hasParameter("key4"));
    }

    @Test
    public void testIndexOfParameter() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=1",
                "key2=2",
                "key3=3",
        };
        IniFile ini = read(lines);
        Section section = ini.getSection("section1");
        assertEquals(0, section.indexOfParameter("key1"));
        assertEquals(1, section.indexOfParameter("key2"));
        assertEquals(2, section.indexOfParameter("key3"));
        assertEquals(-1, section.indexOfParameter("key4"));
    }

    @Test
    public void testGetParameter() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=1",
                "key2=2",
                "key3=3",
        };
        IniFile ini = read(lines);
        Section section = ini.getSection("section1");
        assertEquals(true, section.hasParameter("key1"));
        assertEquals(false, section.hasParameter("key4"));
    }

    @Test
    public void testGetParameters() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=1",
                "key2=2",
                "key3=3",
                "[section2]",
        };
        IniFile ini = read(lines);
        List<Parameter> params = ini.getSection("section1").getParameters();
        assertEquals(3, params.size());
        assertEquals("key1", params.get(0).getKey());
        assertEquals("key2", params.get(1).getKey());
        assertEquals("key3", params.get(2).getKey());
        assertEquals(0, ini.getSection("section2").getParameters().size());
    }

    @Test
    public void testAddParameter() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=1",
                "key2=2",
                "key3=3",
                "[section2]",
                "[section3]",
        };
        IniFile ini = read(lines);
        Section section = ini.getSection("section1");
        section.addParameter(new Parameter("key4", "4"));
        section.addParameter(0, new Parameter("key5", "5"));
        section.addParameter(5, new Parameter("key6", "6"));
        section.addParameter(2, new Parameter("key7", "7"));
        ini.getSection("section2").addParameter(new Parameter("key1", "1"));
        ini.getSection("section3").addParameter(0, new Parameter("key1", "1"));

        List<Parameter> params = section.getParameters();
        assertEquals(7, params.size());
        assertEquals("key5", params.get(0).getKey());
        assertEquals("key1", params.get(1).getKey());
        assertEquals("key7", params.get(2).getKey());
        assertEquals("key2", params.get(3).getKey());
        assertEquals("key3", params.get(4).getKey());
        assertEquals("key4", params.get(5).getKey());
        assertEquals("key6", params.get(6).getKey());
        assertEquals(true, ini.getSection("section2").hasParameter("key1"));
        assertEquals(true, ini.getSection("section3").hasParameter("key1"));
    }

    @Test
    public void testSetParameter() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=1",
                "key2=2",
                "key3=3",
                "[section2]",
        };
        IniFile ini = read(lines);
        Section section = ini.getSection("section1");
        assertEquals("2", section.setParameter(new Parameter("key2", "ABC")).getValue());
        assertEquals("ABC", section.getParameter("key2").getValue());
        assertEquals(null, section.setParameter(new Parameter("key4", "ABC")));
        assertEquals(false, section.hasParameter("key4"));

        assertThrows(NullPointerException.class, () -> ini.getSection("section1").setParameter(null));
    }

    @Test
    public void testRemoveParameter() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=1",
                "key2=2",
                "key3=3",
                "[section2]",
        };
        IniFile ini = read(lines);
        Section section = ini.getSection("section1");
        assertEquals("key1", section.removeParameter("key1").getKey());
        assertEquals(false, section.hasParameter("key1"));
        assertEquals(true, section.hasParameter("key2"));
        assertEquals(true, section.hasParameter("key3"));
        assertEquals("key3", section.removeParameter("key3").getKey());
        assertEquals(true, section.hasParameter("key2"));
        assertEquals(false, section.hasParameter("key3"));
        assertEquals("key2", section.removeParameter("key2").getKey());
        assertEquals(false, section.hasParameter("key2"));
        assertEquals(null, ini.getSection("section2").removeParameter("key"));
        assertEquals(0, ini.getSection("section2").getParameters().size());
    }

    @Test
    public void testClearParameter() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=1",
                "key2=2",
                "key3=3",
                "[section2]",
        };
        IniFile ini = read(lines);
        ini.getSection("section1").clearParameter();
        ini.getSection("section2").clearParameter();
        assertEquals(0, ini.getSection("section1").getParameters().size());
        assertEquals(0, ini.getSection("section2").getParameters().size());
    }
}