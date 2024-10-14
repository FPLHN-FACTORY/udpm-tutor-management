package udpm.hn.server.core.common.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.common.model.response.VersionResponse;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.utils.Helper;

@RestController
@RequestMapping(MappingConstants.VERSION)
public class VersionController {

    @Value("${server.version}")
    private String version;

    @Value("${spring.application.name}")
    private String description;

    @GetMapping
    public ResponseEntity<?> getVersion() {
        return Helper.createResponseEntity(
                new ResponseObject<>(
                        new VersionResponse(
                                version,
                                description
                        ),
                        HttpStatus.OK,
                        "Get version successfully"
                )
        );
    }

}
