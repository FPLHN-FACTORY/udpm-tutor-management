package udpm.hn.server.core.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import udpm.hn.server.infrastructure.constant.MappingConstants;

@Controller
public class CommonController {


    @GetMapping(MappingConstants.ADMIN)
    public String viewAdmin() {
        return "redirect:/admin/subjects";
    }

    @GetMapping(MappingConstants.HEAD_DEPARTMENT)
    public String viewHeadDepartment() {
        return "redirect:/head-department/statistics";
    }

    @GetMapping(MappingConstants.HEAD_SUBJECT)
    public String viewHeadSubject() {
        return "redirect:/head-subject/statistics";
    }

    @GetMapping(MappingConstants.TEACHER)
    public String viewTeacher() {
        return "redirect:/teacher/exam-shift";
    }

    @GetMapping(MappingConstants.STUDENT)
    public String viewStudent() {
        return "redirect:/student/exam-shift";
    }

}
