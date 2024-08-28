package udpm.hn.server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Nationalized;
import udpm.hn.server.entity.base.PrimaryEntity;
import udpm.hn.server.infrastructure.constant.EntityProperties;

import java.io.Serializable;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "staff")
@DynamicUpdate
public class Staff extends PrimaryEntity implements Serializable {

    @Column(name = "name", length = EntityProperties.LENGTH_NAME)
    @Nationalized
    private String name;

    @Column(name = "staff_code", length = EntityProperties.LENGTH_NAME)
    private String staffCode;

    @Column(name = "email_fe", length = EntityProperties.LENGTH_NAME)
    @Nationalized
    private String emailFe;

    @Column(name = "email_fpt", length = EntityProperties.LENGTH_NAME)
    @Nationalized
    private String emailFpt;

    @Column(name = "picture", length = EntityProperties.LENGTH_PICTURE)
    private String picture;

}
