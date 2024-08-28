package udpm.hn.server.infrastructure.constant;

public enum LectureType {
    ONLINE, OFFLINE;

    public static LectureType fromString(String text) {
        for (LectureType b : LectureType.values()) {
            if (b.toString().equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }

}
