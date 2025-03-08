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


    @Test
    public void testIsGitRepository_testTrue(@TempDir Path tempDir) throws IOException {
        Path tempGitDirectory = tempDir.resolve(".git");
        Files.createDirectories(tempGitDirectory);

        boolean result = Util.isGitRepository(tempDir.toString());
        Assertions.assertTrue(result);

    }

    @Test
    public void testIsGitRepository_testFalse(@TempDir Path tempDir) throws IOException {
        Files.createDirectories(tempDir.resolve("git"));
        boolean result = Util.isGitRepository(tempDir.toString());


        Assertions.assertFalse(result);
    }

    @Test
    public void testRunCommand_testSuccessful(@TempDir Path tempDir) throws IOException, InterruptedException {
        String testCommands = "ls";
        int result = Util.runBashCommand(testCommands, tempDir.toString());

        Assertions.assertEquals(0, result);

    }

    @Test
    public void testRunCommand_testFailed(@TempDir Path tempDir) throws IOException, InterruptedException {
        String testCommands = "invalid command";
        int result = Util.runBashCommand(testCommands, tempDir.toString());
        Assertions.assertNotEquals(0, result);
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