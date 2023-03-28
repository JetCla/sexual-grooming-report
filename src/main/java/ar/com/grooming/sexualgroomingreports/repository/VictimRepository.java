package ar.com.grooming.sexualgroomingreports.repository;

import ar.com.grooming.sexualgroomingreports.domain.Victim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data JPA repository for the Victim entity.
 */
public interface VictimRepository extends JpaRepository<Victim, Long>, JpaSpecificationExecutor<Victim> {}
