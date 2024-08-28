package udpm.hn.server.core.admin.subject.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import udpm.hn.server.core.admin.subject.repository.DepartmentSubjectRepository;
import udpm.hn.server.core.common.base.ResponseObject;

@Component
@RequiredArgsConstructor
public class CommonService {

    private final DepartmentSubjectRepository departmentSubjectRepository;

    public ResponseObject<?> getAllDepartmentSubject() {
        return new ResponseObject<>(
                departmentSubjectRepository.getAllDepartment(),
                HttpStatus.OK,
                "Lấy danh sách bộ môn thành công"
        );
    }

}
