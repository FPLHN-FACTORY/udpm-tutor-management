package udpm.hn.server.core.headdepartment.plan.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import udpm.hn.server.core.headdepartment.plan.model.request.HDPLSubjectListRequest;
import udpm.hn.server.core.headdepartment.plan.model.request.HDPLTutorClassDetailRequest;
import udpm.hn.server.core.headdepartment.plan.service.HDPLTutorClassService;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.utils.Helper;

@RestController
@RequestMapping(MappingConstants.API_HEAD_DEPARTMENT_HEAD_PLAN)
@RequiredArgsConstructor
public class HDPLTutorClassRestController {

    private final HDPLTutorClassService tutorClassService;

    @GetMapping("/tutor")
    public ResponseEntity<?> getTutorClasses(HDPLSubjectListRequest request) {
        return Helper.createResponseEntity(tutorClassService.getTutorClasses(request));
    }

    @GetMapping("/tutor/{id}")
    public ResponseEntity<?> getDetailTutorClass(@PathVariable String id) {
        return Helper.createResponseEntity(tutorClassService.getDetailTutorClass(id));
    }

    @GetMapping("/tutor-detail")
    public ResponseEntity<?> getAllTutorClassDetail(HDPLTutorClassDetailRequest request) {
        return Helper.createResponseEntity(tutorClassService.getTutorClassDetailByTutorClassId(request));
    }

}
