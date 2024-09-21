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
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Nationalized;
import udpm.hn.server.entity.base.PrimaryEntity;
import udpm.hn.server.infrastructure.constant.EntityProperties;
import udpm.hn.server.infrastructure.constant.SemesterName;

import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "semester")
@DynamicUpdate
public class Semester extends PrimaryEntity implements Serializable {

    @Column(name = "name", length = EntityProperties.LENGTH_NAME)
    @Nationalized
    @Enumerated(EnumType.STRING)
    private SemesterName semesterName;

    @Column(name = "year")
    private Integer year;

    @Column(name = "start_time")
    private Long startTime;

    @Column(name = "end_time")
    private Long endTime;

    @Column(name = "semester_id")
    private Long semesterId;

}
