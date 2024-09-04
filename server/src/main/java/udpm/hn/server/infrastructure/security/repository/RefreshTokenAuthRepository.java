package udpm.hn.server.infrastructure.security.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import udpm.hn.server.entity.RefreshToken;
import udpm.hn.server.repository.RefreshTokenRepository;

import java.util.Optional;

@Repository
public interface RefreshTokenAuthRepository extends RefreshTokenRepository {

    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    @Modifying
    @Transactional
    @Query(
            value = """
                    delete from refresh_token
                    where user_id = :userId
                    """,
            nativeQuery = true
    )
    int deleteByUserId(Long userId);

    @Query(
            value = """
                            SELECT revoked_at
                            FROM refresh_token rt
                            WHERE rt.user_id = :userId
                    """,
            nativeQuery = true
    )
    Long isRevoked(String userId);


    @Query(
            value = """
                    SELECT rt
                    FROM RefreshToken rt
                    WHERE rt.userId = :userId
                    """
    )
    RefreshToken getRefreshTokenByUserId(String userId);

    Optional<RefreshToken> findByUserId(String userId);

}
