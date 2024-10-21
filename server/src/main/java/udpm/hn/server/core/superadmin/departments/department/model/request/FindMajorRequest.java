package udpm.hn.server.core.superadmin.departments.department.model.request;


import lombok.Getter;
import lombok.Setter;
import udpm.hn.server.core.common.base.PageableRequest;

@Getter
@Setter
public class FindMajorRequest extends PageableRequest {

    private String majorName;

    private String majorCode;

}