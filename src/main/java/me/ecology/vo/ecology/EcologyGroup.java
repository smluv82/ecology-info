package me.ecology.vo.ecology;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.ecology.vo.ecology.validation.EcologyCreateValidation;
import me.ecology.vo.ecology.validation.EcologyKeywordValidation;
import me.ecology.vo.ecology.validation.EcologyUpdateValidation;

@JsonInclude(Include.NON_NULL)
@Data
@NoArgsConstructor
public class EcologyGroup implements Serializable {
	private static final long serialVersionUID = 5844859120054586661L;

//	@NotEmpty(groups = {EcologyUpdateValidation.class})
	private String regionId;
	@NotEmpty(groups = {EcologyCreateValidation.class, EcologyUpdateValidation.class})
	private String regionName;
	@NotEmpty(groups = {EcologyUpdateValidation.class})
	private String programId;
	@NotEmpty(groups = {EcologyCreateValidation.class})
	private String programName;
	@NotEmpty(groups = {EcologyCreateValidation.class, EcologyUpdateValidation.class})
	private String theme;
	private String programInfo;
	private String programDetail;

	private long saveCnt;
	@NotEmpty(groups = {EcologyKeywordValidation.class})
	private String keyword;

	@Builder
	public EcologyGroup(long saveCnt) {
		this.saveCnt = saveCnt;
	}
}
