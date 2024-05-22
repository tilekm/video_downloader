package kz.tilek.downloader.utils.response.youtube;

public class VideosItem {
    private String url;
    private long lengthMS;
    private String mimeType;
    private Extension extension;
    private long lastModified;
    private long size;
    private String sizeText;
    private boolean hasAudio;
    private String quality;
    private long width;
    private long height;

    public String getURL() { return url; }

    public long getLengthMS() { return lengthMS; }

    public String getMIMEType() { return mimeType; }

    public Extension getExtension() { return extension; }

    public long getLastModified() { return lastModified; }

    public long getSize() { return size; }

    public String getSizeText() { return sizeText; }

    public boolean getHasAudio() { return hasAudio; }

    public String getQuality() { return quality; }

    public long getWidth() { return width; }

    public long getHeight() { return height; }
}