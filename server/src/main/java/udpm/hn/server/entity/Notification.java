package udpm.hn.server.entity;

import jakarta.persistence.*;
import lombok.*;
import udpm.hn.server.entity.base.PrimaryEntity;
import udpm.hn.server.infrastructure.constant.EntityProperties;

import java.io.Serializable;

@Entity
@Table(name = "notification")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Notification extends PrimaryEntity implements Serializable {

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;

    @Column(name = "send_to")
    private String sentTo;

    @Column(length = EntityProperties.LENGTH_CONTENT)
    private String content;

    @Column(name = "user_received", length = EntityProperties.LENGTH_CONTENT)
    private String userReceived;

}
