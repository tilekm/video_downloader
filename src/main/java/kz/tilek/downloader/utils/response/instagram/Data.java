package kz.tilek.downloader.utils.response.instagram;

public class Data {
    private String accessibility_caption;
    private Caption caption;
    private Clips_metadata clips_metadata;
    private CarouselMedia[] carousel_media;
    private MusicMetadata music_metadata;
    private MusicInfo music_info;
    private String id;
    private String thumbnail_url;
    private Float video_duration;
    private String video_url;
    public String getPreview() {
        return thumbnail_url;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {

        return accessibility_caption == null ? caption.getText() == null ? id : caption.getText() : accessibility_caption;
    }

    public String getVideoUrl() {
        return video_url;
    }

    public Float getDuration() {
        return video_duration;
    }

    public CarouselMedia[] getMedia() {
        return carousel_media;
    }

    public MusicMetadata getMusicMetadata() {
        return music_metadata;
    }

    public Clips_metadata getClips_metadata() {
        return clips_metadata;
    }

    public MusicInfo getMusic_info() {
        return music_info;
    }
}
