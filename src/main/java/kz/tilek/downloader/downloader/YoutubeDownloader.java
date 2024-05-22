package kz.tilek.downloader.downloader;

import com.google.gson.Gson;
import kz.tilek.downloader.utils.Configs;
import kz.tilek.downloader.utils.custom.CustomException;
import kz.tilek.downloader.utils.Merger;
import kz.tilek.downloader.utils.response.youtube.Audios;
import kz.tilek.downloader.utils.response.youtube.Videos;
import kz.tilek.downloader.utils.response.youtube.YoutubeResponseClass;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

public class YoutubeDownloader extends Downloader {

    private YoutubeResponseClass response;
    public YoutubeDownloader() {
        super();
    }
    @Override
    public String getPreview(String text) throws CustomException {
        String videoId = getVideoId(text);
        if (videoId == null) {
            throw new CustomException("Link error");
        }
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(Configs.YOUTUBE_API_URL + videoId + "&subtitles=false&related=false"))
                    .header("X-RapidAPI-Key", dotenv.get("RAPID_API_KEY"))
                    .header("X-RapidAPI-Host", "youtube-media-downloader.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            this.response = new Gson().fromJson(HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body(), YoutubeResponseClass.class);
            if (!this.response.getStatus()) {
                throw new CustomException("Connection error");
            }
        } catch (IOException | InterruptedException e) {
            throw new CustomException(e.getMessage());
        }
        return "https://i.ytimg.com/vi/"+ videoId + "/hqdefault.jpg";
    }

    @Override
    public void downloadVideo() throws CustomException {
        if (this.response == null) {
            throw new CustomException("Connection error");
        }
        if (this.response.getVideos() == null) {
            throw new CustomException("Video error!");
        }
        String title = this.response.getTitle().strip().replaceAll("[<>:\"\\\\/|?*]", "");
        if (title.isEmpty()) {
            title = response.getId();
        }
        String savePath = getSavePath() + title;
        if (savePath.length() > 70) {
            savePath = savePath.substring(0, 70).strip();
        }
        if (this.response.getType().equals("video")) {
            savePath += ".mp4";
            downloadVideoToFile(this.response.getVideos(), savePath);
        }
    }

    @Override
    public void downloadAudio() throws CustomException {
        if (this.response == null) {
            throw new CustomException("Connection error");
        }
        if (this.response.getAudios() == null) {
            throw new CustomException("Audio error!");
        }
        String title = this.response.getTitle().strip().replaceAll("[<>:\"\\\\/|?*]", "");
        if (title.isEmpty()) {
            title = response.getId();
        }
        String savePath = getSavePath() + title;
        if (savePath.length() > 70) {
            savePath = savePath.substring(0, 70).strip();
        }
        savePath += ".mp3";
        downloadAudioToFile(this.response.getAudios(), savePath);
    }

    private String getVideoId(String link) {
        Pattern reg = Pattern.compile( "^.*((youtu.be\\/)|(v\\/)|(\\/u\\/\\w\\/)|(embed\\/)|(watch\\?)|(shorts\\/))\\??v?=?([^#&?]*).*");
        Matcher matcher = reg.matcher(link);
        if (matcher.find() && matcher.group(8).length() == 11) {
            return matcher.group(8);
        }
        return null;
    }

    private void downloadVideoToFile(Videos videos, String savePath) {
        String url = getURL(videos);
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();
            byte[] video = returnFile(request);
            File outputDir = new File("temp");
            outputDir.mkdir();
            FileOutputStream fos = new FileOutputStream("temp/video.mp4");
            fos.write(video);
            fos.close();
            downloadAudioToFile(this.response.getAudios(), "temp/audio.mp3");
            Merger.mergeAudioAndVideo("temp/audio.mp3", "temp/video.mp4", savePath);
            outputDir.delete();
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    private static String getURL(Videos videos) {
        String url = "";
        String secondUrl = "";
        for (int i = 0; i < videos.getItems().length; i++) {
            if (videos.getItems()[i].getQuality().equals("1080p") || videos.getItems()[i].getQuality().equals("1080p60")) {
                url = videos.getItems()[i].getURL();
            }
            if (videos.getItems()[i].getQuality().equals("720p")) {
                secondUrl = videos.getItems()[i].getURL();
            }
        }
        if (url.isEmpty()) {
            if (secondUrl.isEmpty()) {
                url = videos.getItems()[0].getURL();
            }
            url = secondUrl;
        }
        return url;
    }

    private void downloadAudioToFile(Audios audios, String savePath) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(audios.getItems()[0].getURL()))
                    .build();
            byte[] audio = returnFile(request);
            FileOutputStream fos = new FileOutputStream(savePath);
            fos.write(audio);
            fos.close();
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
