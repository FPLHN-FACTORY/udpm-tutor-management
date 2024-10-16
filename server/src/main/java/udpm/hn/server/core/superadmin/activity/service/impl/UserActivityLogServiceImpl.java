package udpm.hn.server.core.superadmin.activity.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import udpm.hn.server.core.common.base.PageableObject;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.superadmin.activity.model.request.UserActivityLogFilterRequest;
import udpm.hn.server.core.superadmin.activity.model.request.UserActivityLogRequest;
import udpm.hn.server.core.superadmin.activity.model.response.UserActivityLogResponse;
import udpm.hn.server.core.superadmin.activity.repository.OLUserActivityLogRepository;
import udpm.hn.server.core.superadmin.activity.service.UserActivityLogService;
import udpm.hn.server.entity.UserActivityLog;
import udpm.hn.server.infrastructure.constant.StatusUserActivity;
import udpm.hn.server.repository.StaffRepository;
import udpm.hn.server.utils.Helper;

@Service
@Validated
@RequiredArgsConstructor
public class UserActivityLogServiceImpl implements UserActivityLogService {

    private final OLUserActivityLogRepository userActivityLogRepository;

    private final StaffRepository staffRepository;

    @Override
    public ResponseObject<?> getAllUserActivityLog(UserActivityLogFilterRequest request) {
        Pageable pageable = Helper.createPageable(request, "createdDate");
        Page<UserActivityLogResponse> userActivityLogResponses = userActivityLogRepository.getAllUserActivityLogs(request, pageable);

        return new ResponseObject<>(
                PageableObject.of(userActivityLogResponses),
                HttpStatus.OK,
                "Lấy thành công danh sách lịch sử đăng nhập trong hệ thống"
        );
    }

    @Override
    public void createUserActivityLog(UserActivityLogRequest userActivityLogRequest) {
        UserActivityLog userActivityLog = new UserActivityLog();
        try {
            if(userActivityLogRequest.getUser() != null) {
                userActivityLog.setEmail(userActivityLogRequest.getUser().getEmailFpt());
                userActivityLog.setName(userActivityLogRequest.getUser().getName());
                userActivityLog.setCode(userActivityLogRequest.getUser().getStaffCode());
            }
            if(userActivityLogRequest.getAuthentication()!=null) {
                userActivityLog.setWorkstation("Đang fix cứng");
                WebAuthenticationDetails details = (WebAuthenticationDetails) userActivityLogRequest.getAuthentication().getDetails();
                userActivityLog.setSessionId(details.getSessionId());
                userActivityLog.setLoginTime(System.currentTimeMillis());
            }
            userActivityLog.setStatusUserActivity(userActivityLogRequest.getStatusUserActivity());
            if(userActivityLogRequest.getStatusUserActivity() == StatusUserActivity.LOGIN) {
                userActivityLog.setOperation("Đăng nhập");
            } else if(userActivityLogRequest.getStatusUserActivity() == StatusUserActivity.LOGOUT) {
                userActivityLog.setOperation("Đăng xuất");
            } else {
                userActivityLog.setOperation("Hết hạn đăng nhập");
            }

             userActivityLog.setCreatedDate(System.currentTimeMillis());
             userActivityLog.setStatus(userActivityLogRequest.getStatus());
             userActivityLogRepository.save(userActivityLog);
        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Có lỗi sảy ra khi lưu log");
        }
    }

}
