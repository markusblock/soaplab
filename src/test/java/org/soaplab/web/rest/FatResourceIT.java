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
import org.soaplab.domain.Fat;
import org.soaplab.repository.FatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FatResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FatResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_INCI = "AAAAAAAAAA";
    private static final String UPDATED_INCI = "BBBBBBBBBB";

    private static final Double DEFAULT_SAP_NAOH = 1D;
    private static final Double UPDATED_SAP_NAOH = 2D;

    private static final Double DEFAULT_SAP_KOH = 1D;
    private static final Double UPDATED_SAP_KOH = 2D;

    private static final Integer DEFAULT_LAURIC = 1;
    private static final Integer UPDATED_LAURIC = 2;

    private static final Integer DEFAULT_MYRISTIC = 1;
    private static final Integer UPDATED_MYRISTIC = 2;

    private static final Integer DEFAULT_PALMITIC = 1;
    private static final Integer UPDATED_PALMITIC = 2;

    private static final Integer DEFAULT_STEARIC = 1;
    private static final Integer UPDATED_STEARIC = 2;

    private static final Integer DEFAULT_RICINOLEIC = 1;
    private static final Integer UPDATED_RICINOLEIC = 2;

    private static final Integer DEFAULT_OLEIC = 1;
    private static final Integer UPDATED_OLEIC = 2;

    private static final Integer DEFAULT_LINOLEIC = 1;
    private static final Integer UPDATED_LINOLEIC = 2;

    private static final Integer DEFAULT_LINOLENIC = 1;
    private static final Integer UPDATED_LINOLENIC = 2;

    private static final Integer DEFAULT_IODINE = 1;
    private static final Integer UPDATED_IODINE = 2;

    private static final Integer DEFAULT_INS = 1;
    private static final Integer UPDATED_INS = 2;

    private static final String ENTITY_API_URL = "/api/fats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FatRepository fatRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFatMockMvc;

    private Fat fat;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fat createEntity(EntityManager em) {
        Fat fat = new Fat()
            .name(DEFAULT_NAME)
            .inci(DEFAULT_INCI)
            .sapNaoh(DEFAULT_SAP_NAOH)
            .sapKoh(DEFAULT_SAP_KOH)
            .lauric(DEFAULT_LAURIC)
            .myristic(DEFAULT_MYRISTIC)
            .palmitic(DEFAULT_PALMITIC)
            .stearic(DEFAULT_STEARIC)
            .ricinoleic(DEFAULT_RICINOLEIC)
            .oleic(DEFAULT_OLEIC)
            .linoleic(DEFAULT_LINOLEIC)
            .linolenic(DEFAULT_LINOLENIC)
            .iodine(DEFAULT_IODINE)
            .ins(DEFAULT_INS);
        return fat;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fat createUpdatedEntity(EntityManager em) {
        Fat fat = new Fat()
            .name(UPDATED_NAME)
            .inci(UPDATED_INCI)
            .sapNaoh(UPDATED_SAP_NAOH)
            .sapKoh(UPDATED_SAP_KOH)
            .lauric(UPDATED_LAURIC)
            .myristic(UPDATED_MYRISTIC)
            .palmitic(UPDATED_PALMITIC)
            .stearic(UPDATED_STEARIC)
            .ricinoleic(UPDATED_RICINOLEIC)
            .oleic(UPDATED_OLEIC)
            .linoleic(UPDATED_LINOLEIC)
            .linolenic(UPDATED_LINOLENIC)
            .iodine(UPDATED_IODINE)
            .ins(UPDATED_INS);
        return fat;
    }

    @BeforeEach
    public void initTest() {
        fat = createEntity(em);
    }

    @Test
    @Transactional
    void createFat() throws Exception {
        int databaseSizeBeforeCreate = fatRepository.findAll().size();
        // Create the Fat
        restFatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fat)))
            .andExpect(status().isCreated());

        // Validate the Fat in the database
        List<Fat> fatList = fatRepository.findAll();
        assertThat(fatList).hasSize(databaseSizeBeforeCreate + 1);
        Fat testFat = fatList.get(fatList.size() - 1);
        assertThat(testFat.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFat.getInci()).isEqualTo(DEFAULT_INCI);
        assertThat(testFat.getSapNaoh()).isEqualTo(DEFAULT_SAP_NAOH);
        assertThat(testFat.getSapKoh()).isEqualTo(DEFAULT_SAP_KOH);
        assertThat(testFat.getLauric()).isEqualTo(DEFAULT_LAURIC);
        assertThat(testFat.getMyristic()).isEqualTo(DEFAULT_MYRISTIC);
        assertThat(testFat.getPalmitic()).isEqualTo(DEFAULT_PALMITIC);
        assertThat(testFat.getStearic()).isEqualTo(DEFAULT_STEARIC);
        assertThat(testFat.getRicinoleic()).isEqualTo(DEFAULT_RICINOLEIC);
        assertThat(testFat.getOleic()).isEqualTo(DEFAULT_OLEIC);
        assertThat(testFat.getLinoleic()).isEqualTo(DEFAULT_LINOLEIC);
        assertThat(testFat.getLinolenic()).isEqualTo(DEFAULT_LINOLENIC);
        assertThat(testFat.getIodine()).isEqualTo(DEFAULT_IODINE);
        assertThat(testFat.getIns()).isEqualTo(DEFAULT_INS);
    }

    @Test
    @Transactional
    void createFatWithExistingId() throws Exception {
        // Create the Fat with an existing ID
        fat.setId(1L);

        int databaseSizeBeforeCreate = fatRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fat)))
            .andExpect(status().isBadRequest());

        // Validate the Fat in the database
        List<Fat> fatList = fatRepository.findAll();
        assertThat(fatList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFats() throws Exception {
        // Initialize the database
        fatRepository.saveAndFlush(fat);

        // Get all the fatList
        restFatMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fat.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].inci").value(hasItem(DEFAULT_INCI)))
            .andExpect(jsonPath("$.[*].sapNaoh").value(hasItem(DEFAULT_SAP_NAOH.doubleValue())))
            .andExpect(jsonPath("$.[*].sapKoh").value(hasItem(DEFAULT_SAP_KOH.doubleValue())))
            .andExpect(jsonPath("$.[*].lauric").value(hasItem(DEFAULT_LAURIC)))
            .andExpect(jsonPath("$.[*].myristic").value(hasItem(DEFAULT_MYRISTIC)))
            .andExpect(jsonPath("$.[*].palmitic").value(hasItem(DEFAULT_PALMITIC)))
            .andExpect(jsonPath("$.[*].stearic").value(hasItem(DEFAULT_STEARIC)))
            .andExpect(jsonPath("$.[*].ricinoleic").value(hasItem(DEFAULT_RICINOLEIC)))
            .andExpect(jsonPath("$.[*].oleic").value(hasItem(DEFAULT_OLEIC)))
            .andExpect(jsonPath("$.[*].linoleic").value(hasItem(DEFAULT_LINOLEIC)))
            .andExpect(jsonPath("$.[*].linolenic").value(hasItem(DEFAULT_LINOLENIC)))
            .andExpect(jsonPath("$.[*].iodine").value(hasItem(DEFAULT_IODINE)))
            .andExpect(jsonPath("$.[*].ins").value(hasItem(DEFAULT_INS)));
    }

    @Test
    @Transactional
    void getFat() throws Exception {
        // Initialize the database
        fatRepository.saveAndFlush(fat);

        // Get the fat
        restFatMockMvc
            .perform(get(ENTITY_API_URL_ID, fat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fat.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.inci").value(DEFAULT_INCI))
            .andExpect(jsonPath("$.sapNaoh").value(DEFAULT_SAP_NAOH.doubleValue()))
            .andExpect(jsonPath("$.sapKoh").value(DEFAULT_SAP_KOH.doubleValue()))
            .andExpect(jsonPath("$.lauric").value(DEFAULT_LAURIC))
            .andExpect(jsonPath("$.myristic").value(DEFAULT_MYRISTIC))
            .andExpect(jsonPath("$.palmitic").value(DEFAULT_PALMITIC))
            .andExpect(jsonPath("$.stearic").value(DEFAULT_STEARIC))
            .andExpect(jsonPath("$.ricinoleic").value(DEFAULT_RICINOLEIC))
            .andExpect(jsonPath("$.oleic").value(DEFAULT_OLEIC))
            .andExpect(jsonPath("$.linoleic").value(DEFAULT_LINOLEIC))
            .andExpect(jsonPath("$.linolenic").value(DEFAULT_LINOLENIC))
            .andExpect(jsonPath("$.iodine").value(DEFAULT_IODINE))
            .andExpect(jsonPath("$.ins").value(DEFAULT_INS));
    }

    @Test
    @Transactional
    void getNonExistingFat() throws Exception {
        // Get the fat
        restFatMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFat() throws Exception {
        // Initialize the database
        fatRepository.saveAndFlush(fat);

        int databaseSizeBeforeUpdate = fatRepository.findAll().size();

        // Update the fat
        Fat updatedFat = fatRepository.findById(fat.getId()).get();
        // Disconnect from session so that the updates on updatedFat are not directly saved in db
        em.detach(updatedFat);
        updatedFat
            .name(UPDATED_NAME)
            .inci(UPDATED_INCI)
            .sapNaoh(UPDATED_SAP_NAOH)
            .sapKoh(UPDATED_SAP_KOH)
            .lauric(UPDATED_LAURIC)
            .myristic(UPDATED_MYRISTIC)
            .palmitic(UPDATED_PALMITIC)
            .stearic(UPDATED_STEARIC)
            .ricinoleic(UPDATED_RICINOLEIC)
            .oleic(UPDATED_OLEIC)
            .linoleic(UPDATED_LINOLEIC)
            .linolenic(UPDATED_LINOLENIC)
            .iodine(UPDATED_IODINE)
            .ins(UPDATED_INS);

        restFatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFat.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFat))
            )
            .andExpect(status().isOk());

        // Validate the Fat in the database
        List<Fat> fatList = fatRepository.findAll();
        assertThat(fatList).hasSize(databaseSizeBeforeUpdate);
        Fat testFat = fatList.get(fatList.size() - 1);
        assertThat(testFat.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFat.getInci()).isEqualTo(UPDATED_INCI);
        assertThat(testFat.getSapNaoh()).isEqualTo(UPDATED_SAP_NAOH);
        assertThat(testFat.getSapKoh()).isEqualTo(UPDATED_SAP_KOH);
        assertThat(testFat.getLauric()).isEqualTo(UPDATED_LAURIC);
        assertThat(testFat.getMyristic()).isEqualTo(UPDATED_MYRISTIC);
        assertThat(testFat.getPalmitic()).isEqualTo(UPDATED_PALMITIC);
        assertThat(testFat.getStearic()).isEqualTo(UPDATED_STEARIC);
        assertThat(testFat.getRicinoleic()).isEqualTo(UPDATED_RICINOLEIC);
        assertThat(testFat.getOleic()).isEqualTo(UPDATED_OLEIC);
        assertThat(testFat.getLinoleic()).isEqualTo(UPDATED_LINOLEIC);
        assertThat(testFat.getLinolenic()).isEqualTo(UPDATED_LINOLENIC);
        assertThat(testFat.getIodine()).isEqualTo(UPDATED_IODINE);
        assertThat(testFat.getIns()).isEqualTo(UPDATED_INS);
    }

    @Test
    @Transactional
    void putNonExistingFat() throws Exception {
        int databaseSizeBeforeUpdate = fatRepository.findAll().size();
        fat.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fat.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fat in the database
        List<Fat> fatList = fatRepository.findAll();
        assertThat(fatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFat() throws Exception {
        int databaseSizeBeforeUpdate = fatRepository.findAll().size();
        fat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fat in the database
        List<Fat> fatList = fatRepository.findAll();
        assertThat(fatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFat() throws Exception {
        int databaseSizeBeforeUpdate = fatRepository.findAll().size();
        fat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFatMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fat)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fat in the database
        List<Fat> fatList = fatRepository.findAll();
        assertThat(fatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFatWithPatch() throws Exception {
        // Initialize the database
        fatRepository.saveAndFlush(fat);

        int databaseSizeBeforeUpdate = fatRepository.findAll().size();

        // Update the fat using partial update
        Fat partialUpdatedFat = new Fat();
        partialUpdatedFat.setId(fat.getId());

        partialUpdatedFat.name(UPDATED_NAME).ricinoleic(UPDATED_RICINOLEIC).linolenic(UPDATED_LINOLENIC);

        restFatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFat))
            )
            .andExpect(status().isOk());

        // Validate the Fat in the database
        List<Fat> fatList = fatRepository.findAll();
        assertThat(fatList).hasSize(databaseSizeBeforeUpdate);
        Fat testFat = fatList.get(fatList.size() - 1);
        assertThat(testFat.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFat.getInci()).isEqualTo(DEFAULT_INCI);
        assertThat(testFat.getSapNaoh()).isEqualTo(DEFAULT_SAP_NAOH);
        assertThat(testFat.getSapKoh()).isEqualTo(DEFAULT_SAP_KOH);
        assertThat(testFat.getLauric()).isEqualTo(DEFAULT_LAURIC);
        assertThat(testFat.getMyristic()).isEqualTo(DEFAULT_MYRISTIC);
        assertThat(testFat.getPalmitic()).isEqualTo(DEFAULT_PALMITIC);
        assertThat(testFat.getStearic()).isEqualTo(DEFAULT_STEARIC);
        assertThat(testFat.getRicinoleic()).isEqualTo(UPDATED_RICINOLEIC);
        assertThat(testFat.getOleic()).isEqualTo(DEFAULT_OLEIC);
        assertThat(testFat.getLinoleic()).isEqualTo(DEFAULT_LINOLEIC);
        assertThat(testFat.getLinolenic()).isEqualTo(UPDATED_LINOLENIC);
        assertThat(testFat.getIodine()).isEqualTo(DEFAULT_IODINE);
        assertThat(testFat.getIns()).isEqualTo(DEFAULT_INS);
    }

    @Test
    @Transactional
    void fullUpdateFatWithPatch() throws Exception {
        // Initialize the database
        fatRepository.saveAndFlush(fat);

        int databaseSizeBeforeUpdate = fatRepository.findAll().size();

        // Update the fat using partial update
        Fat partialUpdatedFat = new Fat();
        partialUpdatedFat.setId(fat.getId());

        partialUpdatedFat
            .name(UPDATED_NAME)
            .inci(UPDATED_INCI)
            .sapNaoh(UPDATED_SAP_NAOH)
            .sapKoh(UPDATED_SAP_KOH)
            .lauric(UPDATED_LAURIC)
            .myristic(UPDATED_MYRISTIC)
            .palmitic(UPDATED_PALMITIC)
            .stearic(UPDATED_STEARIC)
            .ricinoleic(UPDATED_RICINOLEIC)
            .oleic(UPDATED_OLEIC)
            .linoleic(UPDATED_LINOLEIC)
            .linolenic(UPDATED_LINOLENIC)
            .iodine(UPDATED_IODINE)
            .ins(UPDATED_INS);

        restFatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFat))
            )
            .andExpect(status().isOk());

        // Validate the Fat in the database
        List<Fat> fatList = fatRepository.findAll();
        assertThat(fatList).hasSize(databaseSizeBeforeUpdate);
        Fat testFat = fatList.get(fatList.size() - 1);
        assertThat(testFat.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFat.getInci()).isEqualTo(UPDATED_INCI);
        assertThat(testFat.getSapNaoh()).isEqualTo(UPDATED_SAP_NAOH);
        assertThat(testFat.getSapKoh()).isEqualTo(UPDATED_SAP_KOH);
        assertThat(testFat.getLauric()).isEqualTo(UPDATED_LAURIC);
        assertThat(testFat.getMyristic()).isEqualTo(UPDATED_MYRISTIC);
        assertThat(testFat.getPalmitic()).isEqualTo(UPDATED_PALMITIC);
        assertThat(testFat.getStearic()).isEqualTo(UPDATED_STEARIC);
        assertThat(testFat.getRicinoleic()).isEqualTo(UPDATED_RICINOLEIC);
        assertThat(testFat.getOleic()).isEqualTo(UPDATED_OLEIC);
        assertThat(testFat.getLinoleic()).isEqualTo(UPDATED_LINOLEIC);
        assertThat(testFat.getLinolenic()).isEqualTo(UPDATED_LINOLENIC);
        assertThat(testFat.getIodine()).isEqualTo(UPDATED_IODINE);
        assertThat(testFat.getIns()).isEqualTo(UPDATED_INS);
    }

    @Test
    @Transactional
    void patchNonExistingFat() throws Exception {
        int databaseSizeBeforeUpdate = fatRepository.findAll().size();
        fat.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fat in the database
        List<Fat> fatList = fatRepository.findAll();
        assertThat(fatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFat() throws Exception {
        int databaseSizeBeforeUpdate = fatRepository.findAll().size();
        fat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fat in the database
        List<Fat> fatList = fatRepository.findAll();
        assertThat(fatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFat() throws Exception {
        int databaseSizeBeforeUpdate = fatRepository.findAll().size();
        fat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFatMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(fat)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fat in the database
        List<Fat> fatList = fatRepository.findAll();
        assertThat(fatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFat() throws Exception {
        // Initialize the database
        fatRepository.saveAndFlush(fat);

        int databaseSizeBeforeDelete = fatRepository.findAll().size();

        // Delete the fat
        restFatMockMvc.perform(delete(ENTITY_API_URL_ID, fat.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Fat> fatList = fatRepository.findAll();
        assertThat(fatList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
