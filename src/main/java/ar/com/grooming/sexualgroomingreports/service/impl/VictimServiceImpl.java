package ar.com.grooming.sexualgroomingreports.service.impl;

import ar.com.grooming.sexualgroomingreports.domain.Victim;
import ar.com.grooming.sexualgroomingreports.repository.VictimRepository;
import ar.com.grooming.sexualgroomingreports.service.VictimService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Victim}.
 */
@Service
@Transactional
public class VictimServiceImpl implements VictimService {

    private final Logger log = LoggerFactory.getLogger(VictimServiceImpl.class);

    private final VictimRepository victimRepository;

    public VictimServiceImpl(VictimRepository victimRepository) {
        this.victimRepository = victimRepository;
    }

    @Override
    public Victim save(Victim victim) {
        log.debug("Request to save Victim : {}", victim);
        return victimRepository.save(victim);
    }

    @Override
    public Victim update(Victim victim) {
        log.debug("Request to update Victim : {}", victim);
        return victimRepository.save(victim);
    }

    @Override
    public Optional<Victim> partialUpdate(Victim victim) {
        log.debug("Request to partially update Victim : {}", victim);

        return victimRepository
            .findById(victim.getId())
            .map(existingVictim -> {
                if (victim.getFirstName() != null) {
                    existingVictim.setFirstName(victim.getFirstName());
                }
                if (victim.getLastName() != null) {
                    existingVictim.setLastName(victim.getLastName());
                }
                if (victim.getAge() != null) {
                    existingVictim.setAge(victim.getAge());
                }
                if (victim.getCity() != null) {
                    existingVictim.setCity(victim.getCity());
                }
                if (victim.getState() != null) {
                    existingVictim.setState(victim.getState());
                }
                if (victim.getCountry() != null) {
                    existingVictim.setCountry(victim.getCountry());
                }
                if (victim.getObservations() != null) {
                    existingVictim.setObservations(victim.getObservations());
                }

                return existingVictim;
            })
            .map(victimRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Victim> findAll(Pageable pageable) {
        log.debug("Request to get all Victims");
        return victimRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Victim> findOne(Long id) {
        log.debug("Request to get Victim : {}", id);
        return victimRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Victim : {}", id);
        victimRepository.deleteById(id);
    }
}
