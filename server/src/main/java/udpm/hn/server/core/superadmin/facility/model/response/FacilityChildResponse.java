package udpm.hn.server.core.superadmin.facility.model.response;

import udpm.hn.server.core.common.base.HasOrderNumber;
import udpm.hn.server.entity.base.IsIdentified;

public interface FacilityChildResponse extends IsIdentified, HasOrderNumber {

    String getFacilityChildCode();

    String getFacilityChildName();

    Integer getFacilityChildStatus();

    Long getCreatedDate();

}
