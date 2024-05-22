package kz.tilek.downloader.utils.response.youtube;

public class YoutubeResponseClass {
    private boolean status;
    private String type;
    private String id;
    private String title;
    private String description;
    private Thumbnail[] thumbnails;
    private Videos videos;
    private Audios audios;

    public boolean getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Thumbnail[] getThumbnails() {
        return thumbnails;
    }

    public Videos getVideos() {
        return videos;
    }

    public Audios getAudios() {
        return audios;
    }
}


