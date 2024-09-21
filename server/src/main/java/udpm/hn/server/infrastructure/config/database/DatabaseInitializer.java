package udpm.hn.server.infrastructure.config.database;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseInitializer {

    private final JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        String sql = "CREATE DATABASE IF NOT EXISTS tutor_management";
        jdbcTemplate.execute(sql);
    }

}
