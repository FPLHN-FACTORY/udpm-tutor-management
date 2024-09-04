package udpm.hn.server.infrastructure.security.oauth2;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import udpm.hn.server.entity.Role;
import udpm.hn.server.entity.Staff;
import udpm.hn.server.infrastructure.constant.EntityStatus;
import udpm.hn.server.infrastructure.security.exception.OAuth2AuthenticationProcessingException;
import udpm.hn.server.infrastructure.security.oauth2.user.OAuth2UserInfo;
import udpm.hn.server.infrastructure.security.oauth2.user.OAuth2UserInfoFactory;
import udpm.hn.server.infrastructure.security.repository.RoleAuthRepository;
import udpm.hn.server.infrastructure.security.repository.StaffAuthRepository;
import udpm.hn.server.infrastructure.security.repository.StaffRoleAuthRepository;
import udpm.hn.server.infrastructure.security.user.UserPrincipal;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final StaffAuthRepository staffAuthRepository;

    private final RoleAuthRepository roleAuthRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory
                .getOAuth2UserInfo(
                        oAuth2UserRequest.getClientRegistration().getRegistrationId(),
                        oAuth2User.getAttributes()
                );
        if (oAuth2UserInfo.getEmail() == null || oAuth2UserInfo.getEmail().isBlank()) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        Optional<Staff> staffOptional = staffAuthRepository.findByEmailFe(oAuth2UserInfo.getEmail());
        Optional<Staff> staffOptionalFPT = staffAuthRepository.findByEmailFpt(oAuth2UserInfo.getEmail());
        if (staffOptional.isPresent()) {
            Staff staff = staffOptional.get();
            Staff staffExist = (Staff) updateExistingUser(staff, oAuth2UserInfo);
            List<String> roles = roleAuthRepository.findRoleByStaffId(staffExist.getId());
            return UserPrincipal.create(staffExist, oAuth2User.getAttributes(), roles);
        } else if (staffOptionalFPT.isPresent()) {
            Staff staff = staffOptionalFPT.get();
            Staff staffExist = (Staff) updateExistingUser(staff, oAuth2UserInfo);
            List<String> roles = roleAuthRepository.findRoleByStaffId(staffExist.getId());
            return UserPrincipal.create(staffExist, oAuth2User.getAttributes(), roles);
        }

        Object user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        if (user instanceof Staff) {
            Staff staff = (Staff) user;
            List<String> roles = roleAuthRepository.findRoleByStaffId(staff.getId());
            return UserPrincipal.create(staff, oAuth2User.getAttributes(), roles);
        } else {
            throw new OAuth2AuthenticationProcessingException("Invalid email format");
        }
    }

    private Object registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        String email = oAuth2UserInfo.getEmail();
        if (email.matches(".*[a-zA-Z]+\\d{5}@fpt\\.edu\\.vn$") || email.endsWith("@gmail.com")) {
            if (email.matches("p[a-zA-Z]\\d{5}@fpt\\.edu\\.vn$")) {
                throw new OAuth2AuthenticationProcessingException("Invalid email format");
            }
        } else if (email.endsWith("@fpt.edu.vn") || email.endsWith("@fe.edu.vn")) {
            Staff staff = new Staff();
            if (email.endsWith("@fpt.edu.vn")) {
                staff.setEmailFpt(email);
            } else {
                staff.setEmailFpt(email);
            }
            staff.setStaffCode(email.substring(0, email.indexOf("@")));
            staff.setPicture(oAuth2UserInfo.getImageUrl());
            staff.setName(oAuth2UserInfo.getName());
            staff.setStatus(EntityStatus.ACTIVE);
            Role role = createRoleStaffIfNotFound();
            return staffAuthRepository.save(staff);
        } else {
            throw new OAuth2AuthenticationProcessingException("Invalid email format");
        }

        return null;
    }

    private Object updateExistingUser(Staff existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setName(oAuth2UserInfo.getName());
        existingUser.setPicture(oAuth2UserInfo.getImageUrl());
        if (existingUser.getStatus() == null) existingUser.setStatus(EntityStatus.ACTIVE);
        return staffAuthRepository.save(existingUser);
    }

    private Role createRoleStaffIfNotFound() {
        Optional<Role> role = roleAuthRepository.findRoleStaff();
        if (role.isEmpty()) {
            Role newRole = new Role();
            newRole.setCode("GIANG_VIEN");
            newRole.setName("GIANG_VIEN");
            return roleAuthRepository.save(newRole);
        }
        return role.get();
    }

}
