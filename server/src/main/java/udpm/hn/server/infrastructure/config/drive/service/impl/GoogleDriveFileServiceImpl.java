package udpm.hn.server.infrastructure.config.drive.service.impl;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import udpm.hn.server.infrastructure.config.drive.dto.GoogleDriveFileDTO;
import udpm.hn.server.infrastructure.config.drive.service.GoogleDriveFileService;
import udpm.hn.server.infrastructure.config.drive.service.GoogleDriveManagerService;
import udpm.hn.server.infrastructure.config.drive.utils.PermissionDetail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoogleDriveFileServiceImpl implements GoogleDriveFileService {

    private final GoogleDriveManagerService googleDriveManagerService;

    private final Drive googleDrive;

    @Value("${google.drive.share.email.default}")
    private String emailShareDefault;

    @Override
    public List<GoogleDriveFileDTO> findAll() {

        List<GoogleDriveFileDTO> googleDriveFileDTOS = new ArrayList<>();
        List<File> files = googleDriveManagerService.findAll();

        if (files == null) return googleDriveFileDTOS;

        files.forEach(file -> {
            if (file.getSize() != null) {
                GoogleDriveFileDTO driveFileDto = new GoogleDriveFileDTO();
                fillGoogleDriveFileDTOList(googleDriveFileDTOS, file, driveFileDto);
            }
        });

        return googleDriveFileDTOS;
    }

    @Override
    public List<GoogleDriveFileDTO> findAllInFolder(String folderId) {

        List<GoogleDriveFileDTO> googleDriveFileDTOList = new ArrayList<>();
        List<File> files = googleDriveManagerService.findAllFilesInFolderById(folderId);

        if (files == null) return googleDriveFileDTOList;

        files.forEach(file -> {
            if (file.getSize() != null) {
                GoogleDriveFileDTO driveFileDto = new GoogleDriveFileDTO();
                fillGoogleDriveFileDTOList(googleDriveFileDTOList, file, driveFileDto);
            }
        });

        return googleDriveFileDTOList;
    }

    private void fillGoogleDriveFileDTOList(List<GoogleDriveFileDTO> googleDriveFileDTOS, File file, GoogleDriveFileDTO driveFileDto) {
        driveFileDto.setId(file.getId());
        driveFileDto.setName(file.getName());
        driveFileDto.setThumbnailLink(file.getThumbnailLink());
        driveFileDto.setSize(String.valueOf(file.getSize()));
        driveFileDto.setLink("https://drive.google.com/file/d/" + file.getId() + "/view?usp=sharing");
        driveFileDto.setShared(file.getShared());
        googleDriveFileDTOS.add(driveFileDto);
    }

    @Override
    public void deleteById(String fileId) {
        googleDriveManagerService.deleteFileOrFolderById(fileId);
    }

    @Override
    public GoogleDriveFileDTO upload(MultipartFile file, String folderName, boolean isPublic) {
        PermissionDetail permissionDetail = PermissionDetail.builder().build();

        if (isPublic) {
            permissionDetail.setType("anyone");
            permissionDetail.setRole("reader");
        } else {
            permissionDetail.setType("private");
            permissionDetail.setRole("private");
        }
        permissionDetail.setEmailAddress(emailShareDefault);

        return googleDriveManagerService.uploadFile(file, folderName, permissionDetail);
    }

    @Override
    public void download(String fileId, OutputStream outputStream) {
        googleDriveManagerService.download(fileId, outputStream);
    }

    @Override
    public void copyToFolder(String fileId, String folderId) {
        googleDriveManagerService.copy(fileId, folderId);
    }

    @Override
    public void moveToFolder(String fileId, String folderId) {
        googleDriveManagerService.move(fileId, folderId);
    }

    @Override
    public Permission shareFile(String fileId, String gmail) {
        PermissionDetail permissionDetail = PermissionDetail
                .builder()
                .emailAddress(gmail)
                .type("user")
                .role("reader")
                .build();

        return googleDriveManagerService.createPermissionForEmail(fileId, permissionDetail);
    }

    @Override
    public void deleteShareFile(String fileId, String permissionId) {
        googleDriveManagerService.deletePermissionForEmail(fileId, permissionId);
    }

    @Override
    public Resource loadFile(String fileId) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            googleDrive.files().get(fileId)
                    .executeMediaAndDownloadTo(outputStream);
        } catch (IOException e) {
            throw new RuntimeException("Could not download the file!", e);
        }
        return new ByteArrayResource(outputStream.toByteArray());
    }

    @Override
    public String getFileName(String fileId) {
        return googleDriveManagerService.getFileName(fileId);
    }

}
