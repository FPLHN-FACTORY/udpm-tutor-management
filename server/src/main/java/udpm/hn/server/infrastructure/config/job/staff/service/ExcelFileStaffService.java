package udpm.hn.server.infrastructure.config.job.staff.service;


import udpm.hn.server.core.common.base.ResponseObject;

import java.io.ByteArrayInputStream;

public interface ExcelFileStaffService {

    ResponseObject<ByteArrayInputStream> downloadTemplate();

}
