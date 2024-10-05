package udpm.hn.server.core.planloghistory.model.response;

import jakarta.persistence.Column;
import udpm.hn.server.core.common.base.HasOrderNumber;
import udpm.hn.server.core.common.base.IsIdentify;
import udpm.hn.server.infrastructure.constant.Format;

public interface PlanLogHistoryResponse extends IsIdentify, HasOrderNumber {
    Long getCreateDate();

    Integer getStatus();

    String getActionName();

    String getCodeStaff();

    String getEmail();

    String getRoleStaff();

    Long getTimeStampDate();

    Integer getTypeFunction();

    String getUserName();

    String getNamePlan();

    String getNameBlock();

    String getDescription();

    Long getStartDate();

    Long getEndDate();

    Integer getNumberOfClass();

    Integer getNumberOfLecture();

    Integer getFormality();

    String getStaffInfo();

    String getCodeTutorClassDetail();

    String getRejectNote();

    String getStudentTutor();

    String getRoomPlan();

}
