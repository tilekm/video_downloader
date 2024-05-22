package kz.tilek.downloader.utils.response.instagram;

public class Clips_metadata {
    private Original_sound_info original_sound_info;

    public String getAudioURL() {
        return original_sound_info.getURL();
    }
}
