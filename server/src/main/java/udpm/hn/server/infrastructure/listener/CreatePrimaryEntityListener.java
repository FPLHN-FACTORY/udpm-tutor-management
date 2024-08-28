package udpm.hn.server.infrastructure.listener;

import jakarta.persistence.PrePersist;
import udpm.hn.server.entity.base.PrimaryEntity;

import java.util.UUID;

public class CreatePrimaryEntityListener {

    @PrePersist
    private void onCreate(PrimaryEntity entity) {
        entity.setId(UUID.randomUUID().toString());
    }

}
