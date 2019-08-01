package me.ecology.app.ecology.service;

import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ecology.app.ecology.repository.EcologyCodeRepository;
import me.ecology.vo.ecology.EcologyCode;

@Slf4j
@RequiredArgsConstructor
@Service
public class EcologyCodeService {
	private static int test = 0;

	private final EcologyCodeRepository ecologyCodeRepository;

	public String getRegionCode(final String regionName) {
		String resultCode = null;
		Optional<EcologyCode> srcCode = findByRegionName(regionName);

		if(!srcCode.isPresent()) {
			do {
				//TODO redis로 값 세팅 변경 필요!!!
				String regCode = String.join(StringUtils.EMPTY, "reg", RandomStringUtils.randomNumeric(4));
//				String regCode = String.join(StringUtils.EMPTY, "reg", String.format("%04d", test));
				if(!ecologyCodeRepository.existsById(regCode)) {
					resultCode = regCode;
					test++;
					break;
				}
			}while(true);
		}

		return resultCode;
	}

	public Optional<EcologyCode> findByRegionName(final String regionName) {
		return ecologyCodeRepository.findByRegionName(regionName);
	}

	public Optional<EcologyCode> findById(final String regionId) throws Exception {
		return ecologyCodeRepository.findById(regionId);
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public EcologyCode save(EcologyCode ecologyCode) throws Exception {
		Optional<EcologyCode> srcEcologyCode = ecologyCodeRepository.findByRegionName(ecologyCode.getRegionName());

		if(srcEcologyCode.isPresent())
			return srcEcologyCode.get();

		log.info("save ecologyCode[{}]", ecologyCode);

		return ecologyCodeRepository.save(ecologyCode);
	}
}
