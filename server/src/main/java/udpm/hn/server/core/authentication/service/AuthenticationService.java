package udpm.hn.server.core.authentication.service;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import udpm.hn.server.core.authentication.model.request.RefreshRequest;
import udpm.hn.server.core.common.base.ResponseObject;

public interface AuthenticationService {

    ResponseObject<?> getRefreshToken(@Valid RefreshRequest request);

    ResponseObject<?> logout(@Valid RefreshRequest request);

}
