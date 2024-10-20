package udpm.hn.server.infrastructure.config.drive.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class FileTypeChecker {

    public static String getMimeType(MultipartFile file) throws IOException {
        return file.getContentType();
    }

    public static boolean isPdf(MultipartFile file) throws IOException {
        return "application/pdf".equals(getMimeType(file));
    }

    public static boolean isDocx(MultipartFile file) throws IOException {
        return "application/vnd.openxmlformats-officedocument.wordprocessingml.document".equals(getMimeType(file));
    }

}
