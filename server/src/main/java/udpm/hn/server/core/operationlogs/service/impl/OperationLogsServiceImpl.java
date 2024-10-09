package udpm.hn.server.core.operationlogs.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import udpm.hn.server.core.operationlogs.model.request.OperationLogsFilterRequest;
import udpm.hn.server.core.operationlogs.model.request.OperationLogsRequest;
import udpm.hn.server.core.operationlogs.model.response.OperationLogsResponse;
import udpm.hn.server.core.operationlogs.repository.OLOperationLogsRepository;
import udpm.hn.server.core.operationlogs.service.OperationLogsService;
import udpm.hn.server.core.common.base.PageableObject;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.entity.OperationLog;
import udpm.hn.server.infrastructure.constant.EntityStatus;
import udpm.hn.server.repository.StaffRepository;
import udpm.hn.server.utils.Helper;
import udpm.hn.server.utils.UserContextHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@Validated
@RequiredArgsConstructor
public class OperationLogsServiceImpl implements OperationLogsService {

    private final StaffRepository staffRepository;

    private final OLOperationLogsRepository logSystemRepository;

    @Override
    public OperationLog createOperationLog(OperationLogsRequest request) {
        ObjectMapper mapper = new ObjectMapper();
        OperationLog operationLog = new OperationLog();

        try {
            String requestJson = (request.getRequest() != null) ? convertToJsonString(mapper, request.getRequest()) : "{}";
            String responseJson = (request.getResponse() != null) ? convertToJsonString(mapper, request.getResponse()) : "{}";
            if (requestJson.trim().startsWith("{") || requestJson.trim().startsWith("[")) {
                validateJson(requestJson);
            }
            if (responseJson.trim().startsWith("{") || responseJson.trim().startsWith("[")) {
                validateJson(responseJson);
            }
            String userId = UserContextHelper.getCurrentUserId();
            if (userId != null) {
                staffRepository.findById(userId).ifPresent(staff -> {
                    operationLog.setUserName(staff.getName());
                    operationLog.setCode(staff.getStaffCode());
                    operationLog.setEmail(staff.getEmailFpt());
                });
            }
            operationLog.setTypeFunction(request.getTypeFunction());
            operationLog.setWorkstation("ĐANG FIX CỨNG");
            operationLog.setRequest(requestJson);
            operationLog.setResponse(responseJson);
            operationLog.setApi(request.getHttpRequest().getRequestURI());
            operationLog.setCreatedDate(System.currentTimeMillis());
            if(request.getStatus()) {
                operationLog.setStatus(EntityStatus.ACTIVE);
            } else {
                operationLog.setStatus(EntityStatus.INACTIVE);
            }
            return logSystemRepository.save(operationLog);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public ResponseObject<?> getDetailOperationLog(String id) {
        return new ResponseObject<>(logSystemRepository.getDetailOperationLog(id), HttpStatus.OK, "Lấy thành công chi tiết log");
    }


    @Override
    public ResponseObject<?> getAllOperationLog(OperationLogsFilterRequest request) {
        Pageable pageable = Helper.createPageable(request, "createdDate");
        Page<OperationLogsResponse> logSystemResponses = logSystemRepository.getAllLogSystems(request, pageable);
        
        return new ResponseObject<>(
                PageableObject.of(logSystemResponses),
                HttpStatus.OK,
                "Lấy thành công danh sách lịch sử sử dụng chức năng trong hệ thống"
        );
    }

    private String convertToJsonString(ObjectMapper mapper, Object obj) throws IOException {

        if (obj instanceof OperationLogsRequest) {
            OperationLogsRequest logRequest = (OperationLogsRequest) obj;
            Map<String, Object> requestMap = new HashMap<>();
            requestMap.put("workstation", logRequest.getWorkstation());
            requestMap.put("userName", logRequest.getUserName());
            requestMap.put("typeFunction", logRequest.getTypeFunction());
            requestMap.put("request", logRequest.getRequest());
            requestMap.put("response", logRequest.getResponse());
            requestMap.put("email", logRequest.getEmail());
            requestMap.put("code", logRequest.getCode());
            requestMap.put("status", logRequest.getStatus());

            return mapper.writeValueAsString(requestMap);
        }

        if (obj instanceof String) {
            return (String) obj;
        }

        return mapper.writeValueAsString(obj);
    }

    private void validateJson(String jsonString) throws JSONException, Exception {
        try {
            if (!jsonString.trim().startsWith("{") && !jsonString.trim().startsWith("[")) {
                return;
            }
            if (jsonString.trim().startsWith("[")) {
                new JSONArray(jsonString);
            } else {
                new JSONObject(jsonString);
            }
        } catch (JSONException e) {
            throw new Exception("Invalid JSON string", e);
        }
    }

}
