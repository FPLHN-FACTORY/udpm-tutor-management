package udpm.hn.server.core.headdepartment.headsubjects.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import udpm.hn.server.infrastructure.constant.MappingConstants;

@Controller
@RequestMapping(MappingConstants.REDIRECT_HEAD_DEPARTMENT_HEAD_SUBJECTS)
public class HeadSubjectsController {

    @GetMapping
    public String getHeadSubjects() {
        return "head-department/head-of-subjects/head-of-subjects";
    }

}
