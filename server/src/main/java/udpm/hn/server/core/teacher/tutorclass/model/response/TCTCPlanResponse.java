package udpm.hn.server.core.teacher.tutorclass.model.response;

import udpm.hn.server.core.common.base.IsIdentify;

public interface TCTCPlanResponse extends IsIdentify {

    String getName();
    String getPlanStatus();
    Long getIsCurrent();

}
