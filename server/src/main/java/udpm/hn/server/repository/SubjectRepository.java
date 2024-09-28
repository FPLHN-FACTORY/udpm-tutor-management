package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.admin.subject.model.response.SubjectResponse;
import udpm.hn.server.entity.Subject;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, String> {

    @Query("""
    select s.subjectCode as subjectCode,
               s.name as subjectName,
               d.name as departmentName,
               s.subjectType as subjectType,
               s.status as subjectStatus,
               s.createdTime as createdDate,
               s.id as id
        from Subject s
        left join HeadSubjectBySemester h on s.id = h.subject.id 
        left join Staff sf on sf.id = h.staff.id
        left join StaffRole sr on sr.staff.id = sf.id
        left join Role r on r.id = sr.role.id
        left join Department d on d.id = s.department.id  
        
    """)
    List<SubjectResponse> getAllSubjectByStaffId(@Param("staffId") String staffId);

}
