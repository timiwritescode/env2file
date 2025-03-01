import java.util.List;

class Env2File {

    private List<String> retrieveKeysFromEnvFile(String filepath) {
        // an environment key pair is defined as that which follows the regexp
        // [0-9][A-Za-Z]s+=[0-9][A-Za-Z]s
        String fileContent = Util.readFileToString(filepath);
        return null;
    }

    public static void writeEnvKeysToAFile(String[] keys) {

    }

}