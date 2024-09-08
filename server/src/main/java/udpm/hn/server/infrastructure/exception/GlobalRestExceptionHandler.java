package udpm.hn.server.infrastructure.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.utils.Helper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalRestExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handlerException(Exception ex) {
        if (ex instanceof RestApiException) {
            log.warn(ex.getMessage());
            APIError apiError = new APIError(ex.getMessage());
            return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
        } else if (ex instanceof ConstraintViolationException) {
            Set<ConstraintViolation<?>> violations = ((ConstraintViolationException) ex).getConstraintViolations();
            List<ErrorModel> errors = violations.stream()
                    .map(violation ->
                            new ErrorModel(getPropertyName(violation.getPropertyPath()), violation.getMessage()))
                    .collect(Collectors.toList());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        } else if (ex instanceof ForbiddenException) {
            ex.printStackTrace(System.out);
            log.warn(ex.getMessage());
            APIError apiError = new APIError(ex.getMessage());
            return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
        } else if (ex instanceof HttpMessageNotReadableException) {
            log.warn(ex.getMessage());
            APIError apiError = new APIError("Invalid request body");
            return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
        } else if (ex instanceof NoResourceFoundException) {
            log.warn(ex.getMessage());
            return Helper.createResponseEntity(
                    ResponseObject.errorForward(
                            "Resource not found",
                            HttpStatus.NOT_FOUND
                    )
            );
        } else {
            log.warn(ex.getMessage());
            return Helper.createResponseEntity(
                    ResponseObject.errorForward(
                            "Internal Server Error",
                            HttpStatus.INTERNAL_SERVER_ERROR
                    )
            );
        }
    }

    public String getPropertyName(Path path) {
        String pathStr = path.toString();
        String[] comps = pathStr.split("\\.");
        if (comps.length > 0) {
            return comps[comps.length - 1];
        } else {
            return pathStr;
        }
    }

}
