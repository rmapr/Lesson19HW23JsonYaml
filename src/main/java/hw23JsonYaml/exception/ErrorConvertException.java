package hw23JsonYaml.exception;

import hw23JsonYaml.entity.FileInfo;
import hw23JsonYaml.service.ServiceFile;

import java.io.IOException;
import java.nio.file.Path;

public class ErrorConvertException extends RuntimeException {
    public ErrorConvertException(Path path, FileInfo fileInfo, String s) throws IOException {
        super(s);
        fileInfo.setErrorConvert(fileInfo.getOldfileName().toString().concat(" Error file format!!!\n"));
        new ServiceFile().saveResult(path, fileInfo);
    }
}
