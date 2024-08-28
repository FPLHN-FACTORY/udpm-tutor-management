package udpm.hn.server.infrastructure.config.job.staff.jobconfig;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.excel.RowMapper;
import org.springframework.batch.item.excel.poi.PoiItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import udpm.hn.server.infrastructure.config.job.staff.commonio.StaffProcessor;
import udpm.hn.server.infrastructure.config.job.staff.commonio.StaffRowMapper;
import udpm.hn.server.infrastructure.config.job.staff.commonio.StaffWriter;
import udpm.hn.server.infrastructure.config.job.staff.model.dto.TranferStaffRole;
import udpm.hn.server.infrastructure.config.job.staff.model.request.StaffExcelRequest;

import java.io.File;

@Configuration
@Slf4j
//@EnableBatchProcessing
@EnableTransactionManagement
public class ExcelFileToDatabaseJobConfig {

    @Autowired
    @Qualifier("transactionManager")
    private PlatformTransactionManager transactionManager;

    @Bean
    @StepScope
    ItemReader<StaffExcelRequest> excelStaffReader(@Value("#{jobParameters['fullPathFileName']}") String path) {
        try {
            PoiItemReader<StaffExcelRequest> reader = new PoiItemReader<>();
            Resource resource = new FileSystemResource(new File("src/main/resources/staff-excel/" + path));
            if (!resource.exists()) {
                throw new RuntimeException("Could not read the file!");
            }
            log.info("RESOURCES: " + resource.getURI());
            reader.setResource(resource);
            reader.setLinesToSkip(1);
            reader.open(new ExecutionContext());
            reader.setRowMapper(rowMapper());
            reader.setLinesToSkip(1);
            return reader;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @StepScope
    private RowMapper<StaffExcelRequest> rowMapper() {
        return new StaffRowMapper();
    }

    @StepScope
    @Bean
    @Qualifier("staffProcessor")
    ItemProcessor<StaffExcelRequest, TranferStaffRole> sstaffProcessor() {
        return new StaffProcessor();
    }

    @StepScope
    @Bean
    ItemWriter<TranferStaffRole> excelNhanVienWriter() {
        return new StaffWriter();
    }

    @Bean
    Step step1(@Qualifier("excelStaffReader") ItemReader<StaffExcelRequest> excelRequestItemReader
            , JobRepository jobRepository) {
        return new StepBuilder("Excel-File-To-Database-Step", jobRepository)
                .<StaffExcelRequest, TranferStaffRole>chunk(100, transactionManager)
                .reader(excelRequestItemReader)
                .processor(item -> sstaffProcessor().process(item))
                .writer(chunk -> excelNhanVienWriter().write(chunk))
                .listener(new StepExecutionListener() {
                    @Override
                    public ExitStatus afterStep(StepExecution stepExecution) {
                        if (stepExecution.getExitStatus().equals(ExitStatus.COMPLETED)) {
                            try {
                                if (excelRequestItemReader.read() == null) {
                                    log.info("No items found in excel file");
                                    return ExitStatus.FAILED;
                                }
                                return ExitStatus.COMPLETED;
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                        return stepExecution.getExitStatus();
                    }
                })
                .build();
    }

    @Bean
    Job excelFileToDatabaseJob(@Qualifier("step1") Step step1, JobRepository jobRepository) {
        return new JobBuilder("Excel-File-To-Database-Job", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step1)
                .build();
    }


}
