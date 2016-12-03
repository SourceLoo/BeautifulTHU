package com.thu.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by JasonLee on 16/12/2.
 */
@Entity
public class Role {
    @Id
    private String role;
    private String displayName;
    private Boolean editContacts;
    private Boolean refuseQuestion;
    private Boolean respondQuestion;
    private Boolean devolveQuestion;
    private Boolean classifyQuestion;
    private Boolean delayDdl;
    private Boolean approveResponse;
    private Long receivedNumber;
    private Long ontimeNumber;
    private Long overtimeNumber;
    private Long directRespondNumber;
    private Float goodRate;
    private Float badRate;
}
