package udpm.hn.server.infrastructure.config.googleform.model;

import udpm.hn.server.core.common.base.HasOrderNumber;
import udpm.hn.server.core.common.base.IsIdentify;

public interface GFTutorClassDetailResponse extends IsIdentify, HasOrderNumber {

    String getTutorClassCode();
    String getSubjectCode();
    String getShift();
    String getLink();
    String getTeacher();

}