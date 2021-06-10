package com.simplify.approval.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.simplify.approval.IntegrationTest;
import com.simplify.approval.domain.ApprovalRequestItem;
import com.simplify.approval.domain.enumeration.DataType;
import com.simplify.approval.repository.ApprovalRequestItemRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ApprovalRequestItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ApprovalRequestItemResourceIT {

    private static final String DEFAULT_FIELD_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_NAME = "BBBBBBBBBB";

    private static final DataType DEFAULT_DATA_TYPE = DataType.STR;
    private static final DataType UPDATED_DATA_TYPE = DataType.DEC;

    private static final String DEFAULT_STR_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_STR_VALUE = "BBBBBBBBBB";

    private static final Float DEFAULT_DEC_VALUE = 1F;
    private static final Float UPDATED_DEC_VALUE = 2F;

    private static final String ENTITY_API_URL = "/api/approval-request-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ApprovalRequestItemRepository approvalRequestItemRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApprovalRequestItemMockMvc;

    private ApprovalRequestItem approvalRequestItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApprovalRequestItem createEntity(EntityManager em) {
        ApprovalRequestItem approvalRequestItem = new ApprovalRequestItem()
            .fieldName(DEFAULT_FIELD_NAME)
            .dataType(DEFAULT_DATA_TYPE)
            .strValue(DEFAULT_STR_VALUE)
            .decValue(DEFAULT_DEC_VALUE);
        return approvalRequestItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApprovalRequestItem createUpdatedEntity(EntityManager em) {
        ApprovalRequestItem approvalRequestItem = new ApprovalRequestItem()
            .fieldName(UPDATED_FIELD_NAME)
            .dataType(UPDATED_DATA_TYPE)
            .strValue(UPDATED_STR_VALUE)
            .decValue(UPDATED_DEC_VALUE);
        return approvalRequestItem;
    }

    @BeforeEach
    public void initTest() {
        approvalRequestItem = createEntity(em);
    }

    @Test
    @Transactional
    void createApprovalRequestItem() throws Exception {
        int databaseSizeBeforeCreate = approvalRequestItemRepository.findAll().size();
        // Create the ApprovalRequestItem
        restApprovalRequestItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(approvalRequestItem))
            )
            .andExpect(status().isCreated());

        // Validate the ApprovalRequestItem in the database
        List<ApprovalRequestItem> approvalRequestItemList = approvalRequestItemRepository.findAll();
        assertThat(approvalRequestItemList).hasSize(databaseSizeBeforeCreate + 1);
        ApprovalRequestItem testApprovalRequestItem = approvalRequestItemList.get(approvalRequestItemList.size() - 1);
        assertThat(testApprovalRequestItem.getFieldName()).isEqualTo(DEFAULT_FIELD_NAME);
        assertThat(testApprovalRequestItem.getDataType()).isEqualTo(DEFAULT_DATA_TYPE);
        assertThat(testApprovalRequestItem.getStrValue()).isEqualTo(DEFAULT_STR_VALUE);
        assertThat(testApprovalRequestItem.getDecValue()).isEqualTo(DEFAULT_DEC_VALUE);
    }

    @Test
    @Transactional
    void createApprovalRequestItemWithExistingId() throws Exception {
        // Create the ApprovalRequestItem with an existing ID
        approvalRequestItem.setId(1L);

        int databaseSizeBeforeCreate = approvalRequestItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApprovalRequestItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(approvalRequestItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApprovalRequestItem in the database
        List<ApprovalRequestItem> approvalRequestItemList = approvalRequestItemRepository.findAll();
        assertThat(approvalRequestItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFieldNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = approvalRequestItemRepository.findAll().size();
        // set the field null
        approvalRequestItem.setFieldName(null);

        // Create the ApprovalRequestItem, which fails.

        restApprovalRequestItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(approvalRequestItem))
            )
            .andExpect(status().isBadRequest());

        List<ApprovalRequestItem> approvalRequestItemList = approvalRequestItemRepository.findAll();
        assertThat(approvalRequestItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDataTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = approvalRequestItemRepository.findAll().size();
        // set the field null
        approvalRequestItem.setDataType(null);

        // Create the ApprovalRequestItem, which fails.

        restApprovalRequestItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(approvalRequestItem))
            )
            .andExpect(status().isBadRequest());

        List<ApprovalRequestItem> approvalRequestItemList = approvalRequestItemRepository.findAll();
        assertThat(approvalRequestItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllApprovalRequestItems() throws Exception {
        // Initialize the database
        approvalRequestItemRepository.saveAndFlush(approvalRequestItem);

        // Get all the approvalRequestItemList
        restApprovalRequestItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(approvalRequestItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].fieldName").value(hasItem(DEFAULT_FIELD_NAME)))
            .andExpect(jsonPath("$.[*].dataType").value(hasItem(DEFAULT_DATA_TYPE.toString())))
            .andExpect(jsonPath("$.[*].strValue").value(hasItem(DEFAULT_STR_VALUE)))
            .andExpect(jsonPath("$.[*].decValue").value(hasItem(DEFAULT_DEC_VALUE.doubleValue())));
    }

    @Test
    @Transactional
    void getApprovalRequestItem() throws Exception {
        // Initialize the database
        approvalRequestItemRepository.saveAndFlush(approvalRequestItem);

        // Get the approvalRequestItem
        restApprovalRequestItemMockMvc
            .perform(get(ENTITY_API_URL_ID, approvalRequestItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(approvalRequestItem.getId().intValue()))
            .andExpect(jsonPath("$.fieldName").value(DEFAULT_FIELD_NAME))
            .andExpect(jsonPath("$.dataType").value(DEFAULT_DATA_TYPE.toString()))
            .andExpect(jsonPath("$.strValue").value(DEFAULT_STR_VALUE))
            .andExpect(jsonPath("$.decValue").value(DEFAULT_DEC_VALUE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingApprovalRequestItem() throws Exception {
        // Get the approvalRequestItem
        restApprovalRequestItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewApprovalRequestItem() throws Exception {
        // Initialize the database
        approvalRequestItemRepository.saveAndFlush(approvalRequestItem);

        int databaseSizeBeforeUpdate = approvalRequestItemRepository.findAll().size();

        // Update the approvalRequestItem
        ApprovalRequestItem updatedApprovalRequestItem = approvalRequestItemRepository.findById(approvalRequestItem.getId()).get();
        // Disconnect from session so that the updates on updatedApprovalRequestItem are not directly saved in db
        em.detach(updatedApprovalRequestItem);
        updatedApprovalRequestItem
            .fieldName(UPDATED_FIELD_NAME)
            .dataType(UPDATED_DATA_TYPE)
            .strValue(UPDATED_STR_VALUE)
            .decValue(UPDATED_DEC_VALUE);

        restApprovalRequestItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedApprovalRequestItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedApprovalRequestItem))
            )
            .andExpect(status().isOk());

        // Validate the ApprovalRequestItem in the database
        List<ApprovalRequestItem> approvalRequestItemList = approvalRequestItemRepository.findAll();
        assertThat(approvalRequestItemList).hasSize(databaseSizeBeforeUpdate);
        ApprovalRequestItem testApprovalRequestItem = approvalRequestItemList.get(approvalRequestItemList.size() - 1);
        assertThat(testApprovalRequestItem.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);
        assertThat(testApprovalRequestItem.getDataType()).isEqualTo(UPDATED_DATA_TYPE);
        assertThat(testApprovalRequestItem.getStrValue()).isEqualTo(UPDATED_STR_VALUE);
        assertThat(testApprovalRequestItem.getDecValue()).isEqualTo(UPDATED_DEC_VALUE);
    }

    @Test
    @Transactional
    void putNonExistingApprovalRequestItem() throws Exception {
        int databaseSizeBeforeUpdate = approvalRequestItemRepository.findAll().size();
        approvalRequestItem.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApprovalRequestItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, approvalRequestItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(approvalRequestItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApprovalRequestItem in the database
        List<ApprovalRequestItem> approvalRequestItemList = approvalRequestItemRepository.findAll();
        assertThat(approvalRequestItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchApprovalRequestItem() throws Exception {
        int databaseSizeBeforeUpdate = approvalRequestItemRepository.findAll().size();
        approvalRequestItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApprovalRequestItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(approvalRequestItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApprovalRequestItem in the database
        List<ApprovalRequestItem> approvalRequestItemList = approvalRequestItemRepository.findAll();
        assertThat(approvalRequestItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApprovalRequestItem() throws Exception {
        int databaseSizeBeforeUpdate = approvalRequestItemRepository.findAll().size();
        approvalRequestItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApprovalRequestItemMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(approvalRequestItem))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApprovalRequestItem in the database
        List<ApprovalRequestItem> approvalRequestItemList = approvalRequestItemRepository.findAll();
        assertThat(approvalRequestItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateApprovalRequestItemWithPatch() throws Exception {
        // Initialize the database
        approvalRequestItemRepository.saveAndFlush(approvalRequestItem);

        int databaseSizeBeforeUpdate = approvalRequestItemRepository.findAll().size();

        // Update the approvalRequestItem using partial update
        ApprovalRequestItem partialUpdatedApprovalRequestItem = new ApprovalRequestItem();
        partialUpdatedApprovalRequestItem.setId(approvalRequestItem.getId());

        partialUpdatedApprovalRequestItem
            .fieldName(UPDATED_FIELD_NAME)
            .dataType(UPDATED_DATA_TYPE)
            .strValue(UPDATED_STR_VALUE)
            .decValue(UPDATED_DEC_VALUE);

        restApprovalRequestItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApprovalRequestItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApprovalRequestItem))
            )
            .andExpect(status().isOk());

        // Validate the ApprovalRequestItem in the database
        List<ApprovalRequestItem> approvalRequestItemList = approvalRequestItemRepository.findAll();
        assertThat(approvalRequestItemList).hasSize(databaseSizeBeforeUpdate);
        ApprovalRequestItem testApprovalRequestItem = approvalRequestItemList.get(approvalRequestItemList.size() - 1);
        assertThat(testApprovalRequestItem.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);
        assertThat(testApprovalRequestItem.getDataType()).isEqualTo(UPDATED_DATA_TYPE);
        assertThat(testApprovalRequestItem.getStrValue()).isEqualTo(UPDATED_STR_VALUE);
        assertThat(testApprovalRequestItem.getDecValue()).isEqualTo(UPDATED_DEC_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateApprovalRequestItemWithPatch() throws Exception {
        // Initialize the database
        approvalRequestItemRepository.saveAndFlush(approvalRequestItem);

        int databaseSizeBeforeUpdate = approvalRequestItemRepository.findAll().size();

        // Update the approvalRequestItem using partial update
        ApprovalRequestItem partialUpdatedApprovalRequestItem = new ApprovalRequestItem();
        partialUpdatedApprovalRequestItem.setId(approvalRequestItem.getId());

        partialUpdatedApprovalRequestItem
            .fieldName(UPDATED_FIELD_NAME)
            .dataType(UPDATED_DATA_TYPE)
            .strValue(UPDATED_STR_VALUE)
            .decValue(UPDATED_DEC_VALUE);

        restApprovalRequestItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApprovalRequestItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApprovalRequestItem))
            )
            .andExpect(status().isOk());

        // Validate the ApprovalRequestItem in the database
        List<ApprovalRequestItem> approvalRequestItemList = approvalRequestItemRepository.findAll();
        assertThat(approvalRequestItemList).hasSize(databaseSizeBeforeUpdate);
        ApprovalRequestItem testApprovalRequestItem = approvalRequestItemList.get(approvalRequestItemList.size() - 1);
        assertThat(testApprovalRequestItem.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);
        assertThat(testApprovalRequestItem.getDataType()).isEqualTo(UPDATED_DATA_TYPE);
        assertThat(testApprovalRequestItem.getStrValue()).isEqualTo(UPDATED_STR_VALUE);
        assertThat(testApprovalRequestItem.getDecValue()).isEqualTo(UPDATED_DEC_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingApprovalRequestItem() throws Exception {
        int databaseSizeBeforeUpdate = approvalRequestItemRepository.findAll().size();
        approvalRequestItem.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApprovalRequestItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, approvalRequestItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(approvalRequestItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApprovalRequestItem in the database
        List<ApprovalRequestItem> approvalRequestItemList = approvalRequestItemRepository.findAll();
        assertThat(approvalRequestItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApprovalRequestItem() throws Exception {
        int databaseSizeBeforeUpdate = approvalRequestItemRepository.findAll().size();
        approvalRequestItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApprovalRequestItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(approvalRequestItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApprovalRequestItem in the database
        List<ApprovalRequestItem> approvalRequestItemList = approvalRequestItemRepository.findAll();
        assertThat(approvalRequestItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApprovalRequestItem() throws Exception {
        int databaseSizeBeforeUpdate = approvalRequestItemRepository.findAll().size();
        approvalRequestItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApprovalRequestItemMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(approvalRequestItem))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApprovalRequestItem in the database
        List<ApprovalRequestItem> approvalRequestItemList = approvalRequestItemRepository.findAll();
        assertThat(approvalRequestItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteApprovalRequestItem() throws Exception {
        // Initialize the database
        approvalRequestItemRepository.saveAndFlush(approvalRequestItem);

        int databaseSizeBeforeDelete = approvalRequestItemRepository.findAll().size();

        // Delete the approvalRequestItem
        restApprovalRequestItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, approvalRequestItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ApprovalRequestItem> approvalRequestItemList = approvalRequestItemRepository.findAll();
        assertThat(approvalRequestItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
