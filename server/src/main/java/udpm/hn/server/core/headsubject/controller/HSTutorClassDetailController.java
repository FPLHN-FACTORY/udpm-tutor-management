package udpm.hn.server.core.headsubject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import udpm.hn.server.core.headsubject.model.request.CreateTutorClassDetailRequest;
import udpm.hn.server.core.headsubject.model.request.TutorClassDetailRequest;
import udpm.hn.server.core.headsubject.service.HSTutorClassDetailService;
import udpm.hn.server.core.planner.plan.model.request.PLPLTutorListRequest;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.utils.Helper;

@RestController
@RequestMapping(MappingConstants.API_HEAD_SUBJECT_TUTOR_DETAIL)
@RequiredArgsConstructor
public class HSTutorClassDetailController {

    private final HSTutorClassDetailService hsTutorClassDetailService;

    @PostMapping
    public ResponseEntity<?> createTutorClass(@RequestBody CreateTutorClassDetailRequest request) {
        return Helper.createResponseEntity(hsTutorClassDetailService.createTutorClassDetail(request));
    }


    @GetMapping
    public ResponseEntity<?> getAllTutorClassDetailResponses(TutorClassDetailRequest request) {
        return Helper.createResponseEntity(hsTutorClassDetailService.getTutorClassDetailByTutorClassId(request));
    }

}
