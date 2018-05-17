package com.cellinfo.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * The persistent class for the tl_gamma_user database table.
 * 
 */
@JsonIgnoreProperties({ "account_enabled","account_non_expired","account_non_locked",
	"enabled","password","roleId",
    "username","authorities","accountNonLocked","accountNonExpired",
    "credentialsNonExpired"})

@Entity
@Table(name="tl_gamma_user")
@NamedQuery(name="TlGammaUser.findAll", query="SELECT t FROM TlGammaUser t")
public class TlGammaUser implements Serializable , UserDetails{
	private static final long serialVersionUID = 1L;

	@Column(name="group_guid")
	private String groupGuid;

	@Column(name="role_id", length=32)
	private String roleId;

	@Column(name="user_cnname", length=32)
	private String userCnname;

	@Column(name="user_guid", nullable=false)
	private String userGuid;
	
	@Column(name="user_email", length=64)
	private String userEmail;

	@Id
	@Column(name="user_name", length=64)
	private String userName;

	@Column(name="user_password", length=256)
	private String userPassword;

	@Column(name="account_enabled")
	private Integer accEnabled;
	
	@Column(name="account_non_expired")
	private Integer accNonExpired;
	
	@Column(name="account_non_locked")
	private Integer accNonLocked;
	
	public TlGammaUser() {
	}

	public TlGammaUser(String username, String password, boolean account_enabled,
			boolean account_non_expired, boolean account_non_locked,
			Collection<? extends GrantedAuthority> authorities) {
		// TODO Auto-generated constructor stub
		this.userName = username;
		this.userPassword = password;

		if(authorities!= null && authorities.size()>0)
		{
			//System.out.println(authorities.toArray()[0].toString());
			 for(GrantedAuthority ga:authorities){
				 this.roleId = ga.getAuthority();
	            }
		}
		if(account_enabled)
			this.accEnabled = 1;
		else
			this.accEnabled = 0;
		
		if(account_non_expired)
			this.accNonExpired = 1;
		else
			this.accNonExpired = 0;
		
		if(account_non_locked)
			this.accNonLocked = 1;
		else
			this.accNonLocked = 0;
	}

	public String getGroupGuid() {
		return this.groupGuid;
	}

	public void setGroupGuid(String groupGuid) {
		this.groupGuid = groupGuid;
	}

	public String getRoleId() {
		return this.roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getUserCnname() {
		return this.userCnname;
	}

	public void setUserCnname(String userCnname) {
		this.userCnname = userCnname;
	}

	public String getUserGuid() {
		return this.userGuid;
	}

	public void setUserGuid(String userGuid) {
		this.userGuid = userGuid;
	}


	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return this.userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public UsernamePasswordAuthenticationToken toAuthenticationToken() {
		return  new UsernamePasswordAuthenticationToken(userName, userPassword, getAuthorities());
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		if(this.getRoleId() != null)
		{
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(this.getRoleId());
            authorities.add(authority);
		}
		return authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.userPassword;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return this.accNonExpired == 1? true :false;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return this.accNonLocked == 1? true:false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return this.accEnabled == 1? true:false;
	}

	/**
	 * @return the userEmail
	 */
	public String getUserEmail() {
		return userEmail;
	}

	/**
	 * @param userEmail the userEmail to set
	 */
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	
	

}