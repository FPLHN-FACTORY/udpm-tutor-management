package udpm.hn.server.core.headdepartment.plan.service;

import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.headdepartment.plan.model.request.HDPLSubjectListRequest;
import udpm.hn.server.core.headdepartment.plan.model.request.HDPLTutorClassDetailRequest;

public interface HDPLTutorClassService {

    ResponseObject<?> getDetailTutorClass(String id);
    ResponseObject<?> getTutorClasses(HDPLSubjectListRequest request);
    ResponseObject<?> getTutorClassDetailByTutorClassId(HDPLTutorClassDetailRequest request);

}
