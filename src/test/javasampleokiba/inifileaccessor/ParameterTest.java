package test.javasampleokiba.inifileaccessor;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import javasampleokiba.inifileaccessor.IniFile;
import javasampleokiba.inifileaccessor.Section;

/**
 * JUnit5によるParameterクラスのテスト
 */
public class ParameterTest extends TestBase {

    @Test
    public void testGetKey() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=",
                "  key2  =",
                "=",
        };
        IniFile ini = read(lines);
        Section section = ini.getSection("section1");
        assertTrue(section.hasParameter("key1"));
        assertTrue(section.hasParameter("  key2  "));
        assertTrue(section.hasParameter(""));
    }

    @Test
    public void testHasMultiValues() throws IOException {
        {
            String[] lines = {
                    "[section1]",
                    "key1=",
            };
            IniFile ini = read(lines);
            Section section = ini.getSection("section1");
            assertEquals(false, section.getParameter("key1").hasMultiValues());
        }
        {
            String[] lines = {
                    "[section1]",
                    "key1=",
                    "key1=",
                    "key1=",
            };
            IniFile ini = read(lines);
            Section section = ini.getSection("section1");
            assertEquals(true, section.getParameter("key1").hasMultiValues());
        }
    }

    @Test
    public void testGetValue() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=",
                "key2=1",
                "key3=ABC",
                "key4=  ABC  ",
                "key5=====",
                "key2=2",
        };
        IniFile ini = read(lines);
        Section section = ini.getSection("section1");
        assertEquals("", section.getParameter("key1").getValue());
        assertEquals("1", section.getParameter("key2").getValue());
        assertEquals("ABC", section.getParameter("key3").getValue());
        assertEquals("  ABC  ", section.getParameter("key4").getValue());
        assertEquals("====", section.getParameter("key5").getValue());
    }

    @Test
    public void testGetValues() throws IOException {
        {
            String[] lines = {
                    "[section1]",
                    "key1=1",
            };
            IniFile ini = read(lines);
            Section section = ini.getSection("section1");
            assertIterableEquals(Arrays.asList("1"), section.getParameter("key1").getValues());
        }
        {
            String[] lines = {
                    "[section1]",
                    "key1=",
                    "key1=1",
                    "key1=ABC",
            };
            IniFile ini = read(lines);
            Section section = ini.getSection("section1");
            assertIterableEquals(Arrays.asList("", "1", "ABC"), section.getParameter("key1").getValues());
        }
    }

    @Test
    public void testSetValue() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=",
                "key1=1",
        };
        IniFile ini = read(lines);
        Section section = ini.getSection("section1");
        section.getParameter("key1").setValue("ABC");
        assertIterableEquals(Arrays.asList("ABC"), section.getParameter("key1").getValues());

        assertThrows(NullPointerException.class, () -> section.getParameter("key1").setValue(null));
    }

    @Test
    public void testAddValue() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=1",
        };
        IniFile ini = read(lines);
        Section section = ini.getSection("section1");
        section.getParameter("key1").addValue("2");
        section.getParameter("key1").addValue("3");
        assertIterableEquals(Arrays.asList("1", "2", "3"), section.getParameter("key1").getValues());

        assertThrows(NullPointerException.class, () -> section.getParameter("key1").addValue(null));
    }

    @Test
    public void testClearValue() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=1",
                "key1=2",
        };
        IniFile ini = read(lines);
        Section section = ini.getSection("section1");
        section.getParameter("key1").clearValue();
        assertIterableEquals(Arrays.asList(""), section.getParameter("key1").getValues());
    }

    @Test
    public void testHasComment() throws IOException {
        String[] lines = {
                "[section1]",
                ";This is a parameter comment.",
                "key1=1",
                "key2=1",
        };
        IniFile ini = read(lines);
        Section section = ini.getSection("section1");
        assertEquals(true, section.getParameter("key1").hasComment());
        assertEquals(false, section.getParameter("key2").hasComment());
    }

    @Test
    public void testGetComments() throws IOException {
        String[] lines = {
                "[section1]",
                ";This is",
                ";a parameter comment.",
                "key1=1",
                "key2=1",
        };
        IniFile ini = read(lines);
        Section section = ini.getSection("section1");
        assertIterableEquals(Arrays.asList(";This is", ";a parameter comment."), section.getParameter("key1").getComments());
        assertIterableEquals(Arrays.asList(), section.getParameter("key2").getComments());
    }

    @Test
    public void testSetComment() throws IOException {
        String[] lines = {
                "[section1]",
                ";This is",
                ";a parameter comment.",
                "key1=1",
        };
        IniFile ini = read(lines);
        Section section = ini.getSection("section1");
        section.getParameter("key1").setComment(";This is a parameter comment.");
        assertIterableEquals(Arrays.asList(";This is a parameter comment."), section.getParameter("key1").getComments());

        assertThrows(NullPointerException.class, () -> section.getParameter("key1").setComment(null));
    }

    @Test
    public void testAddComment() throws IOException {
        String[] lines = {
                "[section1]",
                ";This is",
                ";a parameter comment.",
                "key1=1",
        };
        IniFile ini = read(lines);
        Section section = ini.getSection("section1");
        section.getParameter("key1").addComment(";Added comment1.");
        section.getParameter("key1").addComment(";Added comment2.");
        assertIterableEquals(Arrays.asList(";This is", ";a parameter comment.", ";Added comment1.", ";Added comment2."),
                section.getParameter("key1").getComments());

        assertThrows(NullPointerException.class, () -> section.getParameter("key1").addComment(null));
    }

    @Test
    public void testClearComment() throws IOException {
        String[] lines = {
                "[section1]",
                ";This is",
                ";a parameter comment.",
                "key1=1",
        };
        IniFile ini = read(lines);
        Section section = ini.getSection("section1");
        section.getParameter("key1").clearComment();
        assertIterableEquals(Arrays.asList(), section.getParameter("key1").getComments());
    }
}