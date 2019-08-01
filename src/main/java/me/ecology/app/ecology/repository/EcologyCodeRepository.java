package me.ecology.app.ecology.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.ecology.vo.ecology.EcologyCode;

@Repository
public interface EcologyCodeRepository extends JpaRepository<EcologyCode, String>{
	Optional<EcologyCode> findByRegionName(String regionName);

}
