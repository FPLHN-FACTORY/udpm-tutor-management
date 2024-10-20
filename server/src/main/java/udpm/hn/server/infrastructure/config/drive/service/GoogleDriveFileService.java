package udpm.hn.server.infrastructure.config.drive.service;

import org.springframework.web.multipart.MultipartFile;
import udpm.hn.server.infrastructure.config.drive.dto.GoogleDriveFileDTO;

import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.OutputStream;

import com.google.api.services.drive.model.Permission;

import java.util.List;

public interface GoogleDriveFileService {

    List<GoogleDriveFileDTO> findAll();

    List<GoogleDriveFileDTO> findAllInFolder(String folderId);

    void deleteById(String fileId);

    GoogleDriveFileDTO upload(MultipartFile file, String folderName, boolean isPublic);

    void download(String fileId, OutputStream outputStream);

    void copyToFolder(String fileId, String folderId);

    void moveToFolder(String fileId, String folderId);

    Permission shareFile(String fileId, String gmail);

    void deleteShareFile(String fileId, String permissionId);

    Resource loadFile(String fileId) throws IOException;

    String getFileName(String fileId);

}
