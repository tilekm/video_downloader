package kz.tilek.downloader.utils.response.tiktok;

public class Data {
    private String id;
    private String title;
    private String origin_cover;
    private String play;
    private String wmplay;
    private String music;
    private int duration;
    private String[] images;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }


    public String getPlay() {
        return play;
    }

    public String getWmplay() {
        return wmplay;
    }

    public String getMusic() {
        return music;
    }

    public String[] getImages() {
        return images;
    }

    public int getDuration() {
        return duration;
    }

    public String getPreview() {
        return origin_cover;
    }
}
