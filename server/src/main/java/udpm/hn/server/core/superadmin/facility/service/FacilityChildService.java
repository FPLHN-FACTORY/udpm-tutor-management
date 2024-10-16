package udpm.hn.server.core.superadmin.facility.service;

import jakarta.validation.Valid;
import udpm.hn.server.core.superadmin.facility.model.request.CreateUpdateFacilityChildRequest;
import udpm.hn.server.core.superadmin.facility.model.request.FacilityRequest;
import udpm.hn.server.core.common.base.ResponseObject;

public interface FacilityChildService {

    ResponseObject<?> getAllFacilityChild(String facilityId, FacilityRequest request);

    ResponseObject<?> createFacilityChild(String facilityId, @Valid CreateUpdateFacilityChildRequest request);

    ResponseObject<?> updateFacilityChild(String facilityChildId, @Valid CreateUpdateFacilityChildRequest request);

    ResponseObject<?> changeFacilityStatusChild(String facilityChildId);

    ResponseObject<?> getFacilityByIdChild(String facilityChildId);

}
