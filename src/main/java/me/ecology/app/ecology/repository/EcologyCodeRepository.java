package me.ecology.app.ecology.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import me.ecology.vo.ecology.EcologyCode;

@Repository
public interface EcologyCodeRepository extends JpaRepository<EcologyCode, String>{
	Optional<EcologyCode> findByRegionName(String regionName);

	@Query(value="SELECT * FROM ecology_code WHERE region_name LIKE %:regionName%", nativeQuery = true)
	List<EcologyCode> findQueryByRegionName(@Param("regionName") String regionName);
}
