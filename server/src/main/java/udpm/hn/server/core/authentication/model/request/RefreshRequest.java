package udpm.hn.server.core.authentication.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshRequest {

    private String refreshToken;

}
