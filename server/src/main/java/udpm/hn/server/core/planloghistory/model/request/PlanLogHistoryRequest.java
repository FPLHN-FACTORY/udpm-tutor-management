package udpm.hn.server.core.planloghistory.model.request;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import udpm.hn.server.core.common.base.PageableRequest;
import udpm.hn.server.infrastructure.constant.Format;
import udpm.hn.server.infrastructure.constant.FunctionLogType;

@Getter
@Setter
public class PlanLogHistoryRequest {


    private String userName;

    private String email;

    private String codeStaff;

    private String roleStaff;

    private FunctionLogType typeFunction;

    private String action;

    private Long timestamp;

    private String planId;

    private String description;

    private Boolean status;

    private Integer numberOfClass;

    private Integer numberOfLecture;

    private Format formality;

    private String staffInfo;

    private String studentTutor;

    private String rejectNote;

    private Long startDate;

    private Long endDate;

    private String codeTutorClassDetail;

    private String roomPlan;

}
