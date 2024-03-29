package helloworld.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;

import java.util.Date;

public class UserStatDto {
    @JsonProperty("user_id")
    private String userId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    @JsonDeserialize(using = DateDeserializers.DateDeserializer.class)
    private Date timestamp;
    private Integer weight;

    @JsonProperty("blood_pressure")
    private Integer bloodPressure;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(Integer bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "UserStatDto{" +
                "userId='" + userId + '\'' +
                ", timestamp=" + timestamp +
                ", weight=" + weight +
                ", bloodPressure=" + bloodPressure +
                '}';
    }
}
