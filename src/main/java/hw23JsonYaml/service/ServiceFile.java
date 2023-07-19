package hw23JsonYaml.service;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.StandardOpenOption;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

import hw23JsonYaml.entity.FileInfo;
import hw23JsonYaml.entity.InitDataPath;
import hw23JsonYaml.exception.ErrorConvertException;
import org.yaml.snakeyaml.Yaml;

public class ServiceFile {

    public List<File> listFile(Path path) {
        InitDataPath initDataPath = new InitDataPath();
        Predicate<File> isJson = f -> (f).getName().contains(initDataPath.getExtensionJson());
        Predicate<File> isYaml = f -> (f).getName().contains(initDataPath.getExtensionYaml());
        List<File> fileList = Arrays.stream(Objects.requireNonNull(path.toFile().listFiles()))
                .filter(isJson.or(isYaml))
                .toList();
        return fileList;
    }

    public String readToString(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }

    public FileInfo fileConvert(Path path) throws IOException {
        FileInfo fileInfo = new FileInfo();
        InitDataPath initDataPath = new InitDataPath();
        Path oldNameFile = path;

//        create dir converted
        File fileDir = new File(path.getParent().toString(), "/converted");
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        Long timeStart = System.nanoTime();
        fileInfo.setOldfileName(oldNameFile.getFileName()); //write old fileName
        fileInfo.setOldSize(Files.size(oldNameFile)); // write old size file

        if (oldNameFile.toString().contains(initDataPath.getExtensionJson())) {

//read string with Json file
            String jsonString = readToString(path.toString());

            ObjectMapper objectMapperYaml = new ObjectMapper();

            Map[] map;
            File newNameFileYaml = new File(fileDir, oldNameFile.getFileName().toString().replace(".json", ".yaml"));

//                Поки така примитивна перевірка для масиву (, на інше потрібен час
            if (jsonString.contains("[{") && (!jsonString.contains("}]"))
                    || (!jsonString.contains("[{") && (jsonString.contains("}]")))) {
                throw new ErrorConvertException(path, fileInfo, "Error file format!!!");
            }
            try {
//                Поки така примитивна перевірка для масиву (, на інше потрібен час
                if (jsonString.contains("[{") && (jsonString.contains("}]"))) {
                    map = objectMapperYaml.readValue(jsonString, Map[].class);
//        write new Yaml file
                    objectMapperYaml.writeValue(newNameFileYaml, map);
                } else {
                    objectMapperYaml.writeValue(newNameFileYaml, jsonString);
                }
            } catch (Exception e) {
                throw new ErrorConvertException(path, fileInfo, "Error file format!!!");
            }

            fileInfo.setNewfileName(newNameFileYaml.toPath().getFileName()); // write new fileName
            fileInfo.setNewSize(Files.size(newNameFileYaml.toPath())); // write new size file
            fileInfo.setTimeConvert(System.nanoTime() - timeStart); // write time convert
            if (!newNameFileYaml.exists()) {
                newNameFileYaml.createNewFile();
            }
        } else if (oldNameFile.toString().contains(initDataPath.getExtensionYaml())) {

//read string with Yaml file
            String yamlString = readToString(path.toString());
            Yaml yaml = new Yaml();
            Map<String, Object> obj;
            try {
                obj = yaml.load(yamlString);
            } catch (Exception e) {
                throw new ErrorConvertException(path, fileInfo, "Error file format!!!");
            }

            ObjectMapper objectMapperJson = new ObjectMapper(new JsonFactory());
            File newNameFileJson = new File(fileDir, oldNameFile.getFileName().toString().replace(".yaml", ".json"));

//                objectMapperJson.writeValue(newNameFileJson, yamlString);
            objectMapperJson.writeValue(newNameFileJson, obj);
            fileInfo.setNewfileName(newNameFileJson.toPath().getFileName()); // write new fileName
            fileInfo.setNewSize(Files.size(newNameFileJson.toPath())); // write new size file
            fileInfo.setTimeConvert(System.nanoTime() - timeStart); // write time convert
            if (!newNameFileJson.exists()) {
                newNameFileJson.createNewFile();
            }
        }
        return fileInfo;
    }

    public void saveResult(Path path, FileInfo fileInfo) throws IOException {
        File logFile = new File(path.getParent().toFile(), new InitDataPath().getLogFile());
        if (!logFile.exists()) {
            logFile.createNewFile();
        }
//        write in log file
        try {
//            System.out.println(fileInfo.getOldfileName().toString());
            String result;
            if (fileInfo.getErrorConvert().equals("")) {

                double timeConvert = (double) fileInfo.getTimeConvert() / 1_000_000_000;

                result = fileInfo.getOldfileName().toString().concat(" -> ")
                        .concat(fileInfo.getNewfileName().toString()).concat(", ")
                        .concat(String.valueOf(timeConvert)).concat("с, ")
//                        .concat(fileInfo.getTimeConvert().toString()).concat(", ")
                        .concat(fileInfo.getOldSize().toString()).concat(" -> ")
                        .concat(fileInfo.getNewSize().toString()).concat(".\n");
            } else result = fileInfo.getErrorConvert();
            Files.write(logFile.toPath(), result.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
