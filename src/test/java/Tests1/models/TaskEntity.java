package Tests1.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskEntity {
    private String id;
    private String status;
    private String patientId;
    private String deviceSerial;
    private String customer;
}