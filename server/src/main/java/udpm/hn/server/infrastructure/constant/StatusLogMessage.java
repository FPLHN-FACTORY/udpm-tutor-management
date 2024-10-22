package udpm.hn.server.infrastructure.constant;

import lombok.Getter;

@Getter
public enum StatusLogMessage {
    SUCCESS("Thành công"),
    ERROR("Thất bại");

    private final String message;

    StatusLogMessage(String message) {
        this.message = message;
    }
}
