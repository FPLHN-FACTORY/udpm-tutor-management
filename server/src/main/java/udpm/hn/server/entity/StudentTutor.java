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
import udpm.hn.server.entity.base.PrimaryEntity;
import udpm.hn.server.infrastructure.constant.EntityProperties;

import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "student_tutor")
@DynamicUpdate
public class StudentTutor extends PrimaryEntity implements Serializable {

    @Column(name = "student_code", length = EntityProperties.LENGTH_CODE)
    private String studentCode;

    @Column(name = "student_name", length = EntityProperties.LENGTH_NAME)
    private String studentName;

    @Column(name = "student_email")
    private String studentEmail;

}
