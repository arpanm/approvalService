package com.simplify.approval.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.simplify.approval.IntegrationTest;
import com.simplify.approval.domain.ApprovalLevelStatus;
import com.simplify.approval.domain.enumeration.Status;
import com.simplify.approval.repository.ApprovalLevelStatusRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link ApprovalLevelStatusResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ApprovalLevelStatusResourceIT {

    private static final Status DEFAULT_STATUS = Status.INIT;
    private static final Status UPDATED_STATUS = Status.EMAIL_SEND;

    private static final Integer DEFAULT_LEVEL = 1;
    private static final Integer UPDATED_LEVEL = 2;

    private static final String DEFAULT_CLIENT_TIME = "AAAAAAAAAA";
    private static final String UPDATED_CLIENT_TIME = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_UPDATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/approval-level-statuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ApprovalLevelStatusRepository approvalLevelStatusRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApprovalLevelStatusMockMvc;

    private ApprovalLevelStatus approvalLevelStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApprovalLevelStatus createEntity(EntityManager em) {
        ApprovalLevelStatus approvalLevelStatus = new ApprovalLevelStatus()
            .status(DEFAULT_STATUS)
            .level(DEFAULT_LEVEL)
            .clientTime(DEFAULT_CLIENT_TIME)
            .createdBy(DEFAULT_CREATED_BY)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedAt(DEFAULT_UPDATED_AT);
        return approvalLevelStatus;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApprovalLevelStatus createUpdatedEntity(EntityManager em) {
        ApprovalLevelStatus approvalLevelStatus = new ApprovalLevelStatus()
            .status(UPDATED_STATUS)
            .level(UPDATED_LEVEL)
            .clientTime(UPDATED_CLIENT_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT);
        return approvalLevelStatus;
    }

    @BeforeEach
    public void initTest() {
        approvalLevelStatus = createEntity(em);
    }

    @Test
    @Transactional
    void createApprovalLevelStatus() throws Exception {
        int databaseSizeBeforeCreate = approvalLevelStatusRepository.findAll().size();
        // Create the ApprovalLevelStatus
        restApprovalLevelStatusMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(approvalLevelStatus))
            )
            .andExpect(status().isCreated());

        // Validate the ApprovalLevelStatus in the database
        List<ApprovalLevelStatus> approvalLevelStatusList = approvalLevelStatusRepository.findAll();
        assertThat(approvalLevelStatusList).hasSize(databaseSizeBeforeCreate + 1);
        ApprovalLevelStatus testApprovalLevelStatus = approvalLevelStatusList.get(approvalLevelStatusList.size() - 1);
        assertThat(testApprovalLevelStatus.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testApprovalLevelStatus.getLevel()).isEqualTo(DEFAULT_LEVEL);
        assertThat(testApprovalLevelStatus.getClientTime()).isEqualTo(DEFAULT_CLIENT_TIME);
        assertThat(testApprovalLevelStatus.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testApprovalLevelStatus.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testApprovalLevelStatus.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testApprovalLevelStatus.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createApprovalLevelStatusWithExistingId() throws Exception {
        // Create the ApprovalLevelStatus with an existing ID
        approvalLevelStatus.setId(1L);

        int databaseSizeBeforeCreate = approvalLevelStatusRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApprovalLevelStatusMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(approvalLevelStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApprovalLevelStatus in the database
        List<ApprovalLevelStatus> approvalLevelStatusList = approvalLevelStatusRepository.findAll();
        assertThat(approvalLevelStatusList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = approvalLevelStatusRepository.findAll().size();
        // set the field null
        approvalLevelStatus.setStatus(null);

        // Create the ApprovalLevelStatus, which fails.

        restApprovalLevelStatusMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(approvalLevelStatus))
            )
            .andExpect(status().isBadRequest());

        List<ApprovalLevelStatus> approvalLevelStatusList = approvalLevelStatusRepository.findAll();
        assertThat(approvalLevelStatusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = approvalLevelStatusRepository.findAll().size();
        // set the field null
        approvalLevelStatus.setLevel(null);

        // Create the ApprovalLevelStatus, which fails.

        restApprovalLevelStatusMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(approvalLevelStatus))
            )
            .andExpect(status().isBadRequest());

        List<ApprovalLevelStatus> approvalLevelStatusList = approvalLevelStatusRepository.findAll();
        assertThat(approvalLevelStatusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkClientTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = approvalLevelStatusRepository.findAll().size();
        // set the field null
        approvalLevelStatus.setClientTime(null);

        // Create the ApprovalLevelStatus, which fails.

        restApprovalLevelStatusMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(approvalLevelStatus))
            )
            .andExpect(status().isBadRequest());

        List<ApprovalLevelStatus> approvalLevelStatusList = approvalLevelStatusRepository.findAll();
        assertThat(approvalLevelStatusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllApprovalLevelStatuses() throws Exception {
        // Initialize the database
        approvalLevelStatusRepository.saveAndFlush(approvalLevelStatus);

        // Get all the approvalLevelStatusList
        restApprovalLevelStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(approvalLevelStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)))
            .andExpect(jsonPath("$.[*].clientTime").value(hasItem(DEFAULT_CLIENT_TIME)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getApprovalLevelStatus() throws Exception {
        // Initialize the database
        approvalLevelStatusRepository.saveAndFlush(approvalLevelStatus);

        // Get the approvalLevelStatus
        restApprovalLevelStatusMockMvc
            .perform(get(ENTITY_API_URL_ID, approvalLevelStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(approvalLevelStatus.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL))
            .andExpect(jsonPath("$.clientTime").value(DEFAULT_CLIENT_TIME))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingApprovalLevelStatus() throws Exception {
        // Get the approvalLevelStatus
        restApprovalLevelStatusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewApprovalLevelStatus() throws Exception {
        // Initialize the database
        approvalLevelStatusRepository.saveAndFlush(approvalLevelStatus);

        int databaseSizeBeforeUpdate = approvalLevelStatusRepository.findAll().size();

        // Update the approvalLevelStatus
        ApprovalLevelStatus updatedApprovalLevelStatus = approvalLevelStatusRepository.findById(approvalLevelStatus.getId()).get();
        // Disconnect from session so that the updates on updatedApprovalLevelStatus are not directly saved in db
        em.detach(updatedApprovalLevelStatus);
        updatedApprovalLevelStatus
            .status(UPDATED_STATUS)
            .level(UPDATED_LEVEL)
            .clientTime(UPDATED_CLIENT_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT);

        restApprovalLevelStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedApprovalLevelStatus.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedApprovalLevelStatus))
            )
            .andExpect(status().isOk());

        // Validate the ApprovalLevelStatus in the database
        List<ApprovalLevelStatus> approvalLevelStatusList = approvalLevelStatusRepository.findAll();
        assertThat(approvalLevelStatusList).hasSize(databaseSizeBeforeUpdate);
        ApprovalLevelStatus testApprovalLevelStatus = approvalLevelStatusList.get(approvalLevelStatusList.size() - 1);
        assertThat(testApprovalLevelStatus.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testApprovalLevelStatus.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testApprovalLevelStatus.getClientTime()).isEqualTo(UPDATED_CLIENT_TIME);
        assertThat(testApprovalLevelStatus.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testApprovalLevelStatus.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testApprovalLevelStatus.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testApprovalLevelStatus.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingApprovalLevelStatus() throws Exception {
        int databaseSizeBeforeUpdate = approvalLevelStatusRepository.findAll().size();
        approvalLevelStatus.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApprovalLevelStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, approvalLevelStatus.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(approvalLevelStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApprovalLevelStatus in the database
        List<ApprovalLevelStatus> approvalLevelStatusList = approvalLevelStatusRepository.findAll();
        assertThat(approvalLevelStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchApprovalLevelStatus() throws Exception {
        int databaseSizeBeforeUpdate = approvalLevelStatusRepository.findAll().size();
        approvalLevelStatus.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApprovalLevelStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(approvalLevelStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApprovalLevelStatus in the database
        List<ApprovalLevelStatus> approvalLevelStatusList = approvalLevelStatusRepository.findAll();
        assertThat(approvalLevelStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApprovalLevelStatus() throws Exception {
        int databaseSizeBeforeUpdate = approvalLevelStatusRepository.findAll().size();
        approvalLevelStatus.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApprovalLevelStatusMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(approvalLevelStatus))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApprovalLevelStatus in the database
        List<ApprovalLevelStatus> approvalLevelStatusList = approvalLevelStatusRepository.findAll();
        assertThat(approvalLevelStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateApprovalLevelStatusWithPatch() throws Exception {
        // Initialize the database
        approvalLevelStatusRepository.saveAndFlush(approvalLevelStatus);

        int databaseSizeBeforeUpdate = approvalLevelStatusRepository.findAll().size();

        // Update the approvalLevelStatus using partial update
        ApprovalLevelStatus partialUpdatedApprovalLevelStatus = new ApprovalLevelStatus();
        partialUpdatedApprovalLevelStatus.setId(approvalLevelStatus.getId());

        partialUpdatedApprovalLevelStatus
            .level(UPDATED_LEVEL)
            .clientTime(UPDATED_CLIENT_TIME)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restApprovalLevelStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApprovalLevelStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApprovalLevelStatus))
            )
            .andExpect(status().isOk());

        // Validate the ApprovalLevelStatus in the database
        List<ApprovalLevelStatus> approvalLevelStatusList = approvalLevelStatusRepository.findAll();
        assertThat(approvalLevelStatusList).hasSize(databaseSizeBeforeUpdate);
        ApprovalLevelStatus testApprovalLevelStatus = approvalLevelStatusList.get(approvalLevelStatusList.size() - 1);
        assertThat(testApprovalLevelStatus.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testApprovalLevelStatus.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testApprovalLevelStatus.getClientTime()).isEqualTo(UPDATED_CLIENT_TIME);
        assertThat(testApprovalLevelStatus.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testApprovalLevelStatus.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testApprovalLevelStatus.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testApprovalLevelStatus.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateApprovalLevelStatusWithPatch() throws Exception {
        // Initialize the database
        approvalLevelStatusRepository.saveAndFlush(approvalLevelStatus);

        int databaseSizeBeforeUpdate = approvalLevelStatusRepository.findAll().size();

        // Update the approvalLevelStatus using partial update
        ApprovalLevelStatus partialUpdatedApprovalLevelStatus = new ApprovalLevelStatus();
        partialUpdatedApprovalLevelStatus.setId(approvalLevelStatus.getId());

        partialUpdatedApprovalLevelStatus
            .status(UPDATED_STATUS)
            .level(UPDATED_LEVEL)
            .clientTime(UPDATED_CLIENT_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT);

        restApprovalLevelStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApprovalLevelStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApprovalLevelStatus))
            )
            .andExpect(status().isOk());

        // Validate the ApprovalLevelStatus in the database
        List<ApprovalLevelStatus> approvalLevelStatusList = approvalLevelStatusRepository.findAll();
        assertThat(approvalLevelStatusList).hasSize(databaseSizeBeforeUpdate);
        ApprovalLevelStatus testApprovalLevelStatus = approvalLevelStatusList.get(approvalLevelStatusList.size() - 1);
        assertThat(testApprovalLevelStatus.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testApprovalLevelStatus.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testApprovalLevelStatus.getClientTime()).isEqualTo(UPDATED_CLIENT_TIME);
        assertThat(testApprovalLevelStatus.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testApprovalLevelStatus.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testApprovalLevelStatus.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testApprovalLevelStatus.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingApprovalLevelStatus() throws Exception {
        int databaseSizeBeforeUpdate = approvalLevelStatusRepository.findAll().size();
        approvalLevelStatus.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApprovalLevelStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, approvalLevelStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(approvalLevelStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApprovalLevelStatus in the database
        List<ApprovalLevelStatus> approvalLevelStatusList = approvalLevelStatusRepository.findAll();
        assertThat(approvalLevelStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApprovalLevelStatus() throws Exception {
        int databaseSizeBeforeUpdate = approvalLevelStatusRepository.findAll().size();
        approvalLevelStatus.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApprovalLevelStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(approvalLevelStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApprovalLevelStatus in the database
        List<ApprovalLevelStatus> approvalLevelStatusList = approvalLevelStatusRepository.findAll();
        assertThat(approvalLevelStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApprovalLevelStatus() throws Exception {
        int databaseSizeBeforeUpdate = approvalLevelStatusRepository.findAll().size();
        approvalLevelStatus.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApprovalLevelStatusMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(approvalLevelStatus))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApprovalLevelStatus in the database
        List<ApprovalLevelStatus> approvalLevelStatusList = approvalLevelStatusRepository.findAll();
        assertThat(approvalLevelStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteApprovalLevelStatus() throws Exception {
        // Initialize the database
        approvalLevelStatusRepository.saveAndFlush(approvalLevelStatus);

        int databaseSizeBeforeDelete = approvalLevelStatusRepository.findAll().size();

        // Delete the approvalLevelStatus
        restApprovalLevelStatusMockMvc
            .perform(delete(ENTITY_API_URL_ID, approvalLevelStatus.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ApprovalLevelStatus> approvalLevelStatusList = approvalLevelStatusRepository.findAll();
        assertThat(approvalLevelStatusList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
