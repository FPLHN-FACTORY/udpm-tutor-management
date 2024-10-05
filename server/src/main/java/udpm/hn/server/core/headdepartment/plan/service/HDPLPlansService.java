package udpm.hn.server.core.headdepartment.plan.service;

import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.headdepartment.plan.model.request.HDPLPlanInfoRequest;
import udpm.hn.server.core.headdepartment.plan.model.request.HDPLPlanListRequest;
import udpm.hn.server.core.headdepartment.plan.model.request.HDPLRejectPlanRequest;

public interface HDPLPlansService {

    ResponseObject<?> getPlans(HDPLPlanListRequest request);
    ResponseObject<?> approvePlan(String planId);
    ResponseObject<?> rejectPlan(HDPLRejectPlanRequest request);
    ResponseObject<?> getPlansDetail(String planId);
    ResponseObject<?> getPlansBySemester(HDPLPlanInfoRequest request);
    ResponseObject<?> getPlansInfo(HDPLPlanInfoRequest request);
    ResponseObject<?> getPlansInfoById(String id);

}
