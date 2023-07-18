package hw23JsonYaml.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.nio.file.FileSystems;
import java.nio.file.Path;

@Data
@Accessors(chain = true)

public class InitDataPath {
    private Path pathWorkDir;
    private String logFile = "result.log";
    private String extensionJson = ".json";
    private String  extensionYaml= ".yaml";


public void initPath (String[] args) {
    Path path;
    if (args.length == 0) {
        path = Path.of(String.valueOf(FileSystems.getDefault().getPath("").toAbsolutePath()));
    } else path = Path.of(args[0]);
    System.out.println("Current working directory is : " + path);
    System.out.println("--------------");
    this.pathWorkDir = path;
}

    public InitDataPath() {
    }
}
