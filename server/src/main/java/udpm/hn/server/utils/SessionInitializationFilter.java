package udpm.hn.server.utils;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import udpm.hn.server.infrastructure.constant.SessionConstant;

import java.io.IOException;

@Component
public class SessionInitializationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Có thể dùng để thực hiện thao tác khởi tạo filter nếu cần.
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession();

        // Thiết lập các giá trị mặc định cho session nếu chúng chưa được thiết lập.
        if (session.getAttribute(SessionConstant.CURRENT_USER_EMAIL) == null) {
            session.setAttribute(SessionConstant.CURRENT_USER_EMAIL, "congdvph31357@fpt.edu.vn");
            session.setAttribute(SessionConstant.CURRENT_USER_ID, "d1d9973e-90d8-49ed-983e-ce4bae5a6d63");
            session.setAttribute(SessionConstant.CURRENT_SEMESTER_ID, "1d470496-bb6a-402f-8016-b96b49e71c49");
            session.setAttribute(SessionConstant.CURRENT_BLOCK_ID, "block1");
            session.setAttribute(SessionConstant.CURRENT_USER_ROLE, "TRUONG_MON");
            session.setAttribute(SessionConstant.CURRENT_USER_FACILITY_ID, "e80ae0b3-7591-4c3e-8fce-19e9a91d12b7");
            session.setAttribute(SessionConstant.CURRENT_USER_DEPARTMENT_FACILITY_ID, "8f6d49dd-ae83-426a-bfb2-c4372bde9157");
        }

        // Tiếp tục chuỗi filter.
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Có thể dùng để giải phóng tài nguyên nếu cần.
    }
}