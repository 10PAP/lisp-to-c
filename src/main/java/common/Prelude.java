package common;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Prelude {

    public static String getPrelude() {
        try {
            String path = "src/main/resources/prelude.clj";
            return Files.readString(Paths.get(path), StandardCharsets.US_ASCII);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
