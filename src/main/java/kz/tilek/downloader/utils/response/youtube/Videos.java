package kz.tilek.downloader.utils.response.youtube;


public class Videos {
    private boolean status;
    private long expiration;
    private VideosItem[] items;

    public boolean getStatus() { return status; }

    public long getExpiration() { return expiration; }

    public VideosItem[] getItems() { return items; }
}
