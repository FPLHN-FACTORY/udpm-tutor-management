package udpm.hn.server.infrastructure.security.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import udpm.hn.server.entity.Staff;
import udpm.hn.server.infrastructure.security.repository.RoleAuthRepository;
import udpm.hn.server.infrastructure.security.repository.StaffAuthRepository;
import udpm.hn.server.infrastructure.security.user.UserPrincipal;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final StaffAuthRepository staffRepository;

    private final RoleAuthRepository roleRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        Optional<Staff> staffFpt = staffRepository.findByEmailFpt(email);
        if (staffFpt.isPresent()) {
            List<String> roles = roleRepository.findRoleByStaffId(staffFpt.get().getId());
            return UserPrincipal.create(staffFpt.get(), roles);
        }

        Optional<Staff> staffFe = staffRepository.findByEmailFe(email);
        if (staffFe.isPresent()) {
            List<String> roles = roleRepository.findRoleByStaffId(staffFe.get().getId());
            return UserPrincipal.create(staffFe.get(), roles);
        }

        throw new UsernameNotFoundException("User not found with email : " + email);
    }

    @Transactional
    public UserDetails loadUserById(String id) {
        Optional<Staff> staff = staffRepository.findById(id);
        if (staff.isPresent()) {
            List<String> roles = roleRepository.findRoleByStaffId(id);
            return UserPrincipal.create(staff.get(), roles);
        }
        throw new UsernameNotFoundException("User not found with id : " + id);
    }

}