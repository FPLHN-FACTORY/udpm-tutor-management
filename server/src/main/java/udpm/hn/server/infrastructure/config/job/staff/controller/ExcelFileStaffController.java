package udpm.hn.server.infrastructure.config.job.staff.controller;

import org.apache.poi.util.IOUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
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
import udpm.hn.server.infrastructure.config.job.staff.jobconfig.ExcelFileToDatabaseJobLauncher;
import udpm.hn.server.infrastructure.config.job.staff.service.ExcelFileStaffService;
import udpm.hn.server.infrastructure.config.job.staff.service.UploadStaffService;
import udpm.hn.server.infrastructure.constant.MappingConstants;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping(MappingConstants.API_ADMIN_STAFF)
public class ExcelFileStaffController {

    private final UploadStaffService storageService;

    private final ExcelFileStaffService excelFileStaffService;

    private final ExcelFileToDatabaseJobLauncher excelFileToDatabaseJobLauncher;

    private final JobLauncher jobLauncher;

    private final Job excelFileToDatabaseJob;

    public ExcelFileStaffController(
            UploadStaffService storageService,
            ExcelFileStaffService excelFileStaffService,
            ExcelFileToDatabaseJobLauncher excelFileToDatabaseJobLauncher,
            JobLauncher jobLauncher,
            Job excelFileToDatabaseJob
    ) {
        this.storageService = storageService;
        this.excelFileStaffService = excelFileStaffService;
        this.excelFileToDatabaseJobLauncher = excelFileToDatabaseJobLauncher;
        this.jobLauncher = jobLauncher;
        this.excelFileToDatabaseJob = excelFileToDatabaseJob;
    }

    @GetMapping(value = "/download-template-staffs")
    public ResponseEntity<?> downloadTemplate() throws IOException {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "force-download"));
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=template.xlsx");
        ByteArrayInputStream byteArrayInputStream = excelFileStaffService.downloadTemplate().getData();
        return new ResponseEntity<>(IOUtils.toByteArray(byteArrayInputStream), header, HttpStatus.CREATED);
    }

    @PostMapping("/file/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";
        try {
            Optional<String> extension = storageService.getExtensionByStringHandling(file.getOriginalFilename()).getData();
            if (extension.isEmpty() || !extension.get().equals("xlsx")) {
                message = "File Không Đúng Định Dạng";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
            }
            storageService.init();
            String fileName = storageService.save(file);
            message = "Tải File Excel Thành Công: " + file.getOriginalFilename();
            excelFileToDatabaseJobLauncher.setFullPathFileName(fileName);
            excelFileToDatabaseJobLauncher.enable();
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (Exception e) {
            e.printStackTrace();
            message = "Đã Xảy Ra Lỗi: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }

    @PostMapping("/import-staffs")
    public ResponseEntity<?> importStaffs(@RequestParam("file") MultipartFile file) {
        String message = "";
        try {
            Optional<String> extension = storageService.getExtensionByStringHandling(file.getOriginalFilename()).getData();
            if (extension.isEmpty() || !extension.get().equals("xlsx")) {
                message = "File Không Đúng Định Dạng";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
            }

            // Lưu file Excel vào thư mục
            storageService.init();
            String fileName = storageService.save(file);

            // Kích hoạt Spring Batch job để import dữ liệu từ file Excel vào database
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("fullPathFileName", fileName)
                    .addLong("startAt", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(excelFileToDatabaseJob, jobParameters);

            message = "Import Staffs Thành Công: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (Exception e) {
            e.printStackTrace();
            message = "Đã Xảy Ra Lỗi: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }
}
