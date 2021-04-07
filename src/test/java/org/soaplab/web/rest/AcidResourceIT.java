package org.soaplab.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.soaplab.IntegrationTest;
import org.soaplab.domain.Acid;
import org.soaplab.repository.AcidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AcidResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AcidResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_INCI = "AAAAAAAAAA";
    private static final String UPDATED_INCI = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/acids";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AcidRepository acidRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAcidMockMvc;

    private Acid acid;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Acid createEntity(EntityManager em) {
        Acid acid = new Acid().name(DEFAULT_NAME).inci(DEFAULT_INCI);
        return acid;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Acid createUpdatedEntity(EntityManager em) {
        Acid acid = new Acid().name(UPDATED_NAME).inci(UPDATED_INCI);
        return acid;
    }

    @BeforeEach
    public void initTest() {
        acid = createEntity(em);
    }

    @Test
    @Transactional
    void createAcid() throws Exception {
        int databaseSizeBeforeCreate = acidRepository.findAll().size();
        // Create the Acid
        restAcidMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(acid)))
            .andExpect(status().isCreated());

        // Validate the Acid in the database
        List<Acid> acidList = acidRepository.findAll();
        assertThat(acidList).hasSize(databaseSizeBeforeCreate + 1);
        Acid testAcid = acidList.get(acidList.size() - 1);
        assertThat(testAcid.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAcid.getInci()).isEqualTo(DEFAULT_INCI);
    }

    @Test
    @Transactional
    void createAcidWithExistingId() throws Exception {
        // Create the Acid with an existing ID
        acid.setId(1L);

        int databaseSizeBeforeCreate = acidRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAcidMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(acid)))
            .andExpect(status().isBadRequest());

        // Validate the Acid in the database
        List<Acid> acidList = acidRepository.findAll();
        assertThat(acidList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAcids() throws Exception {
        // Initialize the database
        acidRepository.saveAndFlush(acid);

        // Get all the acidList
        restAcidMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(acid.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].inci").value(hasItem(DEFAULT_INCI)));
    }

    @Test
    @Transactional
    void getAcid() throws Exception {
        // Initialize the database
        acidRepository.saveAndFlush(acid);

        // Get the acid
        restAcidMockMvc
            .perform(get(ENTITY_API_URL_ID, acid.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(acid.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.inci").value(DEFAULT_INCI));
    }

    @Test
    @Transactional
    void getNonExistingAcid() throws Exception {
        // Get the acid
        restAcidMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAcid() throws Exception {
        // Initialize the database
        acidRepository.saveAndFlush(acid);

        int databaseSizeBeforeUpdate = acidRepository.findAll().size();

        // Update the acid
        Acid updatedAcid = acidRepository.findById(acid.getId()).get();
        // Disconnect from session so that the updates on updatedAcid are not directly saved in db
        em.detach(updatedAcid);
        updatedAcid.name(UPDATED_NAME).inci(UPDATED_INCI);

        restAcidMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAcid.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAcid))
            )
            .andExpect(status().isOk());

        // Validate the Acid in the database
        List<Acid> acidList = acidRepository.findAll();
        assertThat(acidList).hasSize(databaseSizeBeforeUpdate);
        Acid testAcid = acidList.get(acidList.size() - 1);
        assertThat(testAcid.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAcid.getInci()).isEqualTo(UPDATED_INCI);
    }

    @Test
    @Transactional
    void putNonExistingAcid() throws Exception {
        int databaseSizeBeforeUpdate = acidRepository.findAll().size();
        acid.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAcidMockMvc
            .perform(
                put(ENTITY_API_URL_ID, acid.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(acid))
            )
            .andExpect(status().isBadRequest());

        // Validate the Acid in the database
        List<Acid> acidList = acidRepository.findAll();
        assertThat(acidList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAcid() throws Exception {
        int databaseSizeBeforeUpdate = acidRepository.findAll().size();
        acid.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcidMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(acid))
            )
            .andExpect(status().isBadRequest());

        // Validate the Acid in the database
        List<Acid> acidList = acidRepository.findAll();
        assertThat(acidList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAcid() throws Exception {
        int databaseSizeBeforeUpdate = acidRepository.findAll().size();
        acid.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcidMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(acid)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Acid in the database
        List<Acid> acidList = acidRepository.findAll();
        assertThat(acidList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAcidWithPatch() throws Exception {
        // Initialize the database
        acidRepository.saveAndFlush(acid);

        int databaseSizeBeforeUpdate = acidRepository.findAll().size();

        // Update the acid using partial update
        Acid partialUpdatedAcid = new Acid();
        partialUpdatedAcid.setId(acid.getId());

        partialUpdatedAcid.inci(UPDATED_INCI);

        restAcidMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAcid.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAcid))
            )
            .andExpect(status().isOk());

        // Validate the Acid in the database
        List<Acid> acidList = acidRepository.findAll();
        assertThat(acidList).hasSize(databaseSizeBeforeUpdate);
        Acid testAcid = acidList.get(acidList.size() - 1);
        assertThat(testAcid.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAcid.getInci()).isEqualTo(UPDATED_INCI);
    }

    @Test
    @Transactional
    void fullUpdateAcidWithPatch() throws Exception {
        // Initialize the database
        acidRepository.saveAndFlush(acid);

        int databaseSizeBeforeUpdate = acidRepository.findAll().size();

        // Update the acid using partial update
        Acid partialUpdatedAcid = new Acid();
        partialUpdatedAcid.setId(acid.getId());

        partialUpdatedAcid.name(UPDATED_NAME).inci(UPDATED_INCI);

        restAcidMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAcid.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAcid))
            )
            .andExpect(status().isOk());

        // Validate the Acid in the database
        List<Acid> acidList = acidRepository.findAll();
        assertThat(acidList).hasSize(databaseSizeBeforeUpdate);
        Acid testAcid = acidList.get(acidList.size() - 1);
        assertThat(testAcid.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAcid.getInci()).isEqualTo(UPDATED_INCI);
    }

    @Test
    @Transactional
    void patchNonExistingAcid() throws Exception {
        int databaseSizeBeforeUpdate = acidRepository.findAll().size();
        acid.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAcidMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, acid.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(acid))
            )
            .andExpect(status().isBadRequest());

        // Validate the Acid in the database
        List<Acid> acidList = acidRepository.findAll();
        assertThat(acidList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAcid() throws Exception {
        int databaseSizeBeforeUpdate = acidRepository.findAll().size();
        acid.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcidMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(acid))
            )
            .andExpect(status().isBadRequest());

        // Validate the Acid in the database
        List<Acid> acidList = acidRepository.findAll();
        assertThat(acidList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAcid() throws Exception {
        int databaseSizeBeforeUpdate = acidRepository.findAll().size();
        acid.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcidMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(acid)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Acid in the database
        List<Acid> acidList = acidRepository.findAll();
        assertThat(acidList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAcid() throws Exception {
        // Initialize the database
        acidRepository.saveAndFlush(acid);

        int databaseSizeBeforeDelete = acidRepository.findAll().size();

        // Delete the acid
        restAcidMockMvc
            .perform(delete(ENTITY_API_URL_ID, acid.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Acid> acidList = acidRepository.findAll();
        assertThat(acidList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
