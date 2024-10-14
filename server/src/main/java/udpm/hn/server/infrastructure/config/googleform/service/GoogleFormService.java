package udpm.hn.server.infrastructure.config.googleform.service;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.planner.plan.model.response.PLPLTutorClassDetailResponse;
import udpm.hn.server.core.planner.plan.repository.PLPLStaffsRepository;
import udpm.hn.server.entity.Block;
import udpm.hn.server.entity.Plan;
import udpm.hn.server.entity.Semester;
import udpm.hn.server.entity.Staff;
import udpm.hn.server.infrastructure.config.googleform.repository.GFPLPlansRepository;
import udpm.hn.server.infrastructure.config.googleform.repository.GFPLTutorClassDetailRepository;
import udpm.hn.server.infrastructure.constant.PlanStatus;
import udpm.hn.server.utils.UserContextHelper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GoogleFormService {

    @Value("${google.app-script}")
    private String googleAppScript;
    private final PLPLStaffsRepository pLPLStaffsRepository;
    private final GFPLTutorClassDetailRepository tutorClassDetailRepository;
    private final GFPLPlansRepository pLPLPlansRepository;

    public ResponseObject<?> createForm(Plan plan) {
        // Lấy danh sách mã giảng viên
        List<String> staffs = pLPLStaffsRepository.getStaffCodesByRole(
                Arrays.asList("GIANG_VIEN"),
                plan.getDepartmentFacility().getDepartment().getCode(),
                plan.getDepartmentFacility().getFacility().getCode()
        );

        // Lấy danh sách chi tiết lớp học
        List<PLPLTutorClassDetailResponse> tcdList = tutorClassDetailRepository.getDataTutorClassDetail(plan.getId());
        Semester semester = plan.getBlock().getSemester();
        Block block = plan.getBlock();
        String formName = "Đăng ký Tutor_" + semester.getSemesterName() + "_" + semester.getYear() + "_" + block.getName();
        // Tạo dữ liệu yêu cầu
        Map<String, Object> data = new HashMap<>();
        data.put("instructors", staffs);
        data.put("formName", formName);
        Staff staffEdit = pLPLStaffsRepository.findById(UserContextHelper.getCurrentUserId()).orElse(null);
        data.put("editorEmail", staffEdit.getEmailFpt());
        // Tạo danh sách lớp học và link
        Map<String, String> classMap = new HashMap<>();
        tcdList.forEach(tcd -> {
            classMap.put(tcd.getTutorClassCode(), tcd.getLink());
        });
        data.put("classMap", classMap); // Danh sách lớp học và link Zalo

        // Gửi yêu cầu POST
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(googleAppScript).openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            // Chuyển đổi dữ liệu thành JSON
            String jsonInputString = new JSONObject(data).toString();

            // Gửi dữ liệu
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Kiểm tra phản hồi
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                StringBuilder response = new StringBuilder();
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
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
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream(), "utf-8"))) {
                    String errorLine;
                    while ((errorLine = br.readLine()) != null) {
                        errorResponse.append(errorLine.trim());
                    }
                }
                return new ResponseObject<>(null, HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred: " + errorResponse.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseObject<>(null, HttpStatus.INTERNAL_SERVER_ERROR, "Exception occurred: " + e.getMessage());
        }
    }

}
