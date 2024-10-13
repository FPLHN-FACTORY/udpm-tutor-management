package udpm.hn.server.infrastructure.config.websocket.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotifyModel {

    private String[] roles;

    public static String[] getRoles(String... roles) {
        return roles;
    }

}
