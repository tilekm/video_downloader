package kz.tilek.downloader.utils;

import java.io.IOException;

public class Merger {
    public static void mergeAudioAndVideo(String audioPath, String videoPath, String outputPath) {
        try {
            String[] exeCmd = new String[]{"ffmpeg", "-i", audioPath, "-i", videoPath, "-acodec", "copy", "-vcodec", "copy", outputPath};
            ProcessBuilder pb = new ProcessBuilder(exeCmd);
            pb.start().waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}