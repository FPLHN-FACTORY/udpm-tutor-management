package udpm.hn.server.core.headdepartment.headsubjects.service;

import jakarta.validation.Valid;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.headdepartment.headsubjects.model.request.AssignOrUnAssignSubjectForHeadSubjectRequest;
import udpm.hn.server.core.headdepartment.headsubjects.model.request.HeadSubjectRequest;
import udpm.hn.server.core.headdepartment.headsubjects.model.request.HeadSubjectSearchRequest;
import udpm.hn.server.core.headdepartment.headsubjects.model.request.ReassignHeadSubjectRequest;
import udpm.hn.server.core.headdepartment.headsubjects.model.request.SubjectByHeadSubjectRequest;

public interface HeadSubjectsService {

    ResponseObject<?> getAllHeadSubjects(HeadSubjectRequest request);

    ResponseObject<?> getSubjectsByHeadSubject(String headSubjectId, SubjectByHeadSubjectRequest request);

    ResponseObject<?> getSubjectsWithAssign(String headSubjectId, SubjectByHeadSubjectRequest request);

    ResponseObject<?> assignSubjectForHeadSubject(
            String headSubjectId,
            @Valid AssignOrUnAssignSubjectForHeadSubjectRequest request
    );

    ResponseObject<?> unassignSubjectForHeadSubject(
            String headSubjectId,
            @Valid AssignOrUnAssignSubjectForHeadSubjectRequest request
    );

    ResponseObject<?> reassignSubjectForAnotherHeadSubject(@Valid ReassignHeadSubjectRequest request);

    ResponseObject<?> searchStaff(HeadSubjectSearchRequest request);

    ResponseObject<?> syncHeadSubjectAttachWithSubjectFromPreviousSemesterToCurrentSemester();

    ResponseObject<?> checkCurrentSemesterHasHeadSubject();

}
