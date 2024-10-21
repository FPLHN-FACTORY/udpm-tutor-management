package udpm.hn.server.infrastructure.config.job.student.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import udpm.hn.server.entity.ClassStudentJoined;
import udpm.hn.server.entity.Role;
import udpm.hn.server.entity.Staff;
import udpm.hn.server.entity.StaffMajorFacility;
import udpm.hn.server.entity.Student;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferStudent {

    private Student student;

    private ClassStudentJoined classStudentJoined;

}
