package me.ecology.vo.ecology;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EcologyResultDetail implements Serializable {
	private static final long serialVersionUID = 1345442771151079853L;

	private String region;
	private Long count;
}
