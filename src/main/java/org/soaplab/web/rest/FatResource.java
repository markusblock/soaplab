package org.soaplab.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soaplab.domain.Fat;
import org.soaplab.repository.FatRepository;
import org.soaplab.web.rest.errors.BadRequestAlertException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.soaplab.domain.Fat}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FatResource {

    private final Logger log = LoggerFactory.getLogger(FatResource.class);

    private static final String ENTITY_NAME = "fat";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FatRepository fatRepository;

    public FatResource(FatRepository fatRepository) {
        this.fatRepository = fatRepository;
    }

    /**
     * {@code POST  /fats} : Create a new fat.
     *
     * @param fat the fat to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fat, or with status {@code 400 (Bad Request)} if the fat has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fats")
    public ResponseEntity<Fat> createFat(@RequestBody Fat fat) throws URISyntaxException {
        log.debug("REST request to save Fat : {}", fat);
        if (fat.getId() != null) {
            throw new BadRequestAlertException("A new fat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Fat result = fatRepository.save(fat);
        return ResponseEntity
            .created(new URI("/api/fats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fats/:id} : Updates an existing fat.
     *
     * @param id the id of the fat to save.
     * @param fat the fat to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fat,
     * or with status {@code 400 (Bad Request)} if the fat is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fat couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fats/{id}")
    public ResponseEntity<Fat> updateFat(@PathVariable(value = "id", required = false) final Long id, @RequestBody Fat fat)
        throws URISyntaxException {
        log.debug("REST request to update Fat : {}, {}", id, fat);
        if (fat.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fat.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fatRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Fat result = fatRepository.save(fat);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fat.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /fats/:id} : Partial updates given fields of an existing fat, field will ignore if it is null
     *
     * @param id the id of the fat to save.
     * @param fat the fat to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fat,
     * or with status {@code 400 (Bad Request)} if the fat is not valid,
     * or with status {@code 404 (Not Found)} if the fat is not found,
     * or with status {@code 500 (Internal Server Error)} if the fat couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/fats/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Fat> partialUpdateFat(@PathVariable(value = "id", required = false) final Long id, @RequestBody Fat fat)
        throws URISyntaxException {
        log.debug("REST request to partial update Fat partially : {}, {}", id, fat);
        if (fat.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fat.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fatRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Fat> result = fatRepository
            .findById(fat.getId())
            .map(
                existingFat -> {
                    if (fat.getName() != null) {
                        existingFat.setName(fat.getName());
                    }
                    if (fat.getInci() != null) {
                        existingFat.setInci(fat.getInci());
                    }
                    if (fat.getSapNaoh() != null) {
                        existingFat.setSapNaoh(fat.getSapNaoh());
                    }
                    if (fat.getSapKoh() != null) {
                        existingFat.setSapKoh(fat.getSapKoh());
                    }
                    if (fat.getLauric() != null) {
                        existingFat.setLauric(fat.getLauric());
                    }
                    if (fat.getMyristic() != null) {
                        existingFat.setMyristic(fat.getMyristic());
                    }
                    if (fat.getPalmitic() != null) {
                        existingFat.setPalmitic(fat.getPalmitic());
                    }
                    if (fat.getStearic() != null) {
                        existingFat.setStearic(fat.getStearic());
                    }
                    if (fat.getRicinoleic() != null) {
                        existingFat.setRicinoleic(fat.getRicinoleic());
                    }
                    if (fat.getOleic() != null) {
                        existingFat.setOleic(fat.getOleic());
                    }
                    if (fat.getLinoleic() != null) {
                        existingFat.setLinoleic(fat.getLinoleic());
                    }
                    if (fat.getLinolenic() != null) {
                        existingFat.setLinolenic(fat.getLinolenic());
                    }
                    if (fat.getIodine() != null) {
                        existingFat.setIodine(fat.getIodine());
                    }
                    if (fat.getIns() != null) {
                        existingFat.setIns(fat.getIns());
                    }

                    return existingFat;
                }
            )
            .map(fatRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fat.getId().toString())
        );
    }

    /**
     * {@code GET  /fats} : get all the fats.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fats in body.
     */
    @GetMapping("/fats")
    public List<Fat> getAllFats() {
        log.debug("REST request to get all Fats");
        return fatRepository.findAll();
    }

    /**
     * {@code GET  /fats/:id} : get the "id" fat.
     *
     * @param id the id of the fat to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fat, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fats/{id}")
    public ResponseEntity<Fat> getFat(@PathVariable Long id) {
        log.debug("REST request to get Fat : {}", id);
        Optional<Fat> fat = fatRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(fat);
    }

    /**
     * {@code DELETE  /fats/:id} : delete the "id" fat.
     *
     * @param id the id of the fat to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fats/{id}")
    public ResponseEntity<Void> deleteFat(@PathVariable Long id) {
        log.debug("REST request to delete Fat : {}", id);
        fatRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
