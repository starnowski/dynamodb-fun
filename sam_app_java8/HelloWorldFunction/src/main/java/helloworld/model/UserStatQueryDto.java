package helloworld.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class UserStatQueryDto {

    private String userId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private Date after_timestamp;
    private Long limit;
}
