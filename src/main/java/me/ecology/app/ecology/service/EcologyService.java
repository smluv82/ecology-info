package me.ecology.app.ecology.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ecology.vo.ecology.EcologyCode;
import me.ecology.vo.ecology.EcologyGroup;
import me.ecology.vo.ecology.EcologyProgram;
import me.ecology.vo.ecology.EcologyResult;
import me.ecology.vo.ecology.EcologyResultDetail;

@Slf4j
@RequiredArgsConstructor
@Service
public class EcologyService {
	private final EcologyCodeService ecologyCodeService;
	private final EcologyProgramService ecologyProgramService;

	/**
	 * @param ecologyList
	 * @return
	 * @throws Exception
	 */
//	@Transactional(propagation=Propagation.REQUIRED)
	public EcologyGroup saveCsv(final List<EcologyGroup> ecologyList) throws Exception {
		log.info("saveCsv service");

//		for(EcologyGroup e : ecologyList) {
//			log.info("e : {}", e);
//		}

//		List<EcologyCode> codeList = ecologyList.parallelStream().map(x-> {
//			EcologyCode ecologyCode = new EcologyCode();
//
//			String codeId = ecologyCodeService.getRegionCode(x.getRegionName());
//
//			if(Strings.isNullOrEmpty(codeId))
//				return ecologyCode;
//
//			ecologyCode.setRegionId(codeId);
//			ecologyCode.setRegionName(x.getRegionName());
//			ecologyCodeService.save(ecologyCode);
//
//			return ecologyCode;
//		})
//		.peek(x-> log.info("peek x : {}", x))
//		.filter(x-> Objects.nonNull(x.getRegionId()))
//		.collect(Collectors.toList());

//		ecologyList.parallelStream().forEach(x-> {


//		List<EcologyProgram> ecoPrgmList = ecologyList.stream()
//				.filter(Objects::nonNull)
//				.peek(x-> log.info("before insert data[{}]", x))
//				.map(x-> {
//					EcologyCode ecologyCode = new EcologyCode();
//
//					String codeId = ecologyCodeService.getRegionCode(x.getRegionName());
//
//					if(!Strings.isNullOrEmpty(codeId)) {
//						try {
//							ecologyCode.setRegionId(codeId);
//							ecologyCode.setRegionName(x.getRegionName());
//							ecologyCode = ecologyCodeService.save(ecologyCode);
//						}catch(Exception e) {
//							log.error("SQLException : ", e);
//						}
//					}
//
//					EcologyProgram ecologyProgram = new EcologyProgram();
//					BeanUtils.copyProperties(x, ecologyProgram);
//					ecologyProgram.setEcologyCode(ecologyCode);
//					return ecologyProgram;
//				})
//				.peek(x -> log.info("ecologyProgram[{}]", x))
//				.collect(Collectors.toList());

		long beforeCnt = ecologyProgramService.count();

		ecologyList.stream()
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

		return EcologyGroup.builder().saveCnt(saveCnt).build();
	}

	/**
	 * @param regionId
	 * @return
	 * @throws Exception
	 */
	public EcologyCode search(final String regionId) throws Exception {
		log.info("search regionId[{}]", regionId);

		return ecologyCodeService.findById(regionId).orElse(new EcologyCode());
	}

	public EcologyProgram create(final EcologyGroup ecologyGroup) throws Exception {
		//TODO 나중에 에러코드 정의할것
		if(ecologyProgramService.existsByProgramName(ecologyGroup.getProgramName())) {
			log.info("already exist data");
			return null;
		}

		EcologyCode ecologyCode = new EcologyCode();
		String codeId = ecologyCodeService.getRegionCode(ecologyGroup.getRegionName());

		if(!Strings.isNullOrEmpty(codeId)) {
			ecologyCode.setRegionId(codeId);
			ecologyCode.setRegionName(ecologyGroup.getRegionName());
			ecologyCode = ecologyCodeService.save(ecologyCode);
		} else {
			ecologyCode = ecologyCodeService.findByRegionName(ecologyGroup.getRegionName()).get();
		}

		//ecologyProgram save
		EcologyProgram ecologyProgram = new EcologyProgram();
		BeanUtils.copyProperties(ecologyGroup, ecologyProgram);

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

	public EcologyProgram update(final EcologyGroup ecologyGroup) throws Exception {
		log.info("update ecologyGroup[{}]", ecologyGroup);
		//TODO exception 변경 필요
		EcologyProgram srcPrgm = ecologyProgramService.findById(ecologyGroup.getProgramId()).orElseThrow(() -> new Exception("not exist program"));

		log.info("srcPrgm[{}]", srcPrgm);

		if(!ecologyGroup.getRegionName().equals(srcPrgm.getEcologyCode().getRegionName())) {
			//서비스 명이 변경 됨
			EcologyCode ecologyCode = new EcologyCode();
			String codeId = ecologyCodeService.getRegionCode(ecologyGroup.getRegionName());

			if(!Strings.isNullOrEmpty(codeId)) {
				ecologyCode.setRegionId(codeId);
				ecologyCode.setRegionName(ecologyGroup.getRegionName());
				ecologyCode = ecologyCodeService.save(ecologyCode);
			} else {
				ecologyCode = ecologyCodeService.findByRegionName(ecologyGroup.getRegionName()).get();
			}

			srcPrgm.setEcologyCode(ecologyCode);
		}

		srcPrgm.setProgramInfo(Optional.ofNullable(ecologyGroup.getProgramInfo()).orElse(StringUtils.EMPTY));
		srcPrgm.setProgramDetail(Optional.ofNullable(ecologyGroup.getProgramDetail()).orElse(StringUtils.EMPTY));
		srcPrgm.setTheme(ecologyGroup.getTheme());

		return ecologyProgramService.save(srcPrgm);
	}

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

	public EcologyResult searchForDetail(final String keyword) throws Exception {
		log.info("searchForDetail service call");

		List<EcologyProgram> ecologyProgramList = ecologyProgramService.findAll();

		if(ecologyProgramList.isEmpty()) {
			log.info("list is not exist");
			return new EcologyResult(keyword, 0);
		}

		int count = 0;

		for(EcologyProgram program : ecologyProgramList) {
//			log.debug(program.getProgramDetail());
			log.info("detail : {}", program.getProgramDetail());

			int tmpCount = StringUtils.countMatches(program.getProgramDetail(), keyword);
			log.info("tmpCount : {}", tmpCount);
			count = count + tmpCount;
		}

		log.info("keyword count : {}", count);

		return new EcologyResult(keyword, count);
//		return EcologyResult.builder().keyword(keyword).count(1L).build();
	}
}
