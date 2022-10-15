package com.crmstudy.domain;

public class User {
    private String id;
    private String login_act;
    private String name;
    private String login_pwd;
    private String email;
    private String expire_time;
    private String lock_state;
    private String deptno;
    private String allow_ips;
    private String createTime;
    private String create_by;
    private String edit_time;
    private String edit_by;

    public User() {
    }

    public User(String id, String login_act, String name, String login_pwd, String email, String expire_time, String lock_state, String deptno, String allow_ips, String createTime, String create_by, String edit_time, String edit_by) {
        this.id = id;
        this.login_act = login_act;
        this.name = name;
        this.login_pwd = login_pwd;
        this.email = email;
        this.expire_time = expire_time;
        this.lock_state = lock_state;
        this.deptno = deptno;
        this.allow_ips = allow_ips;
        this.createTime = createTime;
        this.create_by = create_by;
        this.edit_time = edit_time;
        this.edit_by = edit_by;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin_act() {
        return login_act;
    }

    public void setLogin_act(String login_act) {
        this.login_act = login_act;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin_pwd() {
        return login_pwd;
    }

    public void setLogin_pwd(String login_pwd) {
        this.login_pwd = login_pwd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(String expire_time) {
        this.expire_time = expire_time;
    }

    public String getLock_state() {
        return lock_state;
    }

    public void setLock_state(String lock_state) {
        this.lock_state = lock_state;
    }

    public String getDeptno() {
        return deptno;
    }

    public void setDeptno(String deptno) {
        this.deptno = deptno;
    }

    public String getAllow_ips() {
        return allow_ips;
    }

    public void setAllow_ips(String allow_ips) {
        this.allow_ips = allow_ips;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreate_by() {
        return create_by;
    }

    public void setCreate_by(String create_by) {
        this.create_by = create_by;
    }

    public String getEdit_time() {
        return edit_time;
    }

    public void setEdit_time(String edit_time) {
        this.edit_time = edit_time;
    }

    public String getEdit_by() {
        return edit_by;
    }

    public void setEdit_by(String edit_by) {
        this.edit_by = edit_by;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", login_act='" + login_act + '\'' +
                ", name='" + name + '\'' +
                ", login_pwd='" + login_pwd + '\'' +
                ", email='" + email + '\'' +
                ", expire_time='" + expire_time + '\'' +
                ", lock_state='" + lock_state + '\'' +
                ", deptno='" + deptno + '\'' +
                ", allow_ips='" + allow_ips + '\'' +
                ", createTime='" + createTime + '\'' +
                ", create_by='" + create_by + '\'' +
                ", edit_time='" + edit_time + '\'' +
                ", edit_by='" + edit_by + '\'' +
                '}';
    }
}
