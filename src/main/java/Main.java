import picocli.CommandLine;

import java.io.File;
import java.io.IOException;

@CommandLine.Command(
        name="env2file",
        mixinStandardHelpOptions = true,
        description = "A tool to extract environment keys from .env file " +
                "into another file."
)
public class Main implements Runnable {
    @CommandLine.Parameters(index = "0", description = "Path to file")
    private String filepath;

    @CommandLine.Parameters(index = "1", description = "Path to output file")
    private String filename;

    @Override
    public void run() {
        try {
            Env2File env2File = new Env2File(filepath);
            env2File.writeEnvKeysToAFile("", filename);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }
}
