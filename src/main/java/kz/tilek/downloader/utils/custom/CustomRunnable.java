package kz.tilek.downloader.utils.custom;

import kz.tilek.downloader.downloader.Downloader;

import java.io.IOException;

public class CustomRunnable extends Thread {

    private String link;
    private Downloader downloader;
    private String message;
    private String mediaType;

    public CustomRunnable(Downloader downloader, String mediaType) {
        this.downloader = downloader;
        this.mediaType = mediaType;
    }

    @Override
    public void run() {
        try {
            if (mediaType.equals("Audio"))
                downloader.downloadAudio();
            else {
                downloader.downloadVideo();
            }
            if (downloader.isOpenAfterDownload()) {
                new ProcessBuilder("explorer.exe", downloader.getSavePath()).start();
            }
        } catch (CustomException | IOException e) {
            message = e.getMessage();
            Thread.currentThread().interrupt();
        }
    }

    public String getMessage() {
        return message;
    }
}

