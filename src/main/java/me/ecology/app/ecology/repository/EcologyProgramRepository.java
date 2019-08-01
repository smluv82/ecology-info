package me.ecology.app.ecology.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import me.ecology.vo.ecology.EcologyProgram;

public interface EcologyProgramRepository extends JpaRepository<EcologyProgram, String> {
	boolean existsByProgramName(String programName);

	List<EcologyProgram> findFirstByOrderByProgramIdDesc();

	@Query("select new me.ecology.vo.ecology.EcologyProgram(e.programName, e.theme) from EcologyProgram e where e.ecologyCode.regionId = :regionId")
	List<EcologyProgram> findPrgmListQueryByRegionId(@Param("regionId") String regionId);

	@Query(value="SELECT * FROM ecology_program WHERE program_info LIKE %:keyword%", nativeQuery = true)
	List<EcologyProgram> findProgramInfoQueryByKeyword(@Param("keyword") String keyword);
}
