import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

}