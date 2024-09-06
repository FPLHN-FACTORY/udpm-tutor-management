package udpm.hn.server.core.authentication.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshResponse {

    private String accessToken;

    private String refreshToken;

}
