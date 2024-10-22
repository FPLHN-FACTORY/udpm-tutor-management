package udpm.hn.server.infrastructure.config.job.student.commonio;

import jakarta.transaction.Transactional;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import udpm.hn.server.entity.ClassStudentJoined;
import udpm.hn.server.entity.Role;
import udpm.hn.server.entity.Student;
import udpm.hn.server.infrastructure.config.job.student.model.dto.TransferStudent;
import udpm.hn.server.infrastructure.config.job.student.repository.ConfigClassStudentJoinCustomRepository;
import udpm.hn.server.infrastructure.config.job.student.repository.ConfigStudentCustomRepository;
import udpm.hn.server.infrastructure.constant.EntityStatus;

import java.util.UUID;

@Component
@Slf4j
@Transactional
public class StudentWriter implements ItemWriter<TransferStudent> {

    @Setter(onMethod_ = {@Autowired})
    private ConfigStudentCustomRepository studentCustomRepository;

    @Setter(onMethod_ = {@Autowired})
    private ConfigClassStudentJoinCustomRepository classStudentJoinCustomRepository;

    @Override
    public void write(Chunk<? extends TransferStudent> chunk) throws Exception {
        int recordNumber = 0;

        for (TransferStudent transferStudent : chunk) {
            recordNumber++;
            try {
                Student savedStudent = saveOrUpdateStudent(transferStudent.getStudent());
                ClassStudentJoined classStudentJoined = saveClassStudentJoin(transferStudent.getClassStudentJoined(), savedStudent);
                log.info(
                        "Successfully processed record number {}: Saved Student - {}, Saved Class Student Joined - {}",
                        recordNumber, savedStudent, classStudentJoined
                );
            } catch (Exception e) {
                log.error("Error processing record number {}: {}", recordNumber, transferStudent, e);
            }
        }
    }

    private Student saveOrUpdateStudent(Student student) {
        return studentCustomRepository.save(student);
    }

    private ClassStudentJoined saveClassStudentJoin(ClassStudentJoined classStudentJoined, Student student) {
        classStudentJoined.setStudent(student);
        return classStudentJoinCustomRepository.save(classStudentJoined);
    }

}
