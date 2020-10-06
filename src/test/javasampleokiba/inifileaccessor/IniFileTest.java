package test.javasampleokiba.inifileaccessor;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import javasampleokiba.inifileaccessor.IniFile;
import javasampleokiba.inifileaccessor.IniFileReadOption;
import javasampleokiba.inifileaccessor.Section;

/**
 * JUnit5によるIniFileクラスのテスト
 */
public class IniFileTest extends TestBase {

    @Test
    public void testHasComment() throws IOException {
        {
            String[] lines = {
                    ";This is a header comment.",
                    "[section1]",
                    "key1=1",
            };
            IniFile ini = read(lines);
            assertEquals(true, ini.hasComment());
        }
        {
            String[] lines = {
                    "",
                    ";This is a section comment.",
                    "[section1]",
                    "key1=1",
            };
            IniFile ini = read(lines);
            assertEquals(false, ini.hasComment());
        }
    }

    @Test
    public void testGetComments() throws IOException {
        String[] lines = {
                ";This is",
                ";a header comment.",
                "[section1]",
                "key1=1",
        };
        IniFile ini = read(lines);
        assertIterableEquals(Arrays.asList(";This is", ";a header comment."), ini.getComments());
    }

    @Test
    public void testSetComment() throws IOException {
        String[] lines = {
                ";This is",
                ";a header comment.",
                "[section1]",
                "key1=1",
        };
        IniFile ini = read(lines);
        ini.setComment(";This is a header comment.");
        assertIterableEquals(Arrays.asList(";This is a header comment."), ini.getComments());

        assertThrows(NullPointerException.class, () -> ini.setComment(null));
    }

    @Test
    public void testAddComment() throws IOException {
        String[] lines = {
                ";This is a header comment.",
                "[section1]",
                "key1=1",
        };
        IniFile ini = read(lines);
        ini.addComment(";Added comment1.");
        ini.addComment(";Added comment2.");
        assertIterableEquals(Arrays.asList(";This is a header comment.", ";Added comment1.", ";Added comment2."), ini.getComments());

        assertThrows(NullPointerException.class, () -> ini.addComment(null));
    }

    @Test
    public void testClearComment() throws IOException {
        String[] lines = {
                ";This is a header comment.",
                "[section1]",
                "key1=1",
        };
        IniFile ini = read(lines);
        ini.clearComment();
        assertIterableEquals(Arrays.asList(), ini.getComments());
    }

    @Test
    public void testIsIgnoreCase() throws IOException {
        {
            String[] lines = {
                    ";This is a header comment.",
                    "[section1]",
                    "key1=1",
            };
            IniFile ini = read(lines);
            assertEquals(true, ini.isIgnoreCase());
        }
        {
            String[] lines = {
                    ";This is a header comment.",
                    "[section1]",
                    "key1=1",
            };
            IniFileReadOption option = new IniFileReadOption()
                    .setIgnoreCase(false);
            IniFile ini = read(lines, option);
            assertEquals(false, ini.isIgnoreCase());
        }
    }

    @Test
    public void testHasSection() throws IOException {
        {
            String[] lines = {
                    "key0=0",
                    "[section1]",
                    "key1=1",
            };
            IniFile ini = read(lines);
            assertEquals(true, ini.hasSection(null));
            assertEquals(true, ini.hasSection("section1"));
            assertEquals(false, ini.hasSection("section2"));
        }
        {
            String[] lines = {
                    "[section1]",
                    "key1=1",
            };
            IniFile ini = read(lines);
            assertEquals(false, ini.hasSection(null));
        }
    }

    @Test
    public void testIndexOfSection() throws IOException {
        {
            String[] lines = {
                    "key0=0",
                    "[section1]",
                    "key1=1",
                    "[section2]",
                    "key2=2",
                    "[section3]",
                    "key3=3",
            };
            IniFile ini = read(lines);
            assertEquals(0, ini.indexOfSection(null));
            assertEquals(1, ini.indexOfSection("section1"));
            assertEquals(2, ini.indexOfSection("section2"));
            assertEquals(3, ini.indexOfSection("section3"));
            assertEquals(-1, ini.indexOfSection("section4"));
        }
        {
            String[] lines = {
                    "[section1]",
                    "key1=1",
                    "[section2]",
                    "key2=2",
                    "[section3]",
                    "key3=3",
            };
            IniFile ini = read(lines);
            assertEquals(-1, ini.indexOfSection(null));
            assertEquals(0, ini.indexOfSection("section1"));
            assertEquals(1, ini.indexOfSection("section2"));
            assertEquals(2, ini.indexOfSection("section3"));
            assertEquals(-1, ini.indexOfSection("section4"));
        }
    }

    @Test
    public void testGetSection() throws IOException {
        String[] lines = {
                "key0=0",
                "[section1]",
                "key1=1",
                "[section2]",
                "key2=2",
        };
        IniFile ini = read(lines);
        assertEquals(true, ini.getSection(null).isGlobalSection());
        assertEquals("section1", ini.getSection("section1").getName());
        assertEquals("section2", ini.getSection("section2").getName());
        assertEquals(null, ini.getSection("section3"));
    }

    @Test
    public void testGetSections() throws IOException {
        String[] lines = {
                "key0=0",
                "[section1]",
                "key1=1",
                "[section2]",
                "key2=2",
        };
        IniFile ini = read(lines);
        List<Section> sections = ini.getSections();
        assertEquals(true, sections.get(0).isGlobalSection());
        assertEquals("section1", sections.get(1).getName());
        assertEquals("section2", sections.get(2).getName());
    }

    @Test
    public void testAddSection() throws IOException {
        String[] lines = {
        };
        IniFile ini = read(lines);
        ini.addSection(0, new Section("section1"));
        ini.addSection(1, new Section("section2"));
        ini.addSection(0, new Section(null));
        ini.addSection(new Section("section3"));
        ini.addSection(2, new Section("section4"));

        List<Section> sections = ini.getSections();
        assertEquals(true, sections.get(0).isGlobalSection());
        assertEquals("section1", sections.get(1).getName());
        assertEquals("section4", sections.get(2).getName());
        assertEquals("section2", sections.get(3).getName());
        assertEquals("section3", sections.get(4).getName());

        assertThrows(NullPointerException.class, () -> ini.addSection(null));
        assertThrows(IllegalArgumentException.class, () -> ini.addSection(new Section("section1")));
    }

    @Test
    public void testSetSection() throws IOException {
        String[] lines = {
                "key0=0",
                "[section1]",
                "key1=1",
                "[section2]",
                "key2=2",
        };
        IniFile ini = read(lines);
        {
            Section newSection = new Section(null, ";Updated");
            assertEquals(false, ini.setSection(newSection).hasComment());
            assertIterableEquals(Arrays.asList(";Updated"), ini.getSection(null).getComments());
        }
        {
            Section newSection = new Section("section1", ";Updated");
            assertEquals(false, ini.setSection(newSection).hasComment());
            assertIterableEquals(Arrays.asList(";Updated"), ini.getSection("section1").getComments());
        }
        {
            Section newSection = new Section("section3", ";Updated");
            assertEquals(null, ini.setSection(newSection));
            assertEquals(false, ini.hasSection("section3"));
        }

        assertThrows(NullPointerException.class, () -> ini.setSection(null));
    }

    @Test
    public void testRemoveSection() throws IOException {
        String[] lines = {
                "key0=0",
                "[section1]",
                "key1=1",
                "[section2]",
                "key2=2",
        };
        IniFile ini = read(lines);
        assertEquals(true, ini.removeSection(null).isGlobalSection());
        assertEquals("section1", ini.removeSection("section1").getName());
        assertEquals(null, ini.removeSection("section3"));
        assertEquals(0, ini.indexOfSection("section2"));
    }

    @Test
    public void testClearSection() throws IOException {
        String[] lines = {
                "key0=0",
                "[section1]",
                "key1=1",
                "[section2]",
                "key2=2",
                "[section3]",
                "key3=3",
        };
        IniFile ini = read(lines);
        ini.clearSection();
        assertEquals(0, ini.getSections().size());
    }
}