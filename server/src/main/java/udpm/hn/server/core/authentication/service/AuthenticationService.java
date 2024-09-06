package udpm.hn.server.core.authentication.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import udpm.hn.server.core.authentication.model.request.RefreshRequest;
import udpm.hn.server.core.common.base.ResponseObject;

public interface AuthenticationService {

    ResponseObject<?> getRefreshToken(@Valid RefreshRequest request) throws BadRequestException, JsonProcessingException;

    ResponseObject<?> logout(@Valid RefreshRequest request);

}
