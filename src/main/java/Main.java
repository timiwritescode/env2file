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

    @CommandLine.Parameters(index = "1", description = "Path to output file", arity ="0..1")
    private String filename;

    @CommandLine.Option(
            names = {"-g", "--git"},
            description = "Enables the automatic git option for env2file",
            required = false
    )
    private boolean gitOption;

    @Override
    public void run() {

        try {

            Env2File env2File = new Env2File(filepath);

            if (gitOption) {
                String outputfile = filename == null ? ".env.sample.txt" : filename;
                env2File.saveEnvironmentKeysToGit(outputfile);

            } else {
                env2File.writeEnvKeysToAFile("", filename);
            }


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }
}
