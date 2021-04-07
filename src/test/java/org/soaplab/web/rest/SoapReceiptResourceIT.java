package org.soaplab.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.soaplab.IntegrationTest;
import org.soaplab.domain.SoapReceipt;
import org.soaplab.repository.SoapReceiptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SoapReceiptResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SoapReceiptResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_LIQUID = 1;
    private static final Integer UPDATED_LIQUID = 2;

    private static final Integer DEFAULT_SUPERFAT = 1;
    private static final Integer UPDATED_SUPERFAT = 2;

    private static final String ENTITY_API_URL = "/api/soap-receipts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SoapReceiptRepository soapReceiptRepository;

    @Mock
    private SoapReceiptRepository soapReceiptRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSoapReceiptMockMvc;

    private SoapReceipt soapReceipt;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SoapReceipt createEntity(EntityManager em) {
        SoapReceipt soapReceipt = new SoapReceipt().name(DEFAULT_NAME).liquid(DEFAULT_LIQUID).superfat(DEFAULT_SUPERFAT);
        return soapReceipt;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SoapReceipt createUpdatedEntity(EntityManager em) {
        SoapReceipt soapReceipt = new SoapReceipt().name(UPDATED_NAME).liquid(UPDATED_LIQUID).superfat(UPDATED_SUPERFAT);
        return soapReceipt;
    }

    @BeforeEach
    public void initTest() {
        soapReceipt = createEntity(em);
    }

    @Test
    @Transactional
    void createSoapReceipt() throws Exception {
        int databaseSizeBeforeCreate = soapReceiptRepository.findAll().size();
        // Create the SoapReceipt
        restSoapReceiptMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(soapReceipt)))
            .andExpect(status().isCreated());

        // Validate the SoapReceipt in the database
        List<SoapReceipt> soapReceiptList = soapReceiptRepository.findAll();
        assertThat(soapReceiptList).hasSize(databaseSizeBeforeCreate + 1);
        SoapReceipt testSoapReceipt = soapReceiptList.get(soapReceiptList.size() - 1);
        assertThat(testSoapReceipt.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSoapReceipt.getLiquid()).isEqualTo(DEFAULT_LIQUID);
        assertThat(testSoapReceipt.getSuperfat()).isEqualTo(DEFAULT_SUPERFAT);
    }

    @Test
    @Transactional
    void createSoapReceiptWithExistingId() throws Exception {
        // Create the SoapReceipt with an existing ID
        soapReceipt.setId(1L);

        int databaseSizeBeforeCreate = soapReceiptRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSoapReceiptMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(soapReceipt)))
            .andExpect(status().isBadRequest());

        // Validate the SoapReceipt in the database
        List<SoapReceipt> soapReceiptList = soapReceiptRepository.findAll();
        assertThat(soapReceiptList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSoapReceipts() throws Exception {
        // Initialize the database
        soapReceiptRepository.saveAndFlush(soapReceipt);

        // Get all the soapReceiptList
        restSoapReceiptMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(soapReceipt.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].liquid").value(hasItem(DEFAULT_LIQUID)))
            .andExpect(jsonPath("$.[*].superfat").value(hasItem(DEFAULT_SUPERFAT)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSoapReceiptsWithEagerRelationshipsIsEnabled() throws Exception {
        when(soapReceiptRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSoapReceiptMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(soapReceiptRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSoapReceiptsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(soapReceiptRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSoapReceiptMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(soapReceiptRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getSoapReceipt() throws Exception {
        // Initialize the database
        soapReceiptRepository.saveAndFlush(soapReceipt);

        // Get the soapReceipt
        restSoapReceiptMockMvc
            .perform(get(ENTITY_API_URL_ID, soapReceipt.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(soapReceipt.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.liquid").value(DEFAULT_LIQUID))
            .andExpect(jsonPath("$.superfat").value(DEFAULT_SUPERFAT));
    }

    @Test
    @Transactional
    void getNonExistingSoapReceipt() throws Exception {
        // Get the soapReceipt
        restSoapReceiptMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSoapReceipt() throws Exception {
        // Initialize the database
        soapReceiptRepository.saveAndFlush(soapReceipt);

        int databaseSizeBeforeUpdate = soapReceiptRepository.findAll().size();

        // Update the soapReceipt
        SoapReceipt updatedSoapReceipt = soapReceiptRepository.findById(soapReceipt.getId()).get();
        // Disconnect from session so that the updates on updatedSoapReceipt are not directly saved in db
        em.detach(updatedSoapReceipt);
        updatedSoapReceipt.name(UPDATED_NAME).liquid(UPDATED_LIQUID).superfat(UPDATED_SUPERFAT);

        restSoapReceiptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSoapReceipt.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSoapReceipt))
            )
            .andExpect(status().isOk());

        // Validate the SoapReceipt in the database
        List<SoapReceipt> soapReceiptList = soapReceiptRepository.findAll();
        assertThat(soapReceiptList).hasSize(databaseSizeBeforeUpdate);
        SoapReceipt testSoapReceipt = soapReceiptList.get(soapReceiptList.size() - 1);
        assertThat(testSoapReceipt.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSoapReceipt.getLiquid()).isEqualTo(UPDATED_LIQUID);
        assertThat(testSoapReceipt.getSuperfat()).isEqualTo(UPDATED_SUPERFAT);
    }

    @Test
    @Transactional
    void putNonExistingSoapReceipt() throws Exception {
        int databaseSizeBeforeUpdate = soapReceiptRepository.findAll().size();
        soapReceipt.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSoapReceiptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, soapReceipt.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(soapReceipt))
            )
            .andExpect(status().isBadRequest());

        // Validate the SoapReceipt in the database
        List<SoapReceipt> soapReceiptList = soapReceiptRepository.findAll();
        assertThat(soapReceiptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSoapReceipt() throws Exception {
        int databaseSizeBeforeUpdate = soapReceiptRepository.findAll().size();
        soapReceipt.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSoapReceiptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(soapReceipt))
            )
            .andExpect(status().isBadRequest());

        // Validate the SoapReceipt in the database
        List<SoapReceipt> soapReceiptList = soapReceiptRepository.findAll();
        assertThat(soapReceiptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSoapReceipt() throws Exception {
        int databaseSizeBeforeUpdate = soapReceiptRepository.findAll().size();
        soapReceipt.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSoapReceiptMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(soapReceipt)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SoapReceipt in the database
        List<SoapReceipt> soapReceiptList = soapReceiptRepository.findAll();
        assertThat(soapReceiptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSoapReceiptWithPatch() throws Exception {
        // Initialize the database
        soapReceiptRepository.saveAndFlush(soapReceipt);

        int databaseSizeBeforeUpdate = soapReceiptRepository.findAll().size();

        // Update the soapReceipt using partial update
        SoapReceipt partialUpdatedSoapReceipt = new SoapReceipt();
        partialUpdatedSoapReceipt.setId(soapReceipt.getId());

        partialUpdatedSoapReceipt.name(UPDATED_NAME).liquid(UPDATED_LIQUID);

        restSoapReceiptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSoapReceipt.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSoapReceipt))
            )
            .andExpect(status().isOk());

        // Validate the SoapReceipt in the database
        List<SoapReceipt> soapReceiptList = soapReceiptRepository.findAll();
        assertThat(soapReceiptList).hasSize(databaseSizeBeforeUpdate);
        SoapReceipt testSoapReceipt = soapReceiptList.get(soapReceiptList.size() - 1);
        assertThat(testSoapReceipt.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSoapReceipt.getLiquid()).isEqualTo(UPDATED_LIQUID);
        assertThat(testSoapReceipt.getSuperfat()).isEqualTo(DEFAULT_SUPERFAT);
    }

    @Test
    @Transactional
    void fullUpdateSoapReceiptWithPatch() throws Exception {
        // Initialize the database
        soapReceiptRepository.saveAndFlush(soapReceipt);

        int databaseSizeBeforeUpdate = soapReceiptRepository.findAll().size();

        // Update the soapReceipt using partial update
        SoapReceipt partialUpdatedSoapReceipt = new SoapReceipt();
        partialUpdatedSoapReceipt.setId(soapReceipt.getId());

        partialUpdatedSoapReceipt.name(UPDATED_NAME).liquid(UPDATED_LIQUID).superfat(UPDATED_SUPERFAT);

        restSoapReceiptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSoapReceipt.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSoapReceipt))
            )
            .andExpect(status().isOk());

        // Validate the SoapReceipt in the database
        List<SoapReceipt> soapReceiptList = soapReceiptRepository.findAll();
        assertThat(soapReceiptList).hasSize(databaseSizeBeforeUpdate);
        SoapReceipt testSoapReceipt = soapReceiptList.get(soapReceiptList.size() - 1);
        assertThat(testSoapReceipt.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSoapReceipt.getLiquid()).isEqualTo(UPDATED_LIQUID);
        assertThat(testSoapReceipt.getSuperfat()).isEqualTo(UPDATED_SUPERFAT);
    }

    @Test
    @Transactional
    void patchNonExistingSoapReceipt() throws Exception {
        int databaseSizeBeforeUpdate = soapReceiptRepository.findAll().size();
        soapReceipt.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSoapReceiptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, soapReceipt.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(soapReceipt))
            )
            .andExpect(status().isBadRequest());

        // Validate the SoapReceipt in the database
        List<SoapReceipt> soapReceiptList = soapReceiptRepository.findAll();
        assertThat(soapReceiptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSoapReceipt() throws Exception {
        int databaseSizeBeforeUpdate = soapReceiptRepository.findAll().size();
        soapReceipt.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSoapReceiptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(soapReceipt))
            )
            .andExpect(status().isBadRequest());

        // Validate the SoapReceipt in the database
        List<SoapReceipt> soapReceiptList = soapReceiptRepository.findAll();
        assertThat(soapReceiptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSoapReceipt() throws Exception {
        int databaseSizeBeforeUpdate = soapReceiptRepository.findAll().size();
        soapReceipt.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSoapReceiptMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(soapReceipt))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SoapReceipt in the database
        List<SoapReceipt> soapReceiptList = soapReceiptRepository.findAll();
        assertThat(soapReceiptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSoapReceipt() throws Exception {
        // Initialize the database
        soapReceiptRepository.saveAndFlush(soapReceipt);

        int databaseSizeBeforeDelete = soapReceiptRepository.findAll().size();

        // Delete the soapReceipt
        restSoapReceiptMockMvc
            .perform(delete(ENTITY_API_URL_ID, soapReceipt.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SoapReceipt> soapReceiptList = soapReceiptRepository.findAll();
        assertThat(soapReceiptList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
