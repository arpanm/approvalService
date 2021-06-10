package com.simplify.approval.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.simplify.approval.IntegrationTest;
import com.simplify.approval.domain.IndividualApprovalStatus;
import com.simplify.approval.domain.enumeration.Status;
import com.simplify.approval.repository.IndividualApprovalStatusRepository;
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
 * Integration tests for the {@link IndividualApprovalStatusResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IndividualApprovalStatusResourceIT {

    private static final Status DEFAULT_STATUS = Status.INIT;
    private static final Status UPDATED_STATUS = Status.EMAIL_SEND;

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

    private static final String ENTITY_API_URL = "/api/individual-approval-statuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IndividualApprovalStatusRepository individualApprovalStatusRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIndividualApprovalStatusMockMvc;

    private IndividualApprovalStatus individualApprovalStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IndividualApprovalStatus createEntity(EntityManager em) {
        IndividualApprovalStatus individualApprovalStatus = new IndividualApprovalStatus()
            .status(DEFAULT_STATUS)
            .clientTime(DEFAULT_CLIENT_TIME)
            .createdBy(DEFAULT_CREATED_BY)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedAt(DEFAULT_UPDATED_AT);
        return individualApprovalStatus;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IndividualApprovalStatus createUpdatedEntity(EntityManager em) {
        IndividualApprovalStatus individualApprovalStatus = new IndividualApprovalStatus()
            .status(UPDATED_STATUS)
            .clientTime(UPDATED_CLIENT_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT);
        return individualApprovalStatus;
    }

    @BeforeEach
    public void initTest() {
        individualApprovalStatus = createEntity(em);
    }

    @Test
    @Transactional
    void createIndividualApprovalStatus() throws Exception {
        int databaseSizeBeforeCreate = individualApprovalStatusRepository.findAll().size();
        // Create the IndividualApprovalStatus
        restIndividualApprovalStatusMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(individualApprovalStatus))
            )
            .andExpect(status().isCreated());

        // Validate the IndividualApprovalStatus in the database
        List<IndividualApprovalStatus> individualApprovalStatusList = individualApprovalStatusRepository.findAll();
        assertThat(individualApprovalStatusList).hasSize(databaseSizeBeforeCreate + 1);
        IndividualApprovalStatus testIndividualApprovalStatus = individualApprovalStatusList.get(individualApprovalStatusList.size() - 1);
        assertThat(testIndividualApprovalStatus.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testIndividualApprovalStatus.getClientTime()).isEqualTo(DEFAULT_CLIENT_TIME);
        assertThat(testIndividualApprovalStatus.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testIndividualApprovalStatus.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testIndividualApprovalStatus.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testIndividualApprovalStatus.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createIndividualApprovalStatusWithExistingId() throws Exception {
        // Create the IndividualApprovalStatus with an existing ID
        individualApprovalStatus.setId(1L);

        int databaseSizeBeforeCreate = individualApprovalStatusRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIndividualApprovalStatusMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(individualApprovalStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndividualApprovalStatus in the database
        List<IndividualApprovalStatus> individualApprovalStatusList = individualApprovalStatusRepository.findAll();
        assertThat(individualApprovalStatusList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = individualApprovalStatusRepository.findAll().size();
        // set the field null
        individualApprovalStatus.setStatus(null);

        // Create the IndividualApprovalStatus, which fails.

        restIndividualApprovalStatusMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(individualApprovalStatus))
            )
            .andExpect(status().isBadRequest());

        List<IndividualApprovalStatus> individualApprovalStatusList = individualApprovalStatusRepository.findAll();
        assertThat(individualApprovalStatusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkClientTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = individualApprovalStatusRepository.findAll().size();
        // set the field null
        individualApprovalStatus.setClientTime(null);

        // Create the IndividualApprovalStatus, which fails.

        restIndividualApprovalStatusMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(individualApprovalStatus))
            )
            .andExpect(status().isBadRequest());

        List<IndividualApprovalStatus> individualApprovalStatusList = individualApprovalStatusRepository.findAll();
        assertThat(individualApprovalStatusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllIndividualApprovalStatuses() throws Exception {
        // Initialize the database
        individualApprovalStatusRepository.saveAndFlush(individualApprovalStatus);

        // Get all the individualApprovalStatusList
        restIndividualApprovalStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(individualApprovalStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].clientTime").value(hasItem(DEFAULT_CLIENT_TIME)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getIndividualApprovalStatus() throws Exception {
        // Initialize the database
        individualApprovalStatusRepository.saveAndFlush(individualApprovalStatus);

        // Get the individualApprovalStatus
        restIndividualApprovalStatusMockMvc
            .perform(get(ENTITY_API_URL_ID, individualApprovalStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(individualApprovalStatus.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.clientTime").value(DEFAULT_CLIENT_TIME))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingIndividualApprovalStatus() throws Exception {
        // Get the individualApprovalStatus
        restIndividualApprovalStatusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewIndividualApprovalStatus() throws Exception {
        // Initialize the database
        individualApprovalStatusRepository.saveAndFlush(individualApprovalStatus);

        int databaseSizeBeforeUpdate = individualApprovalStatusRepository.findAll().size();

        // Update the individualApprovalStatus
        IndividualApprovalStatus updatedIndividualApprovalStatus = individualApprovalStatusRepository
            .findById(individualApprovalStatus.getId())
            .get();
        // Disconnect from session so that the updates on updatedIndividualApprovalStatus are not directly saved in db
        em.detach(updatedIndividualApprovalStatus);
        updatedIndividualApprovalStatus
            .status(UPDATED_STATUS)
            .clientTime(UPDATED_CLIENT_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT);

        restIndividualApprovalStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedIndividualApprovalStatus.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedIndividualApprovalStatus))
            )
            .andExpect(status().isOk());

        // Validate the IndividualApprovalStatus in the database
        List<IndividualApprovalStatus> individualApprovalStatusList = individualApprovalStatusRepository.findAll();
        assertThat(individualApprovalStatusList).hasSize(databaseSizeBeforeUpdate);
        IndividualApprovalStatus testIndividualApprovalStatus = individualApprovalStatusList.get(individualApprovalStatusList.size() - 1);
        assertThat(testIndividualApprovalStatus.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testIndividualApprovalStatus.getClientTime()).isEqualTo(UPDATED_CLIENT_TIME);
        assertThat(testIndividualApprovalStatus.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testIndividualApprovalStatus.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testIndividualApprovalStatus.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testIndividualApprovalStatus.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingIndividualApprovalStatus() throws Exception {
        int databaseSizeBeforeUpdate = individualApprovalStatusRepository.findAll().size();
        individualApprovalStatus.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIndividualApprovalStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, individualApprovalStatus.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(individualApprovalStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndividualApprovalStatus in the database
        List<IndividualApprovalStatus> individualApprovalStatusList = individualApprovalStatusRepository.findAll();
        assertThat(individualApprovalStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIndividualApprovalStatus() throws Exception {
        int databaseSizeBeforeUpdate = individualApprovalStatusRepository.findAll().size();
        individualApprovalStatus.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndividualApprovalStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(individualApprovalStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndividualApprovalStatus in the database
        List<IndividualApprovalStatus> individualApprovalStatusList = individualApprovalStatusRepository.findAll();
        assertThat(individualApprovalStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIndividualApprovalStatus() throws Exception {
        int databaseSizeBeforeUpdate = individualApprovalStatusRepository.findAll().size();
        individualApprovalStatus.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndividualApprovalStatusMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(individualApprovalStatus))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IndividualApprovalStatus in the database
        List<IndividualApprovalStatus> individualApprovalStatusList = individualApprovalStatusRepository.findAll();
        assertThat(individualApprovalStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIndividualApprovalStatusWithPatch() throws Exception {
        // Initialize the database
        individualApprovalStatusRepository.saveAndFlush(individualApprovalStatus);

        int databaseSizeBeforeUpdate = individualApprovalStatusRepository.findAll().size();

        // Update the individualApprovalStatus using partial update
        IndividualApprovalStatus partialUpdatedIndividualApprovalStatus = new IndividualApprovalStatus();
        partialUpdatedIndividualApprovalStatus.setId(individualApprovalStatus.getId());

        partialUpdatedIndividualApprovalStatus.status(UPDATED_STATUS).createdBy(UPDATED_CREATED_BY);

        restIndividualApprovalStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIndividualApprovalStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIndividualApprovalStatus))
            )
            .andExpect(status().isOk());

        // Validate the IndividualApprovalStatus in the database
        List<IndividualApprovalStatus> individualApprovalStatusList = individualApprovalStatusRepository.findAll();
        assertThat(individualApprovalStatusList).hasSize(databaseSizeBeforeUpdate);
        IndividualApprovalStatus testIndividualApprovalStatus = individualApprovalStatusList.get(individualApprovalStatusList.size() - 1);
        assertThat(testIndividualApprovalStatus.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testIndividualApprovalStatus.getClientTime()).isEqualTo(DEFAULT_CLIENT_TIME);
        assertThat(testIndividualApprovalStatus.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testIndividualApprovalStatus.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testIndividualApprovalStatus.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testIndividualApprovalStatus.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateIndividualApprovalStatusWithPatch() throws Exception {
        // Initialize the database
        individualApprovalStatusRepository.saveAndFlush(individualApprovalStatus);

        int databaseSizeBeforeUpdate = individualApprovalStatusRepository.findAll().size();

        // Update the individualApprovalStatus using partial update
        IndividualApprovalStatus partialUpdatedIndividualApprovalStatus = new IndividualApprovalStatus();
        partialUpdatedIndividualApprovalStatus.setId(individualApprovalStatus.getId());

        partialUpdatedIndividualApprovalStatus
            .status(UPDATED_STATUS)
            .clientTime(UPDATED_CLIENT_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT);

        restIndividualApprovalStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIndividualApprovalStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIndividualApprovalStatus))
            )
            .andExpect(status().isOk());

        // Validate the IndividualApprovalStatus in the database
        List<IndividualApprovalStatus> individualApprovalStatusList = individualApprovalStatusRepository.findAll();
        assertThat(individualApprovalStatusList).hasSize(databaseSizeBeforeUpdate);
        IndividualApprovalStatus testIndividualApprovalStatus = individualApprovalStatusList.get(individualApprovalStatusList.size() - 1);
        assertThat(testIndividualApprovalStatus.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testIndividualApprovalStatus.getClientTime()).isEqualTo(UPDATED_CLIENT_TIME);
        assertThat(testIndividualApprovalStatus.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testIndividualApprovalStatus.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testIndividualApprovalStatus.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testIndividualApprovalStatus.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingIndividualApprovalStatus() throws Exception {
        int databaseSizeBeforeUpdate = individualApprovalStatusRepository.findAll().size();
        individualApprovalStatus.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIndividualApprovalStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, individualApprovalStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(individualApprovalStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndividualApprovalStatus in the database
        List<IndividualApprovalStatus> individualApprovalStatusList = individualApprovalStatusRepository.findAll();
        assertThat(individualApprovalStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIndividualApprovalStatus() throws Exception {
        int databaseSizeBeforeUpdate = individualApprovalStatusRepository.findAll().size();
        individualApprovalStatus.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndividualApprovalStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(individualApprovalStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndividualApprovalStatus in the database
        List<IndividualApprovalStatus> individualApprovalStatusList = individualApprovalStatusRepository.findAll();
        assertThat(individualApprovalStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIndividualApprovalStatus() throws Exception {
        int databaseSizeBeforeUpdate = individualApprovalStatusRepository.findAll().size();
        individualApprovalStatus.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndividualApprovalStatusMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(individualApprovalStatus))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IndividualApprovalStatus in the database
        List<IndividualApprovalStatus> individualApprovalStatusList = individualApprovalStatusRepository.findAll();
        assertThat(individualApprovalStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIndividualApprovalStatus() throws Exception {
        // Initialize the database
        individualApprovalStatusRepository.saveAndFlush(individualApprovalStatus);

        int databaseSizeBeforeDelete = individualApprovalStatusRepository.findAll().size();

        // Delete the individualApprovalStatus
        restIndividualApprovalStatusMockMvc
            .perform(delete(ENTITY_API_URL_ID, individualApprovalStatus.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<IndividualApprovalStatus> individualApprovalStatusList = individualApprovalStatusRepository.findAll();
        assertThat(individualApprovalStatusList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
