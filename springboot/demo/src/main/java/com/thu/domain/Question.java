package com.thu.domain;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Created by JasonLee on 16/12/2.
 */
@Entity
public class Question {
    @Id
    @GeneratedValue
    private Long questionId;
    private String title;
    private String content;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;
    @OneToOne(optional = false)
    private Role leaderRole;
    @OneToMany
    private Set<Role> otherRoles;
    private enum status {
        UNAPPROVE, UNCLASSIFY, UNSOLVE, SOLVING, RECLASSIFY, DELAY, SOVLVED, INVALID
    }
    private Long likes;
    private String comment;
    private String createdLocation;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date ddl;
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp1;
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp2;
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp3;
    private Boolean isCommon;
    private Boolean isCommonTop;

    // delayDays, delayReason

    private String instruction;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Pic> pics;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Response> responses;

}
