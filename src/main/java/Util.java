import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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

    static void writeStringToFile(String value, String filename, String pathToNewFile) {
        // should default to current working directory
        // if pathToNewFile is not defined
        // name should default to .env.example is not provided
        // it should reject .env as a name

    }

}