package udpm.hn.server.core.headsubject.model.response;

import udpm.hn.server.core.common.base.HasOrderNumber;
import udpm.hn.server.core.common.base.IsIdentify;

public interface TutorClassDetailResponse extends IsIdentify, HasOrderNumber {

    String getNameTutorClass();

    String getHeadSubject();

    Long getNumberOfLectures();

    Long getStartTime();

    Long getEndTime();

}
