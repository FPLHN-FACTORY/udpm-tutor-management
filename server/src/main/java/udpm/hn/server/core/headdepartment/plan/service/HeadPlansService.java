package udpm.hn.server.core.headdepartment.plan.service;

import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.headdepartment.plan.model.request.HeadPlanRequest;

public interface HeadPlansService {

    ResponseObject<?> getPlans(HeadPlanRequest request);
    ResponseObject<?> approvePlan(String planId);

}
