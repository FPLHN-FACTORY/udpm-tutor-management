package udpm.hn.server.core.admin.subject.model.response;


import udpm.hn.server.core.common.base.IsIdentify;

public interface DetailSubjectResponse extends IsIdentify {

    String getSubjectCode();

    String getSubjectName();

    String getDepartmentId();

    String getSubjectType();

    String getSubjectStatus();

    Long getCreatedDate();

}
