package testTaskOvsiy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    /**
     * Counts number of files in directories
     *
     * As input parameters application takes two files
     *
     * User has an opportunity to press ESC button and this will terminate counting end
     * will display already counted results. In this case file with results won't be created
     *
     * @param args should take 2 parameters:
     *             1. Path to file with list of directories which counting should be done in
     *             2. Path to file with results
     */
    public static void main(String[] args) {

        if (args.length != 2) {
            logger.severe("Please run this application with 2 arguments. See docs.");
            System.exit(0);
        }

        String pathToInputFile = args[0];
        String pathToResultsFile = args[1];

        List<Inbox> foldersToProcess = new Parser().parseInputFile(pathToInputFile);
        AtomicBoolean shouldInterrupt = new AtomicBoolean(false);

        if (foldersToProcess.isEmpty()) {
            logger.warning("Your file is empty...");
        }
        else {
            List<Folder> resultsList = Collections.synchronizedList(new ArrayList<>());

            ExecutorService executorService =
                    Executors.newFixedThreadPool(foldersToProcess.size());
            CountDownLatch doneSignal = new CountDownLatch(foldersToProcess.size());

            for (Inbox inbox : foldersToProcess) {
                Counter counter = new Counter(
                        resultsList, inbox, doneSignal, shouldInterrupt);
                executorService.submit(counter);
            }

            EscapeButtonHook escapeButtonHook = new EscapeButtonHook();
            escapeButtonHook.disableLogging();
            escapeButtonHook.registerHook(shouldInterrupt);

            try {
                doneSignal.await();
            } catch (InterruptedException e) {
                logger.severe(String.format("Application was interrupted: %s\n",
                        e.getMessage()));
            } finally {
                escapeButtonHook.unregisterHook();
                shutdownAndAwaitTermination(executorService);
            }

            if (!shouldInterrupt.get()) {
                try {
                    new PrinterAndWriter().writeToCSV(pathToResultsFile, resultsList);
                } catch (IOException e) {
                    logger.severe(String.format("File write error: %s", e.getMessage()));
                }
                new PrinterAndWriter().displayFolders(resultsList);
            }
            else {
                if (resultsList.size() != 0) {
                    logger.warning("Counting has not been completed. Partial results:");
                    new PrinterAndWriter().displayFolders(resultsList);
                }
                else {
                    logger.warning(
                            "Counting has not been completed. You have no result.");
                }
            }
        }
    }

    private static void shutdownAndAwaitTermination(ExecutorService pool) {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
                pool.shutdownNow();
                if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
                    System.err.println("Pool did not terminate");
                }
            }
        } catch (InterruptedException ie) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
