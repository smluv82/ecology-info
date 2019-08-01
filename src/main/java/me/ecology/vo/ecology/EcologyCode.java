package me.ecology.vo.ecology;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name="ecology_code")
@JsonInclude(Include.NON_NULL)
@EqualsAndHashCode(callSuper = false, exclude= {"ecologyPrograms"})
@ToString(exclude="ecologyPrograms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EcologyCode implements Serializable {
	private static final long serialVersionUID = 3645619299259469267L;

	@Id
	@Column(name="region_id")
	private String regionId;

//	@NotEmpty
	@Column(name="region_name", unique=true, nullable=false, length=200)
	private String regionName;

//	@OneToMany(cascade=CascadeType.ALL)
	@JsonManagedReference
	@OneToMany(mappedBy="ecologyCode")
//	@JoinColumn(name="region_id")
	private List<EcologyProgram> ecologyPrograms;

	@Builder
	public EcologyCode(String regionId, List<EcologyProgram> ecologyPrograms) {
		this.regionId = regionId;
		this.ecologyPrograms = ecologyPrograms;
	}
}
