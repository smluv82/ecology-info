package me.ecology.app.ecology.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import me.ecology.vo.ecology.EcologyProgram;

public interface EcologyProgramRepository extends JpaRepository<EcologyProgram, String> {
//	Optional<EcologyProgram> findByProgramName(String programName);
	boolean existsByProgramName(String programName);

	List<EcologyProgram> findFirstByOrderByProgramIdDesc();
//	Optional<EcologyProgram> findFirstByOrderByIdDesc();

//	@Query(value="SELECT programName")
//	List<EcologyProgram> findAllByRegionId();

//	@Query(value="SELECT program_name, theme FROM ecology_program WHERE region_id = :regionId", nativeQuery = true)
//	@Query(value="SELECT * FROM ecology_program WHERE region_id = :regionId", nativeQuery = true)
	@Query("select new me.ecology.vo.ecology.EcologyProgram(e.programName, e.theme) from EcologyProgram e where e.ecologyCode.regionId = :regionId")
	List<EcologyProgram> findPrgmListQueryByRegionId(@Param("regionId") String regionId);

	@Query(value="SELECT * FROM ecology_program WHERE program_info LIKE %:keyword%", nativeQuery = true)
	List<EcologyProgram> findProgramInfoQueryByKeyword(@Param("keyword") String keyword);
}
