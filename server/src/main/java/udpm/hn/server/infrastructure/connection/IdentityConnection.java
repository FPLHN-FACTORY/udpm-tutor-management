package udpm.hn.server.infrastructure.connection;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import udpm.hn.server.infrastructure.constant.Message;
import udpm.hn.server.infrastructure.exception.RestApiException;
import udpm.hn.server.infrastructure.connection.response.CampusResponse;
import udpm.hn.server.infrastructure.connection.response.DepartmentCampusResponse;
import udpm.hn.server.infrastructure.connection.response.DepartmentResponse;
import udpm.hn.server.infrastructure.connection.response.DepartmentSingleResponse;
import udpm.hn.server.infrastructure.connection.response.MajorCampusResponse;
import udpm.hn.server.infrastructure.connection.response.MajorResponse;
import udpm.hn.server.infrastructure.connection.response.UserInformationResponse;
import udpm.hn.server.utils.SessionHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class IdentityConnection {

    @Setter(onMethod_ = @Autowired)
    private SessionHelper sessionHelper;

    @Value("${identity.host}")
    private String domainIdentity;

    @Value("${identity.client-id}")
    private String clientId;

    @Value("${identity.client-secret}")
    private String clientSecret;

    private static final Map<String, Object> EMPTY_ADDITIONAL_PROPS = null;

    /**
     * @param userCodes: Truyền một list id của các user cần lấy thông tìn
     * @return Trả về 1 list UserInformationResponse (là các user có id được truyền vào bằng list)
     */
    public List<UserInformationResponse> handleCallApiGetListUserByListId(List<String> userCodes) {
        try {
            String apiUrl = domainIdentity + "/api/connector/get-detail-users";
            WebClient webClient = WebClient.create(apiUrl);

            Map<String, Object> requestBody = getCurrentClientAndCampusSubjectAuthorizeProps(EMPTY_ADDITIONAL_PROPS);
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
    public List<UserInformationResponse> handleCallApiGetUserByRoleAndModule(String roleCode) {
        try {
            String apiUrl = domainIdentity + "/api/connector/get-list-user-by-role-code";
            WebClient webClient = WebClient.create(apiUrl);

            Map<String, Object> requestBody = getCurrentClientAndCampusSubjectAuthorizeProps(EMPTY_ADDITIONAL_PROPS);
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
    public UserInformationResponse getUserByCode(String userCode) {
        try {
            String apiUrl = domainIdentity + "/api/connector/get-user-by-id";
            WebClient webClient = WebClient.create(apiUrl);

            Map<String, Object> requestBody = getCurrentClientAndCampusSubjectAuthorizeProps(EMPTY_ADDITIONAL_PROPS);
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
            String apiUrl = domainIdentity + "/api/connector/get-departments";

            WebClient webClient = WebClient.create(apiUrl);

            Map<String, Object> requestBody = getCurrentClientAndCampusSubjectAuthorizeProps(EMPTY_ADDITIONAL_PROPS);

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

    private Map<String, Object> getCurrentClientAndCampusSubjectAuthorizeProps(Map<String, Object> additionalProps) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("clientId", clientId);
        requestBody.put("clientSecret", clientSecret);
        requestBody.put("campusCode", sessionHelper.getCurrentUserFacilityId());
        requestBody.put("subjectCode", sessionHelper.getCurrentUserDepartmentId());

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
