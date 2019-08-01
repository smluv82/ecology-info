package me.ecology.vo.ecology;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name="ecology_program")
@JsonInclude(Include.NON_NULL)
@EqualsAndHashCode(callSuper = false, exclude= {"ecologyCode"})
@ToString(exclude="ecologyCode")
@Data
@NoArgsConstructor
//@AllArgsConstructor
public class EcologyProgram implements Serializable {
	private static final long serialVersionUID = -2288973070249938655L;

	@Id
	@Column(name="program_id")
	private String programId;

	@Column(name="program_name", unique=true, nullable=false, length=100)
	private String programName;

	@Column
	private String theme;

	@Column(name="program_info", nullable=true, length=1024)
	private String programInfo;

	@Column(name="program_detail", columnDefinition="TEXT")
	private String programDetail;

	@CreationTimestamp
	@Column(name="created_at", updatable=false, columnDefinition = "TIMESTAMP")
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name="updated_at", columnDefinition = "TIMESTAMP")
	private LocalDateTime updatedAt;

//	@ManyToOne(cascade=CascadeType.PERSIST)
	@JsonBackReference
	@ManyToOne(targetEntity=EcologyCode.class, fetch=FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="region_id")
	private EcologyCode ecologyCode;

	public EcologyProgram(String programName, String theme) {
		this.programName = programName;
		this.theme = theme;
	}

	public EcologyProgram(String theme) {
		this.theme = theme;
	}
}
