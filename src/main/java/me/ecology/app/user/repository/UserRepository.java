package me.ecology.app.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import me.ecology.vo.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	boolean existsByUserId(String userId);

	Optional<User> findByUserId(@Param("userId") String userId);
}
