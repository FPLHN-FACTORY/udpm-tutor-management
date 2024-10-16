package udpm.hn.server.core.teacher.tutorclass.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import udpm.hn.server.core.common.base.PageableObject;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.teacher.tutorclass.model.request.TCTCUpdateLectureRequest;
import udpm.hn.server.core.teacher.tutorclass.repository.TCTCLectureRepository;
import udpm.hn.server.core.teacher.tutorclass.model.request.TCTCUpdateTutorClassDetailRequest;
import udpm.hn.server.core.teacher.tutorclass.model.request.TCTCTutorClassListRequest;
import udpm.hn.server.core.teacher.tutorclass.repository.TCTCTutorClassDetailExtendRepository;
import udpm.hn.server.core.teacher.tutorclass.repository.TCTCExtendRepository;
import udpm.hn.server.core.teacher.tutorclass.repository.TCTCTutorClassExtendRepository;
import udpm.hn.server.core.teacher.tutorclass.service.TCTCTutorClassService;
import udpm.hn.server.entity.Lecture;
import udpm.hn.server.entity.Staff;
import udpm.hn.server.entity.TutorClassDetail;
import udpm.hn.server.infrastructure.constant.Format;
import udpm.hn.server.infrastructure.constant.Shift;
import udpm.hn.server.utils.Helper;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class TCTCTutorClassServiceImpl implements TCTCTutorClassService {

    private final TCTCTutorClassExtendRepository tutorClassRepository;

    private final TCTCExtendRepository teacherRepository;

    private final TCTCTutorClassDetailExtendRepository tutorClassDetailExtendRepository;

    private final TCTCLectureRepository lectureRepository;

    @Override
    public ResponseObject<?> getTutorClassesByTeacher(TCTCTutorClassListRequest request) {
        log.info("Teacher id: " +  request.getTeacherId());
        log.info("Request: " + request.toString());
        Optional<Staff> staffOptional = teacherRepository.findById(request.getTeacherId());
        if (staffOptional.isEmpty()) {
            return new ResponseObject<>(null, HttpStatus.BAD_GATEWAY, "Giảng viên không tồn tại");
        }
        Pageable pageable = Helper.createPageable(request, "createDate");
        return new ResponseObject<>(
                PageableObject.of(tutorClassRepository.getTutorClassByTeacher(pageable, request)),
                HttpStatus.OK,
                "Lấy list thành công."
        );
    }

    @Override
    public ResponseObject<?> getLecturesByTutorClassDetailId(String id) {
        TutorClassDetail tutorClassDetail = tutorClassDetailExtendRepository.findById(id).orElse(null);
        if (tutorClassDetail == null) {
            return new ResponseObject<>(null, HttpStatus.BAD_GATEWAY, "Lớp tutor không tồn tại!");
        }
        return new ResponseObject<>(
                lectureRepository.getLectures(tutorClassDetail.getId()),
                HttpStatus.OK,
                "Lấy list lectures thành công."
        );
    }

    @Override
    public ResponseObject<?> updateTutorClassDetail(List<TCTCUpdateTutorClassDetailRequest> request) {
        try {
            // Lọc request chỉ lấy những cái có shift
            List<TCTCUpdateTutorClassDetailRequest> filteredRequest = request.stream()
                    .filter(r -> r.getShift() != null)
                    .toList();

            if (filteredRequest.isEmpty()) {
                return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Không có dữ liệu cần cập nhật");
            }

            // Cập nhật cho từng yêu cầu đã lọc
            filteredRequest.forEach(updateRequest -> {
                Optional<TutorClassDetail> optionalTutorClassDetail = tutorClassDetailExtendRepository.findById(updateRequest.getId());

                optionalTutorClassDetail.ifPresent(tutorClassDetail -> {
                    // Cập nhật shift
                    Shift shift = Shift.fromString(updateRequest.getShift());
                    tutorClassDetail.setShift(shift);

                    // Lưu lớp tutor
                    tutorClassDetailExtendRepository.save(tutorClassDetail);

                    // Cập nhật hoặc tạo các lecture
                    List<Lecture> list = lectureRepository.findByTutorClassDetail(tutorClassDetail);

                    if (list.isEmpty()) {
                        createLectures(tutorClassDetail, shift);
                    } else {
                        updateLectures(list, shift);
                    }
                });
            });

            return new ResponseObject<>(true, HttpStatus.OK, "Cập nhật thành công");
        } catch (Exception e) {
            // Xử lý lỗi
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Cập nhật thất bại: " + e.getMessage());
        }
    }

    @Override
    public ResponseObject<?> updateLecture(List<TCTCUpdateLectureRequest> list) {
        try {
            // Lọc request chỉ lấy những cái có shift
            List<TCTCUpdateLectureRequest> filteredRequest = list.stream()
                    .filter(r -> r.getShift() != null || r.getStartTime() != null)
                    .toList();

            if (filteredRequest.isEmpty()) {
                return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Không có dữ liệu cần cập nhật");
            }

            // Cập nhật cho từng yêu cầu đã lọc
            filteredRequest.forEach(updateRequest -> {
                Optional<Lecture> optionalLecture = lectureRepository.findById(updateRequest.getId());

                optionalLecture.ifPresent(lecture -> {
                    // Cập nhật shift
                    Shift shift = Shift.fromString(updateRequest.getShift());
                    lecture.setShift(shift);
                    lecture.setStartDate(updateRequest.getStartTime());
                    lecture.setFormat(Format.fromString(updateRequest.getFormat()));
                    // Lưu lớp tutor
                    lectureRepository.save(lecture);
                });
            });

            return new ResponseObject<>(true, HttpStatus.OK, "Cập nhật thành công");
        } catch (Exception e) {
            // Xử lý lỗi
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Cập nhật thất bại: " + e.getMessage());
        }
    }

    private void createLectures(TutorClassDetail tutorClassDetail, Shift shift) {
        IntStream.range(0, tutorClassDetail.getNumberOfLectures()).forEach(j -> {
            Lecture lecture = new Lecture();
            lecture.setTutorClassDetail(tutorClassDetail);
            lecture.setShift(shift);
            lecture.setFormat(tutorClassDetail.getTutorClass().getFormat());
            lecture.setName("Buổi " + (j + 1));
            lectureRepository.save(lecture);
        });
    }

    private void updateLectures(List<Lecture> lectures, Shift shift) {
        lectures.forEach(lecture -> {
            lecture.setShift(shift);
            lectureRepository.save(lecture);
        });
    }

}
