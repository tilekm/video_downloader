package kz.tilek.downloader.utils.response.youtube;


public class Audios {
    private boolean status;
    private long expiration;
    private AudiosItem[] items;

    public boolean getStatus() { return status; }
    public void setStatus(boolean value) { this.status = value; }

    public long getExpiration() { return expiration; }
    public void setExpiration(long value) { this.expiration = value; }

    public AudiosItem[] getItems() { return items; }
    public void setItems(AudiosItem[] value) { this.items = value; }
}
