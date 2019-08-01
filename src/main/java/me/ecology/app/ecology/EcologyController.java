package me.ecology.app.ecology;

import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
import me.ecology.vo.ecology.EcologyParam;
import me.ecology.vo.ecology.EcologyProgram;
import me.ecology.vo.ecology.EcologyResult;
import me.ecology.vo.ecology.validation.EcologyCreateValidation;
import me.ecology.vo.ecology.validation.EcologyKeywordValidation;
import me.ecology.vo.ecology.validation.EcologyUpdateValidation;
import me.ecology.vo.ecology.validation.EcologyWeightValidation;

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
	 * @param ecologyParam
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/ecology")
	public ResponseEntity<EcologyProgram> create(@RequestBody @Validated(EcologyCreateValidation.class) final EcologyParam ecologyParam) throws Exception {
		log.info("create");

		return ResponseEntity.ok(ecologyService.create(ecologyParam));
	}

	/**
	 * 생태 관광정보 수정
	 *
	 * @param ecologyParam
	 * @return
	 * @throws Exception
	 */
	@PutMapping("/ecology")
	public ResponseEntity<EcologyProgram> update(@RequestBody @Validated(EcologyUpdateValidation.class) final EcologyParam ecologyParam) throws Exception {
		log.info("update");

		return ResponseEntity.ok(ecologyService.update(ecologyParam));
	}

	/**
	 * 특정 지역명으로 검색 및 결과 지역코드 및 프로그램명, 테마 조회
	 *
	 * @param ecologyParam
	 * @return
	 * @throws Exception
	 */
//	@PostMapping("/ecology/region/search")
//	public ResponseEntity<EcologyCode> searchForRegion(@RequestBody final ecologyParam ecologyParam) throws Exception {
	@GetMapping("/ecology/region/search")
	public ResponseEntity<EcologyCode> searchForRegion(@ModelAttribute final EcologyParam ecologyParam) throws Exception {
		log.info("search for place");

		if(Strings.isNullOrEmpty(ecologyParam.getRegionName()))
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		return ResponseEntity.ok(ecologyService.searchForRegion(ecologyParam.getRegionName()));
	}

	/**
	 * 프로그램 정보 키워드 카운트 검색
	 *
	 * @param ecologyParam
	 * @return
	 * @throws Exception
	 */
//	@PostMapping("/ecology/info/search")
//	public ResponseEntity<EcologyResult> searchForInfo(@RequestBody @Validated(EcologyKeywordValidation.class) final ecologyParam ecologyParam) throws Exception {
	@GetMapping("/ecology/info/search")
	public ResponseEntity<EcologyResult> searchForInfo(@ModelAttribute @Validated(EcologyKeywordValidation.class) final EcologyParam ecologyParam) throws Exception {
		log.info("search program info for keyword");

		return ResponseEntity.ok(ecologyService.searchForInfo(ecologyParam.getKeyword()));
	}

	/**
	 * 프로그램 상세 정보 키워드 카운트 조회
	 *
	 * @param ecologyParam
	 * @return
	 * @throws Exception
	 */
//	@PostMapping("/ecology/detail/search")
//	public ResponseEntity<EcologyResult> searchForDetail(@RequestBody @Validated(EcologyKeywordValidation.class) final ecologyParam ecologyParam) throws Exception {
	@GetMapping("/ecology/detail/search")
	public ResponseEntity<EcologyResult> searchForDetail(@ModelAttribute @Validated(EcologyKeywordValidation.class) final EcologyParam ecologyParam) throws Exception {
		log.info("search program detail for keyword");

		return ResponseEntity.ok(ecologyService.searchForDetail(ecologyParam.getKeyword()));
	}

	/**
	 * 지역명, 키워드를 입력 받아 추천 프로그램 (가중치) 조회
	 *
	 * @param ecologyParam
	 * @return
	 * @throws Exception
	 */
//	@PostMapping("/ecology/weight/search")
//	public ResponseEntity<EcologyResult> searchForWeight(@RequestBody @Validated(EcologyWeightValidation.class) final ecologyParam ecologyParam) throws Exception {
	@GetMapping("/ecology/weight/search")
	public ResponseEntity<EcologyResult> searchForWeight(@ModelAttribute @Validated(EcologyWeightValidation.class) final EcologyParam ecologyParam) throws Exception {
		log.info("search program weight for keyword");

		return ResponseEntity.ok(ecologyService.searchForWeight(ecologyParam));
	}
}
