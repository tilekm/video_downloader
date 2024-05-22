package kz.tilek.downloader.utils.response.youtube;

import java.io.IOException;

public enum Extension {
    MP4, WEBM;

    public String toValue() {
        switch (this) {
            case MP4: return "mp4";
            case WEBM: return "webm";
        }
        return null;
    }

    public static Extension forValue(String value) throws IOException {
        if (value.equals("mp4")) return MP4;
        if (value.equals("webm")) return WEBM;
        throw new IOException("Cannot deserialize Extension");
    }
}