package testTaskOvsiy;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Parser for input file with paths to directories
 */
public class Parser {

    private static final Logger logger = Logger.getLogger(Parser.class.getName());

    /**
     * Parses input file and creates a list of directories for counting files
     *
     * @param pathToFile path to file
     * @return list of directories for counting
     */
    public List<Inbox> parseInputFile(String pathToFile) {
        List<Inbox> filesList = new ArrayList<>();
//        try (Scanner scanner = new Scanner(
//                new FileInputStream(pathToFile),
//                StandardCharsets.UTF_8.name())) {
//            int number = 1;
//            while (scanner.hasNext()) {
//                Inbox inbox = new Inbox();
//                inbox.setPath(scanner.nextLine());
//                inbox.setFileNumber(number);
//                filesList.add(inbox);
//                number++;
//            }
//        } catch (FileNotFoundException e) {
//            logger.warning(String.format("File not found: %%s%s", e.getMessage()));
//        }

        try(BufferedReader reader = new BufferedReader(new FileReader(pathToFile))){
            int number = 1;
            String line;
            while ((line = reader.readLine()) != null) {
                Inbox inbox = new Inbox();
                inbox.setPath(line);
                inbox.setFileNumber(number);
                filesList.add(inbox);
                number++;
            }
        } catch (IOException e) {
            logger.warning("File read error");
            e.printStackTrace();
        }

        return filesList;
    }
}

