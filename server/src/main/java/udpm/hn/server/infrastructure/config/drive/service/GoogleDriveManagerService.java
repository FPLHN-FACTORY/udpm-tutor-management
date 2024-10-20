package udpm.hn.server.infrastructure.config.drive.service;

import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;
import org.springframework.web.multipart.MultipartFile;
import udpm.hn.server.infrastructure.config.drive.dto.GoogleDriveFileDTO;
import udpm.hn.server.infrastructure.config.drive.utils.PermissionDetail;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface GoogleDriveManagerService {

    List<File> findAll();

    List<File> findAllFilesInFolderById(String folderId);

    List<File> findAllFoldersInFolderById(String folderId);

    void download(String fileId, OutputStream outputStream);

    Permission createPermissionForEmail(String id, PermissionDetail permissionDetail);

    void deletePermissionForEmail(String id, String permissionId);

    GoogleDriveFileDTO uploadFile(MultipartFile multipartFile, String folderName, PermissionDetail permissionDetail);

    String getFolderId(String folderName);

    File findFolderById(String folderId);

    void deleteFileOrFolderById(String id);

    InputStream getFileAsInputStream(String fileID);

    void copy(String fileId, String folderId);

    void move(String fileId, String folderId);

    String createFolder(String folderName, String parentId);

    String getFileName(String fileId);

}