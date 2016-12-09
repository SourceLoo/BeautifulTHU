package com.thu.domain;

import javax.persistence.*;
import java.util.*;

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
    private List<Role> otherRoles = new ArrayList<Role>();

    private Status status = Status.UNCLASSIFIED;
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

    private Integer delayDays = 0;
    private String delayReason = null;
    private String reclassifyReason = null;
    private String rejectReason = null;

    @ManyToOne
    private Role transferRole;
    private Boolean isRead = false;

    private String instruction;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pic> pics = new ArrayList<Pic>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Response> responses = new ArrayList<Response>();

    protected Question() {}

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreatedLocation() {
        return createdLocation;
    }

    public void setCreatedLocation(String createdLocation) {
        this.createdLocation = createdLocation;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getDdl() {
        return ddl;
    }

    public void setDdl(Date ddl) {
        this.ddl = ddl;
    }

    public Date getTimestamp1() {
        return timestamp1;
    }

    public void setTimestamp1(Date timestamp1) {
        this.timestamp1 = timestamp1;
    }

    public Date getTimestamp2() {
        return timestamp2;
    }

    public void setTimestamp2(Date timestamp2) {
        this.timestamp2 = timestamp2;
    }

    public Date getTimestamp3() {
        return timestamp3;
    }

    public void setTimestamp3(Date timestamp3) {
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
        this.responses.add(response);
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
