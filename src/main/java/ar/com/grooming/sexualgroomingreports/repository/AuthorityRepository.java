package ar.com.grooming.sexualgroomingreports.repository;

import ar.com.grooming.sexualgroomingreports.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
