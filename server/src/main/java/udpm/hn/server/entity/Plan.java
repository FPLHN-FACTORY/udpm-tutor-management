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
import org.hibernate.annotations.Nationalized;
import udpm.hn.server.entity.base.PrimaryEntity;
import udpm.hn.server.infrastructure.constant.PlanStatus;

import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "plan")
@DynamicUpdate
public class Plan extends PrimaryEntity implements Serializable {

    @ManyToOne
    @JoinColumn(name = "block_id")
    private Block block;

    @ManyToOne
    @JoinColumn(name = "planner_id")
    private Staff planner;

    @ManyToOne
    @JoinColumn(name = "department_facility_id")
    private DepartmentFacility departmentFacility;

    @Column(name = "description")
    private String description;

    @Column(name = "plan_status")
    @Nationalized
    @Enumerated(EnumType.STRING)
    private PlanStatus planStatus;

    @Column(name = "reason_reject")
    private String reason;

    @Column(name = "link_google_form")
    private String linkGoogleForm;

    @Column(name = "link_sheet")
    private String linkSheet;

    @Column(name = "start_date")
    private Long startDate;

    @Column(name = "end_date")
    private Long endDate;

}
