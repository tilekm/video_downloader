package kz.tilek.downloader.downloader;

import kz.tilek.downloader.utils.Configs;
import kz.tilek.downloader.utils.custom.CustomException;

import java.io.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import io.github.cdimascio.dotenv.Dotenv;

public abstract class Downloader {

    private String savePath;
    private boolean openAfterDownload;
    protected final Dotenv dotenv = Dotenv.load();

    protected Downloader() {
        try (BufferedReader bis = new BufferedReader(new FileReader(Configs.SETTINGS_FILE))) {
            String line;
            while ((line = bis.readLine()) != null) {
                if (line.startsWith(Configs.DOWNLOAD_PATH)) {
                    savePath = line.substring(Configs.DOWNLOAD_PATH.length());
                } else if (line.startsWith(Configs.OPEN_AFTER_DOWNLOAD)) {
                    openAfterDownload = Boolean.parseBoolean(line.substring(Configs.OPEN_AFTER_DOWNLOAD.length()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getSavePath() {
        return savePath;
    }

    public boolean isOpenAfterDownload() {
        return openAfterDownload;
    }
    abstract public String getPreview(String VideoUrl) throws CustomException;

    public abstract void downloadVideo() throws CustomException;

    public abstract void downloadAudio() throws CustomException;

    protected static byte[] returnFile(HttpRequest request) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS).build();
        HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
        InputStream in = response.body();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024 * 94];
        int n;
        while ((n = in.read(buf)) != -1) {
            out.write(buf, 0, n);
        }
        out.close();
        in.close();
        return out.toByteArray();
    }
}