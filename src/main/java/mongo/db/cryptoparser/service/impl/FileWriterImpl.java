package mongo.db.cryptoparser.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import mongo.db.cryptoparser.service.FileWriter;
import org.springframework.stereotype.Component;

@Component
public class FileWriterImpl implements FileWriter {
    @Override
    public void writeDataToFile(String report, String filePath) {
        try {
            Files.write(Path.of(filePath), report.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Cant write content to file " + filePath,e);
        }
    }
}
