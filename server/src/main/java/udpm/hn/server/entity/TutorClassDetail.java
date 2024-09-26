package udpm.hn.server.entity;

import jakarta.persistence.Column;
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
@Table(name = "tutor_class_detail")
@DynamicUpdate
public class TutorClassDetail extends PrimaryEntity implements Serializable {

    @ManyToOne
    @JoinColumn(name = "teacher_conduct_id")
    private Staff teacherConduct;

    @ManyToOne
    @JoinColumn(name = "student_conduct_id")
    private StudentTutor studentConduct;

    @ManyToOne
    @JoinColumn(name = "tutor_class_id")
    private TutorClass tutorClass;

    @Column(name = "number_of_lectures")
    private Integer numberOfLectures;

    @Column(name = "start_date")
    private Long startDate;

    @Column(name = "end_date")
    private Long endDate;
}
