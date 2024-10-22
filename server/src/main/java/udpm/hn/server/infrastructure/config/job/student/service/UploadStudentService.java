package udpm.hn.server.infrastructure.config.job.student.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import udpm.hn.server.core.common.base.ResponseObject;

import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

public interface UploadStudentService {

    void init();

    String save(MultipartFile file);

    Resource load(String filename);

    void deleteAll();

    void delete(String filename);

    Stream<Path> loadAll();

    ResponseObject<Optional<String>> getExtensionByStringHandling(String filename);

}
