package udpm.hn.server.infrastructure.config.job.student.jobconfig;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import udpm.hn.server.infrastructure.config.job.student.service.UploadStudentService;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@NoArgsConstructor
public class StudentJobLauncher {

    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    private ScheduledFuture<?> scheduledTask;

    @Setter(onMethod_ = @Autowired, onParam_ = @Qualifier("saveStudentDataToDatabaseJob"))
    private Job job;

    @Setter(onMethod_ = @Autowired)
    private JobLauncher jobLauncher;

    @Setter(onMethod_ = @Autowired)
    private UploadStudentService storageService;

    @Setter
    private String studentExcelFileName;

    @Setter
    private String planId;

    @Value("${job.delay}")
    private int DELAY;

    @Value("${job.period}")
    private int PERIOD;

    public void startTask() {
        if (scheduledTask == null || scheduledTask.isCancelled()) {
            scheduledTask = executorService.scheduleAtFixedRate(
                    this::launchExcelToDatabaseJob,
                    DELAY, PERIOD, TimeUnit.MINUTES // 1 minute
            );
        }
    }

    public void stopTask() {
        if (scheduledTask != null && !scheduledTask.isCancelled()) {
            scheduledTask.cancel(false);
        }
    }

    @Async
    protected void launchExcelToDatabaseJob() {
        if (studentExcelFileName != null && planId != null) {
            try {
                JobExecution jobExecution = jobLauncher.run(job, newExecution());
                ExitStatus exitStatus = jobExecution.getExitStatus();
                if (exitStatus.getExitCode().equals(ExitStatus.COMPLETED.getExitCode())) {
                    storageService.delete(studentExcelFileName);
                    stopTask();
                }
                if (exitStatus.getExitCode().equals(ExitStatus.FAILED.getExitCode())) {
                    log.error("Error launching excel to database job");
                }
            } catch (Exception e) {
                log.error("Error launching excel to database job", e);
            } finally {
                studentExcelFileName = null;
            }
        }
    }

    private JobParameters newExecution() {
        Map<String, JobParameter<?>> parameters = new HashMap<>();
        parameters.put("time", new JobParameter<>(new Date(), Date.class));
        parameters.put("studentExcelFileName", new JobParameter<>(studentExcelFileName, String.class));
        parameters.put("planId", new JobParameter<>(planId, String.class));
        return new JobParameters(parameters);
    }

}
