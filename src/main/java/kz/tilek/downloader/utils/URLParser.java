package kz.tilek.downloader.utils;

public class URLParser {
    public static String checkURL(String url) {
        if (url.isEmpty()) {
            return "Link is empty";
        }
        String[] parts = url.split("\\.");
        if (parts.length > 2) {
            return switch (parts[1]) {
                case "tiktok", "https//tiktok" -> "tiktok";
                case "instagram", "https://instagram" -> "instagram";
                case "youtube", "youtu", "https://youtu", "https://youtube" -> "youtube";
                default -> "";
            };
        } else if (parts.length > 1) {
            return switch (parts[0]) {
                case "tiktok", "https//tiktok" -> "tiktok";
                case "instagram", "https://instagram" -> "instagram";
                case "youtube", "https://youtu", "https://youtube" -> "youtube";
                default -> "";
            };
        } else {
            if (url.contains("tiktok.com/")) {
                return "tiktok";
            } else if (url.contains("instagram.com/")) {
                return "instagram";
            } else if (url.contains("youtube.com/")) {
                return "youtube";
            } else if (url.contains("youtu.be/")) {
                return "youtube";
            } else {
                return "";
            }
        }
    }
}
