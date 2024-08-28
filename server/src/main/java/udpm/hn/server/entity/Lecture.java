package udpm.hn.server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import udpm.hn.server.infrastructure.constant.EntityProperties;
import udpm.hn.server.infrastructure.constant.LectureType;
import udpm.hn.server.infrastructure.constant.Shift;

import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "lecture")
@DynamicUpdate
public class Lecture extends PrimaryEntity implements Serializable {

    @ManyToOne
    @JoinColumn(name = "tutor_class_id")
    private TutorClass tutorClass;

    @Column(name = "order_number")
    private Integer orderNumber;

    @Column(name = "lecture_content", length = EntityProperties.LENGTH_CONTENT)
    private String lectureContent;

    @Column(name = "start_date")
    private Long startDate;

    @Column(name = "exercise_link", length = EntityProperties.LENGTH_URL)
    private String exerciseLink;

    @Column(name = "record_link", length = EntityProperties.LENGTH_URL)
    private String recordLink;

    @Column(name = "evidence_link", length = EntityProperties.LENGTH_URL)
    private String evidenceLink;

    @Column(name = "lecture_type")
    @Enumerated(EnumType.STRING)
    private LectureType lectureType;

    @Column(name = "shift")
    @Enumerated(EnumType.STRING)
    private Shift shift;

}
