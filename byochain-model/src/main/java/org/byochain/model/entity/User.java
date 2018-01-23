package org.byochain.model.entity;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Entity bean User for table "user"
 * @author Giuseppe Vincenzi
 *
 */
@Entity
@Table(name = "user")
@JsonInclude(Include.NON_NULL)
public class User implements Comparable<User>{
	/**
	 * Column user_id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id")
	private Long userId;
	
	/**
	 * Column username
	 */
	@Column(name = "username")
	private String username;
	
	/**
	 * Column password: encrypted value
	 */
	@JsonIgnore
	@Column(name = "password")
	private String password;
	
	/**
	 * Temporary Password to send after user creation
	 */
	@Transient
	private String temporaryPassword;
	
	/**
	 * Column creation_date
	 */
	@Column(name = "creationDate")
	private Calendar creationDate;
	
	/**
	 * Column last_login
	 */
	@Column(name = "lastLogin")
	private Calendar lastLogin;
	
	/**
	 * Column enabled
	 */
	@Column(name = "enabled")
	private Boolean enabled = Boolean.TRUE;
	
	/**
	 * Mapping join table user_roles
	 */
	@ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
        name = "user_roles", 
        joinColumns = { @JoinColumn(name = "user_id") }, 
        inverseJoinColumns = { @JoinColumn(name = "role_id") }
    )
	private Set<Role> roles;
	
	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the creationDate
	 */
	public Calendar getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Calendar creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return the lastLogin
	 */
	public Calendar getLastLogin() {
		return lastLogin;
	}

	/**
	 * @param lastLogin the lastLogin to set
	 */
	public void setLastLogin(Calendar lastLogin) {
		this.lastLogin = lastLogin;
	}

	/**
	 * @return the roles
	 */
	public Set<Role> getRoles() {
		if(roles == null){
			setRoles(new HashSet<Role>(0));
		}
		return roles;
	}

	/**
	 * @param roles the roles to set
	 */
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public int compareTo(User o) {
		return this.getUsername().compareToIgnoreCase(o.getUsername());
	}

	/**
	 * @return the temporaryPassword
	 */
	public String getTemporaryPassword() {
		return temporaryPassword;
	}

	/**
	 * @param temporaryPassword the temporaryPassword to set
	 */
	public void setTemporaryPassword(String temporaryPassword) {
		this.temporaryPassword = temporaryPassword;
	}

	/**
	 * @return the enabled
	 */
	public Boolean getEnabled() {
		return enabled;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

}
