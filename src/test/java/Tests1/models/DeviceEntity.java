package Tests1.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeviceEntity {
    private String serialNumber;
    private String model;
    private String status;
    private String manufacturer;
}