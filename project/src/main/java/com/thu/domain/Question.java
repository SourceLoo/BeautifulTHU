package com.thu.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by JasonLee on 16/12/2.
 */
@Entity
public class Question {
    @Id
    //@GeneratedValue
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "questionGenerator")
    @SequenceGenerator(name = "questionGenerator", sequenceName = "questionSequence", allocationSize=1)
    private Long questionId;
    private String title;
    private String content;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private TUser TUser;
    @ManyToOne
    private Role leaderRole;
    @ManyToMany
    private List<Role> otherRoles = new ArrayList<Role>();

    private Status status = Status.UNAPPROVED;
    private Long likes = 0L;
    private EvaluationType evaluationType;
    private String evaluationDetail;
    private String createdLocation;
    private LocalDateTime createdTime = LocalDateTime.now();
    private LocalDateTime ddl;
    private LocalDateTime timestamp1;
    private LocalDateTime timestamp2;
    private LocalDateTime timestamp3;
    private Boolean isCommon = false;
    private Boolean isCommonTop = false;

    private Integer delayDays;
    private String delayReason;
    private String reclassifyReason;
    private String rejectReason;

    @ManyToOne
    private Role transferRole;
    private Boolean isRead = false;

    private String instruction;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pic> pics = new ArrayList<Pic>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Response> responses = new ArrayList<Response>();

    protected Question() {}

    // lyqtest
    public Question(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Question(String title, String content, TUser TUser, String createdLocation, List<String> picPaths) {
        this.title = title;
        this.content = content;
        this.TUser = TUser;
        this.createdLocation = createdLocation;
        for (String path: picPaths) {
            pics.add(new Pic(path));
        }

        this.setStatus(Status.UNAPPROVED);
        //this.responses.add(new Response("回复1"));
    }

    public Long getQuestionId() {
        return questionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public TUser getTUser() {
        return TUser;
    }

    public void setTUser(TUser TUser) {
        this.TUser = TUser;
    }

    public Role getLeaderRole() {
        return leaderRole;
    }

    public void setLeaderRole(Role leaderRole) {
        this.leaderRole = leaderRole;
    }

    public List<Role> getOtherRoles() {
        return otherRoles;
    }

    public void setOtherRoles(List<Role> otherRoles) {
        this.otherRoles = otherRoles;
    }

    public Long getLikes() {
        return likes;
    }

    public void setLikes(Long likes) {
        this.likes = likes;
    }

    public EvaluationType getEvaluationType() {
        return evaluationType;
    }

    public void setEvaluationType(EvaluationType evaluationType) {
        this.evaluationType = evaluationType;
    }

    public String getEvaluationDetail() {
        return evaluationDetail;
    }

    public void setEvaluationDetail(String evaluationDetail) {
        this.evaluationDetail = evaluationDetail;
    }

    public String getCreatedLocation() {
        return createdLocation;
    }

    public void setCreatedLocation(String createdLocation) {
        this.createdLocation = createdLocation;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getDdl() {
        return ddl;
    }

    public void setDdl(LocalDateTime ddl) {
        this.ddl = ddl;
    }

    public LocalDateTime getTimestamp1() {
        return timestamp1;
    }

    public void setTimestamp1(LocalDateTime timestamp1) {
        this.timestamp1 = timestamp1;
    }

    public LocalDateTime getTimestamp2() {
        return timestamp2;
    }

    public void setTimestamp2(LocalDateTime timestamp2) {
        this.timestamp2 = timestamp2;
    }

    public LocalDateTime getTimestamp3() {
        return timestamp3;
    }

    public void setTimestamp3(LocalDateTime timestamp3) {
        this.timestamp3 = timestamp3;
    }

    public Boolean getCommon() {
        return isCommon;
    }

    public void setCommon(Boolean common) {
        isCommon = common;
    }

    public Boolean getCommonTop() {
        return isCommonTop;
    }

    public void setCommonTop(Boolean commonTop) {
        isCommonTop = commonTop;
    }

    public Integer getDelayDays() {
        return delayDays;
    }

    public void setDelayDays(Integer delayDays) {
        this.delayDays = delayDays;
    }

    public String getDelayReason() {
        return delayReason;
    }

    public void setDelayReason(String delayReason) {
        this.delayReason = delayReason;
    }

    public String getReclassifyReason() {
        return reclassifyReason;
    }

    public void setReclassifyReason(String reclassifyReason) {
        this.reclassifyReason = reclassifyReason;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public Role getTransferRole() {
        return transferRole;
    }

    public void setTransferRole(Role transferRole) {
        this.transferRole = transferRole;
    }

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public List<Pic> getPics() {
        return pics;
    }

    public void setPics(List<Pic> pics) {
        this.pics = pics;
    }

    public List<Response> getResponses() {
        return responses;
    }

    public void setResponses(List<Response> responses) {
        this.responses = responses;
    }

    public void addResponse(Response response) {
        this.getResponses().add(response);
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void incrementLikes() {
        this.likes += 1;
    }

    public void decrementLikes() {
        if (this.likes > 0) {
            this.likes -= 1;
        }
    }
}
