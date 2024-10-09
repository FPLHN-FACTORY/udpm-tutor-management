package udpm.hn.server.core.planloghistory.model.request;

import lombok.Getter;
import lombok.Setter;
import udpm.hn.server.core.common.base.PageableRequest;
import udpm.hn.server.infrastructure.constant.EntityStatus;
import udpm.hn.server.infrastructure.constant.FunctionLogType;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class PlanLogHistoryFilterRequest extends PageableRequest {
    private String staffId;
    private String blockId;
    private String semesterId;
    private String planId;
    private String roleStaff;
    private String facilityId;
    private String logCodeRole;
    private Long fromDate;
    private Long toDate;
    private EntityStatus status;
    private Integer logType;
//    private FunctionLogType logType;

    public List<String> getRoleStaffList() {
        return roleStaff != null ? Arrays.asList(roleStaff.split(",")) : null;
    }

}
