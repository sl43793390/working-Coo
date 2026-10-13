package com.sl.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import cn.hutool.core.util.RandomUtil;
@TableName("users")
public class User implements Serializable {
	@TableId
    private String userId;

    private String userName;//

    private String email;//

    private String password;//

    private Date createTime;//

    private Date expireTime;//

    private String department;//

    private String organization;//

    private String idInstitution;//

    private Integer version;

    private String cdPhone;

    private String userFlag;

    private String cdFrozenState;
    
    private Date dtLogin;
    @TableField(exist = false)
    private boolean isFlag;

    private static final long serialVersionUID = 1L;

    public String getUserId() {
        return userId;
    }

    
    public boolean isFlag() {
		return isFlag;
	}


	public void setFlag(boolean isFlag) {
		this.isFlag = isFlag;
	}


	public User() {
		super();
	}


	public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department == null ? null : department.trim();
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization == null ? null : organization.trim();
    }

    public String getIdInstitution() {
        return idInstitution;
    }

    public void setIdInstitution(String idInstitution) {
        this.idInstitution = idInstitution == null ? null : idInstitution.trim();
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getCdPhone() {
        return cdPhone;
    }

    public void setCdPhone(String cdPhone) {
        this.cdPhone = cdPhone == null ? null : cdPhone.trim();
    }

    public String getUserFlag() {
        return userFlag;
    }

    public void setUserFlag(String userFlag) {
        this.userFlag = userFlag == null ? null : userFlag.trim();
    }

    public String getCdFrozenState() {
        return cdFrozenState;
    }

    public void setCdFrozenState(String cdFrozenState) {
        this.cdFrozenState = cdFrozenState == null ? null : cdFrozenState.trim();
    }

    public Date getDtLogin() {
        return dtLogin;
    }

    public void setDtLogin(Date dtLogin) {
        this.dtLogin = dtLogin;
    }


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}


	public User(String userID, String userName,String cdPhone,String email, String department,String organization) {
		super();
		this.userId = userID;
		this.userName = userName;
		this.cdPhone = cdPhone;
		this.email = email;
		this.department = department;
		this.organization = organization;
		this.dtLogin = new Date();
	}
	public static List<User> createDemoData(int i) {
		List<User> users = new ArrayList<>();
		for (int j = 0; j < i; j++) {
			User user = new User("userId"+j,RandomUtil.randomString(RandomUtil.BASE_CHAR, 6), "123456"+j,"emai@"+j, "部门"+j,"机构"+j);
			users.add(user);
		}
		return users;
	}


	@Override
	public String toString() {
		return "User [userId=" + userId + ", userName=" + userName + ", email=" + email + ", password=" + password
				+ ", createTime=" + createTime + ", expireTime=" + expireTime + ", department=" + department
				+ ", organization=" + organization + ", idInstitution=" + idInstitution + ", version=" + version
				+ ", cdPhone=" + cdPhone + ", userFlag=" + userFlag + ", cdFrozenState=" + cdFrozenState + ", dtLogin="
				+ dtLogin + ", isFlag=" + isFlag + "]";
	}
    
    
}