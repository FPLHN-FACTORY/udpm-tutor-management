package udpm.hn.server.infrastructure.constant;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum Role {

    SUPER_ADMIN("Super Admin"),

    ADMIN("Admin"),

    TRUONG_MON("Trưởng Môn"),

    GIANG_VIEN("Giảng Viên"),

    SINH_VIEN("Sinh Viên"),

    CHU_NHIEM_BO_MON("Chủ Nhiệm Bộ Môn"),

    NGUOI_LAP_KE_HOACH("Người Lập Kế Hoạch");

    private final String nameInVietnamese;

    Role(String nameInVietnamese) {
        this.nameInVietnamese = nameInVietnamese;
    }

    public static List<String> getAllRoles() {
        return Arrays.stream(Role.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    public static String getAllRolesString() {
        return Arrays.stream(Role.values())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }

    public static List<String> getAllRolesInVietnamese() {
        return Arrays.stream(Role.values())
                .map(Role::getNameInVietnamese)
                .collect(Collectors.toList());
    }

}
