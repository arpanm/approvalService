package com.simplify.approval.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.simplify.approval.IntegrationTest;
import com.simplify.approval.domain.ApprovalRequest;
import com.simplify.approval.domain.enumeration.ApprovalType;
import com.simplify.approval.domain.enumeration.Status;
import com.simplify.approval.repository.ApprovalRequestRepository;
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
 * Integration tests for the {@link ApprovalRequestResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ApprovalRequestResourceIT {

    private static final String DEFAULT_PROGRAM_ID = "AAAAAAAAAA";
    private static final String UPDATED_PROGRAM_ID = "BBBBBBBBBB";

    private static final ApprovalType DEFAULT_TYPE = ApprovalType.Job;
    private static final ApprovalType UPDATED_TYPE = ApprovalType.Offer;

    private static final String DEFAULT_APPROVE_CALL_BACK_URL = "AAAAAAAAAA";
    private static final String UPDATED_APPROVE_CALL_BACK_URL = "BBBBBBBBBB";

    private static final String DEFAULT_REJECT_CALL_BACK_URL = "AAAAAAAAAA";
    private static final String UPDATED_REJECT_CALL_BACK_URL = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_UPDATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final Status DEFAULT_FINAL_STATUS = Status.INIT;
    private static final Status UPDATED_FINAL_STATUS = Status.EMAIL_SEND;

    private static final String ENTITY_API_URL = "/api/approval-requests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ApprovalRequestRepository approvalRequestRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApprovalRequestMockMvc;

    private ApprovalRequest approvalRequest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApprovalRequest createEntity(EntityManager em) {
        ApprovalRequest approvalRequest = new ApprovalRequest()
            .programId(DEFAULT_PROGRAM_ID)
            .type(DEFAULT_TYPE)
            .approveCallBackUrl(DEFAULT_APPROVE_CALL_BACK_URL)
            .rejectCallBackUrl(DEFAULT_REJECT_CALL_BACK_URL)
            .createdBy(DEFAULT_CREATED_BY)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedAt(DEFAULT_UPDATED_AT)
            .finalStatus(DEFAULT_FINAL_STATUS);
        return approvalRequest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApprovalRequest createUpdatedEntity(EntityManager em) {
        ApprovalRequest approvalRequest = new ApprovalRequest()
            .programId(UPDATED_PROGRAM_ID)
            .type(UPDATED_TYPE)
            .approveCallBackUrl(UPDATED_APPROVE_CALL_BACK_URL)
            .rejectCallBackUrl(UPDATED_REJECT_CALL_BACK_URL)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .finalStatus(UPDATED_FINAL_STATUS);
        return approvalRequest;
    }

    @BeforeEach
    public void initTest() {
        approvalRequest = createEntity(em);
    }

    @Test
    @Transactional
    void createApprovalRequest() throws Exception {
        int databaseSizeBeforeCreate = approvalRequestRepository.findAll().size();
        // Create the ApprovalRequest
        restApprovalRequestMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(approvalRequest))
            )
            .andExpect(status().isCreated());

        // Validate the ApprovalRequest in the database
        List<ApprovalRequest> approvalRequestList = approvalRequestRepository.findAll();
        assertThat(approvalRequestList).hasSize(databaseSizeBeforeCreate + 1);
        ApprovalRequest testApprovalRequest = approvalRequestList.get(approvalRequestList.size() - 1);
        assertThat(testApprovalRequest.getProgramId()).isEqualTo(DEFAULT_PROGRAM_ID);
        assertThat(testApprovalRequest.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testApprovalRequest.getApproveCallBackUrl()).isEqualTo(DEFAULT_APPROVE_CALL_BACK_URL);
        assertThat(testApprovalRequest.getRejectCallBackUrl()).isEqualTo(DEFAULT_REJECT_CALL_BACK_URL);
        assertThat(testApprovalRequest.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testApprovalRequest.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testApprovalRequest.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testApprovalRequest.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testApprovalRequest.getFinalStatus()).isEqualTo(DEFAULT_FINAL_STATUS);
    }

    @Test
    @Transactional
    void createApprovalRequestWithExistingId() throws Exception {
        // Create the ApprovalRequest with an existing ID
        approvalRequest.setId(1L);

        int databaseSizeBeforeCreate = approvalRequestRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApprovalRequestMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(approvalRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApprovalRequest in the database
        List<ApprovalRequest> approvalRequestList = approvalRequestRepository.findAll();
        assertThat(approvalRequestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkProgramIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = approvalRequestRepository.findAll().size();
        // set the field null
        approvalRequest.setProgramId(null);

        // Create the ApprovalRequest, which fails.

        restApprovalRequestMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(approvalRequest))
            )
            .andExpect(status().isBadRequest());

        List<ApprovalRequest> approvalRequestList = approvalRequestRepository.findAll();
        assertThat(approvalRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = approvalRequestRepository.findAll().size();
        // set the field null
        approvalRequest.setType(null);

        // Create the ApprovalRequest, which fails.

        restApprovalRequestMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(approvalRequest))
            )
            .andExpect(status().isBadRequest());

        List<ApprovalRequest> approvalRequestList = approvalRequestRepository.findAll();
        assertThat(approvalRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllApprovalRequests() throws Exception {
        // Initialize the database
        approvalRequestRepository.saveAndFlush(approvalRequest);

        // Get all the approvalRequestList
        restApprovalRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(approvalRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].programId").value(hasItem(DEFAULT_PROGRAM_ID)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].approveCallBackUrl").value(hasItem(DEFAULT_APPROVE_CALL_BACK_URL)))
            .andExpect(jsonPath("$.[*].rejectCallBackUrl").value(hasItem(DEFAULT_REJECT_CALL_BACK_URL)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].finalStatus").value(hasItem(DEFAULT_FINAL_STATUS.toString())));
    }

    @Test
    @Transactional
    void getApprovalRequest() throws Exception {
        // Initialize the database
        approvalRequestRepository.saveAndFlush(approvalRequest);

        // Get the approvalRequest
        restApprovalRequestMockMvc
            .perform(get(ENTITY_API_URL_ID, approvalRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(approvalRequest.getId().intValue()))
            .andExpect(jsonPath("$.programId").value(DEFAULT_PROGRAM_ID))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.approveCallBackUrl").value(DEFAULT_APPROVE_CALL_BACK_URL))
            .andExpect(jsonPath("$.rejectCallBackUrl").value(DEFAULT_REJECT_CALL_BACK_URL))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.finalStatus").value(DEFAULT_FINAL_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingApprovalRequest() throws Exception {
        // Get the approvalRequest
        restApprovalRequestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewApprovalRequest() throws Exception {
        // Initialize the database
        approvalRequestRepository.saveAndFlush(approvalRequest);

        int databaseSizeBeforeUpdate = approvalRequestRepository.findAll().size();

        // Update the approvalRequest
        ApprovalRequest updatedApprovalRequest = approvalRequestRepository.findById(approvalRequest.getId()).get();
        // Disconnect from session so that the updates on updatedApprovalRequest are not directly saved in db
        em.detach(updatedApprovalRequest);
        updatedApprovalRequest
            .programId(UPDATED_PROGRAM_ID)
            .type(UPDATED_TYPE)
            .approveCallBackUrl(UPDATED_APPROVE_CALL_BACK_URL)
            .rejectCallBackUrl(UPDATED_REJECT_CALL_BACK_URL)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .finalStatus(UPDATED_FINAL_STATUS);

        restApprovalRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedApprovalRequest.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedApprovalRequest))
            )
            .andExpect(status().isOk());

        // Validate the ApprovalRequest in the database
        List<ApprovalRequest> approvalRequestList = approvalRequestRepository.findAll();
        assertThat(approvalRequestList).hasSize(databaseSizeBeforeUpdate);
        ApprovalRequest testApprovalRequest = approvalRequestList.get(approvalRequestList.size() - 1);
        assertThat(testApprovalRequest.getProgramId()).isEqualTo(UPDATED_PROGRAM_ID);
        assertThat(testApprovalRequest.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testApprovalRequest.getApproveCallBackUrl()).isEqualTo(UPDATED_APPROVE_CALL_BACK_URL);
        assertThat(testApprovalRequest.getRejectCallBackUrl()).isEqualTo(UPDATED_REJECT_CALL_BACK_URL);
        assertThat(testApprovalRequest.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testApprovalRequest.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testApprovalRequest.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testApprovalRequest.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testApprovalRequest.getFinalStatus()).isEqualTo(UPDATED_FINAL_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingApprovalRequest() throws Exception {
        int databaseSizeBeforeUpdate = approvalRequestRepository.findAll().size();
        approvalRequest.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApprovalRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, approvalRequest.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(approvalRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApprovalRequest in the database
        List<ApprovalRequest> approvalRequestList = approvalRequestRepository.findAll();
        assertThat(approvalRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchApprovalRequest() throws Exception {
        int databaseSizeBeforeUpdate = approvalRequestRepository.findAll().size();
        approvalRequest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApprovalRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(approvalRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApprovalRequest in the database
        List<ApprovalRequest> approvalRequestList = approvalRequestRepository.findAll();
        assertThat(approvalRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApprovalRequest() throws Exception {
        int databaseSizeBeforeUpdate = approvalRequestRepository.findAll().size();
        approvalRequest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApprovalRequestMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(approvalRequest))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApprovalRequest in the database
        List<ApprovalRequest> approvalRequestList = approvalRequestRepository.findAll();
        assertThat(approvalRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateApprovalRequestWithPatch() throws Exception {
        // Initialize the database
        approvalRequestRepository.saveAndFlush(approvalRequest);

        int databaseSizeBeforeUpdate = approvalRequestRepository.findAll().size();

        // Update the approvalRequest using partial update
        ApprovalRequest partialUpdatedApprovalRequest = new ApprovalRequest();
        partialUpdatedApprovalRequest.setId(approvalRequest.getId());

        partialUpdatedApprovalRequest.createdBy(UPDATED_CREATED_BY);

        restApprovalRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApprovalRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApprovalRequest))
            )
            .andExpect(status().isOk());

        // Validate the ApprovalRequest in the database
        List<ApprovalRequest> approvalRequestList = approvalRequestRepository.findAll();
        assertThat(approvalRequestList).hasSize(databaseSizeBeforeUpdate);
        ApprovalRequest testApprovalRequest = approvalRequestList.get(approvalRequestList.size() - 1);
        assertThat(testApprovalRequest.getProgramId()).isEqualTo(DEFAULT_PROGRAM_ID);
        assertThat(testApprovalRequest.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testApprovalRequest.getApproveCallBackUrl()).isEqualTo(DEFAULT_APPROVE_CALL_BACK_URL);
        assertThat(testApprovalRequest.getRejectCallBackUrl()).isEqualTo(DEFAULT_REJECT_CALL_BACK_URL);
        assertThat(testApprovalRequest.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testApprovalRequest.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testApprovalRequest.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testApprovalRequest.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testApprovalRequest.getFinalStatus()).isEqualTo(DEFAULT_FINAL_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateApprovalRequestWithPatch() throws Exception {
        // Initialize the database
        approvalRequestRepository.saveAndFlush(approvalRequest);

        int databaseSizeBeforeUpdate = approvalRequestRepository.findAll().size();

        // Update the approvalRequest using partial update
        ApprovalRequest partialUpdatedApprovalRequest = new ApprovalRequest();
        partialUpdatedApprovalRequest.setId(approvalRequest.getId());

        partialUpdatedApprovalRequest
            .programId(UPDATED_PROGRAM_ID)
            .type(UPDATED_TYPE)
            .approveCallBackUrl(UPDATED_APPROVE_CALL_BACK_URL)
            .rejectCallBackUrl(UPDATED_REJECT_CALL_BACK_URL)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .finalStatus(UPDATED_FINAL_STATUS);

        restApprovalRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApprovalRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApprovalRequest))
            )
            .andExpect(status().isOk());

        // Validate the ApprovalRequest in the database
        List<ApprovalRequest> approvalRequestList = approvalRequestRepository.findAll();
        assertThat(approvalRequestList).hasSize(databaseSizeBeforeUpdate);
        ApprovalRequest testApprovalRequest = approvalRequestList.get(approvalRequestList.size() - 1);
        assertThat(testApprovalRequest.getProgramId()).isEqualTo(UPDATED_PROGRAM_ID);
        assertThat(testApprovalRequest.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testApprovalRequest.getApproveCallBackUrl()).isEqualTo(UPDATED_APPROVE_CALL_BACK_URL);
        assertThat(testApprovalRequest.getRejectCallBackUrl()).isEqualTo(UPDATED_REJECT_CALL_BACK_URL);
        assertThat(testApprovalRequest.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testApprovalRequest.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testApprovalRequest.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testApprovalRequest.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testApprovalRequest.getFinalStatus()).isEqualTo(UPDATED_FINAL_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingApprovalRequest() throws Exception {
        int databaseSizeBeforeUpdate = approvalRequestRepository.findAll().size();
        approvalRequest.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApprovalRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, approvalRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(approvalRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApprovalRequest in the database
        List<ApprovalRequest> approvalRequestList = approvalRequestRepository.findAll();
        assertThat(approvalRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApprovalRequest() throws Exception {
        int databaseSizeBeforeUpdate = approvalRequestRepository.findAll().size();
        approvalRequest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApprovalRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(approvalRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApprovalRequest in the database
        List<ApprovalRequest> approvalRequestList = approvalRequestRepository.findAll();
        assertThat(approvalRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApprovalRequest() throws Exception {
        int databaseSizeBeforeUpdate = approvalRequestRepository.findAll().size();
        approvalRequest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApprovalRequestMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(approvalRequest))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApprovalRequest in the database
        List<ApprovalRequest> approvalRequestList = approvalRequestRepository.findAll();
        assertThat(approvalRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteApprovalRequest() throws Exception {
        // Initialize the database
        approvalRequestRepository.saveAndFlush(approvalRequest);

        int databaseSizeBeforeDelete = approvalRequestRepository.findAll().size();

        // Delete the approvalRequest
        restApprovalRequestMockMvc
            .perform(delete(ENTITY_API_URL_ID, approvalRequest.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ApprovalRequest> approvalRequestList = approvalRequestRepository.findAll();
        assertThat(approvalRequestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
