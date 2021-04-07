package org.soaplab.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soaplab.domain.Fragrance;
import org.soaplab.repository.FragranceRepository;
import org.soaplab.web.rest.errors.BadRequestAlertException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.soaplab.domain.Fragrance}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FragranceResource {

    private final Logger log = LoggerFactory.getLogger(FragranceResource.class);

    private static final String ENTITY_NAME = "fragrance";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FragranceRepository fragranceRepository;

    public FragranceResource(FragranceRepository fragranceRepository) {
        this.fragranceRepository = fragranceRepository;
    }

    /**
     * {@code POST  /fragrances} : Create a new fragrance.
     *
     * @param fragrance the fragrance to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fragrance, or with status {@code 400 (Bad Request)} if the fragrance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fragrances")
    public ResponseEntity<Fragrance> createFragrance(@RequestBody Fragrance fragrance) throws URISyntaxException {
        log.debug("REST request to save Fragrance : {}", fragrance);
        if (fragrance.getId() != null) {
            throw new BadRequestAlertException("A new fragrance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Fragrance result = fragranceRepository.save(fragrance);
        return ResponseEntity
            .created(new URI("/api/fragrances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fragrances/:id} : Updates an existing fragrance.
     *
     * @param id the id of the fragrance to save.
     * @param fragrance the fragrance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fragrance,
     * or with status {@code 400 (Bad Request)} if the fragrance is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fragrance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fragrances/{id}")
    public ResponseEntity<Fragrance> updateFragrance(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Fragrance fragrance
    ) throws URISyntaxException {
        log.debug("REST request to update Fragrance : {}, {}", id, fragrance);
        if (fragrance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fragrance.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fragranceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Fragrance result = fragranceRepository.save(fragrance);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fragrance.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /fragrances/:id} : Partial updates given fields of an existing fragrance, field will ignore if it is null
     *
     * @param id the id of the fragrance to save.
     * @param fragrance the fragrance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fragrance,
     * or with status {@code 400 (Bad Request)} if the fragrance is not valid,
     * or with status {@code 404 (Not Found)} if the fragrance is not found,
     * or with status {@code 500 (Internal Server Error)} if the fragrance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/fragrances/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Fragrance> partialUpdateFragrance(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Fragrance fragrance
    ) throws URISyntaxException {
        log.debug("REST request to partial update Fragrance partially : {}, {}", id, fragrance);
        if (fragrance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fragrance.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fragranceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Fragrance> result = fragranceRepository
            .findById(fragrance.getId())
            .map(
                existingFragrance -> {
                    if (fragrance.getName() != null) {
                        existingFragrance.setName(fragrance.getName());
                    }
                    if (fragrance.getInci() != null) {
                        existingFragrance.setInci(fragrance.getInci());
                    }
                    if (fragrance.getTyp() != null) {
                        existingFragrance.setTyp(fragrance.getTyp());
                    }

                    return existingFragrance;
                }
            )
            .map(fragranceRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fragrance.getId().toString())
        );
    }

    /**
     * {@code GET  /fragrances} : get all the fragrances.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fragrances in body.
     */
    @GetMapping("/fragrances")
    public List<Fragrance> getAllFragrances() {
        log.debug("REST request to get all Fragrances");
        return fragranceRepository.findAll();
    }

    /**
     * {@code GET  /fragrances/:id} : get the "id" fragrance.
     *
     * @param id the id of the fragrance to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fragrance, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fragrances/{id}")
    public ResponseEntity<Fragrance> getFragrance(@PathVariable Long id) {
        log.debug("REST request to get Fragrance : {}", id);
        Optional<Fragrance> fragrance = fragranceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(fragrance);
    }

    /**
     * {@code DELETE  /fragrances/:id} : delete the "id" fragrance.
     *
     * @param id the id of the fragrance to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fragrances/{id}")
    public ResponseEntity<Void> deleteFragrance(@PathVariable Long id) {
        log.debug("REST request to delete Fragrance : {}", id);
        fragranceRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
