package org.soaplab.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soaplab.domain.SoapReceipt;
import org.soaplab.repository.SoapReceiptRepository;
import org.soaplab.web.rest.errors.BadRequestAlertException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.soaplab.domain.SoapReceipt}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SoapReceiptResource {

    private final Logger log = LoggerFactory.getLogger(SoapReceiptResource.class);

    private static final String ENTITY_NAME = "soapReceipt";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SoapReceiptRepository soapReceiptRepository;

    public SoapReceiptResource(SoapReceiptRepository soapReceiptRepository) {
        this.soapReceiptRepository = soapReceiptRepository;
    }

    /**
     * {@code POST  /soap-receipts} : Create a new soapReceipt.
     *
     * @param soapReceipt the soapReceipt to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new soapReceipt, or with status {@code 400 (Bad Request)} if the soapReceipt has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/soap-receipts")
    public ResponseEntity<SoapReceipt> createSoapReceipt(@RequestBody SoapReceipt soapReceipt) throws URISyntaxException {
        log.debug("REST request to save SoapReceipt : {}", soapReceipt);
        if (soapReceipt.getId() != null) {
            throw new BadRequestAlertException("A new soapReceipt cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SoapReceipt result = soapReceiptRepository.save(soapReceipt);
        return ResponseEntity
            .created(new URI("/api/soap-receipts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /soap-receipts/:id} : Updates an existing soapReceipt.
     *
     * @param id the id of the soapReceipt to save.
     * @param soapReceipt the soapReceipt to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated soapReceipt,
     * or with status {@code 400 (Bad Request)} if the soapReceipt is not valid,
     * or with status {@code 500 (Internal Server Error)} if the soapReceipt couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/soap-receipts/{id}")
    public ResponseEntity<SoapReceipt> updateSoapReceipt(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SoapReceipt soapReceipt
    ) throws URISyntaxException {
        log.debug("REST request to update SoapReceipt : {}, {}", id, soapReceipt);
        if (soapReceipt.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, soapReceipt.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!soapReceiptRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SoapReceipt result = soapReceiptRepository.save(soapReceipt);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, soapReceipt.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /soap-receipts/:id} : Partial updates given fields of an existing soapReceipt, field will ignore if it is null
     *
     * @param id the id of the soapReceipt to save.
     * @param soapReceipt the soapReceipt to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated soapReceipt,
     * or with status {@code 400 (Bad Request)} if the soapReceipt is not valid,
     * or with status {@code 404 (Not Found)} if the soapReceipt is not found,
     * or with status {@code 500 (Internal Server Error)} if the soapReceipt couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/soap-receipts/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SoapReceipt> partialUpdateSoapReceipt(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SoapReceipt soapReceipt
    ) throws URISyntaxException {
        log.debug("REST request to partial update SoapReceipt partially : {}, {}", id, soapReceipt);
        if (soapReceipt.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, soapReceipt.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!soapReceiptRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SoapReceipt> result = soapReceiptRepository
            .findById(soapReceipt.getId())
            .map(
                existingSoapReceipt -> {
                    if (soapReceipt.getName() != null) {
                        existingSoapReceipt.setName(soapReceipt.getName());
                    }
                    if (soapReceipt.getLiquid() != null) {
                        existingSoapReceipt.setLiquid(soapReceipt.getLiquid());
                    }
                    if (soapReceipt.getSuperfat() != null) {
                        existingSoapReceipt.setSuperfat(soapReceipt.getSuperfat());
                    }

                    return existingSoapReceipt;
                }
            )
            .map(soapReceiptRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, soapReceipt.getId().toString())
        );
    }

    /**
     * {@code GET  /soap-receipts} : get all the soapReceipts.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of soapReceipts in body.
     */
    @GetMapping("/soap-receipts")
    public List<SoapReceipt> getAllSoapReceipts(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all SoapReceipts");
        return soapReceiptRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /soap-receipts/:id} : get the "id" soapReceipt.
     *
     * @param id the id of the soapReceipt to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the soapReceipt, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/soap-receipts/{id}")
    public ResponseEntity<SoapReceipt> getSoapReceipt(@PathVariable Long id) {
        log.debug("REST request to get SoapReceipt : {}", id);
        Optional<SoapReceipt> soapReceipt = soapReceiptRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(soapReceipt);
    }

    /**
     * {@code DELETE  /soap-receipts/:id} : delete the "id" soapReceipt.
     *
     * @param id the id of the soapReceipt to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/soap-receipts/{id}")
    public ResponseEntity<Void> deleteSoapReceipt(@PathVariable Long id) {
        log.debug("REST request to delete SoapReceipt : {}", id);
        soapReceiptRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
