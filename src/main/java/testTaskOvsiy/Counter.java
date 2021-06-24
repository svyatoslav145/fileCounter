package testTaskOvsiy;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

/**
 * Counts number of files in directory
 */
public class Counter implements Runnable {

    private static final Logger logger = Logger.getLogger(Counter.class.getName());

    private final List<Folder> folderList;
    private final Inbox inbox;
    private final CountDownLatch doneSignal;
    private final AtomicBoolean shouldInterrupt;

    /**
     * Creates an instant of the class.
     *
     * @param resultsList     list of counted results
     * @param inbox           folder where counting should be done
     * @param doneSignal      countDownLatch to inform this thread is finished
     * @param shouldInterrupt flag to interrupt counting
     */
    public Counter(List<Folder> resultsList, Inbox inbox, CountDownLatch doneSignal, AtomicBoolean shouldInterrupt) {
        this.folderList = resultsList;
        this.inbox = inbox;
        this.doneSignal = doneSignal;
        this.shouldInterrupt = shouldInterrupt;
    }

    @Override
    public void run() {
        File file = new File(inbox.getPath());

        Folder folder = new Folder();
        folder.setPath(inbox.getPath());
        folder.setNumber(inbox.getFileNumber());

        simulateDelay(3000);

        if (file.isDirectory()) {
            int filesCount = getFilesCount(file);
            folder.setFilesNumber(filesCount);
            folderList.add(folder);
        }
        else {
            folder.setFilesNumber(0);
            folderList.add(folder);

            logger.warning(String.format("Number %d file \"%s\" either is not directory" +
                            " or is not exist. Check path.\n",
                    folder.getNumber(),
                    file.getName()));
        }

        doneSignal.countDown();
    }

    /**
     * Counts all files in the directory
     * and all directories using recursion
     *
     * @param inputFile directory to start from
     * @return number of files
     */
    private int getFilesCount(File inputFile) {
        File[] files = inputFile.listFiles();
        int result = 0;

        for (File file : Objects.requireNonNull(files)) {
            if (!shouldInterrupt.get()) {
                if (file.isDirectory()) {
                    result = result + getFilesCount(file);
                }
                else {
                    result++;
                }
            }
        }
        return result;
    }

    /**
     * Simulates long counting process to allow user to press esc
     *
     * @param millis how long to simulate
     */
    private void simulateDelay(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }
}
