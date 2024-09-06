package udpm.hn.server.infrastructure.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class APIError {

    private String message;

}
