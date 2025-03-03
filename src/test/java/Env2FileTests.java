import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class Env2FileTests {

    @Test
    public void testEnv2File(@TempDir Path tempDir) throws IOException {
     System.setProperty("user.dir", tempDir.toString());
     System.out.println(System.getProperty("user.dir"));
     Path tempFile = tempDir.resolve("envFile.txt");
     String fileContent = "This is sample env file\nSOME_KEY=some_value\nKEY=value";

     Files.writeString(tempFile, fileContent);

     Env2File env2File = new Env2File(tempFile.toString());
     env2File.writeEnvKeysToAFile(tempDir.toString(), "envkeys.txt");


     Path expectedOutPutFile = tempDir.resolve("envkeys.txt");

     String contentOfProcessedFile = Files.readString(expectedOutPutFile);
     String expectedOutcome = "SOME_KEY\nKEY";

    Assertions.assertTrue(Files.exists(expectedOutPutFile));
    Assertions.assertEquals(expectedOutcome, contentOfProcessedFile);

    System.setProperty("user.dir", Paths.get("").toAbsolutePath().toString());
    }


}