package udpm.hn.server.infrastructure.config.job.staff.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.infrastructure.config.job.staff.jobconfig.StaffJobLauncher;
import udpm.hn.server.infrastructure.config.job.staff.service.UploadStaffService;
import udpm.hn.server.infrastructure.config.job.staff.service.impl.DownloadStaffTemplate;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.utils.Helper;

import java.io.ByteArrayInputStream;
import java.util.Optional;

@RestController
@RequestMapping(MappingConstants.API_ADMIN_STAFF)
@RequiredArgsConstructor
public class FileControlStaffController {

    private final UploadStaffService storageService;

    private final DownloadStaffTemplate excelFileStaffService;

    private final StaffJobLauncher staffJobLauncher;

    private static final String TEMPLATE_FILE_NAME = "template_import_nhan_vien.xlsx";

    private static final String XLSX_EXTENSION = "xlsx";

    private static final String SUCCESS_MESSAGE = "Tải File Excel Thành Công: ";

    private static final String ERROR_MESSAGE = "Đã Xảy Ra Lỗi: ";


    @GetMapping("/file/template")
    public ResponseEntity<?> downloadTemplate() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", TEMPLATE_FILE_NAME);

        ResponseObject<?> response = excelFileStaffService.downloadTemplate();

        if (response.getData() instanceof ByteArrayInputStream) {
            return new ResponseEntity<>(
                    ((ByteArrayInputStream) response.getData()).readAllBytes(),
                    headers,
                    HttpStatus.OK
            );
        }

        return Helper.createResponseEntity(response);
    }

    @PostMapping("/file/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
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

            staffJobLauncher.setStaffExcelFileName(fileName);
            staffJobLauncher.startTask();

            String successMessage = SUCCESS_MESSAGE + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(successMessage);

        } catch (Exception e) {
            String errorMessage = ERROR_MESSAGE + file.getOriginalFilename() + ". Error: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(errorMessage);
        }
    }

}
