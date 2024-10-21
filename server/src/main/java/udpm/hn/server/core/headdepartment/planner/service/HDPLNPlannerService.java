package udpm.hn.server.core.headdepartment.planner.service;

import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.headdepartment.planner.model.request.HDPLNAssignedPlannerRequest;
import udpm.hn.server.core.headdepartment.planner.model.request.HDPLNPlannerListRequest;

public interface HDPLNPlannerService {

    ResponseObject<?> getAllPlannerManager(HDPLNPlannerListRequest request);

    ResponseObject<?> isAssignedPlanner(String id, HDPLNAssignedPlannerRequest request);

    ResponseObject<?> unAssignPlanner(String id);

}
