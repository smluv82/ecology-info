package me.ecology.app.ecology.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ecology.vo.ecology.EcologyCode;
import me.ecology.vo.ecology.EcologyParam;
import me.ecology.vo.ecology.EcologyProgram;
import me.ecology.vo.ecology.EcologyResult;
import me.ecology.vo.ecology.EcologyResultDetail;

@Slf4j
@RequiredArgsConstructor
@Service
public class EcologyService {
	private final Environment env;
	private final EcologyCodeService ecologyCodeService;
	private final EcologyProgramService ecologyProgramService;

	/**
	 * csv 파일을 통한 업로드
	 *
	 * @param ecologyParamList
	 * @return
	 * @throws Exception
	 */
//	@Transactional(propagation=Propagation.REQUIRED)
	public EcologyParam saveCsv(final List<EcologyParam> ecologyParamList) throws Exception {
		log.info("saveCsv service");

		long beforeCnt = ecologyProgramService.count();

		ecologyParamList.stream()
				.filter(Objects::nonNull)
				.peek(x-> log.info("before insert data[{}]", x))
				.forEach(x-> {
					try {
						//ecologyCode save
						EcologyCode ecologyCode = new EcologyCode();
						String codeId = ecologyCodeService.getRegionCode(x.getRegionName());

						if(!Strings.isNullOrEmpty(codeId)) {
							//서비스 코드 존재 하지 않는 경우 추가
							ecologyCode.setRegionId(codeId);
							ecologyCode.setRegionName(x.getRegionName());
							ecologyCode = ecologyCodeService.save(ecologyCode);
						} else {
							//서비스 코드 존재하는 경우
							ecologyCode = ecologyCodeService.findByRegionName(x.getRegionName()).get();
						}

						//ecologyProgram save
						EcologyProgram ecologyProgram = new EcologyProgram();
						BeanUtils.copyProperties(x, ecologyProgram);
						ecologyProgram.setEcologyCode(ecologyCode);

						ecologyProgramService.save(ecologyProgram);
					}catch(Exception e) {
						log.error("SQLException : ", e);
					}
				});

		long saveCnt = ecologyProgramService.count() - beforeCnt;

		return EcologyParam.builder().saveCnt(saveCnt).build();
	}

	/**
	 * 지역명을 통한 조회
	 *
	 * @param regionId
	 * @return
	 * @throws Exception
	 */
	public EcologyCode search(final String regionId) throws Exception {
		log.info("search regionId[{}]", regionId);

		return ecologyCodeService.findById(regionId).orElse(new EcologyCode());
	}

	/**
	 * 생태 관광정보 추가
	 *
	 * @param ecologyParam
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public EcologyProgram create(final EcologyParam ecologyParam) throws Exception {
		//TODO 나중에 에러코드 정의할것
		if(ecologyProgramService.existsByProgramName(ecologyParam.getProgramName())) {
			log.info("already exist data");
			return null;
		}

		EcologyCode ecologyCode = new EcologyCode();
		String codeId = ecologyCodeService.getRegionCode(ecologyParam.getRegionName());

		if(!Strings.isNullOrEmpty(codeId)) {
			ecologyCode.setRegionId(codeId);
			ecologyCode.setRegionName(ecologyParam.getRegionName());
			ecologyCode = ecologyCodeService.save(ecologyCode);
		} else {
			ecologyCode = ecologyCodeService.findByRegionName(ecologyParam.getRegionName()).get();
		}

		//ecologyProgram save
		EcologyProgram ecologyProgram = new EcologyProgram();
		BeanUtils.copyProperties(ecologyParam, ecologyProgram);

		//TODO 나중에 시간되면 redis로 변경
		List<EcologyProgram> srcPrgmList = ecologyProgramService.findFirstByOrderByIdDesc();
		String programId = StringUtils.EMPTY;

		if(srcPrgmList.isEmpty()) {
			//데이터가 없음
			programId = "prgm0000";
		}else {
			//TODO
			//데이터 있는 경우
			int seq = Integer.parseInt(srcPrgmList.get(0).getProgramId().substring(4, 8)) + 1;
			programId = String.join(StringUtils.EMPTY, "prgm", String.format("%04d", seq));
		}

		ecologyProgram.setProgramId(programId);
		ecologyProgram.setEcologyCode(ecologyCode);

		return ecologyProgramService.save(ecologyProgram);
	}

	/**
	 * 생태 관광정보 수정
	 *
	 * @param ecologyParam
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public EcologyProgram update(final EcologyParam ecologyParam) throws Exception {
		log.info("update ecologyParam[{}]", ecologyParam);
		//TODO exception 변경 필요
		EcologyProgram srcPrgm = ecologyProgramService.findById(ecologyParam.getProgramId()).orElseThrow(() -> new Exception("not exist program"));

		log.info("srcPrgm[{}]", srcPrgm);

		if(!ecologyParam.getRegionName().equals(srcPrgm.getEcologyCode().getRegionName())) {
			//서비스 명이 변경 됨
			EcologyCode ecologyCode = new EcologyCode();
			String codeId = ecologyCodeService.getRegionCode(ecologyParam.getRegionName());

			if(!Strings.isNullOrEmpty(codeId)) {
				ecologyCode.setRegionId(codeId);
				ecologyCode.setRegionName(ecologyParam.getRegionName());
				ecologyCode = ecologyCodeService.save(ecologyCode);
			} else {
				ecologyCode = ecologyCodeService.findByRegionName(ecologyParam.getRegionName()).get();
			}

			srcPrgm.setEcologyCode(ecologyCode);
		}

		srcPrgm.setProgramInfo(Optional.ofNullable(ecologyParam.getProgramInfo()).orElse(StringUtils.EMPTY));
		srcPrgm.setProgramDetail(Optional.ofNullable(ecologyParam.getProgramDetail()).orElse(StringUtils.EMPTY));
		srcPrgm.setTheme(ecologyParam.getTheme());

		return ecologyProgramService.save(srcPrgm);
	}

	/**
	 * 특정 지역명으로 검색 및 결과 지역코드 및 프로그램명, 테마 조회
	 *
	 * @param regionName
	 * @return
	 * @throws Exception
	 */
	public EcologyCode searchForRegion(final String regionName) throws Exception {
		//TODO exception 변경 필요
		EcologyCode srcEcologyCode = ecologyCodeService.findByRegionName(regionName)
				.orElseThrow(() -> new Exception("not exist regionName"));
		log.info("srcEcologyCode[{}]", srcEcologyCode);

		List<EcologyProgram> ecologyPrograms = ecologyProgramService.findPrgmListQueryByRegionId(srcEcologyCode.getRegionId());
		ecologyPrograms.parallelStream().forEach(x->log.info("ecologyPrograms data[{}]", x));

		return EcologyCode.builder()
				.regionId(srcEcologyCode.getRegionId())
				.ecologyPrograms(ecologyPrograms)
				.build();
	}

	/**
	 *  프로그램 정보 키워드 카운트 검색
	 *
	 * @param keyword
	 * @return
	 * @throws Exception
	 */
	public EcologyResult searchForInfo(final String keyword) throws Exception {
		log.info("searchForInfo service call");

		List<EcologyProgram> list = ecologyProgramService.findProgramInfoQueryByKeyword(keyword);

		if(list.isEmpty()) {
			log.info("keyword is not exist");
			return EcologyResult.builder().keyword(keyword).programs(Lists.newArrayList()).build();
		}

		//중복 카운트 용 Map
		Map<String, Long> countMap = Maps.newHashMap();

		List<String> regionNameList = list.parallelStream()
		.map(x-> x.getEcologyCode().getRegionName())
		.collect(Collectors.toList());

		regionNameList.forEach(x -> {
			Long count = countMap.get(x);
			countMap.put(x, count == null ? 1 : count + 1);
		});

		List<EcologyResultDetail> detailList = countMap.entrySet().parallelStream()
				.map(x -> new EcologyResultDetail(x.getKey(), x.getValue()))
				.collect(Collectors.toList());

		return EcologyResult.builder()
				.keyword(keyword)
				.programs(detailList)
				.build();
	}

	/**
	 * 프로그램 상세 정보 키워드 카운트 조회
	 *
	 * @param keyword
	 * @return
	 * @throws Exception
	 */
	public EcologyResult searchForDetail(final String keyword) throws Exception {
		log.info("searchForDetail service call");

		List<EcologyProgram> ecologyProgramList = ecologyProgramService.findAll();

		if(ecologyProgramList.isEmpty()) {
			log.info("list is not exist");
			return new EcologyResult(keyword, 0);
		}

		int count = 0;

		for(EcologyProgram program : ecologyProgramList) {
			log.debug("detail : {}", program.getProgramDetail());

			int tmpCount = StringUtils.countMatches(program.getProgramDetail(), keyword);
			log.debug("tmpCount : {}", tmpCount);
			count = count + tmpCount;
		}

		log.info("keyword count : {}", count);

		return new EcologyResult(keyword, count);
//		return EcologyResult.builder().keyword(keyword).count(1L).build();
	}

	/**
	 * 지역 및 키워드를 통한 프로그램 추천(가중치)
	 *
	 * @param ecologyParam
	 * @return
	 * @throws Exception
	 */
	public EcologyResult searchForWeight(final EcologyParam ecologyParam) throws Exception {
		log.info("searchForWeight service call");

		EcologyResult result = new EcologyResult();

		final String keyword = ecologyParam.getKeyword();
		final int themeWeight = env.getProperty("ecology.weight.theme", int.class);
		final int infoWeight = env.getProperty("ecology.weight.info", int.class);
		final int detailWeight = env.getProperty("ecology.weight.detail", int.class);

		log.info("themeWeight[{}] <> infoWeight[{}] <> detailWeight[{}]", themeWeight, infoWeight, detailWeight);

		List<EcologyCode> codeList = ecologyCodeService.findQueryByRegionName(ecologyParam.getRegionName());

		for(EcologyCode code : codeList) {
			log.debug("for loop code[{}]", code);

			List<EcologyProgram> prgmList = code.getEcologyPrograms();

			for(EcologyProgram prgm : prgmList) {
				//동일한 프로그램 인 경우 continue
				if(Objects.equals(prgm.getProgramId(), result.getProgram())) {
					log.info("prgm.getProgramId() : {} <> result.getProgram() : {}", prgm.getProgramId(), result.getProgram());
					continue;
				}

				log.debug("for loop prgm[{}]", prgm);

				int themeAvg = StringUtils.countMatches(prgm.getTheme(), keyword) * themeWeight;
				int infoAvg = StringUtils.countMatches(prgm.getProgramInfo(), keyword) * infoWeight;
				int detailAvg = StringUtils.countMatches(prgm.getProgramDetail(), keyword) * detailWeight;
				int totalWeight = (themeAvg + infoAvg + detailAvg) / (themeWeight + infoWeight + detailWeight);

				log.info("themeAvg[{}] <> infoAvg[{}] <> detailAvg[{}] <> totalWeight[{}]", themeAvg, infoAvg, detailAvg, totalWeight);

				//가중치가 같은 경우에는??
				if(result.getWeight() < totalWeight) {
					log.info("change before result[{}]", result);
					result.setProgram(prgm.getProgramId());
					result.setWeight(totalWeight);
				}
			}
		}

		log.info("last result[{}]", result);
		return result;
	}
}
