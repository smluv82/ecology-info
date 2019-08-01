package me.ecology.app.ecology;

import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Strings;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ecology.app.ecology.service.EcologyService;
import me.ecology.vo.ecology.EcologyCode;
import me.ecology.vo.ecology.EcologyGroup;
import me.ecology.vo.ecology.EcologyProgram;
import me.ecology.vo.ecology.EcologyResult;
import me.ecology.vo.ecology.validation.EcologyCreateValidation;
import me.ecology.vo.ecology.validation.EcologyKeywordValidation;
import me.ecology.vo.ecology.validation.EcologyUpdateValidation;

@Slf4j
@RequiredArgsConstructor
@RestController
//@RequestMapping(value="/ecology", consumes=MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequestMapping(consumes=MediaType.APPLICATION_JSON_UTF8_VALUE)
public class EcologyController {
	private final EcologyService ecologyService;

	/**
	 * 생태 관광정보 조회
	 *
	 * @param regionId
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/ecology/{regionId}")
	public ResponseEntity<EcologyCode> search(@PathVariable final String regionId) throws Exception {
		log.info("search");

		if(Strings.isNullOrEmpty(regionId))
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		EcologyCode code = ecologyService.search(regionId);

		if(Objects.isNull(code))
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);

		return ResponseEntity.ok(code);
	}

	/**
	 * 생태 관광정보 추가
	 *
	 * @param ecologyGroup
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/ecology")
	public ResponseEntity<EcologyProgram> create(@RequestBody @Validated(EcologyCreateValidation.class) final EcologyGroup ecologyGroup) throws Exception {
		log.info("create");

		return ResponseEntity.ok(ecologyService.create(ecologyGroup));
	}

	/**
	 * 생태 관광정보 수정
	 *
	 * @param ecologyGroup
	 * @return
	 * @throws Exception
	 */
	@PutMapping("/ecology")
	public ResponseEntity<EcologyProgram> update(@RequestBody @Validated(EcologyUpdateValidation.class) final EcologyGroup ecologyGroup) throws Exception {
		log.info("update");

		return ResponseEntity.ok(ecologyService.update(ecologyGroup));
	}

	/**
	 * 특정 지역명으로 검색 및 결과 지역코드 및 프로그램명, 테마 조회
	 *
	 * @param ecologyGroup
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/ecology/region/search")
	public ResponseEntity<EcologyCode> searchForRegion(@RequestBody final EcologyGroup ecologyGroup) throws Exception {
		log.info("search for place");

		if(Strings.isNullOrEmpty(ecologyGroup.getRegionName()))
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		return ResponseEntity.ok(ecologyService.searchForRegion(ecologyGroup.getRegionName()));
	}

	/**
	 * 프로그램 정보 키워드 검색
	 *
	 * @param ecologyGroup
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/ecology/info/search")
	public ResponseEntity<EcologyResult> searchForInfo(@RequestBody @Validated(EcologyKeywordValidation.class) final EcologyGroup ecologyGroup) throws Exception {
		log.info("search program info for keyword");

		return ResponseEntity.ok(ecologyService.searchForInfo(ecologyGroup.getKeyword()));
	}

	@PostMapping("/ecology/detail/search")
//	public ResponseEntity<EcologyResult> searchForDetail(@RequestBody @Validated(EcologyKeywordValidation.class) final EcologyGroup ecologyGroup) throws Exception {
	public ResponseEntity<String> searchForDetail(@RequestBody @Validated(EcologyKeywordValidation.class) final EcologyGroup ecologyGroup) throws Exception {
		log.info("search program detail for keyword");

		return ResponseEntity.ok("TODO");
	}
}
