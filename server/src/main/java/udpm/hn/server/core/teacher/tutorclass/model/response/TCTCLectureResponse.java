package udpm.hn.server.core.teacher.tutorclass.model.response;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.hibernate.annotations.Nationalized;
import udpm.hn.server.core.common.base.HasOrderNumber;
import udpm.hn.server.core.common.base.IsIdentify;
import udpm.hn.server.entity.TutorClassDetail;
import udpm.hn.server.infrastructure.constant.EntityProperties;
import udpm.hn.server.infrastructure.constant.Format;
import udpm.hn.server.infrastructure.constant.LectureStatus;
import udpm.hn.server.infrastructure.constant.Shift;

public interface TCTCLectureResponse extends IsIdentify, HasOrderNumber {

    String getName();

    String getLectureContent();

    String getTotalStudents();

    String getNumberStudents();

    Long getStartTime();

    String getExerciseLink();

    String getEvidenceLink();

    String getRecordLink();

    String getFormat();

    String getLectureStatus();

    String getShift();

}
