package com.coresaken.mcserverlist.data.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoteResponse {
    Error error;

    Long voteId;
    LocalDate votedAt;

    public VoteResponse(Long voteId, LocalDate votedAt, int errorId, String errorMessage){
        this.voteId = voteId;
        this.votedAt = votedAt;
        if(errorMessage != null){
            this.error = new Error(errorId, errorMessage);
        }
    }

    @Data
    @AllArgsConstructor
    public static class Error{
        int id;
        String message;
    }
}
