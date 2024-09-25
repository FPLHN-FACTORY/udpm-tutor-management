package udpm.hn.server.infrastructure.security.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import udpm.hn.server.entity.DepartmentFacility;
import udpm.hn.server.entity.Facility;
import udpm.hn.server.entity.MajorFacility;
import udpm.hn.server.entity.Semester;
import udpm.hn.server.entity.Staff;
import udpm.hn.server.entity.StaffMajorFacility;
import udpm.hn.server.infrastructure.connection.IdentityValidation;
import udpm.hn.server.infrastructure.constant.Role;
import udpm.hn.server.infrastructure.security.repository.SemesterAuthRepository;
import udpm.hn.server.infrastructure.security.repository.StaffAuthRepository;
import udpm.hn.server.infrastructure.security.repository.StaffMajorFacilityAuthRepository;
import udpm.hn.server.infrastructure.security.repository.StaffRoleAuthRepository;
import udpm.hn.server.infrastructure.security.response.TokenSubjectResponse;
import udpm.hn.server.infrastructure.security.user.UserPrincipal;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class TokenProvider {

    @Value("${jwt.secret}")
    private String tokenSecret;

    @Value("${security.use-identity}")
    private String useIdentityModule;

    @Value("${identity.client-secret}")
    private String identityClientSecret;

    @Value("${identity.client-id}")
    private String identityClientId;

    private final long TOKEN_EXP = System.currentTimeMillis() + 2 * 60 * 60 * 1000;

    private static final String ISSUER = "udpm.hn.tutor-api";

    @Setter(onMethod_ = @Autowired)
    private StaffAuthRepository staffRepository;

    @Setter(onMethod_ = @Autowired)
    private StaffRoleAuthRepository staffRoleAuthRepository;

    @Setter(onMethod_ = @Autowired)
    private StaffMajorFacilityAuthRepository staffMajorFacilityAuthRepository;

    @Setter(onMethod_ = @Autowired)
    private SemesterAuthRepository semesterAuthRepository;

    public String createToken(Authentication authentication) throws BadRequestException, JsonProcessingException {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Staff user = getCurrentUserLogin(userPrincipal.getEmail());

        if (user == null) throw new BadRequestException("User not found");

        TokenSubjectResponse tokenSubjectResponse = getTokenSubjectResponse(user);
        String subject = new ObjectMapper().writeValueAsString(tokenSubjectResponse);

        Optional<StaffMajorFacility> staffMajorFacility = staffMajorFacilityAuthRepository.findByStaffId(user.getId());

        if (staffMajorFacility.isPresent()) {
            MajorFacility majorFacility = staffMajorFacility.get().getMajorFacility();
            Facility facility = majorFacility.getDepartmentFacility().getFacility();
            DepartmentFacility departmentFacility = majorFacility.getDepartmentFacility();
            tokenSubjectResponse.setFacilityCode(facility.getCode());
            tokenSubjectResponse.setDepartmentCode(departmentFacility.getDepartment().getCode());
            tokenSubjectResponse.setFacilityId(facility.getId());
            tokenSubjectResponse.setFacilityName(facility.getName());
            tokenSubjectResponse.setDepartmentName(departmentFacility.getDepartment().getName());
            Long now = System.currentTimeMillis();
            String semesterOptional = semesterAuthRepository.findSemesterBy(now);
            tokenSubjectResponse.setSemesterId(semesterOptional);
        } else {
            tokenSubjectResponse.setFacilityCode("");
            tokenSubjectResponse.setDepartmentCode("");
            tokenSubjectResponse.setFacilityId("");
            tokenSubjectResponse.setFacilityName("");
            tokenSubjectResponse.setDepartmentName("");
        }

        Map<String, Object> claims = getBodyClaims(tokenSubjectResponse);

        return Jwts.builder()
                .setSubject(subject)
                .setClaims(claims)
                .setIssuedAt(new java.util.Date(System.currentTimeMillis()))
                .setExpiration(new java.util.Date(TOKEN_EXP))
                .setIssuer(ISSUER)
                .signWith(getSecretKey())
                .compact();
    }

    public String createToken(String userId) throws BadRequestException, JsonProcessingException {
        Staff user = staffRepository.findById(userId).orElse(null);
        if (user == null) throw new BadRequestException("User not found");

        TokenSubjectResponse tokenSubjectResponse = getTokenSubjectResponse(user);
        String subject = new ObjectMapper().writeValueAsString(tokenSubjectResponse);

        Optional<StaffMajorFacility> staffMajorFacility = staffMajorFacilityAuthRepository.findByStaffId(user.getId());
        if (staffMajorFacility.isPresent()) {
            MajorFacility majorFacility = staffMajorFacility.get().getMajorFacility();
            Facility facility = majorFacility.getDepartmentFacility().getFacility();
            DepartmentFacility departmentFacility = majorFacility.getDepartmentFacility();
            tokenSubjectResponse.setFacilityCode(facility.getCode());
            tokenSubjectResponse.setDepartmentCode(departmentFacility.getDepartment().getCode());
            tokenSubjectResponse.setFacilityId(facility.getId());
            tokenSubjectResponse.setFacilityName(facility.getName());
            tokenSubjectResponse.setDepartmentName(departmentFacility.getDepartment().getName());
        } else {
            tokenSubjectResponse.setFacilityCode("");
            tokenSubjectResponse.setDepartmentCode("");
            tokenSubjectResponse.setFacilityId("");
            tokenSubjectResponse.setFacilityName("");
            tokenSubjectResponse.setDepartmentName("");
        }

        Map<String, Object> claims = getBodyClaims(tokenSubjectResponse);


        return Jwts.builder()
                .setSubject(subject)
                .setClaims(claims)
                .setIssuedAt(new java.util.Date(System.currentTimeMillis()))
                .setExpiration(new java.util.Date(TOKEN_EXP))
                .setIssuer(ISSUER)
                .signWith(getSecretKey())
                .compact();
    }

    private TokenSubjectResponse getTokenSubjectResponse(Staff staff) {
        TokenSubjectResponse response = new TokenSubjectResponse();
        response.setFullName(staff.getName());
        response.setUserId(staff.getId());
        response.setUserCode(staff.getStaffCode());
        response.setPictureUrl(staff.getPicture());
        response.setEmailFpt(staff.getEmailFpt() != null ? staff.getEmailFpt() : "");
        response.setEmailFe(staff.getEmailFe() != null ? staff.getEmailFe() : "");
        List<String> rolesCode = staffRoleAuthRepository.getRoleCodesByStaffId(staff.getId());
        if (rolesCode.isEmpty()) {
            response.setRolesCode(Collections.singletonList(Role.GIANG_VIEN.name()));
            response.setRolesName(Collections.singletonList(Role.GIANG_VIEN.name()));
        } else {
            response.setRolesCode(rolesCode);
            response.setRolesName(staffRoleAuthRepository.getRoleNamesByStaffId(staff.getId()));
        }
        return response;
    }

    private static Map<String, Object> getBodyClaims(TokenSubjectResponse tokenSubjectResponse) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("fullName", tokenSubjectResponse.getFullName());
        claims.put("userId", tokenSubjectResponse.getUserId());
        claims.put("userName", tokenSubjectResponse.getUserCode());
        claims.put("userCode", tokenSubjectResponse.getUserCode());
        claims.put("emailFpt", tokenSubjectResponse.getEmailFpt());
        claims.put("emailFe", tokenSubjectResponse.getEmailFe());
        claims.put("facilityCode", tokenSubjectResponse.getFacilityCode());
        claims.put("departmentCode", tokenSubjectResponse.getDepartmentCode());
        claims.put("facilityId", tokenSubjectResponse.getFacilityId());
        claims.put("facilityName", tokenSubjectResponse.getFacilityName());
        claims.put("departmentName", tokenSubjectResponse.getDepartmentName());
        claims.put("pictureUrl", tokenSubjectResponse.getPictureUrl());
        claims.put("semesterId", tokenSubjectResponse.getSemesterId());
        List<String> rolesCode = tokenSubjectResponse.getRolesCode();
        List<String> rolesName = tokenSubjectResponse.getRolesName();
        if (rolesCode.size() == 1) {
            claims.put("rolesCode", rolesCode.get(0));
        } else {
            claims.put("rolesCode", rolesCode);
        }
        if (rolesName.size() == 1) {
            claims.put("rolesName", rolesName.get(0));
        } else {
            claims.put("rolesName", rolesName);
        }
        claims.put("host", tokenSubjectResponse.getHost());
        return claims;
    }

    public String getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(tokenSecret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
        return String.valueOf(claims.get("userId").toString());
    }

    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(tokenSecret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
        String emailFpt = claims.get("emailFpt", String.class);
        if (emailFpt != null && !emailFpt.isEmpty()) {
            return emailFpt;
        }
        return claims.get("emailFe", String.class);
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(tokenSecret.getBytes()))
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }

    private Staff getCurrentUserLogin(String email) {
        Optional<Staff> staffFPT = staffRepository.findByEmailFpt(email);
        if (staffFPT.isPresent()) return staffFPT.get();
        Optional<Staff> staffFE = staffRepository.findByEmailFe(email);
        return staffFE.orElse(null);
    }

    private SecretKey getSecretKey() {
        if (useIdentityModule.equals("true")) {
            return IdentityValidation.generateJwtSecretKey(identityClientId, identityClientSecret);
        } else {
            return Keys.hmacShaKeyFor(tokenSecret.getBytes());
        }
    }

}
