package udpm.hn.server.core.superadmin.departments.department.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrUpdateMajorRequest {

    @NotBlank(message = "Bộ môn chưa được chọn")
    private String departmentId;

    @NotBlank(message = "Tên chuyên ngành không được để trống")
    private String majorName;

    @NotBlank(message = "Mã chuyên ngành không được để trống")
    private String majorCode;

}