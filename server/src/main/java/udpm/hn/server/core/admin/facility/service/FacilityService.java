package udpm.hn.server.core.admin.facility.service;

import jakarta.validation.Valid;
import udpm.hn.server.core.admin.facility.model.request.CreateUpdateFacilityRequest;
import udpm.hn.server.core.admin.facility.model.request.FacilityRequest;
import udpm.hn.server.core.common.base.ResponseObject;

public interface FacilityService {

    ResponseObject<?> getAllFacility(FacilityRequest request);

    ResponseObject<?> createFacility(@Valid CreateUpdateFacilityRequest request);

    ResponseObject<?> updateFacility(String FacilityId, @Valid CreateUpdateFacilityRequest request);

    ResponseObject<?> changeFacilityStatus(String FacilityId);

    ResponseObject<?> getFacilityById(String FacilityId);

    ResponseObject<?> synchronize();

}
