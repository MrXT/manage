package com.project.entity;

import com.project.common.bean.BaseEntity;
import javax.persistence.*;

@Table(name = "sys_user")
public class User extends BaseEntity {
    @Id
    @Column(name = "user_id")
    private String userId;

    /**
     * 用户名
     */
    private String username;

    public String getOnlyUsername(){
        if(username == null){ 
            return null;      
        }                     
        return "%"+username.trim()+"%";
    }

    /**
     * 姓名
     */
    private String name;

    public String getOnlyName(){
        if(name == null){ 
            return null;      
        }                     
        return "%"+name.trim()+"%";
    }

    private String password;

    private String salt;

    /**
     * 手机
     */
    private String phone;

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

    /**
     * 获取用户名
     *
     * @return username - 用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户名
     *
     * @param username 用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取姓名
     *
     * @return name - 姓名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置姓名
     *
     * @param name 姓名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return salt
     */
    public String getSalt() {
        return salt;
    }

    /**
     * @param salt
     */
    public void setSalt(String salt) {
        this.salt = salt;
    }

    /**
     * 获取手机
     *
     * @return phone - 手机
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置手机
     *
     * @param phone 手机
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }
}