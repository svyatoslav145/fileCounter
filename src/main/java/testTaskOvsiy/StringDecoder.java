package testTaskOvsiy;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * String decoder  to UTF-8 for work with directories which have cyrillic characters
 */
public class StringDecoder {

    public String decodeString(String input) throws IOException {
        return new BufferedReader(
                new InputStreamReader(
                        new ByteArrayInputStream(
                                input.getBytes()), StandardCharsets.UTF_8)).readLine();
    }
}
