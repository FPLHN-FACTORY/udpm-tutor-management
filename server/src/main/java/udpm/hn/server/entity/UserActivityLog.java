package udpm.hn.server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import udpm.hn.server.entity.base.PrimaryEntity;
import udpm.hn.server.infrastructure.constant.StatusUserActivity;

import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_activity_log")
public class UserActivityLog extends PrimaryEntity implements Serializable {
    private String workstation;
    private String email;
    private String name;
    private String code;
    @Column(name = "session_id")
    private String sessionId;
    private String operation;
    @Column(name = "login_time")
    private Long loginTime;
    @Column(name = "logout_time")
    private Long logoutTime;
    @Column(name = "status_user_activity")
    @Enumerated(EnumType.ORDINAL)
    private StatusUserActivity statusUserActivity;
}
