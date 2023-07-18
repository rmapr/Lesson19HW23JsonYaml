package hw23JsonYaml;

import hw23JsonYaml.entity.InitDataPath;
import hw23JsonYaml.service.ServiceFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException{

        InitDataPath initDataPath = new InitDataPath();
        ServiceFile serviceFile = new ServiceFile();

        initDataPath.initPath(args);
        Path rootDir = initDataPath.getPathWorkDir();

        List<File> stringList = serviceFile.listFile(rootDir);
        System.out.println(stringList);

        for (File s : stringList) {
                serviceFile.saveResult(s.toPath(), serviceFile.fileConvert(s.toPath()));
        }
    }
}
