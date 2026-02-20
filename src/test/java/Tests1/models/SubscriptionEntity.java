package Tests1.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubscriptionEntity {
    private String id;
    private String customer;
    private String criteria;
    private String status;
    private String type;
}