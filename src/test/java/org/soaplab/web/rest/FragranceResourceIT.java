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
import org.soaplab.domain.Fragrance;
import org.soaplab.domain.enumeration.FragranceType;
import org.soaplab.repository.FragranceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FragranceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FragranceResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_INCI = "AAAAAAAAAA";
    private static final String UPDATED_INCI = "BBBBBBBBBB";

    private static final FragranceType DEFAULT_TYP = FragranceType.VOLATILE_OIL;
    private static final FragranceType UPDATED_TYP = FragranceType.PERFUME_OIL;

    private static final String ENTITY_API_URL = "/api/fragrances";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FragranceRepository fragranceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFragranceMockMvc;

    private Fragrance fragrance;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fragrance createEntity(EntityManager em) {
        Fragrance fragrance = new Fragrance().name(DEFAULT_NAME).inci(DEFAULT_INCI).typ(DEFAULT_TYP);
        return fragrance;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fragrance createUpdatedEntity(EntityManager em) {
        Fragrance fragrance = new Fragrance().name(UPDATED_NAME).inci(UPDATED_INCI).typ(UPDATED_TYP);
        return fragrance;
    }

    @BeforeEach
    public void initTest() {
        fragrance = createEntity(em);
    }

    @Test
    @Transactional
    void createFragrance() throws Exception {
        int databaseSizeBeforeCreate = fragranceRepository.findAll().size();
        // Create the Fragrance
        restFragranceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fragrance)))
            .andExpect(status().isCreated());

        // Validate the Fragrance in the database
        List<Fragrance> fragranceList = fragranceRepository.findAll();
        assertThat(fragranceList).hasSize(databaseSizeBeforeCreate + 1);
        Fragrance testFragrance = fragranceList.get(fragranceList.size() - 1);
        assertThat(testFragrance.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFragrance.getInci()).isEqualTo(DEFAULT_INCI);
        assertThat(testFragrance.getTyp()).isEqualTo(DEFAULT_TYP);
    }

    @Test
    @Transactional
    void createFragranceWithExistingId() throws Exception {
        // Create the Fragrance with an existing ID
        fragrance.setId(1L);

        int databaseSizeBeforeCreate = fragranceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFragranceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fragrance)))
            .andExpect(status().isBadRequest());

        // Validate the Fragrance in the database
        List<Fragrance> fragranceList = fragranceRepository.findAll();
        assertThat(fragranceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFragrances() throws Exception {
        // Initialize the database
        fragranceRepository.saveAndFlush(fragrance);

        // Get all the fragranceList
        restFragranceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fragrance.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].inci").value(hasItem(DEFAULT_INCI)))
            .andExpect(jsonPath("$.[*].typ").value(hasItem(DEFAULT_TYP.toString())));
    }

    @Test
    @Transactional
    void getFragrance() throws Exception {
        // Initialize the database
        fragranceRepository.saveAndFlush(fragrance);

        // Get the fragrance
        restFragranceMockMvc
            .perform(get(ENTITY_API_URL_ID, fragrance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fragrance.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.inci").value(DEFAULT_INCI))
            .andExpect(jsonPath("$.typ").value(DEFAULT_TYP.toString()));
    }

    @Test
    @Transactional
    void getNonExistingFragrance() throws Exception {
        // Get the fragrance
        restFragranceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFragrance() throws Exception {
        // Initialize the database
        fragranceRepository.saveAndFlush(fragrance);

        int databaseSizeBeforeUpdate = fragranceRepository.findAll().size();

        // Update the fragrance
        Fragrance updatedFragrance = fragranceRepository.findById(fragrance.getId()).get();
        // Disconnect from session so that the updates on updatedFragrance are not directly saved in db
        em.detach(updatedFragrance);
        updatedFragrance.name(UPDATED_NAME).inci(UPDATED_INCI).typ(UPDATED_TYP);

        restFragranceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFragrance.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFragrance))
            )
            .andExpect(status().isOk());

        // Validate the Fragrance in the database
        List<Fragrance> fragranceList = fragranceRepository.findAll();
        assertThat(fragranceList).hasSize(databaseSizeBeforeUpdate);
        Fragrance testFragrance = fragranceList.get(fragranceList.size() - 1);
        assertThat(testFragrance.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFragrance.getInci()).isEqualTo(UPDATED_INCI);
        assertThat(testFragrance.getTyp()).isEqualTo(UPDATED_TYP);
    }

    @Test
    @Transactional
    void putNonExistingFragrance() throws Exception {
        int databaseSizeBeforeUpdate = fragranceRepository.findAll().size();
        fragrance.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFragranceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fragrance.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fragrance))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fragrance in the database
        List<Fragrance> fragranceList = fragranceRepository.findAll();
        assertThat(fragranceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFragrance() throws Exception {
        int databaseSizeBeforeUpdate = fragranceRepository.findAll().size();
        fragrance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFragranceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fragrance))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fragrance in the database
        List<Fragrance> fragranceList = fragranceRepository.findAll();
        assertThat(fragranceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFragrance() throws Exception {
        int databaseSizeBeforeUpdate = fragranceRepository.findAll().size();
        fragrance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFragranceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fragrance)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fragrance in the database
        List<Fragrance> fragranceList = fragranceRepository.findAll();
        assertThat(fragranceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFragranceWithPatch() throws Exception {
        // Initialize the database
        fragranceRepository.saveAndFlush(fragrance);

        int databaseSizeBeforeUpdate = fragranceRepository.findAll().size();

        // Update the fragrance using partial update
        Fragrance partialUpdatedFragrance = new Fragrance();
        partialUpdatedFragrance.setId(fragrance.getId());

        partialUpdatedFragrance.name(UPDATED_NAME);

        restFragranceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFragrance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFragrance))
            )
            .andExpect(status().isOk());

        // Validate the Fragrance in the database
        List<Fragrance> fragranceList = fragranceRepository.findAll();
        assertThat(fragranceList).hasSize(databaseSizeBeforeUpdate);
        Fragrance testFragrance = fragranceList.get(fragranceList.size() - 1);
        assertThat(testFragrance.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFragrance.getInci()).isEqualTo(DEFAULT_INCI);
        assertThat(testFragrance.getTyp()).isEqualTo(DEFAULT_TYP);
    }

    @Test
    @Transactional
    void fullUpdateFragranceWithPatch() throws Exception {
        // Initialize the database
        fragranceRepository.saveAndFlush(fragrance);

        int databaseSizeBeforeUpdate = fragranceRepository.findAll().size();

        // Update the fragrance using partial update
        Fragrance partialUpdatedFragrance = new Fragrance();
        partialUpdatedFragrance.setId(fragrance.getId());

        partialUpdatedFragrance.name(UPDATED_NAME).inci(UPDATED_INCI).typ(UPDATED_TYP);

        restFragranceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFragrance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFragrance))
            )
            .andExpect(status().isOk());

        // Validate the Fragrance in the database
        List<Fragrance> fragranceList = fragranceRepository.findAll();
        assertThat(fragranceList).hasSize(databaseSizeBeforeUpdate);
        Fragrance testFragrance = fragranceList.get(fragranceList.size() - 1);
        assertThat(testFragrance.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFragrance.getInci()).isEqualTo(UPDATED_INCI);
        assertThat(testFragrance.getTyp()).isEqualTo(UPDATED_TYP);
    }

    @Test
    @Transactional
    void patchNonExistingFragrance() throws Exception {
        int databaseSizeBeforeUpdate = fragranceRepository.findAll().size();
        fragrance.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFragranceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fragrance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fragrance))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fragrance in the database
        List<Fragrance> fragranceList = fragranceRepository.findAll();
        assertThat(fragranceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFragrance() throws Exception {
        int databaseSizeBeforeUpdate = fragranceRepository.findAll().size();
        fragrance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFragranceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fragrance))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fragrance in the database
        List<Fragrance> fragranceList = fragranceRepository.findAll();
        assertThat(fragranceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFragrance() throws Exception {
        int databaseSizeBeforeUpdate = fragranceRepository.findAll().size();
        fragrance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFragranceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(fragrance))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fragrance in the database
        List<Fragrance> fragranceList = fragranceRepository.findAll();
        assertThat(fragranceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFragrance() throws Exception {
        // Initialize the database
        fragranceRepository.saveAndFlush(fragrance);

        int databaseSizeBeforeDelete = fragranceRepository.findAll().size();

        // Delete the fragrance
        restFragranceMockMvc
            .perform(delete(ENTITY_API_URL_ID, fragrance.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Fragrance> fragranceList = fragranceRepository.findAll();
        assertThat(fragranceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
