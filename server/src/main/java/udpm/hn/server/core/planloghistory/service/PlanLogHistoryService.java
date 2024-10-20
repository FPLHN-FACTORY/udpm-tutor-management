package udpm.hn.server.core.planloghistory.service;

import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.planloghistory.model.request.PlanLogHistoryFilterRequest;
import udpm.hn.server.core.planloghistory.model.request.PlanLogHistoryRequest;

public interface PlanLogHistoryService {

    Boolean createPlanLogHistory(PlanLogHistoryRequest request);

    ResponseObject<?> getAllPlanLogHistory(PlanLogHistoryFilterRequest request);

    ResponseObject<?> getDetailPlanLogHistory(String id);

}
