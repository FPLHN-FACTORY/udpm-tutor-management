package udpm.hn.server.infrastructure.constant;

public enum Format {
    ONLINE,
    OFFLINE;

    public static Format fromString(String format) {
        if (format != null) {
            for (Format f : Format.values()) {
                if (f.name().equalsIgnoreCase(format)) {
                    return f;
                }
            }
        }
        return null;
    }
}
