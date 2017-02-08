package com.project.entity;

import javax.persistence.*;

@Table(name = "bs_meeting_user_rel")
public class MeetingUser {
    @Id
    @Column(name = "meeting_user_id")
    private Integer meetingUserId;

    @Column(name = "meeting_id")
    private String meetingId;

    @Column(name = "user_id")
    private String userId;

    /**
     * @return meeting_user_id
     */
    public Integer getMeetingUserId() {
        return meetingUserId;
    }

    /**
     * @param meetingUserId
     */
    public void setMeetingUserId(Integer meetingUserId) {
        this.meetingUserId = meetingUserId;
    }

    /**
     * @return meeting_id
     */
    public String getMeetingId() {
        return meetingId;
    }

    /**
     * @param meetingId
     */
    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    /**
     * @return user_id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }
}