package udpm.hn.server.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import udpm.hn.server.entity.base.PrimaryEntity;
import udpm.hn.server.infrastructure.constant.FunctionLogType;

import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "operation_log")
public class OperationLog extends PrimaryEntity implements Serializable {

    private String workstation;

    @Column(name = "user_name")
    private String userName;

    private String email;

    private String code;

    private String api;

    @Column(name = "type_function")
    private FunctionLogType typeFunction;

    @Lob
    @Column(length = 16777216)
    private String request;

    @Lob
    @Column(length = 16777216)
    private String response;

    @Column(name = "status_log")
    private String statusLog;

}
