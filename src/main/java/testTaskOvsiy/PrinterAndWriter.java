package testTaskOvsiy;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;

/**
 * Utility class to display or save results in different ways
 */
public class PrinterAndWriter {
    String border = "-------------------------------------------------------------";

    /**
     * Displays to console results of counted files
     *
     * @param folders list of results folders
     */
    public void displayFolders(List<Folder> folders) {

        folders.sort(Comparator.comparing(Folder::getNumber));
        System.out.println(border);
        System.out.printf("%-4s | %-12s | %-12s\n", "Num.", "Files number", "Path");
        System.out.println(border);
        for (Folder folder : folders) {

//            byte[] bytes = folder.getPath().getBytes(StandardCharsets.UTF_8);
//            String folderPath = new String(bytes, StandardCharsets.UTF_8);

            System.out.printf("%-4d | %-12d | %-12s\n",
                    folder.getNumber(),
                    folder.getFilesNumber(),
                    folder.getPath());
            System.out.println(border);
        }
    }

    /**
     * Writes results to CSV file
     *
     * @param path    path to results file
     * @param folders list of results folders
     * @throws IOException
     */
    public void writeToCSV(String path, List<Folder> folders) throws IOException {

        folders.sort(Comparator.comparing(Folder::getNumber));

        String finalPath;

        if (path.contains(".")) {
            finalPath = path.substring(0, path.lastIndexOf('.')).concat(".csv");
        } else {
            finalPath = path.concat(".csv");
        }

        FileWriter fileWriter = new FileWriter(finalPath);
        try (CSVPrinter printer = new CSVPrinter(
                fileWriter,
                CSVFormat.DEFAULT
                        .withDelimiter(';')
                        .withHeader("incoming_path; files_number"))) {
            for (Folder folder : folders) {
                printer.printRecord(folder.getPath(), folder.getFilesNumber());
            }
        }
    }
}
