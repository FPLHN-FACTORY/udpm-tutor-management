package udpm.hn.server.core.authentication.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import udpm.hn.server.core.authentication.model.request.RefreshRequest;
import udpm.hn.server.core.authentication.model.response.RefreshResponse;
import udpm.hn.server.core.authentication.repository.ARefreshTokenExtendRepository;
import udpm.hn.server.core.authentication.repository.AStaffExtendRepository;
import udpm.hn.server.core.authentication.service.AuthenticationService;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.entity.RefreshToken;
import udpm.hn.server.infrastructure.security.service.TokenProvider;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
public class AuthenticationServiceImpl implements AuthenticationService {

    private final TokenProvider tokenProvider;

    private final ARefreshTokenExtendRepository aRefreshTokenExtendRepository;

    @Override
    public ResponseObject<?> getRefreshToken(@Valid RefreshRequest request) throws BadRequestException, JsonProcessingException {
        String refreshToken = request.getRefreshToken();

        Optional<RefreshToken> refreshTokenOptional = aRefreshTokenExtendRepository.findByRefreshToken(refreshToken);
        if (refreshTokenOptional.isEmpty()) {
            return ResponseObject.errorForward("Refresh token not found", HttpStatus.NOT_FOUND);
        }

        RefreshToken refreshTokenEntity = refreshTokenOptional.get();
        if (refreshTokenEntity.getRevokedAt() != null) {
            return ResponseObject.errorForward("Refresh token has been revoked", HttpStatus.BAD_REQUEST);
        }

        String accessToken = tokenProvider.createToken(refreshTokenEntity.getUserId());
        return ResponseObject.successForward(new RefreshResponse(accessToken, refreshToken), "Get refresh token successfully");
    }

    @Override
    public ResponseObject<?> logout(@Valid RefreshRequest request) {
        String refreshToken = request.getRefreshToken();

        Optional<RefreshToken> refreshTokenOptional = aRefreshTokenExtendRepository.findByRefreshToken(refreshToken);
        if (refreshTokenOptional.isEmpty()) {
            return ResponseObject.errorForward("Refresh token not found", HttpStatus.NOT_FOUND);
        }

        RefreshToken refreshTokenEntity = refreshTokenOptional.get();
        refreshTokenEntity.setRevokedAt(System.currentTimeMillis());
        aRefreshTokenExtendRepository.save(refreshTokenEntity);

        return ResponseObject.successForward(null, "Logout successfully");
    }

}
