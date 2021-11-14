package helloworld.model;

import lombok.Data;

import java.util.List;

@Data
public class UserStatSearchResponse {

    private List<UserStat> results;
}
