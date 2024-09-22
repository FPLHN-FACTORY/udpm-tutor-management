package udpm.hn.server.infrastructure.connection;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.entity.Block;
import udpm.hn.server.entity.Semester;
import udpm.hn.server.infrastructure.connection.response.*;
import udpm.hn.server.infrastructure.constant.BlockName;
import udpm.hn.server.infrastructure.constant.EntityStatus;
import udpm.hn.server.infrastructure.constant.Message;
import udpm.hn.server.infrastructure.constant.SemesterName;
import udpm.hn.server.infrastructure.exception.RestApiException;
import udpm.hn.server.repository.BlockRepository;
import udpm.hn.server.repository.SemesterRepository;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class IdentityConnection {

    @Value("${identity.host}")
    private String domainIdentity;

    @Value("${identity.client-id}")
    private String clientId;

    @Value("${identity.client-secret}")
    private String clientSecret;

    private final SemesterRepository semesterRepository;

    private final BlockRepository blockRepository;

    private static final Map<String, Object> EMPTY_ADDITIONAL_PROPS = null;

    /**
     * @param userCodes: Truyền một list id của các user cần lấy thông tìn
     * @return Trả về 1 list UserInformationResponse (là các user có id được truyền vào bằng list)
     */
    public List<UserInformationResponse> handleCallApiGetListUserByListId(List<String> userCodes, String facilityId, String departmentId) {
        try {
            String apiUrl = domainIdentity + "/api/connector/get-detail-users";
            WebClient webClient = WebClient.create(apiUrl);

            Map<String, Object> requestBody = getCurrentClientAndCampusSubjectAuthorizeProps(EMPTY_ADDITIONAL_PROPS, facilityId, departmentId);
            requestBody.put("userCodes", userCodes);

            Mono<List<UserInformationResponse>> responseMono = webClient
                    .post()
                    .body(BodyInserters.fromValue(requestBody))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<>() {
                    });

            return responseMono.block();
        } catch (Exception e) {
            throw new RestApiException(Message.Exception.CALL_API_FAIL);
        }
    }

    /**
     * @param roleCode: Truyền vào Constants role
     * @return List tất cả các user có role được truyền vào
     */
    public List<UserInformationResponse> handleCallApiGetUserByRoleAndModule(String roleCode, String facilityId, String departmentId) {
        try {
            String apiUrl = domainIdentity + "/api/connector/get-list-user-by-role-code";
            WebClient webClient = WebClient.create(apiUrl);

            Map<String, Object> requestBody = getCurrentClientAndCampusSubjectAuthorizeProps(EMPTY_ADDITIONAL_PROPS, facilityId, departmentId);
            requestBody.put("roleCode", roleCode);

            Mono<List<UserInformationResponse>> responseMono = webClient
                    .post()
                    .body(BodyInserters.fromValue(requestBody))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<>() {
                    });

            return responseMono.block();
        } catch (Exception e) {
            e.printStackTrace(System.out);
            throw new RestApiException(Message.Exception.CALL_API_FAIL);
        }
    }

    /**
     * @param userCode vào id của 1 user cần lấy thông tin
     * @return Trả về thông tin của 1 user
     */
    public UserInformationResponse getUserByCode(String userCode, String facilityId, String departmentId) {
        try {
            String apiUrl = domainIdentity + "/api/connector/get-user-by-id";
            WebClient webClient = WebClient.create(apiUrl);

            Map<String, Object> requestBody = getCurrentClientAndCampusSubjectAuthorizeProps(EMPTY_ADDITIONAL_PROPS, facilityId, departmentId);
            requestBody.put("userCode", userCode);

            Mono<UserInformationResponse> responseMono = webClient
                    .post()
                    .body(BodyInserters.fromValue(requestBody))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<>() {
                    });

            return responseMono.block();
        } catch (Exception e) {
            e.printStackTrace(System.out);
            throw new RestApiException(Message.Exception.CALL_API_FAIL);
        }
    }

    /**
     * @return Trả về list các department
     */
    public List<DepartmentResponse> getDepartments(String facilityId, String departmentId) {
        try {
            String apiUrl = domainIdentity + "/api/connector/get-departments";

            WebClient webClient = WebClient.create(apiUrl);

            Map<String, Object> requestBody = getCurrentClientAndCampusSubjectAuthorizeProps(EMPTY_ADDITIONAL_PROPS, facilityId, departmentId);

            Mono<List<DepartmentResponse>> responseMono = webClient
                    .post()
                    .body(BodyInserters.fromValue(requestBody))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<>() {
                    });

            return responseMono.block();
        } catch (Exception e) {
            throw new RestApiException(Message.Exception.CALL_API_FAIL);
        }
    }

    /**
     * lấy data campus từ identity
     */
    public List<CampusResponse> handleCallApiGetCampusByStatus() {
        try {
            String apiUrl = domainIdentity + "/api/connector/get-campus-by-status";

            WebClient webClient = WebClient.create(apiUrl);

            Map<String, Object> requestBody = getCurrentClientAuthorizeProps();

            Mono<List<CampusResponse>> responseMono = webClient
                    .post()
                    .body(BodyInserters.fromValue(requestBody))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<>() {
                    });

            return responseMono.block();
        } catch (Exception e) {
            e.printStackTrace(System.out);
            throw new RestApiException(Message.Exception.CALL_API_FAIL);
        }
    }

    /**
     * Lấy danh sách các department theo status
     */
    public List<DepartmentSingleResponse> handleCallApiGetDepartmentsByStatus() {
        try {
            String apiUrl = domainIdentity + "/api/connector/get-departments-by-status";

            WebClient webClient = WebClient.create(apiUrl);

            Map<String, Object> requestBody = getCurrentClientAuthorizeProps();

            Mono<List<DepartmentSingleResponse>> responseMono = webClient
                    .post()
                    .body(BodyInserters.fromValue(requestBody))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<>() {
                    });

            return responseMono.block();
        } catch (Exception e) {
            e.printStackTrace(System.out);
            throw new RestApiException(Message.Exception.CALL_API_FAIL);
        }
    }

    /**
     * @return Trả về list các major
     */
    public List<MajorResponse> handleCallApiGetMajorsByStatus() {
        try {
            String apiUrl = domainIdentity + "/api/connector/get-majors-by-status";

            WebClient webClient = WebClient.create(apiUrl);

            Map<String, Object> requestBody = getCurrentClientAuthorizeProps();

            Mono<List<MajorResponse>> responseMono = webClient
                    .post()
                    .body(BodyInserters.fromValue(requestBody))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<>() {
                    });

            return responseMono.block();
        } catch (Exception e) {
            e.printStackTrace(System.out);
            throw new RestApiException(Message.Exception.CALL_API_FAIL);
        }
    }

    /**
     * @return Trả về list các department campus
     */
    public List<DepartmentCampusResponse> handleCallApiGetDepartmentCampusByStatus() {
        try {
            String apiUrl = domainIdentity + "/api/connector/get-department-campus-by-status";

            WebClient webClient = WebClient.create(apiUrl);

            Map<String, Object> requestBody = getCurrentClientAuthorizeProps();

            Mono<List<DepartmentCampusResponse>> responseMono = webClient
                    .post()
                    .body(BodyInserters.fromValue(requestBody))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<>() {
                    });

            return responseMono.block();
        } catch (Exception e) {
            e.printStackTrace(System.out);
            throw new RestApiException(Message.Exception.CALL_API_FAIL);
        }
    }

    /**
     * @return Trả về list các major campus
     */
    public List<MajorCampusResponse> handleCallApiGetMajorCampusByStatus() {
        try {
            String apiUrl = domainIdentity + "/api/connector/get-major-campus-by-status";

            WebClient webClient = WebClient.create(apiUrl);

            Map<String, Object> requestBody = getCurrentClientAuthorizeProps();

            Mono<List<MajorCampusResponse>> responseMono = webClient
                    .post()
                    .body(BodyInserters.fromValue(requestBody))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<>() {
                    });

            return responseMono.block();
        } catch (Exception e) {
            e.printStackTrace(System.out);
            throw new RestApiException(Message.Exception.CALL_API_FAIL);
        }
    }

    /**
     * @return Trả về list các semester và block.
     */
    public ResponseObject<?> handleCallApiGetSemesterByStatus() {
        try {
            String apiUrl = domainIdentity + "/api/connector/get-semesters-by-status";
            WebClient webClient = WebClient.create(apiUrl);
            Map<String, Object> requestBody = getCurrentClientAuthorizeProps();

            // Gọi API và nhận dữ liệu
            ResponseObject<List<SemesterResponse>> responseObject = webClient.post()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(requestBody))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<ResponseObject<List<SemesterResponse>>>() {})
                    .block();

            // Kiểm tra kết quả trả về từ API
            if (responseObject != null && responseObject.isSuccess()) {
                List<SemesterResponse> semesterData = responseObject.getData();
                List<Semester> semesters = semesterRepository.findAll();

                if (semesters.isEmpty()) {
                    // Nếu không có semester, đồng bộ tất cả các semester từ dữ liệu nhận được
                    for (SemesterResponse semesterResponse : semesterData) {
                        syncSemester(null, semesterResponse);
                    }
                } else {
                    // Nếu đã có semester, đồng bộ từng semester từ dữ liệu nhận được
                    for (SemesterResponse semesterResponse : semesterData) {
                        for (Semester semester : semesters) {
                            syncSemester(semester, semesterResponse);
                        }
                    }
                }

                return ResponseObject.successForward(null, "Đồng bộ học kỳ và block thành công!");
            } else {
                return ResponseObject.errorForward("Đồng bộ học kỳ và block không thành công!", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();  // In ra stack trace của lỗi để dễ debug
            return ResponseObject.errorForward("Đồng bộ học kỳ và block không thành công! Đã xảy ra lỗi.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void syncSemester(Semester semester, SemesterResponse semesterResponse) {
        // Khởi tạo hoặc lấy đối tượng Semester từ cơ sở dữ liệu
        Semester postSemester;
        if (semester == null) {
            postSemester = new Semester();
        } else {
            Optional<Semester> semesterOptional = semesterRepository.findBySemesterId(semesterResponse.getId());
            postSemester = semesterOptional.orElseGet(Semester::new);
        }

        // Chuyển đổi thời gian và cập nhật thuộc tính của semester
        LocalDateTime startDate = Instant.ofEpochMilli(semesterResponse.getStartTime() * 1000)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        postSemester.setYear(startDate.getYear());
        postSemester.setSemesterName(SemesterName.valueOf(semesterResponse.getSemesterName()));
        postSemester.setStartTime(semesterResponse.getStartTime() * 1000);
        postSemester.setEndTime(semesterResponse.getEndTime() * 1000);
        postSemester.setSemesterId(semesterResponse.getId());
        postSemester.setStatus(EntityStatus.ACTIVE);
        // Lưu semester vào cơ sở dữ liệu
        semesterRepository.save(postSemester);

        // Lấy danh sách blocks liên quan đến semester vừa lưu
        List<Block> blocks = blockRepository.findBlockBySemesterId(postSemester.getId());

        if (blocks.isEmpty()) {
            // Nếu không có blocks, tạo mới hai block và lưu vào cơ sở dữ liệu
            Block block1 = new Block();
            Block block2 = new Block();

            block1.setName(BlockName.BLOCK_1);
            block1.setStartTime(semesterResponse.getStartTimeFirstBlock() * 1000);
            block1.setEndTime(semesterResponse.getEndTimeFirstBlock() * 1000);
            block1.setSemester(postSemester);
            block1.setStatus(EntityStatus.ACTIVE);

            block2.setName(BlockName.BLOCK_2);
            block2.setStartTime(semesterResponse.getStartTimeSecondBlock() * 1000);
            block2.setEndTime(semesterResponse.getEndTimeSecondBlock() * 1000);
            block2.setSemester(postSemester);
            block2.setStatus(EntityStatus.ACTIVE);

            blockRepository.save(block1);
            blockRepository.save(block2);
        } else {
            // Nếu có blocks, cập nhật thông tin của từng block
            for (Block block : blocks) {
                block.setStartTime(semesterResponse.getStartTimeFirstBlock() * 1000);
                block.setEndTime(semesterResponse.getEndTimeFirstBlock() * 1000);
                block.setSemester(postSemester);
                blockRepository.save(block);
            }
        }
    }

    private Map<String, Object> getCurrentClientAndCampusSubjectAuthorizeProps(Map<String, Object> additionalProps, String facilityId, String departmentId) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("clientId", clientId);
        requestBody.put("clientSecret", clientSecret);
        requestBody.put("campusCode", facilityId);
        requestBody.put("subjectCode", departmentId);

        if (additionalProps != null) {
            requestBody.putAll(additionalProps);
        }

        return requestBody;
    }

    private Map<String, Object> getCurrentClientAuthorizeProps() {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("clientId", clientId);
        requestBody.put("clientSecret", clientSecret);
        return requestBody;
    }
}
