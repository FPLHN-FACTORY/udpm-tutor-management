package udpm.hn.server.infrastructure.constant;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Role {

    ADMIN,

    TRUONG_MON,

    GIANG_VIEN,

    SINH_VIEN,

    CHU_NHIEM_BO_MON,

    NGUOI_LAP_KE_HOACH;

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

}
