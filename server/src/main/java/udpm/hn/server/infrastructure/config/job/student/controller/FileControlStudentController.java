package udpm.hn.server.infrastructure.config.job.student.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import udpm.hn.server.infrastructure.config.job.student.jobconfig.StudentJobLauncher;
import udpm.hn.server.infrastructure.config.job.student.service.UploadStudentService;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import java.util.Optional;

@RestController
@RequestMapping(MappingConstants.API_PLANNER_PLAN)
@RequiredArgsConstructor
public class FileControlStudentController {

    private final UploadStudentService storageService;

    private final StudentJobLauncher studentJobLauncher;

    private static final String XLSX_EXTENSION = "xlsx";

    private static final String SUCCESS_MESSAGE = "Tải File Excel Thành Công: ";

    private static final String ERROR_MESSAGE = "Đã Xảy Ra Lỗi: ";

    @PostMapping("/file/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("planId") String planId) {
        try {
            Optional<String> extension = storageService
                    .getExtensionByStringHandling(file.getOriginalFilename())
                    .getData();

            if (extension.isEmpty() || !XLSX_EXTENSION.equalsIgnoreCase(extension.get())) {
                return ResponseEntity
                        .status(HttpStatus.EXPECTATION_FAILED)
                        .body("File Không Đúng Định Dạng");
            }

            storageService.init();
            String fileName = storageService.save(file);

            studentJobLauncher.setStudentExcelFileName(fileName);
            studentJobLauncher.setPlanId(planId);
            studentJobLauncher.startTask();

            String successMessage = SUCCESS_MESSAGE + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(successMessage);

        } catch (Exception e) {
            String errorMessage = ERROR_MESSAGE + file.getOriginalFilename() + ". Error: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(errorMessage);
        }
    }

}
