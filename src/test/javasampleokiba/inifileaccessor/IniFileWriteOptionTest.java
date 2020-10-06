package test.javasampleokiba.inifileaccessor;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import javasampleokiba.inifileaccessor.IniFile;
import javasampleokiba.inifileaccessor.IniFileWriteOption;

/**
 * JUnit5によるIniFileWriteOptionクラスのテスト
 */
public class IniFileWriteOptionTest extends TestBase {

    @Test
    public void testGetDelimiter() throws IOException {
        assertEquals('=', new IniFileWriteOption().getDelimiter());
    }

    @Test
    public void testSetDelimiter() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=1",
                "key2====",
        };
        IniFile ini = read(lines);
        IniFileWriteOption wOption = new IniFileWriteOption()
                .setDelimiter(':');
        assertIterableEquals(Arrays.asList(
                "[section1]",
                "key1:1",
                "key2:==="
                ), write(ini, wOption));
    }

    @Test
    public void testGetBlankLineBetweenSection() throws IOException {
        assertEquals(1, new IniFileWriteOption().getBlankLineBetweenSection());
    }

    @Test
    public void testSetBlankLineBetweenSectionDefault() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=1",
                ";comment",
                "[section2]",
                ";comment",
                ";comment",
                "[section3]",
                "key3=3",
        };
        IniFile ini = read(lines);
        IniFileWriteOption wOption = new IniFileWriteOption();
        assertIterableEquals(Arrays.asList(
                "[section1]",
                "key1=1",
                "",
                ";comment",
                "[section2]",
                "",
                ";comment",
                ";comment",
                "[section3]",
                "key3=3"
                ), write(ini, wOption));

        assertThrows(IllegalArgumentException.class, () -> wOption.setBlankLineBetweenSection(-1));
    }

    @Test
    public void testSetBlankLineBetweenSection0() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=1",
                "",
                ";comment",
                "[section2]",
                "",
                ";comment",
                ";comment",
                "[section3]",
                "key3=3",
        };
        IniFile ini = read(lines);
        IniFileWriteOption wOption = new IniFileWriteOption()
                .setBlankLineBetweenSection(0);
        assertIterableEquals(Arrays.asList(
                "[section1]",
                "key1=1",
                ";comment",
                "[section2]",
                ";comment",
                ";comment",
                "[section3]",
                "key3=3"
                ), write(ini, wOption));
    }

    @Test
    public void testSetBlankLineBetweenSection3() throws IOException {
        String[] lines = {
                "[section1]",
                "key1=1",
                ";comment",
                "[section2]",
                ";comment",
                ";comment",
                "[section3]",
                "key3=3",
        };
        IniFile ini = read(lines);
        IniFileWriteOption wOption = new IniFileWriteOption()
                .setBlankLineBetweenSection(3);
        assertIterableEquals(Arrays.asList(
                "[section1]",
                "key1=1",
                "",
                "",
                "",
                ";comment",
                "[section2]",
                "",
                "",
                "",
                ";comment",
                ";comment",
                "[section3]",
                "key3=3"
                ), write(ini, wOption));
    }

    @Test
    public void testGetBlankLineBetweenParameter() throws IOException {
        assertEquals(0, new IniFileWriteOption().getBlankLineBetweenParameter());
    }

    @Test
    public void testSetBlankLineBetweenParameterDefault() throws IOException {
        String[] lines = {
                "key0=0",
                "",
                ";comment",
                "key1=1",
                "[section1]",
                ";comment",
                "key2=2",
                "",
                "key3=3",
                ";comment",
                "[section2]",
                ";comment",
                ";comment",
                "[section3]",
                "",
                "key4=4",
                ";comment",
                "key5=5",
                "",
                "",
                ";comment",
                ";comment",
                "key6=6",
        };
        IniFile ini = read(lines);
        IniFileWriteOption wOption = new IniFileWriteOption();
        assertIterableEquals(Arrays.asList(
                "key0=0",
                ";comment",
                "key1=1",
                "",
                "[section1]",
                ";comment",
                "key2=2",
                "key3=3",
                "",
                ";comment",
                "[section2]",
                "",
                ";comment",
                ";comment",
                "[section3]",
                "key4=4",
                ";comment",
                "key5=5",
                ";comment",
                ";comment",
                "key6=6"
                ), write(ini, wOption));

        assertThrows(IllegalArgumentException.class, () -> wOption.setBlankLineBetweenParameter(-1));
    }

    @Test
    public void testSetBlankLineBetweenParameter2() throws IOException {
        String[] lines = {
                "key0=0",
                "",
                ";comment",
                "key1=1",
                "[section1]",
                ";comment",
                "key2=2",
                "",
                "key3=3",
                ";comment",
                "[section2]",
                ";comment",
                ";comment",
                "[section3]",
                "",
                "key4=4",
                ";comment",
                "key5=5",
                "",
                "",
                ";comment",
                ";comment",
                "key6=6",
        };
        IniFile ini = read(lines);
        IniFileWriteOption wOption = new IniFileWriteOption()
                .setBlankLineBetweenParameter(2);
        assertIterableEquals(Arrays.asList(
                "key0=0",
                "",
                "",
                ";comment",
                "key1=1",
                "",
                "[section1]",
                ";comment",
                "key2=2",
                "",
                "",
                "key3=3",
                "",
                ";comment",
                "[section2]",
                "",
                ";comment",
                ";comment",
                "[section3]",
                "key4=4",
                "",
                "",
                ";comment",
                "key5=5",
                "",
                "",
                ";comment",
                ";comment",
                "key6=6"
                ), write(ini, wOption));
    }
}