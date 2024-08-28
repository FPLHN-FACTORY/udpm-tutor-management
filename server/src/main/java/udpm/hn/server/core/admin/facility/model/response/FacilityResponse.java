package udpm.hn.server.core.admin.facility.model.response;

import udpm.hn.server.core.common.base.HasOrderNumber;
import udpm.hn.server.entity.base.IsIdentified;

public interface FacilityResponse extends IsIdentified, HasOrderNumber {

    String getFacilityName();

    String getFacilityCode();

    Integer getFacilityStatus();

    Long getCreatedDate();

}
