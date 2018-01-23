package org.byochain.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Entity bean User for table "block_referer"
 * @author Giuseppe Vincenzi
 *
 */
@Entity
@Table(name = "block_referer")
@JsonInclude(Include.NON_NULL)
public class BlockReferer {
	/**
	 * Column block_referer_id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "block_referer_id")
	@JsonIgnore
	private Long blockRefererId;
	
	/**
	 * Column ip
	 */
	@Column(name = "referer")
	private String referer;
	
	/**
	 * Column owner
	 */
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="owner_id")
	private Block owner;

	/**
	 * @return the blockRefererId
	 */
	public Long getBlockRefererId() {
		return blockRefererId;
	}

	/**
	 * @param blockRefererId the blockRefererId to set
	 */
	public void setBlockRefererId(Long blockRefererId) {
		this.blockRefererId = blockRefererId;
	}

	/**
	 * @return the referer
	 */
	public String getReferer() {
		return referer;
	}

	/**
	 * @param referer the referer to set
	 */
	public void setReferer(String referer) {
		this.referer = referer;
	}

	/**
	 * @return the owner
	 */
	public Block getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(Block owner) {
		this.owner = owner;
	}
}
