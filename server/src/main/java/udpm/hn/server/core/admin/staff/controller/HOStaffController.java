package udpm.hn.server.core.admin.staff.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import udpm.hn.server.infrastructure.constant.MappingConstants;

@Controller
@RequestMapping(MappingConstants.REDIRECT_ADMIN_STAFF)
public class HOStaffController {

    @GetMapping
    public String viewStaffs() {
        return "admin/staff/staff";
    }

}
