package kz.tilek.downloader.downloader;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import kz.tilek.downloader.utils.Configs;
import kz.tilek.downloader.utils.custom.CustomException;
import kz.tilek.downloader.utils.response.instagram.Data;
import kz.tilek.downloader.utils.response.instagram.InstagramResponseClass;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class InstagramDownloader extends Downloader {
    private InstagramResponseClass response;
    public InstagramDownloader() {
        super();
    }

    @Override
    public String getPreview(String videoUrl) throws CustomException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Configs.INSTAGRAM_API_URL + videoUrl + "&url_embed_safe=true&include_insights=false"))
                .header("X-RapidAPI-Key", dotenv.get("RAPID_API_KEY"))
                .header("X-RapidAPI-Host", "instagram-scraper-api2.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        try {
            this.response = new Gson().fromJson(HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body(), InstagramResponseClass.class);
            if (this.response.getDetail() != null) {
                System.out.println(this.response.getDetail());
                throw new CustomException(this.response.getDetail());
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
        if (this.response.getData() == null && response.getDetail() != null) {
            throw new CustomException(response.getDetail());
        }
        String title = this.response.getData().getTitle().strip().replaceAll("[^a-zA-Z0-9#]", "");
        if (title.isEmpty()) {
            title = response.getData().getId();
        }
        String savePath = getSavePath() + title.replaceAll("[<>:\"\\\\/|?*]", "");
        if (savePath.length() > 70) {
            savePath = savePath.substring(0, 70).strip();
        }
        if (this.response.getData().getDuration() != null) {
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
        if (this.response.getData() == null && response.getDetail() != null) {
            throw new CustomException(response.getDetail());
        }
        String title = this.response.getData().getTitle().strip().replaceAll("[^a-zA-Z0-9#]", "");
        if (title.isEmpty()) {
            title = response.getData().getId();
        }
        String savePath = getSavePath() + title.replaceAll("[<>:\"\\\\/|?*]", "");
        if (savePath.length() > 70) {
            savePath = savePath.substring(0, 70).strip();
        }
        try {
            if (this.response.getData().getMusicMetadata() != null && this.response.getData().getMusicMetadata().getOriginal_sound_info() != null) {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(response.getData().getMusicMetadata().getOriginal_sound_info().getURL()))
                        .build();
                byte[] audio = returnFile(request);
                FileOutputStream fos = new FileOutputStream(savePath + "audio.mp3");
                fos.write(audio);
                fos.close();
            } else if (response.getData().getClips_metadata() != null) {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(response.getData().getClips_metadata().getAudioURL()))
                        .build();
                byte[] audio = returnFile(request);
                FileOutputStream fos = new FileOutputStream(savePath + "audio.mp3");
                fos.write(audio);
                fos.close();
            } else if (response.getData().getMusic_info() != null && response.getData().getMusic_info().getMusic_asset_info() != null) {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(response.getData().getMusic_info().getMusic_asset_info().getProgressiveDownloadURL()))
                        .build();
                byte[] audio = returnFile(request);
                FileOutputStream fos = new FileOutputStream(savePath + "audio.mp3");
                fos.write(audio);
                fos.close();
            } else {
                throw new CustomException("No audio found");
            }
            
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    private void downloadVideoToFile(Data videoData, String savePath) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(videoData.getVideoUrl()))
                    .build();
            byte[] video = returnFile(request);
            FileOutputStream fos = new FileOutputStream(savePath);
            fos.write(video);
            fos.close();
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    private void downloadImagesToFile(Data response, String savePath) {
        if (response.getMedia() == null) {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(response.getPreview()))
                        .build();
                byte[] image = returnFile(request);
                FileOutputStream fos = new FileOutputStream(savePath + "1.jpg");
                fos.write(image);
                fos.close();
            } catch (IOException | InterruptedException e) {
                System.out.println(e.getMessage());
            }
        } else {
            for (int i = 0; i < response.getMedia().length; i++) {
                try {
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(response.getMedia()[i].getURL()))
                            .build();
                    byte[] image = returnFile(request);
                    FileOutputStream fos = new FileOutputStream(savePath + (i + 1) + ".jpg");
                    fos.write(image);
                    fos.close();
                } catch (IOException | InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        if (response.getMusicMetadata().getOriginal_sound_info() != null) {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(response.getMusicMetadata().getOriginal_sound_info().getURL()))
                        .build();
                byte[] audio = returnFile(request);
                FileOutputStream fos = new FileOutputStream(savePath + "audio.mp3");
                fos.write(audio);
                fos.close();
            } catch (IOException | InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
