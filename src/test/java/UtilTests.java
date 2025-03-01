import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class UtilTests {
    @Test
    public void testReadFileToString() throws IOException {
        String fileContents = "Some file content\nand another line";
        File tempFile = createTempFileWithContent(fileContents);
        String filePath = tempFile.getAbsolutePath();

        String result = Util.readFileToString(filePath);

        Assertions.assertEquals(fileContents, result);

    }

@Test
public void testWriteStringTOFile(@TempDir Path tempDir) throws IOException {
    Path filePath = tempDir.resolve("sample.txt");

    String exampleContent = "This is example content";
    Util.writeStringToFile(exampleContent, "sample.txt", tempDir.toString());

    Assertions.assertTrue(Files.exists(filePath));
    Assertions.assertEquals(exampleContent, Files.readString(filePath));
}

private File createTempFileWithContent(String content) throws IOException {
    File tempFile = File.createTempFile(".env", "");
    tempFile.deleteOnExit();

    try (FileWriter writer = new FileWriter(tempFile)) {
        writer.write(content);

    }

    return tempFile;
    }
}