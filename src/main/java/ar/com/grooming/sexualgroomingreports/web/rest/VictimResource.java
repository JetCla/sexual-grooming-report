package ar.com.grooming.sexualgroomingreports.web.rest;

import ar.com.grooming.sexualgroomingreports.domain.Victim;
import ar.com.grooming.sexualgroomingreports.repository.VictimRepository;
import ar.com.grooming.sexualgroomingreports.service.VictimQueryService;
import ar.com.grooming.sexualgroomingreports.service.VictimService;
import ar.com.grooming.sexualgroomingreports.service.criteria.VictimCriteria;
import ar.com.grooming.sexualgroomingreports.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ar.com.grooming.sexualgroomingreports.domain.Victim}.
 */
@RestController
@RequestMapping("/api")
public class VictimResource {

    private final Logger log = LoggerFactory.getLogger(VictimResource.class);

    private static final String ENTITY_NAME = "victim";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VictimService victimService;

    private final VictimRepository victimRepository;

    private final VictimQueryService victimQueryService;

    public VictimResource(VictimService victimService, VictimRepository victimRepository, VictimQueryService victimQueryService) {
        this.victimService = victimService;
        this.victimRepository = victimRepository;
        this.victimQueryService = victimQueryService;
    }

    /**
     * {@code POST  /victims} : Create a new victim.
     *
     * @param victim the victim to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new victim, or with status {@code 400 (Bad Request)} if the victim has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/victims")
    public ResponseEntity<Victim> createVictim(@RequestBody Victim victim) throws URISyntaxException {
        log.debug("REST request to save Victim : {}", victim);
        if (victim.getId() != null) {
            throw new BadRequestAlertException("A new victim cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Victim result = victimService.save(victim);
        return ResponseEntity
            .created(new URI("/api/victims/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /victims/:id} : Updates an existing victim.
     *
     * @param id the id of the victim to save.
     * @param victim the victim to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated victim,
     * or with status {@code 400 (Bad Request)} if the victim is not valid,
     * or with status {@code 500 (Internal Server Error)} if the victim couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/victims/{id}")
    public ResponseEntity<Victim> updateVictim(@PathVariable(value = "id", required = false) final Long id, @RequestBody Victim victim)
        throws URISyntaxException {
        log.debug("REST request to update Victim : {}, {}", id, victim);
        if (victim.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, victim.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!victimRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Victim result = victimService.update(victim);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, victim.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /victims/:id} : Partial updates given fields of an existing victim, field will ignore if it is null
     *
     * @param id the id of the victim to save.
     * @param victim the victim to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated victim,
     * or with status {@code 400 (Bad Request)} if the victim is not valid,
     * or with status {@code 404 (Not Found)} if the victim is not found,
     * or with status {@code 500 (Internal Server Error)} if the victim couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/victims/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Victim> partialUpdateVictim(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Victim victim
    ) throws URISyntaxException {
        log.debug("REST request to partial update Victim partially : {}, {}", id, victim);
        if (victim.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, victim.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!victimRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Victim> result = victimService.partialUpdate(victim);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, victim.getId().toString())
        );
    }

    /**
     * {@code GET  /victims} : get all the victims.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of victims in body.
     */
    @GetMapping("/victims")
    public ResponseEntity<List<Victim>> getAllVictims(
        VictimCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Victims by criteria: {}", criteria);
        Page<Victim> page = victimQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /victims/count} : count all the victims.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/victims/count")
    public ResponseEntity<Long> countVictims(VictimCriteria criteria) {
        log.debug("REST request to count Victims by criteria: {}", criteria);
        return ResponseEntity.ok().body(victimQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /victims/:id} : get the "id" victim.
     *
     * @param id the id of the victim to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the victim, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/victims/{id}")
    public ResponseEntity<Victim> getVictim(@PathVariable Long id) {
        log.debug("REST request to get Victim : {}", id);
        Optional<Victim> victim = victimService.findOne(id);
        return ResponseUtil.wrapOrNotFound(victim);
    }

    /**
     * {@code DELETE  /victims/:id} : delete the "id" victim.
     *
     * @param id the id of the victim to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/victims/{id}")
    public ResponseEntity<Void> deleteVictim(@PathVariable Long id) {
        log.debug("REST request to delete Victim : {}", id);
        victimService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
