package udpm.hn.server.infrastructure.config.googleform.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.entity.Block;
import udpm.hn.server.entity.Plan;
import udpm.hn.server.entity.Semester;
import udpm.hn.server.entity.Staff;
import udpm.hn.server.infrastructure.config.googleform.model.GFTutorClassDetailResponse;
import udpm.hn.server.infrastructure.config.googleform.repository.GFPLPlansRepository;
import udpm.hn.server.infrastructure.config.googleform.repository.GFPLStaffsRepository;
import udpm.hn.server.infrastructure.config.googleform.repository.GFPLTutorClassDetailRepository;
import udpm.hn.server.infrastructure.constant.PlanStatus;
import udpm.hn.server.utils.Helper;
import udpm.hn.server.utils.UserContextHelper;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleFormService {

    @Value("${google.app-script}")
    private String googleAppScript;
    private final GFPLStaffsRepository pLPLStaffsRepository;
    private final GFPLTutorClassDetailRepository tutorClassDetailRepository;
    private final GFPLPlansRepository pLPLPlansRepository;

    public ResponseObject<?> createForm(Plan plan) {
        List<String> staffs = pLPLStaffsRepository.getStaffCodesByRole(
                List.of("GIANG_VIEN"),
                plan.getDepartmentFacility().getDepartment().getCode(),
                plan.getDepartmentFacility().getFacility().getCode()
        );

        List<GFTutorClassDetailResponse> tcdList = tutorClassDetailRepository.getDataTutorClassDetail(plan.getId());
        Semester semester = plan.getBlock().getSemester();
        Block block = plan.getBlock();
        String formName = "Đăng ký Tutor_" + semester.getSemesterName() + "_" + semester.getYear() + "_" + block.getName();
        // Tạo dữ liệu yêu cầu
        Map<String, Object> data = new HashMap<>();
        data.put("instructors", staffs);
        data.put("formName", formName);
        Staff staffEdit = pLPLStaffsRepository.findById(UserContextHelper.getCurrentUserId()).orElse(null);
        data.put("editorEmail", staffEdit != null? staffEdit.getEmailFpt(): null);
        Map<String, String> classMap = new LinkedHashMap<>();
        Map<String, AtomicInteger> classCounters = new HashMap<>();  // Map để theo dõi số lớp cho từng subjectCode

        if (tcdList.stream().anyMatch(tcd -> tcd.getLink() == null || tcd.getLink().trim().isEmpty() || tcd.getShift() == null)) {
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Vui lòng nhập link zalo/ca học của tất cả lớp tutor!");
        }

        tcdList.forEach(tcd -> {
            String subjectCode = tcd.getSubjectCode();

            AtomicInteger classCounter = classCounters.computeIfAbsent(subjectCode, k -> new AtomicInteger(1));
            int currentClassNumber = classCounter.getAndIncrement();  // Tăng số lớp lên 1
            classMap.put(subjectCode + " lớp " + currentClassNumber + ' ' + Helper.convertShiftName(tcd.getShift()) + " ( " + tcd.getTutorClassCode() + " ) ", tcd.getLink());
        });

        data.put("classMap", classMap);

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(googleAppScript).openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            String jsonInputString = new JSONObject(data).toString();

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Kiểm tra phản hồi
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                StringBuilder response = new StringBuilder();
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                }

                // Phân tích cú pháp JSON
                JSONObject jsonObject = new JSONObject(response.toString());
                String formUrl = jsonObject.getString("formUrl");
                String sheetUrl = jsonObject.getString("sheetUrl");
                plan.setPlanStatus(PlanStatus.IN_PROGRESS);
                plan.setLinkGoogleForm(formUrl);
                plan.setLinkSheet(sheetUrl);
                pLPLPlansRepository.save(plan);
                return new ResponseObject<>(formUrl, HttpStatus.OK, "Tạo Google Form thành công."); // Trả về URL của Google Form
            } else {
                // Xử lý lỗi từ yêu cầu
                StringBuilder errorResponse = new StringBuilder();
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
                    String errorLine;
                    while ((errorLine = br.readLine()) != null) {
                        errorResponse.append(errorLine.trim());
                    }
                }
                log.error("Lỗi khi tạo form : {}", errorResponse);
                return new ResponseObject<>(null, HttpStatus.INTERNAL_SERVER_ERROR, "Có lôi xảy ra khi tạo form!" + errorResponse.toString());
            }
        } catch (Exception e) {
            log.error("Lỗi khi tạo form : {}", e.getMessage());
            return new ResponseObject<>(null, HttpStatus.INTERNAL_SERVER_ERROR, "Có lôi xảy ra khi tạo form!");
        }
    }

}
