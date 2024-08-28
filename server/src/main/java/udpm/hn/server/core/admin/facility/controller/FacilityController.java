package udpm.hn.server.core.admin.facility.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import udpm.hn.server.infrastructure.constant.MappingConstants;

@Controller
@RequestMapping(MappingConstants.REDIRECT_ADMIN_FACILITY)
public class FacilityController {

    @GetMapping
    public String viewRole() {
        return "admin/facility/facility";
    }

}
