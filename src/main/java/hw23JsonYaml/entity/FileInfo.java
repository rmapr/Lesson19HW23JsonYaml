package hw23JsonYaml.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.nio.file.Path;

@Data
@Accessors(chain = true)
public class FileInfo {
    private Path oldfileName;
    private Path newfileName;
    private Long timeConvert;
    private Long oldSize;
    private Long newSize;
    private String errorConvert = "";
}
