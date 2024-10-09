package udpm.hn.server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import udpm.hn.server.entity.base.PrimaryEntity;
import udpm.hn.server.infrastructure.constant.Format;
import udpm.hn.server.infrastructure.constant.FunctionLogType;

import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@Table(name = "plan_log_history")
public class PlanLogHistory extends PrimaryEntity implements Serializable {

    @Column(name = "user_name")
    private String userName;

    private String email;

    @Column(name = "code_staff")
    private String codeStaff;

    @Column(name = "role_staff")
    private String roleStaff;

    @Column(name = "type_function")
    @Enumerated(EnumType.ORDINAL)
    private FunctionLogType typeFunction;

    @Column(name = "action")
    private String action;

    @Column(name = "timestamp")
    private Long timestamp;

    @Column(name = "id_plan")
    private String plan;

    @Column(name = "description")
    private String description;

    @Column(name = "number_of_class")
    private Integer numberOfClass;

    @Column(name = "number_of_lecture")
    private Integer numberOfLecture;

    @Column(name = "formality")
    private Format formality;

    @Column(name = "staff_info")
    private String staffInfo;

    @Column(name = "student_tutor")
    private String studentTutor;

    @Column(name = "reject_note")
    private String rejectNote;

    @Column(name = "start_date")
    private Long startDate;

    @Column(name = "end_date")
    private Long endDate;

    @Column(name = "code_tutor_class_detail")
    private String codeTutorClassDetail;

    @Column(name = "room_plan")
    private String roomPlan;

}
