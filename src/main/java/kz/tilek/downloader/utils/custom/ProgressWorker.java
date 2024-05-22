package kz.tilek.downloader.utils.custom;

import javafx.concurrent.Task;

public class ProgressWorker extends Task<Void> {
    private final CustomRunnable customRunnable;
    public ProgressWorker(CustomRunnable customRunnable) {
        this.customRunnable = customRunnable;
    }
    
    @Override
    protected Void call() throws Exception {
        int i = 0;
        while (customRunnable.isAlive()) {
            Thread.sleep(100);
            if (i < 90) {
                updateProgress(i++, 100);
            }
        }
        if (customRunnable.isInterrupted()) {
            updateProgress(0, 100);
            throw new Exception("Error while downloading video");
        }
        updateProgress(100, 100);
        return null;
    }
}
