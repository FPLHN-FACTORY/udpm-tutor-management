package udpm.hn.server.infrastructure.constant;

public enum LectureStatus {
    NOT_STARTED,
    IN_PROGRESS,
    COMPLETED,
    RESCHEDULED;

    public static LectureStatus fromString(String status) {
        for (LectureStatus lectureStatus : LectureStatus.values()) {
            if (lectureStatus.name().equalsIgnoreCase(status)) {
                return lectureStatus;
            }
        }
        return null;
    }

}
