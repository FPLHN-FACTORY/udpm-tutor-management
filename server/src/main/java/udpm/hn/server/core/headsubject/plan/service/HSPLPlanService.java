package udpm.hn.server.core.headsubject.plan.service;

import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.headsubject.plan.model.request.AssignedPlannerRequest;
import udpm.hn.server.core.headsubject.plan.model.request.CreatePlannerRequest;
import udpm.hn.server.core.headsubject.plan.model.request.HSPLPlanInfoRequest;
import udpm.hn.server.core.headsubject.plan.model.request.HSPLPlanListRequest;
import udpm.hn.server.core.headsubject.plan.model.request.HSPLPlannerListRequest;
import udpm.hn.server.core.planner.plan.model.request.PLPLPlanInfoRequest;
import udpm.hn.server.core.planner.plan.model.request.PLPLPlanListRequest;
import udpm.hn.server.core.planner.plan.model.request.PLPLTutorListRequest;

public interface HSPLPlanService {

    ResponseObject<?> getAllPlans(HSPLPlanListRequest request);

    ResponseObject<?> getPlansDetail(String planId);

    ResponseObject<?> getPlansBySemester(HSPLPlanInfoRequest request);

    ResponseObject<?> getPlansInfo(HSPLPlanInfoRequest request);

    ResponseObject<?> getPlansInfoById(String id);

    ResponseObject<?> getAllPlannerManager(HSPLPlannerListRequest request);

    ResponseObject<?> isAssignedPlanner(String id, AssignedPlannerRequest request);

    ResponseObject<?> createPlanner(CreatePlannerRequest request);

}
