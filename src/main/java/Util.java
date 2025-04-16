import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Util {
    static String readFileToString(String filepath) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        try(BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String currentLine = reader.readLine();

            while (currentLine != null) {
                stringBuilder.append(currentLine);
                stringBuilder.append("\n");
                currentLine = reader.readLine();
            }

            if (!stringBuilder.isEmpty()) {
                stringBuilder.setLength(stringBuilder.length() - 1);
            }
        }
        return stringBuilder.toString();
    }

    static String  writeStringToFile(String value, String filename, String pathToNewFile) throws IOException {
        // should default to current working directory
        // if pathToNewFile is not defined
        // name should default to .env.example is not provided
        // it should reject .env as a name

        if (pathToNewFile == null || pathToNewFile.isEmpty()) {
            pathToNewFile = Paths.get("")
                    .toAbsolutePath()
                    .toString();
        }

        Path directoryPath = Paths.get(pathToNewFile);
        Files.createDirectories(directoryPath);
        File file = new File(pathToNewFile + "/" + filename);

        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file)))  {
            bufferedWriter.write(value);
        }

        return file.getAbsolutePath();

    }


    static boolean isGitRepository(String path) {
        // check if current directory is a git repository
        String directoryPath = Paths.get(path).toAbsolutePath() + "/.git";

        return Files.exists(Path.of(directoryPath));
    }


    static List<String> getEnvFilesPathsInADirectory(String directory) {
        return Stream.of(Objects.requireNonNull(new File(directory).listFiles()))
                .filter(file -> !file.isDirectory() && file.getName().endsWith(".env"))
                .map(File::getAbsolutePath)
                .collect(Collectors.toList());

    }

    static int runBashCommand(String command, String filePath) throws IOException, InterruptedException{
        Process process = new ProcessBuilder("bash", "-c", command)
                                        .redirectErrorStream(true)
                                        .directory(new File(filePath)).start();
        System.out.println(new String(process.getInputStream().readAllBytes()).trim());

            return process.waitFor();


    }
}