package udpm.hn.server.infrastructure.config.job.student.jobconfig;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
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
import udpm.hn.server.infrastructure.config.job.student.commonio.StudentProcessor;
import udpm.hn.server.infrastructure.config.job.student.commonio.StudentRowMapper;
import udpm.hn.server.infrastructure.config.job.student.commonio.StudentWriter;
import udpm.hn.server.infrastructure.config.job.student.model.dto.TransferStudent;
import udpm.hn.server.infrastructure.config.job.student.model.request.StudentExcelRequest;

import java.io.File;

@Configuration
@Slf4j
@EnableTransactionManagement
public class StudentJobConfig {

    @Setter(onMethod_ = {@Autowired}, onParam_ = {@Qualifier("transactionManager")})
    private PlatformTransactionManager transactionManager;

    @Value("${file.upload.student.path}")
    private String fullPath;

    @Bean
    @StepScope
    public ItemReader<StudentExcelRequest> excelStudentReader(
            @Value("#{jobParameters['studentExcelFileName']}") String path,
            @Value("#{jobParameters['planId']}") String planId) {
        try {
            PoiItemReader<StudentExcelRequest> reader = new PoiItemReader<>();
            Resource resource = new FileSystemResource(new File(fullPath + "/" + path));

            if (!resource.exists()) {
                throw new RuntimeException("Could not read the file!");
            }

            reader.setResource(resource);
            reader.open(new ExecutionContext());
            reader.setLinesToSkip(1);
            reader.setRowMapper(rowMapper(planId));
            return reader;
        } catch (Exception e) {
            log.error("Error reading excel file: {}", e.getMessage());
            return null;
        }
    }

    RowMapper<StudentExcelRequest> rowMapper(String planId) {
        return new StudentRowMapper(planId);
    }

    @Bean(name = "customStudentProcessor")
    @StepScope
    public ItemProcessor<StudentExcelRequest, TransferStudent> customStudentProcessor() {
        return new StudentProcessor();
    }


    @Bean
    @StepScope
    public ItemWriter<TransferStudent> excelStudentWriter() {
        return new StudentWriter();
    }


    @Bean
    Step firstStepStudent(
            @Qualifier("excelStudentReader") ItemReader<StudentExcelRequest> excelStudentReader,
            JobRepository jobRepository
    ) {
        return new StepBuilder("Import Student To Database - Step", jobRepository)
                .<StudentExcelRequest, TransferStudent>chunk(1, transactionManager)
                .reader(excelStudentReader)
                .processor(item -> customStudentProcessor().process(item))
                .writer(chunk -> excelStudentWriter().write(chunk))
                .build();
    }


    @Bean
    public Job saveStudentDataToDatabaseJob(
            @Qualifier("firstStepStudent") Step step,
            JobRepository jobRepository
    ) {
        return new JobBuilder("Save-Data-Student-To-Database-Job", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }

}
