package com.project.entity;

import com.project.common.bean.BaseEntity;
import javax.persistence.*;

@Table(name = "bs_meeting")
public class Meeting extends BaseEntity {
    @Id
    @Column(name = "meeting_id")
    private String meetingId;

    /**
     * 会议名称
     */
    @Column(name = "meeting_name")
    private String meetingName;

    public String getOnlyMeetingName(){
        if(meetingName == null){ 
            return null;      
        }                     
        return "%"+meetingName.trim()+"%";
    }

    /**
     * 会议简介
     */
    private String infor;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private String userId;

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
     * 获取会议名称
     *
     * @return meeting_name - 会议名称
     */
    public String getMeetingName() {
        return meetingName;
    }

    /**
     * 设置会议名称
     *
     * @param meetingName 会议名称
     */
    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }

    /**
     * 获取会议简介
     *
     * @return infor - 会议简介
     */
    public String getInfor() {
        return infor;
    }

    /**
     * 设置会议简介
     *
     * @param infor 会议简介
     */
    public void setInfor(String infor) {
        this.infor = infor;
    }

    /**
     * 获取用户ID
     *
     * @return user_id - 用户ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置用户ID
     *
     * @param userId 用户ID
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }
}