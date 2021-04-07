package org.soaplab.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soaplab.domain.Acid;
import org.soaplab.repository.AcidRepository;
import org.soaplab.web.rest.errors.BadRequestAlertException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.soaplab.domain.Acid}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AcidResource {

    private final Logger log = LoggerFactory.getLogger(AcidResource.class);

    private static final String ENTITY_NAME = "acid";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AcidRepository acidRepository;

    public AcidResource(AcidRepository acidRepository) {
        this.acidRepository = acidRepository;
    }

    /**
     * {@code POST  /acids} : Create a new acid.
     *
     * @param acid the acid to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new acid, or with status {@code 400 (Bad Request)} if the acid has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/acids")
    public ResponseEntity<Acid> createAcid(@RequestBody Acid acid) throws URISyntaxException {
        log.debug("REST request to save Acid : {}", acid);
        if (acid.getId() != null) {
            throw new BadRequestAlertException("A new acid cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Acid result = acidRepository.save(acid);
        return ResponseEntity
            .created(new URI("/api/acids/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /acids/:id} : Updates an existing acid.
     *
     * @param id the id of the acid to save.
     * @param acid the acid to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated acid,
     * or with status {@code 400 (Bad Request)} if the acid is not valid,
     * or with status {@code 500 (Internal Server Error)} if the acid couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/acids/{id}")
    public ResponseEntity<Acid> updateAcid(@PathVariable(value = "id", required = false) final Long id, @RequestBody Acid acid)
        throws URISyntaxException {
        log.debug("REST request to update Acid : {}, {}", id, acid);
        if (acid.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, acid.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!acidRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Acid result = acidRepository.save(acid);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, acid.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /acids/:id} : Partial updates given fields of an existing acid, field will ignore if it is null
     *
     * @param id the id of the acid to save.
     * @param acid the acid to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated acid,
     * or with status {@code 400 (Bad Request)} if the acid is not valid,
     * or with status {@code 404 (Not Found)} if the acid is not found,
     * or with status {@code 500 (Internal Server Error)} if the acid couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/acids/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Acid> partialUpdateAcid(@PathVariable(value = "id", required = false) final Long id, @RequestBody Acid acid)
        throws URISyntaxException {
        log.debug("REST request to partial update Acid partially : {}, {}", id, acid);
        if (acid.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, acid.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!acidRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Acid> result = acidRepository
            .findById(acid.getId())
            .map(
                existingAcid -> {
                    if (acid.getName() != null) {
                        existingAcid.setName(acid.getName());
                    }
                    if (acid.getInci() != null) {
                        existingAcid.setInci(acid.getInci());
                    }

                    return existingAcid;
                }
            )
            .map(acidRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, acid.getId().toString())
        );
    }

    /**
     * {@code GET  /acids} : get all the acids.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of acids in body.
     */
    @GetMapping("/acids")
    public List<Acid> getAllAcids() {
        log.debug("REST request to get all Acids");
        return acidRepository.findAll();
    }

    /**
     * {@code GET  /acids/:id} : get the "id" acid.
     *
     * @param id the id of the acid to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the acid, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/acids/{id}")
    public ResponseEntity<Acid> getAcid(@PathVariable Long id) {
        log.debug("REST request to get Acid : {}", id);
        Optional<Acid> acid = acidRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(acid);
    }

    /**
     * {@code DELETE  /acids/:id} : delete the "id" acid.
     *
     * @param id the id of the acid to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/acids/{id}")
    public ResponseEntity<Void> deleteAcid(@PathVariable Long id) {
        log.debug("REST request to delete Acid : {}", id);
        acidRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
