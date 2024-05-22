package kz.tilek.downloader.downloader;

import com.google.gson.JsonSyntaxException;
import kz.tilek.downloader.utils.Configs;
import kz.tilek.downloader.utils.custom.CustomException;
import kz.tilek.downloader.utils.response.tiktok.Data;
import kz.tilek.downloader.utils.response.tiktok.TikTokVideoResponseClass;

import java.io.*;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.Gson;

public class TikTokDownloader extends Downloader{

    private TikTokVideoResponseClass response;

    public TikTokDownloader() {
        super();
    }


    @Override
    public String getPreview(String videoUrl) throws CustomException{
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(Configs.TIKTOK_API_URL + videoUrl + "&hd=1"))
                    .header("X-RapidAPI-Key", dotenv.get("RAPID_API_KEY"))
                    .header("X-RapidAPI-Host", "tiktok-video-no-watermark2.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            try {
                this.response = new Gson().fromJson(HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body(), TikTokVideoResponseClass.class);
                if (this.response.getCode() == -1) {
                    System.out.println(this.response.getMsg());
                    throw new CustomException(this.response.getMsg());
                }
            } catch (ConnectException e) {
                throw new CustomException("Connection error");
            } catch (InterruptedException | JsonSyntaxException | IOException e) {
                throw new CustomException(e.getMessage());
            }
            return this.response.getData().getPreview();
    }

    @Override
    public void downloadVideo() throws CustomException {
        if (this.response == null) {
            throw new CustomException("Connection error");
        }
        if (this.response.getData() == null) {
            throw new CustomException(response.getMsg());
        }
        String title = this.response.getData().getTitle();
        if (title == null || title.isEmpty()) {
            title = response.getData().getId();
        }
        String savePath = getSavePath() + title.strip().replaceAll("[<>:\"\\\\/|?*]", "");
        if (savePath.length() > 70) {
            savePath = savePath.substring(0, 70).strip();
        }
        if (this.response.getData().getDuration() != 0) {
            savePath += ".mp4";
            downloadVideoToFile(this.response.getData(), savePath);
        } else {
            new File(savePath).mkdir();
            downloadImagesToFile(this.response.getData(), savePath + "/");
        }

    }

    @Override
    public void downloadAudio() throws CustomException {
        if (this.response == null) {
            throw new CustomException("Connection error");
        }
        if (this.response.getData()== null) {
            throw new CustomException(response.getMsg());
        }
        try {
            String title = this.response.getData().getTitle().strip();
            if (title.isEmpty()) {
                title = response.getData().getId();
            }
            String savePath = getSavePath() + title.replaceAll("[<>:\"\\\\/|?*]", "");
            if (savePath.length() > 70) {
                savePath = savePath.substring(0, 70).strip();
            }
            savePath += ".mp3";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(response.getData().getMusic()))
                    .build();
            byte[] video = returnFile(request);
            FileOutputStream fos = new FileOutputStream(savePath);
            fos.write(video);
            fos.close();
            
        } catch (IOException | InterruptedException e) {
            throw new CustomException(e.getMessage());
        }
    }

    private static void downloadVideoToFile(Data videoData, String savePath) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(videoData.getPlay()))
                    .build();
            byte[] video = returnFile(request);
            FileOutputStream fos = new FileOutputStream(savePath);
            fos.write(video);
            fos.close();
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void downloadImagesToFile(Data videoData, String savePath) throws CustomException {
        for (int i = 0; i < videoData.getImages().length; i++) {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(videoData.getImages()[i]))
                        .build();
                byte[] image = returnFile(request);
                FileOutputStream fos = new FileOutputStream(savePath + (i + 1) + ".jpg");
                fos.write(image);
                fos.close();
            } catch (IOException | InterruptedException e) {
                throw new CustomException(e.getMessage());
            }
        }
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(videoData.getWmplay()))
                    .build();
            byte[] video = returnFile(request);
            FileOutputStream fos = new FileOutputStream(savePath + "music.mp3");
            fos.write(video);
            fos.close();
        } catch (IOException | InterruptedException e) {
            throw new CustomException(e.getMessage());
        }
    }
}



