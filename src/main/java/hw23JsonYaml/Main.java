package hw23JsonYaml;

import hw23JsonYaml.entity.InitDataPath;
import hw23JsonYaml.service.ServiceFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {

        InitDataPath initDataPath = new InitDataPath();
        ServiceFile serviceFile = new ServiceFile();

        initDataPath.initPath(args);
        Path rootDir = initDataPath.getPathWorkDir();
        List<File> fileList = serviceFile.listFile(rootDir);
        if (!fileList.isEmpty()) {
            for (File s : fileList) {
                serviceFile.saveResult(s.toPath(), serviceFile.fileConvert(s.toPath()));
            }
        } else {
            System.out.println("В директорії " + rootDir + " файлів *.json / *.yaml не має!");
        }
    }
}
