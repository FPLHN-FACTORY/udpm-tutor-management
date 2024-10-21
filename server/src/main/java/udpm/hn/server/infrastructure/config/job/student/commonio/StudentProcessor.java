package udpm.hn.server.infrastructure.config.job.student.commonio;

import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import udpm.hn.server.entity.ClassStudentJoined;
import udpm.hn.server.entity.Plan;
import udpm.hn.server.entity.Student;
import udpm.hn.server.entity.TutorClassDetail;
import udpm.hn.server.infrastructure.config.job.student.model.dto.TransferStudent;
import udpm.hn.server.infrastructure.config.job.student.model.request.StudentExcelRequest;
import udpm.hn.server.infrastructure.config.job.student.repository.ConfigClassStudentJoinCustomRepository;
import udpm.hn.server.infrastructure.config.job.student.repository.ConfigPlanCustomRepository;
import udpm.hn.server.infrastructure.config.job.student.repository.ConfigStudentCustomRepository;
import udpm.hn.server.infrastructure.config.job.student.repository.ConfigTutorClassDetailCustomRepository;
import udpm.hn.server.utils.Helper;
import java.util.Optional;

@Component
@Slf4j
public class StudentProcessor implements ItemProcessor<StudentExcelRequest, TransferStudent > {

    @Setter(onMethod_ = {@Autowired})
    private ConfigStudentCustomRepository studentCustomRepository;

    @Setter(onMethod_ = {@Autowired})
    private ConfigClassStudentJoinCustomRepository classStudentJoinCustomRepository;

    @Setter(onMethod_ = {@Autowired})
    private ConfigTutorClassDetailCustomRepository tutorClassDetailCustomRepository;

    @Setter(onMethod_ = {@Autowired})
    private ConfigPlanCustomRepository planCustomRepository;

    @Override
    public TransferStudent process(@NonNull StudentExcelRequest item) throws Exception {
        try {
            Student student = fetchStudent(item.getCode());
            Plan plan = planCustomRepository.findById(item.getPlanId()).orElse(null);
            TutorClassDetail tutorClassDetail = tutorClassDetailCustomRepository.findByCodeAndTutorClass_Plan(item.getTutorClassDetail(), plan).orElse(null);
            ClassStudentJoined classStudentJoined = fetchClassStudentJoin(tutorClassDetail, item.getCode());

            if (!Helper.checkEmail(item.getEmail())) {
                return null;
            }

            if (classStudentJoined != null) {
                return null;
            }

            if (tutorClassDetail == null) {
                return null;
            }

            return createTransferStaffRole(item, student,tutorClassDetail);
        } catch (Exception e) {
            return null;
        }
    }

    private Student fetchStudent(String studentCode) {
        Optional<Student> studentOptional = studentCustomRepository
                .findByStudentCode(
                        studentCode
                );
        return studentOptional.orElse(null);
    }

    private ClassStudentJoined fetchClassStudentJoin(TutorClassDetail tutorClassDetail, String studentCode) {
        Optional<ClassStudentJoined> classStudentJoined = classStudentJoinCustomRepository
                .findByTutorClassDetail_IdAndStudent_StudentCode(
                        tutorClassDetail.getId(),
                        studentCode
                );
        return classStudentJoined.orElse(null);
    }

    private TransferStudent createTransferStaffRole(StudentExcelRequest item, Student student,TutorClassDetail tutorClassDetail) {
        Student newStudent = updateStudent(student, item);
        ClassStudentJoined classStudentJoined = new ClassStudentJoined();
        classStudentJoined.setTutorClassDetail(tutorClassDetail);

        return new TransferStudent(newStudent, classStudentJoined);
    }

    private Student updateStudent(Student student, StudentExcelRequest item) {
        if(student == null) {
            student = new Student();
        }
        student.setStudentCode(item.getCode());
        student.setStudentName(item.getName());
        student.setStudentEmail(item.getEmail());
        return student;
    }

}
