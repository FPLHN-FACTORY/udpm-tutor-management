package udpm.hn.server.infrastructure.config.job.student.commonio;

import org.springframework.batch.item.excel.RowMapper;
import org.springframework.batch.item.excel.support.rowset.RowSet;
import udpm.hn.server.infrastructure.config.job.student.model.request.StudentExcelRequest;

public class StudentRowMapper implements RowMapper<StudentExcelRequest> {

    private String planId;

    public StudentRowMapper(String planId) {
        this.planId = planId;
    }

    @Override
    public StudentExcelRequest mapRow(RowSet rowSet) {
        try {
            StudentExcelRequest importExcelRequest = new StudentExcelRequest();
            importExcelRequest.setOrderNumber(rowSet.getCurrentRowIndex());
            importExcelRequest.setEmail(rowSet.getColumnValue(1));
            importExcelRequest.setCode(rowSet.getColumnValue(2));
            importExcelRequest.setName(rowSet.getColumnValue(3));
            importExcelRequest.setTutorClassDetail(rowSet.getColumnValue(6).replaceAll("\\s*\\(.*?\\)", "").trim());
            importExcelRequest.setPlanId(planId);

            return importExcelRequest;
        } catch (Exception e) {
            return null;
        }
    }

}
