package test.javasampleokiba.inifileaccessor;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import org.junit.jupiter.api.Test;

import javasampleokiba.inifileaccessor.IniFile;
import javasampleokiba.inifileaccessor.ParameterAccessor;

/**
 * JUnit5によるParameterAccessorクラスのテスト
 */
public class ParameterAccessorTest extends TestBase {

    @Test
    public void testGetSectionNames() throws IOException {
        String[] lines = {
                "key=1",
                "[section1]",
                "key1=2",
                "[  section2  ]",
                "[]",
                "[section1]",
        };
        IniFile ini = read(lines);
        ParameterAccessor pa = new ParameterAccessor(ini);
        assertIterableEquals(Arrays.asList(null, "section1", "  section2  ", ""), pa.getSectionNames());
    }

    @Test
    public void testGetParameterMap() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=1",
                "key2=2",
                "key3=3",
                "key1=ABC",
                "[section2]",
        };
        IniFile ini = read(lines);
        ParameterAccessor pa = new ParameterAccessor(ini);
        {
            Map<String, String> map = pa.getParameterMap("section1");
            assertEquals("1", map.get("key1"));
            assertEquals("2", map.get("key2"));
            assertEquals("3", map.get("key3"));
            assertEquals(3, map.size());
        }
        {
            Map<String, String> map = pa.getParameterMap("section2");
            assertEquals(0, map.size());
        }
        {
            Map<String, String> map = pa.getParameterMap("section3");
            assertEquals(null, map);
        }
    }

    @Test
    public void testGetKeys() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=1",
                "key2=2",
                "key3=3",
                "key1=ABC",
                "[section2]",
        };
        IniFile ini = read(lines);
        ParameterAccessor pa = new ParameterAccessor(ini);
        assertIterableEquals(Arrays.asList("key1", "key2", "key3"), pa.getKeys("section1"));
        assertIterableEquals(Arrays.asList(), pa.getKeys("section2"));
        assertEquals(null, pa.getKeys("section3"));
    }

    @Test
    public void testHasMultiValues() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=1",
                "key2=2",
                "key1=ABC",
        };
        IniFile ini = read(lines);
        ParameterAccessor pa = new ParameterAccessor(ini);
        assertEquals(true, pa.hasMultiValues("section1", "key1"));
        assertEquals(false, pa.hasMultiValues("section1", "key2"));
        assertEquals(false, pa.hasMultiValues("section1", "key3"));
    }

    @Test
    public void testGet() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=1",
                "key2=ABC",
                "key3=  ",
                "key4=",
                "key1=2",
        };
        IniFile ini = read(lines);
        ParameterAccessor pa = new ParameterAccessor(ini);
        assertEquals("1", pa.get("section1", "key1"));
        assertEquals("ABC", pa.get("section1", "key2"));
        assertEquals("  ", pa.get("section1", "key3"));
        assertEquals("", pa.get("section1", "key4", "DEF"));
        assertEquals(null, pa.get("section1", "key5"));
        assertEquals("DEF", pa.get("section1", "key5", "DEF"));
    }

    @Test
    public void testGetMulti() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=1",
                "key1=ABC",
                "key1=  ",
                "key1=",
                "key2=2",
        };
        IniFile ini = read(lines);
        ParameterAccessor pa = new ParameterAccessor(ini);
        assertIterableEquals(Arrays.asList("1", "ABC", "  ", ""), pa.getMulti("section1", "key1"));
        assertIterableEquals(Arrays.asList("2"), pa.getMulti("section1", "key2"));
        assertIterableEquals(Arrays.asList(), pa.getMulti("section1", "key3"));
    }

    @Test
    public void testGetBoolean() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=true",
                "key2=FALSE",
                "key3=True",
                "key4=falsE",
                "key1=false",
                "key6= true\t",
        };
        IniFile ini = read(lines);
        ParameterAccessor pa = new ParameterAccessor(ini);
        assertEquals(true, pa.getBoolean("section1", "key1"));
        assertEquals(false, pa.getBoolean("section1", "key2"));
        assertEquals(true, pa.getBoolean("section1", "key3"));
        assertEquals(false, pa.getBoolean("section1", "key4"));
        assertEquals(false, pa.getBoolean("section1", "key5"));
        assertEquals(true, pa.getBoolean("section1", "key6"));
        pa.setAutoTrim(false);
        assertEquals(false, pa.getBoolean("section1", "key6"));
    }

    @Test
    public void testGetMultiBoolean() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=true",
                "key1=FALSE",
                "key1=True",
                "key2=falsE",
                "key4= true\t",
        };
        IniFile ini = read(lines);
        ParameterAccessor pa = new ParameterAccessor(ini);
        assertIterableEquals(Arrays.asList(true, false, true), pa.getMultiBoolean("section1", "key1"));
        assertIterableEquals(Arrays.asList(false), pa.getMultiBoolean("section1", "key2"));
        assertIterableEquals(Arrays.asList(), pa.getMultiBoolean("section1", "key3"));
        assertIterableEquals(Arrays.asList(true), pa.getMultiBoolean("section1", "key4"));
        pa.setAutoTrim(false);
        assertIterableEquals(Arrays.asList(false), pa.getMultiBoolean("section1", "key4"));
    }

    @Test
    public void testGetByte() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=127",
                "key1=1",
                "key2=2",
                "key3=128",
                "key5= 1\t",
        };
        IniFile ini = read(lines);
        ParameterAccessor pa = new ParameterAccessor(ini);
        assertEquals((byte) 127, pa.getByte("section1", "key1"));
        assertEquals((byte) 2, pa.getByte("section1", "key2", (byte) -1));
        assertEquals((byte) 0, pa.getByte("section1", "key3"));
        assertEquals((byte) -1, pa.getByte("section1", "key3", (byte) -1));
        assertEquals((byte) 0, pa.getByte("section1", "key4"));
        assertEquals((byte) -1, pa.getByte("section1", "key4", (byte) -1));
        assertEquals((byte) 1, pa.getByte("section1", "key5"));
        pa.setAutoTrim(false);
        assertEquals((byte) 0, pa.getByte("section1", "key5"));
    }

    @Test
    public void testGetMultiByte() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=127",
                "key1=1",
                "key2=2",
                "key3=128",
                "key5= 1\t",
        };
        IniFile ini = read(lines);
        ParameterAccessor pa = new ParameterAccessor(ini);
        assertIterableEquals(Arrays.asList((byte) 127, (byte) 1), pa.getMultiByte("section1", "key1", (byte) -1));
        assertIterableEquals(Arrays.asList((byte) 2), pa.getMultiByte("section1", "key2", (byte) -1));
        assertIterableEquals(Arrays.asList((byte) -1), pa.getMultiByte("section1", "key3", (byte) -1));
        assertIterableEquals(Arrays.asList(), pa.getMultiByte("section1", "key4", (byte) -1));
        assertIterableEquals(Arrays.asList((byte) 1), pa.getMultiByte("section1", "key5", (byte) -1));
        pa.setAutoTrim(false);
        assertIterableEquals(Arrays.asList((byte) -1), pa.getMultiByte("section1", "key5", (byte) -1));
    }

    @Test
    public void testGetShort() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=32767",
                "key1=1",
                "key2=2",
                "key3=32768",
                "key5= 1\t",
        };
        IniFile ini = read(lines);
        ParameterAccessor pa = new ParameterAccessor(ini);
        assertEquals((short) 32767, pa.getShort("section1", "key1"));
        assertEquals((short) 2, pa.getShort("section1", "key2", (short) -1));
        assertEquals((short) 0, pa.getShort("section1", "key3"));
        assertEquals((short) -1, pa.getShort("section1", "key3", (short) -1));
        assertEquals((short) 0, pa.getShort("section1", "key4"));
        assertEquals((short) -1, pa.getShort("section1", "key4", (short) -1));
        assertEquals((short) 1, pa.getShort("section1", "key5"));
        pa.setAutoTrim(false);
        assertEquals((short) 0, pa.getShort("section1", "key5"));
    }

    @Test
    public void testGetMultiShort() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=32767",
                "key1=1",
                "key2=2",
                "key3=32768",
                "key5= 1\t",
        };
        IniFile ini = read(lines);
        ParameterAccessor pa = new ParameterAccessor(ini);
        assertIterableEquals(Arrays.asList((short) 32767, (short) 1), pa.getMultiShort("section1", "key1", (short) -1));
        assertIterableEquals(Arrays.asList((short) 2), pa.getMultiShort("section1", "key2", (short) -1));
        assertIterableEquals(Arrays.asList((short) -1), pa.getMultiShort("section1", "key3", (short) -1));
        assertIterableEquals(Arrays.asList(), pa.getMultiShort("section1", "key4", (short) -1));
        assertIterableEquals(Arrays.asList((short) 1), pa.getMultiShort("section1", "key5", (short) -1));
        pa.setAutoTrim(false);
        assertIterableEquals(Arrays.asList((short) -1), pa.getMultiShort("section1", "key5", (short) -1));
    }

    @Test
    public void testGetInt() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=2147483647",
                "key1=1",
                "key2=2",
                "key3=2147483648",
                "key5= 1\t",
        };
        IniFile ini = read(lines);
        ParameterAccessor pa = new ParameterAccessor(ini);
        assertEquals(2147483647, pa.getInt("section1", "key1"));
        assertEquals(2, pa.getInt("section1", "key2", -1));
        assertEquals(0, pa.getInt("section1", "key3"));
        assertEquals(-1, pa.getInt("section1", "key3", -1));
        assertEquals(0, pa.getInt("section1", "key4"));
        assertEquals(-1, pa.getInt("section1", "key4", -1));
        assertEquals(1, pa.getInt("section1", "key5"));
        pa.setAutoTrim(false);
        assertEquals(0, pa.getInt("section1", "key5"));
    }

    @Test
    public void testGetMultiInt() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=2147483647",
                "key1=1",
                "key2=2",
                "key3=2147483648",
                "key5= 1\t",
        };
        IniFile ini = read(lines);
        ParameterAccessor pa = new ParameterAccessor(ini);
        assertIterableEquals(Arrays.asList(2147483647, 1), pa.getMultiInt("section1", "key1", -1));
        assertIterableEquals(Arrays.asList(2), pa.getMultiInt("section1", "key2", -1));
        assertIterableEquals(Arrays.asList(-1), pa.getMultiInt("section1", "key3", -1));
        assertIterableEquals(Arrays.asList(), pa.getMultiInt("section1", "key4", -1));
        assertIterableEquals(Arrays.asList(1), pa.getMultiInt("section1", "key5", -1));
        pa.setAutoTrim(false);
        assertIterableEquals(Arrays.asList(-1), pa.getMultiInt("section1", "key5", -1));
    }

    @Test
    public void testGetLong() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=9223372036854775807",
                "key1=1",
                "key2=2",
                "key3=9223372036854775808",
                "key5= 1\t",
        };
        IniFile ini = read(lines);
        ParameterAccessor pa = new ParameterAccessor(ini);
        assertEquals(9223372036854775807L, pa.getLong("section1", "key1"));
        assertEquals(2L, pa.getLong("section1", "key2", -1L));
        assertEquals(0L, pa.getLong("section1", "key3"));
        assertEquals(-1L, pa.getLong("section1", "key3", -1L));
        assertEquals(0L, pa.getLong("section1", "key4"));
        assertEquals(-1L, pa.getLong("section1", "key4", -1L));
        assertEquals(1L, pa.getLong("section1", "key5"));
        pa.setAutoTrim(false);
        assertEquals(0L, pa.getLong("section1", "key5"));
    }

    @Test
    public void testGetMultiLong() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=9223372036854775807",
                "key1=1",
                "key2=2",
                "key3=9223372036854775808",
                "key5= 1\t",
        };
        IniFile ini = read(lines);
        ParameterAccessor pa = new ParameterAccessor(ini);
        assertIterableEquals(Arrays.asList(9223372036854775807L, 1L), pa.getMultiLong("section1", "key1", -1L));
        assertIterableEquals(Arrays.asList(2L), pa.getMultiLong("section1", "key2", -1L));
        assertIterableEquals(Arrays.asList(-1L), pa.getMultiLong("section1", "key3", -1L));
        assertIterableEquals(Arrays.asList(), pa.getMultiLong("section1", "key4", -1L));
        assertIterableEquals(Arrays.asList(1L), pa.getMultiLong("section1", "key5", -1L));
        pa.setAutoTrim(false);
        assertIterableEquals(Arrays.asList(-1L), pa.getMultiLong("section1", "key5", -1L));
    }

    @Test
    public void testGetFloat() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=1.234",
                "key1=1",
                "key2=2",
                "key3=ABC",
                "key5= 1\t",
        };
        IniFile ini = read(lines);
        ParameterAccessor pa = new ParameterAccessor(ini);
        assertEquals(1.234f, pa.getFloat("section1", "key1"));
        assertEquals(2.0f, pa.getFloat("section1", "key2", -1.0f));
        assertEquals(0.0f, pa.getFloat("section1", "key3"));
        assertEquals(-1.0f, pa.getFloat("section1", "key3", -1.0f));
        assertEquals(0.0f, pa.getFloat("section1", "key4"));
        assertEquals(-1.0f, pa.getFloat("section1", "key4", -1.0f));
        assertEquals(1.0f, pa.getFloat("section1", "key5"));
        pa.setAutoTrim(false);
        assertEquals(1.0f, pa.getFloat("section1", "key5"));    // float型はトリムしなくてもparse可能
    }

    @Test
    public void testGetMultiFloat() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=1.234",
                "key1=1",
                "key2=2",
                "key3=ABC",
                "key5= 1\t",
        };
        IniFile ini = read(lines);
        ParameterAccessor pa = new ParameterAccessor(ini);
        assertIterableEquals(Arrays.asList(1.234f, 1.0f), pa.getMultiFloat("section1", "key1", -1.0f));
        assertIterableEquals(Arrays.asList(2.0f), pa.getMultiFloat("section1", "key2", -1.0f));
        assertIterableEquals(Arrays.asList(-1.0f), pa.getMultiFloat("section1", "key3", -1.0f));
        assertIterableEquals(Arrays.asList(), pa.getMultiFloat("section1", "key4", -1.0f));
        assertIterableEquals(Arrays.asList(1.0f), pa.getMultiFloat("section1", "key5", -1.0f));
        pa.setAutoTrim(false);
        assertIterableEquals(Arrays.asList(1.0f), pa.getMultiFloat("section1", "key5", -1.0f)); // float型はトリムしなくてもparse可能
    }

    @Test
    public void testGetDouble() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=1.234",
                "key1=1",
                "key2=2",
                "key3=ABC",
                "key5= 1\t",
        };
        IniFile ini = read(lines);
        ParameterAccessor pa = new ParameterAccessor(ini);
        assertEquals(1.234, pa.getDouble("section1", "key1"));
        assertEquals(2.0, pa.getDouble("section1", "key2", -1.0));
        assertEquals(0.0, pa.getDouble("section1", "key3"));
        assertEquals(-1.0, pa.getDouble("section1", "key3", -1.0));
        assertEquals(0.0, pa.getDouble("section1", "key4"));
        assertEquals(-1.0, pa.getDouble("section1", "key4", -1.0));
        assertEquals(1.0, pa.getDouble("section1", "key5"));
        pa.setAutoTrim(false);
        assertEquals(1.0, pa.getDouble("section1", "key5"));
    }

    @Test
    public void testGetMultiDouble() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=1.234",
                "key1=1",
                "key2=2",
                "key3=ABC",
                "key5= 1\t",
        };
        IniFile ini = read(lines);
        ParameterAccessor pa = new ParameterAccessor(ini);
        assertIterableEquals(Arrays.asList(1.234, 1.0), pa.getMultiDouble("section1", "key1", -1.0));
        assertIterableEquals(Arrays.asList(2.0), pa.getMultiDouble("section1", "key2", -1.0));
        assertIterableEquals(Arrays.asList(-1.0), pa.getMultiDouble("section1", "key3", -1.0));
        assertIterableEquals(Arrays.asList(), pa.getMultiDouble("section1", "key4", -1.0));
        assertIterableEquals(Arrays.asList(1.0), pa.getMultiDouble("section1", "key5", -1.0));
        pa.setAutoTrim(false);
        assertIterableEquals(Arrays.asList(1.0), pa.getMultiDouble("section1", "key5", -1.0));
    }
}