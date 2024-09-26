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
import udpm.hn.server.core.admin.departments.department.repository.DepartmentExtendRepository;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.entity.Block;
import udpm.hn.server.entity.Department;
import udpm.hn.server.entity.DepartmentFacility;
import udpm.hn.server.entity.Major;
import udpm.hn.server.entity.MajorFacility;
import udpm.hn.server.entity.Semester;
import udpm.hn.server.infrastructure.connection.response.CampusResponse;
import udpm.hn.server.infrastructure.connection.response.DepartmentCampusResponse;
import udpm.hn.server.infrastructure.connection.response.DepartmentResponse;
import udpm.hn.server.infrastructure.connection.response.MajorCampusResponse;
import udpm.hn.server.infrastructure.connection.response.MajorResponse;
import udpm.hn.server.infrastructure.connection.response.SemesterResponse;
import udpm.hn.server.infrastructure.connection.response.StaffResponse;
import udpm.hn.server.infrastructure.connection.response.StaffRoleResponse;
import udpm.hn.server.infrastructure.connection.response.UserInformationResponse;
import udpm.hn.server.infrastructure.constant.BlockName;
import udpm.hn.server.infrastructure.constant.Message;
import udpm.hn.server.infrastructure.constant.SemesterName;
import udpm.hn.server.infrastructure.exception.RestApiException;
import udpm.hn.server.repository.BlockRepository;
import udpm.hn.server.repository.DepartmentFacilityRepository;
import udpm.hn.server.repository.FacilityRepository;
import udpm.hn.server.repository.MajorFacilityRepository;
import udpm.hn.server.repository.MajorRepository;
import udpm.hn.server.repository.SemesterRepository;
import udpm.hn.server.repository.StaffRepository;

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

    private final MajorRepository majorRepository;

    private final MajorFacilityRepository majorFacilityRepository;

    private final DepartmentFacilityRepository departmentFacilityRepository;

    private final StaffRepository staffRepository;

    private final FacilityRepository facilityRepository;

    private final DepartmentExtendRepository departmentExtendRepository;

    private static final Map<String, Object> EMPTY_ADDITIONAL_PROPS = null;

    /**
     * @param userCodes: Truyền một list id của các user cần lấy thông tìn
     * @return Trả về 1 list UserInformationResponse (là các user có id được truyền vào bằng list)
     */
    public List<UserInformationResponse> getListStaffDetailByStaffCode(
            List<String> userCodes,
            String facilityId,
            String departmentId
    ) {
        try {
            String apiUrl = domainIdentity + APIConnection.PREFIX_STAFF + "/list";
            WebClient webClient = WebClient.create(apiUrl);

            Map<String, Object> requestBody = getCurrentClientAndCampusSubjectAuthorizeProps(facilityId, departmentId);
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
    public List<UserInformationResponse> getListStaffByRoleCode(
            String roleCode,
            String facilityId,
            String departmentId
    ) {
        try {
            String apiUrl = domainIdentity + APIConnection.PREFIX_STAFF + "/role-code";
            WebClient webClient = WebClient.create(apiUrl);

            Map<String, Object> requestBody = getCurrentClientAndCampusSubjectAuthorizeProps(facilityId, departmentId);
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
    public UserInformationResponse getStaffByCode(String userCode, String facilityId, String departmentId) {
        try {
            String apiUrl = domainIdentity + APIConnection.PREFIX_STAFF + "/detail";
            WebClient webClient = WebClient.create(apiUrl);

            Map<String, Object> requestBody = getCurrentClientAndCampusSubjectAuthorizeProps(facilityId, departmentId);
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
    public List<DepartmentResponse> getDepartments() {
        try {
            String apiUrl = domainIdentity + APIConnection.PREFIX_DEPARTMENT;
            WebClient webClient = WebClient.create(apiUrl);
            Map<String, Object> requestBody = getCurrentClientAuthorizeProps();

            ResponseObject<List<DepartmentResponse>> responseObject  = webClient.post()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(requestBody))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<ResponseObject<List<DepartmentResponse>>>() {
                    })
                    .block();
            return responseObject.getData();
        } catch (Exception e) {
            throw new RestApiException(Message.Exception.CALL_API_FAIL);
        }
    }

    /**
     * lấy data campus từ identity
     */
    public List<CampusResponse> getCampuses() {
        try {
            String apiUrl = domainIdentity + APIConnection.PREFIX_CAMPUS;

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
     * @return Trả về list các major
     */
    public List<MajorResponse> getMajors() {
        try {
            String apiUrl = domainIdentity + APIConnection.PREFIX_MAJOR;
            WebClient webClient = WebClient.create(apiUrl);
            Map<String, Object> requestBody = getCurrentClientAuthorizeProps();
            ResponseObject<List<MajorResponse>> responseObject  = webClient.post()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(requestBody))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<ResponseObject<List<MajorResponse>>>() {
                    })
                    .block();
            return responseObject.getData();
        } catch (Exception e) {
            e.printStackTrace(System.out);
            throw new RestApiException(Message.Exception.CALL_API_FAIL);
        }
    }

    /**
     * @return Trả về list các department campus
     */
    public List<DepartmentCampusResponse> getDepartmentCampuses() {
        try {
            String apiUrl = domainIdentity + APIConnection.PREFIX_DEPARTMENT_CAMPUS;
            WebClient webClient = WebClient.create(apiUrl);
            Map<String, Object> requestBody = getCurrentClientAuthorizeProps();

            ResponseObject<List<DepartmentCampusResponse>> responseObject  = webClient.post()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(requestBody))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<ResponseObject<List<DepartmentCampusResponse>>>() {
                    })
                    .block();

            return responseObject.getData();
        } catch (Exception e) {
            e.printStackTrace(System.out);
            throw new RestApiException(Message.Exception.CALL_API_FAIL);
        }
    }


    /**
     * @return Trả về list các major campus
     */
    public List<MajorCampusResponse> getMajorCampuses() {
        try {
            String apiUrl = domainIdentity + APIConnection.PREFIX_MAJOR_CAMPUS;
            WebClient webClient = WebClient.create(apiUrl);
            Map<String, Object> requestBody = getCurrentClientAuthorizeProps();
            ResponseObject<List<MajorCampusResponse>> responseObject  = webClient.post()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(requestBody))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<ResponseObject<List<MajorCampusResponse>>>() {
                    })
                    .block();

            return responseObject.getData();
        } catch (Exception e) {
            e.printStackTrace(System.out);
            throw new RestApiException(Message.Exception.CALL_API_FAIL);
        }
    }


    /**
     * @return Trả về list các semester và block.
     */
    public List<SemesterResponse> getSemesters() {
        try {
            String apiUrl = domainIdentity + APIConnection.PREFIX_SEMESTER;
            WebClient webClient = WebClient.create(apiUrl);
            Map<String, Object> requestBody = getCurrentClientAuthorizeProps();

            // Gọi API và nhận dữ liệu
            ResponseObject<List<SemesterResponse>> responseObject = webClient.post()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(requestBody))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<ResponseObject<List<SemesterResponse>>>() {
                    })
                    .block();

            return responseObject.getData();
        } catch (Exception e) {
            e.printStackTrace(System.out);
            throw new RestApiException(Message.Exception.CALL_API_FAIL);
        }
    }

    private Map<String, Object> getCurrentClientAndCampusSubjectAuthorizeProps(
            String facilityId,
            String departmentId
    ) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("clientId", clientId);
        requestBody.put("clientSecret", clientSecret);
        requestBody.put("campusCode", facilityId);
        requestBody.put("subjectCode", departmentId);

        return requestBody;
    }

    private Map<String, Object> getCurrentClientAuthorizeProps() {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("clientId", clientId);
        requestBody.put("clientSecret", clientSecret);
        return requestBody;
    }

    /**
     * @return Trả về list các semester và block.
     */
    public List<StaffResponse> getStaffs(String campusCode) {
        try {
            String apiUrl = domainIdentity + APIConnection.PREFIX_STAFF;
            WebClient webClient = WebClient.create(apiUrl);
            Map<String, Object> requestBody = getCurrentClientAuthorizeProps();
            requestBody.put("campusCode", campusCode);

            // Gọi API và nhận dữ liệu
            ResponseObject<List<StaffResponse>> responseObject = webClient.post()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(requestBody))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<ResponseObject<List<StaffResponse>>>() {
                    })
                    .block();
            return responseObject.getData();
        } catch (Exception e) {
            e.printStackTrace(System.out);
            throw new RestApiException(Message.Exception.CALL_API_FAIL);
        }
    }

    public List<StaffRoleResponse> getRoleStaffs(String campusCode, String userCode) {
        try {
            String apiUrl = domainIdentity + APIConnection.PREFIX_STAFF_ROLE;
            WebClient webClient = WebClient.create(apiUrl);
            Map<String, Object> requestBody = getCurrentClientAuthorizeProps();
            requestBody.put("campusCode", campusCode);
            requestBody.put("userCode", userCode);

            // Gọi API và nhận dữ liệu
            ResponseObject<List<StaffRoleResponse>> responseObject = webClient.post()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(requestBody))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<ResponseObject<List<StaffRoleResponse>>>() {
                    })
                    .block();
            return responseObject.getData();
        } catch (Exception e) {
            e.printStackTrace(System.out);
            throw new RestApiException(Message.Exception.CALL_API_FAIL);
        }
    }


    private static class APIConnection {

        public static final String CONNECTOR = "/api/connector";

        public static final String PREFIX_STAFF = CONNECTOR + "/staffs";

        public static final String PREFIX_STAFF_ROLE = CONNECTOR + "/staffs/roles";

        public static final String PREFIX_DEPARTMENT = CONNECTOR + "/departments";

        public static final String PREFIX_CAMPUS = CONNECTOR + "/campuses";

        public static final String PREFIX_MAJOR = CONNECTOR + "/majors";

        public static final String PREFIX_SEMESTER = CONNECTOR + "/semesters";

        public static final String PREFIX_DEPARTMENT_CAMPUS = CONNECTOR + "/department-campus";

        public static final String PREFIX_MAJOR_CAMPUS = CONNECTOR + "/major-campus";

    }

}
