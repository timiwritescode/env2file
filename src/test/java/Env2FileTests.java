import exceptions.NotFoundException;
import exceptions.NotGitRepositoryException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class Env2FileTests {

    @Test
    public void testWriteEnvKeysToFile(@TempDir Path tempDir) throws IOException {
     System.setProperty("user.dir", tempDir.toString());

     Path tempFile = tempDir.resolve("envFile.txt");
     String fileContent = "This is sample env file\nSOME_KEY=some_value\nKEY=value";

     Files.writeString(tempFile, fileContent);

     Env2File env2File = new Env2File(tempFile.toString());
     env2File.writeEnvKeysToAFile(tempDir.toString(), "envkeys.txt");


     Path expectedOutPutFile = tempDir.resolve("envkeys.txt");

     String contentOfProcessedFile = Files.readString(expectedOutPutFile);
     String expectedOutcome = "SOME_KEY=\nKEY=";

    Assertions.assertTrue(Files.exists(expectedOutPutFile));
    Assertions.assertEquals(expectedOutcome, contentOfProcessedFile);

    System.setProperty("user.dir", Paths.get("").toAbsolutePath().toString());
    }


    @Test
    public void testSaveEnvironmentKeysToGit_throwsNotGitRepositoryException(@TempDir Path tempDir) throws Exception {
        Env2File env2File = new Env2File(tempDir.toString());

        Assertions.assertThrows(NotGitRepositoryException.class, () -> env2File.saveEnvironmentKeysToGit(".env.sample"));

    }

    @Test
    public void testSaveEnvironmentKeysToGit_throwsNotFoundException(@TempDir Path tempDir) throws IOException {
        Files.createDirectories(tempDir.resolve(".git"));

        Env2File env2File = new Env2File(tempDir.toString());

        Assertions.assertThrows(NotFoundException.class,
                () -> env2File.saveEnvironmentKeysToGit(".env.sample"));
    }

    @Test
    public void testSaveEnvironmentKeysToGit(@TempDir Path tempDir) throws Exception {
        System.setProperty("user.dir", tempDir.toString());
        // create git repo in temporary directory
        Process process = new ProcessBuilder("bash" , "-c", "git init")
                .redirectErrorStream(true)
                .directory(new File(tempDir.toString()))
                .start();

        createEnvFileInDirectory(tempDir);
        Env2File env2File = new Env2File(tempDir.toString());

        int result = env2File.saveEnvironmentKeysToGit(".env.example");

        String expectedFileContentOne = "KEY=\nVALUE=";
        String expectedFileContentTwo = "VALUE=\nKEY=";
        String actualFileContent = Files.readString(Path.of(tempDir.toString() + "/.env.example"));


        Assertions.assertTrue(
               actualFileContent.equals(expectedFileContentOne) |
                        actualFileContent.equals(expectedFileContentTwo));

        Assertions.assertTrue(Files.exists(Path.of(tempDir.toString() + "/.env.example")));
        Assertions.assertEquals(0, result);
    }

    private void createEnvFileInDirectory(Path directory) throws IOException {
        String envVariables = "KEY=VALUE\nVALUE=KEY";

        Path envFile = directory.resolve(".env");
        Files.writeString(envFile, envVariables);

    }
}