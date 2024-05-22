package kz.tilek.downloader.utils.response.youtube;


public class AudiosItem {
    private String url;
    private long lengthMS;
    private String mimeType;
    private String extension;
    private long lastModified;
    private long size;
    private String sizeText;

    public String getURL() { return url; }
    public void setURL(String value) { this.url = value; }

    public long getLengthMS() { return lengthMS; }
    public void setLengthMS(long value) { this.lengthMS = value; }

    public String getMIMEType() { return mimeType; }
    public void setMIMEType(String value) { this.mimeType = value; }

    public String getExtension() { return extension; }
    public void setExtension(String value) { this.extension = value; }

    public long getLastModified() { return lastModified; }
    public void setLastModified(long value) { this.lastModified = value; }

    public long getSize() { return size; }
    public void setSize(long value) { this.size = value; }

    public String getSizeText() { return sizeText; }
    public void setSizeText(String value) { this.sizeText = value; }
}