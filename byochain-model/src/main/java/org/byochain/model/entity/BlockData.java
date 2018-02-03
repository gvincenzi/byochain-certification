package org.byochain.model.entity;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Entity bean User for table "block_data"
 * @author Giuseppe Vincenzi
 *
 */
@Entity
@Table(name = "block_data")
@JsonInclude(Include.NON_NULL)
public class BlockData {
	/**
	 * Column block_data_id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "block_data_id")
	@JsonIgnore
	private Long blockDataId;
	
	/**
	 * Column data
	 */
	@Column(name = "data")
	String data;
	
	/**
	 * Column expirationDate
	 */
	@Column(name = "expiration_date")
	Calendar expirationDate;
	
	/**
	 * Column enabled
	 */
	@Column(name = "enabled")
	Boolean enabled = Boolean.TRUE;
	
	/**
	 * Column logo
	 */
	@Column(name = "logo")
	String logo;
	
	/**
	 * @return the expirationDate
	 */
	public Calendar getExpirationDate() {
		return expirationDate;
	}
	/**
	 * @param expirationDate the expirationDate to set
	 */
	public void setExpirationDate(Calendar expirationDate) {
		this.expirationDate = expirationDate;
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
	/**
	 * @return the logo
	 */
	public String getLogo() {
		return logo;
	}
	/**
	 * @param logo the logo to set
	 */
	public void setLogo(String logo) {
		this.logo = logo;
	}
	/**
	 * @return the blockDataId
	 */
	public Long getBlockDataId() {
		return blockDataId;
	}
	/**
	 * @param blockDataId the blockDataId to set
	 */
	public void setBlockDataId(Long blockDataId) {
		this.blockDataId = blockDataId;
	}
	/**
	 * @return the data
	 */
	public String getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((blockDataId == null) ? 0 : blockDataId.hashCode());
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((expirationDate == null) ? 0 : expirationDate.hashCode());
		result = prime * result + ((logo == null) ? 0 : logo.hashCode());
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
		BlockData other = (BlockData) obj;
		if (blockDataId == null) {
			if (other.blockDataId != null)
				return false;
		} else if (!blockDataId.equals(other.blockDataId))
			return false;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (expirationDate == null) {
			if (other.expirationDate != null)
				return false;
		} else if (!expirationDate.equals(other.expirationDate))
			return false;
		if (logo == null) {
			if (other.logo != null)
				return false;
		} else if (!logo.equals(other.logo))
			return false;
		return true;
	}
}
