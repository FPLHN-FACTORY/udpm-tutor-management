package udpm.hn.server.core.planner.plan.service;

import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.planner.plan.model.request.PLPLCreatePlanRequest;
import udpm.hn.server.core.planner.plan.model.request.PLPLPlanInfoRequest;
import udpm.hn.server.core.planner.plan.model.request.PLPLPlanListRequest;
import udpm.hn.server.core.planner.plan.model.request.PLPLTutorListRequest;
import udpm.hn.server.core.planner.plan.model.request.PLPLUpdatePlanRequest;

public interface PLPLPlansService {

    ResponseObject<?> getAllPlans(PLPLPlanListRequest request);

    ResponseObject<?> createPlan(PLPLCreatePlanRequest request);

    ResponseObject<?> updatePlan(String planId, PLPLUpdatePlanRequest request);

    ResponseObject<?> getPlansDetail(String planId);

    ResponseObject<?> getPlansBySemester(PLPLPlanInfoRequest request);

    ResponseObject<?> getPlansInfo(PLPLPlanInfoRequest request);

    ResponseObject<?> getPlansInfoById(String id);

}
