package helloworld.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserStatQueryRequest {

    private String userId;
    private Long after_timestamp;
    private Long limit;
}
