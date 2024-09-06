package udpm.hn.server.core.authentication.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshRequest {

    @NotNull(message = "Refresh token is required")
    @NotBlank(message = "Refresh token is required")
    private String refreshToken;

}
