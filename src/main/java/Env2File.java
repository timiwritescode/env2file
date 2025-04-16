import exceptions.NotFoundException;
import exceptions.NotGitRepositoryException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Env2File {
    private final String filepath;
    private String fileContent;
    private static  final Pattern ENV_PATTERN = Pattern.compile("^[A-Z0-9_]+=[^=]+$");


    Env2File(String filepath) throws IOException {
        this.filepath = filepath;
        if (!Files.isDirectory(Path.of(filepath))) {
            this.fileContent = Util.readFileToString(filepath);
        }

    }


    /*
     * This method processes a string containing file content with line separators.
     * It checks if each line follows the .env key-value format:
     *
     *     SOME_KEY=some_value
     *
     * - If a line matches the expected format, the key (SOME_KEY) is extracted.
     * - If a line does not match the format, it is ignored.
     * - The extracted keys are returned as a list.
     */
    private List<String> retrieveKeysFromFileContent(String content) throws IOException {
        String[] lines = content.split("\\R");
        List<String> keys = new ArrayList<>();
        for (String line: lines) {
            Matcher matcher = ENV_PATTERN.matcher(line.trim());
            if (matcher.matches()) {
                keys.add(line.split("=")[0]);
            }

        }


        return keys;
    }

    public void writeEnvKeysToAFile(String pathToSaveFIle, String filename) throws IOException {
        List<String> envKeys = retrieveKeysFromFileContent(fileContent);
        String outPut = stringifyKeysList(envKeys);

        String absolutePathOfFile = Util.writeStringToFile(outPut, filename, pathToSaveFIle);
        int numberOfKeys = envKeys.size();

        System.out.println("Wrote " + numberOfKeys + " environment keys to " + absolutePathOfFile);
    }


    private String stringifyKeysList(Iterable<String> keys) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String key : keys) {
            stringBuilder.append(key).append("=");
            stringBuilder.append("\n");
        }

        // remove trailing new line character
        stringBuilder.setLength(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }


    public int saveEnvironmentKeysToGit(String outputFilename) throws
            NotFoundException,
            NotGitRepositoryException,
            IOException,
            InterruptedException{
        if (!Util.isGitRepository(filepath)) {
            throw new NotGitRepositoryException("Env2File: Directory specified is not a git directory");
        }

        // check env files are present in the root directory
        List<String> envFilesPath = Util.getEnvFilesPathsInADirectory(filepath);
        if (envFilesPath.isEmpty()) {
            throw new NotFoundException("Env2File: Directory does not contain any .env files");
        }
        Set<String> envKeys =  getEnvKeysFromFilePaths(envFilesPath);

        if (envKeys.isEmpty()) {
            throw new NotFoundException("Env2File: Environment variables not found in the files provided");
        }
        String output = stringifyKeysList(envKeys);
        String outputFilePath = Util.writeStringToFile(output, outputFilename, filepath);
        System.out.println("Wrote " + envKeys.size() + " to " + outputFilePath);

        // add and commit that envfile
        String gitAddCommand = "git add " + outputFilename;
        String commitMessage = "git commit -m 'chore: add environment keys to sample file'";
        Util.runBashCommand(gitAddCommand, filepath);
        if (!hasStagedChanges(filepath)) {
            throw new NotFoundException("Env2File: There are no new Environment variables to add");
        }
        return Util.runBashCommand(commitMessage, filepath);

    }

    private Set<String> getEnvKeysFromFilePaths(List<String> envFilesPath) {
        // get envkeys from a list of provided file paths of environment files
        Set<String> envKeys = new HashSet<>();
        envFilesPath.forEach(path -> {
            try {
                String fileContent = Util.readFileToString(path);
                // get the kys
                List<String> keys = retrieveKeysFromFileContent(fileContent);
                envKeys.addAll(keys);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return envKeys;
    }

    private boolean hasStagedChanges(String workingDir) throws IOException, InterruptedException {
        // check if there has been any change to the environment files
        String gitCommand = "git diff --cached --quiet";
        int exitCode = Util.runBashCommand(gitCommand, workingDir);

        return exitCode != 0;
    }
}