package me.ecology.vo.user;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="user")
@JsonInclude(Include.NON_NULL)
@Data
@NoArgsConstructor
public class User implements Serializable {
	private static final long serialVersionUID = -6705304247426379895L;

	@JsonIgnore
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="user_idx")
	private Long userIdx;

	@Column(name="user_id", unique = true, nullable = false, length=20)
	private String userId;

	@JsonIgnore
	@Column(name="user_pwd", nullable = false, length=128)
	private String userPwd;

	@CreationTimestamp
	@Column(name="created_at", updatable=false, columnDefinition="TIMESTAMP")
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name="updated_at", columnDefinition="TIMESTAMP")
	private LocalDateTime updatedAt;

	@Transient
	private String token;

	@Builder
	public User(String userId, String userPwd) {
		this.userId = userId;
		this.userPwd = userPwd;
	}

//	@Builder
//	public User(String token) {
//		this.token = token;
//	}
}
