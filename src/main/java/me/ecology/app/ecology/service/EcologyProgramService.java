package me.ecology.app.ecology.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ecology.app.ecology.repository.EcologyProgramRepository;
import me.ecology.vo.ecology.EcologyProgram;

@Slf4j
@RequiredArgsConstructor
@Service
public class EcologyProgramService {
	private final EcologyProgramRepository ecologyProgramRepository;

	@Transactional(propagation=Propagation.REQUIRED)
	public List<EcologyProgram> saveAll(final List<EcologyProgram> list) throws Exception {
		if(log.isDebugEnabled())
			list.parallelStream().forEach(x -> log.debug("saveAll ecologyProgram[{}]", x));

		return ecologyProgramRepository.saveAll(list);
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public EcologyProgram save(final EcologyProgram ecologyProgram) throws Exception {
		log.info("save ecologyProgram[{}]", ecologyProgram);
		return ecologyProgramRepository.save(ecologyProgram);
	}

	public long count() {
		return ecologyProgramRepository.count();
	}

	public boolean existsByProgramName(final String programName) throws Exception {
		return ecologyProgramRepository.existsByProgramName(programName);
	}

	public List<EcologyProgram> findFirstByOrderByIdDesc() throws Exception {
		return ecologyProgramRepository.findFirstByOrderByProgramIdDesc();
	}

	public Optional<EcologyProgram> findById(final String programId) throws Exception {
		return ecologyProgramRepository.findById(programId);
	}

	public List<EcologyProgram> findPrgmListQueryByRegionId(final String regionId) throws Exception {
		return ecologyProgramRepository.findPrgmListQueryByRegionId(regionId);
	}

	public List<EcologyProgram> findProgramInfoQueryByKeyword(final String keyword) throws Exception {
		log.info("ecology keyword : {}", keyword);

		return ecologyProgramRepository.findProgramInfoQueryByKeyword(keyword);
	}
}
