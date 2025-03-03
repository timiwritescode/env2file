import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Env2File {
    private final String filepath;
    private final String fileContent;
    private static  final Pattern ENV_PATTERN = Pattern.compile("^[A-Z0-9_]+=[^=]+$");


    Env2File(String filepath) throws IOException {
        this.filepath = filepath;
        this.fileContent = Util.readFileToString(filepath);
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
    private List<String> retrieveKeysFromEnvFile() throws IOException {
        String[] lines = fileContent.split("\\R");
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
        List<String> envKeys = retrieveKeysFromEnvFile();
        StringBuilder stringBuilder = new StringBuilder();
        for (String key: envKeys) {
            stringBuilder.append(key + "=");
            stringBuilder.append("\n");
        }
        stringBuilder.setLength(stringBuilder.length() - 1);

        String outPut = stringBuilder.toString();

        String absolutePathOfFile = Util.writeStringToFile(outPut, filename, pathToSaveFIle);
        int numberOfKeys = envKeys.size();

        System.out.println("Wrote " + numberOfKeys + " environment keys to " + absolutePathOfFile);
    }

}