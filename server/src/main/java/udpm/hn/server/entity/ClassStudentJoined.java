package udpm.hn.server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;
import udpm.hn.server.entity.base.PrimaryEntity;

import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "class_student_joined")
@DynamicUpdate
public class ClassStudentJoined extends PrimaryEntity implements Serializable {

    @ManyToOne
    @JoinColumn(name = "tutor_class_detail_id")
    private TutorClassDetail tutorClassDetail;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

}
