package udpm.hn.server.core.admin.departments.department.model.request;


import lombok.Getter;
import lombok.Setter;
import udpm.hn.server.core.common.base.PageableRequest;

@Getter
@Setter
public class FindMajorRequest extends PageableRequest {

    private String majorName;

}