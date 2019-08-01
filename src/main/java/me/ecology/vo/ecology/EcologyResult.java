package me.ecology.vo.ecology;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Data
@NoArgsConstructor
public class EcologyResult implements Serializable {
	private static final long serialVersionUID = -3363776243060630836L;

	private String keyword;
	private Integer count;
	private List<EcologyResultDetail> programs;

	@Builder
	public EcologyResult(String keyword, List<EcologyResultDetail> programs) {
		this.keyword = keyword;
		this.programs = programs;
	}

//	@Builder
	public EcologyResult(String keyword, Integer count) {
		this.keyword = keyword;
		this.count = count;
	}
}
