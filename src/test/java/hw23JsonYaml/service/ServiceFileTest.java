package hw23JsonYaml.service;

import hw23JsonYaml.entity.InitDataPath;
import hw23JsonYaml.exception.ErrorConvertException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ServiceFileTest {
    private ServiceFile serviceFile;
    InitDataPath initDataPath;

    @BeforeEach
    void setUp() {
        initDataPath = new InitDataPath();
        initDataPath.initPath(new String[]{"C:\\Hillel\\Project\\HomeTaskPro\\Lesson19HW23JsonYaml\\src\\main\\java\\hw23JsonYaml\\workDirWithAllFile"});
        serviceFile = new ServiceFile();
    }

    @Test
    void testListFile() {
        assertNotNull(serviceFile.listFile(initDataPath.getPathWorkDir()));
    }

    @Test
    void testReadToString() {
        String fileTestRead = "C:\\Hillel\\Project\\HomeTaskPro\\Lesson19HW23JsonYaml\\src\\main\\java\\hw23JsonYaml\\workDirWithAllFile\\Test.txt";
        String patternTextTest = "Test read text from a file.\nSuccessfully\n";
        String testText = serviceFile.readToString(fileTestRead);
        assertEquals(patternTextTest, testText);
    }

    @Test
    void testExistDirConverted() {
        File fileDir = new File(initDataPath.getPathWorkDir().toString(), "/converted");
        if (!fileDir.exists()) assertFalse(fileDir.exists());
        else assertTrue(fileDir.exists());
    }

    @Test
    void testFileConvertErrorConvertException() {
        Path path = Path.of("C:\\Hillel\\Project\\HomeTaskPro\\Lesson19HW23JsonYaml\\src\\main\\java\\hw23JsonYaml\\DATA3_Error.yaml");
        assertThrows(ErrorConvertException.class, () -> serviceFile.fileConvert(path));
    }

}