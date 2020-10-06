package test.javasampleokiba.inifileaccessor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;

import javasampleokiba.inifileaccessor.IniFile;
import javasampleokiba.inifileaccessor.IniFileReadOption;
import javasampleokiba.inifileaccessor.IniFileReadOption.DuplicateKeyBehavior;
import javasampleokiba.inifileaccessor.IniFileReadOption.DuplicateSectionBehavior;
import javasampleokiba.inifileaccessor.IniFileReader;
import javasampleokiba.inifileaccessor.IniFileWriteOption;
import javasampleokiba.inifileaccessor.IniFileWriter;

/**
 * テスト用クラスのベースクラス
 */
public class TestBase {

    public IniFile read(String[] lines) throws IOException {
        IniFileReadOption option = new IniFileReadOption()
                .setAllowGlobalSection(true)
                .setHasHeaderComment(true)
                .setDuplicateKeyBehavior(DuplicateKeyBehavior.MULTI_VALUES)
                .setDuplicateSectionBehavior(DuplicateSectionBehavior.MERGE);
        Files.write(Paths.get("temp.ini"), Arrays.asList(lines), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
        return new IniFileReader(option).read("temp.ini");
    }

    public IniFile read(String[] lines, IniFileReadOption option) throws IOException {
        Files.write(Paths.get("temp.ini"), Arrays.asList(lines), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
        return new IniFileReader(option).read("temp.ini");
    }

    public List<String> write(IniFile ini, IniFileWriteOption option) throws IOException {
        new IniFileWriter(option).write("temp.ini", ini);
        return Files.readAllLines(Paths.get("temp.ini"));
    }

    @AfterEach
    public void teardown() throws IOException {
        Files.deleteIfExists(Paths.get("temp.ini"));
    }
}