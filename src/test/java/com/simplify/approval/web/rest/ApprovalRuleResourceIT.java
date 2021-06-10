package com.simplify.approval.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.simplify.approval.IntegrationTest;
import com.simplify.approval.domain.ApprovalRule;
import com.simplify.approval.domain.enumeration.ApprovalType;
import com.simplify.approval.repository.ApprovalRuleRepository;
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
 * Integration tests for the {@link ApprovalRuleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ApprovalRuleResourceIT {

    private static final String DEFAULT_PROGRAM_ID = "AAAAAAAAAA";
    private static final String UPDATED_PROGRAM_ID = "BBBBBBBBBB";

    private static final ApprovalType DEFAULT_TYPE = ApprovalType.Job;
    private static final ApprovalType UPDATED_TYPE = ApprovalType.Offer;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_UPDATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/approval-rules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ApprovalRuleRepository approvalRuleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApprovalRuleMockMvc;

    private ApprovalRule approvalRule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApprovalRule createEntity(EntityManager em) {
        ApprovalRule approvalRule = new ApprovalRule()
            .programId(DEFAULT_PROGRAM_ID)
            .type(DEFAULT_TYPE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedAt(DEFAULT_UPDATED_AT);
        return approvalRule;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApprovalRule createUpdatedEntity(EntityManager em) {
        ApprovalRule approvalRule = new ApprovalRule()
            .programId(UPDATED_PROGRAM_ID)
            .type(UPDATED_TYPE)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT);
        return approvalRule;
    }

    @BeforeEach
    public void initTest() {
        approvalRule = createEntity(em);
    }

    @Test
    @Transactional
    void createApprovalRule() throws Exception {
        int databaseSizeBeforeCreate = approvalRuleRepository.findAll().size();
        // Create the ApprovalRule
        restApprovalRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(approvalRule)))
            .andExpect(status().isCreated());

        // Validate the ApprovalRule in the database
        List<ApprovalRule> approvalRuleList = approvalRuleRepository.findAll();
        assertThat(approvalRuleList).hasSize(databaseSizeBeforeCreate + 1);
        ApprovalRule testApprovalRule = approvalRuleList.get(approvalRuleList.size() - 1);
        assertThat(testApprovalRule.getProgramId()).isEqualTo(DEFAULT_PROGRAM_ID);
        assertThat(testApprovalRule.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testApprovalRule.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testApprovalRule.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testApprovalRule.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testApprovalRule.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createApprovalRuleWithExistingId() throws Exception {
        // Create the ApprovalRule with an existing ID
        approvalRule.setId(1L);

        int databaseSizeBeforeCreate = approvalRuleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApprovalRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(approvalRule)))
            .andExpect(status().isBadRequest());

        // Validate the ApprovalRule in the database
        List<ApprovalRule> approvalRuleList = approvalRuleRepository.findAll();
        assertThat(approvalRuleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkProgramIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = approvalRuleRepository.findAll().size();
        // set the field null
        approvalRule.setProgramId(null);

        // Create the ApprovalRule, which fails.

        restApprovalRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(approvalRule)))
            .andExpect(status().isBadRequest());

        List<ApprovalRule> approvalRuleList = approvalRuleRepository.findAll();
        assertThat(approvalRuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = approvalRuleRepository.findAll().size();
        // set the field null
        approvalRule.setType(null);

        // Create the ApprovalRule, which fails.

        restApprovalRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(approvalRule)))
            .andExpect(status().isBadRequest());

        List<ApprovalRule> approvalRuleList = approvalRuleRepository.findAll();
        assertThat(approvalRuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllApprovalRules() throws Exception {
        // Initialize the database
        approvalRuleRepository.saveAndFlush(approvalRule);

        // Get all the approvalRuleList
        restApprovalRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(approvalRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].programId").value(hasItem(DEFAULT_PROGRAM_ID)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getApprovalRule() throws Exception {
        // Initialize the database
        approvalRuleRepository.saveAndFlush(approvalRule);

        // Get the approvalRule
        restApprovalRuleMockMvc
            .perform(get(ENTITY_API_URL_ID, approvalRule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(approvalRule.getId().intValue()))
            .andExpect(jsonPath("$.programId").value(DEFAULT_PROGRAM_ID))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingApprovalRule() throws Exception {
        // Get the approvalRule
        restApprovalRuleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewApprovalRule() throws Exception {
        // Initialize the database
        approvalRuleRepository.saveAndFlush(approvalRule);

        int databaseSizeBeforeUpdate = approvalRuleRepository.findAll().size();

        // Update the approvalRule
        ApprovalRule updatedApprovalRule = approvalRuleRepository.findById(approvalRule.getId()).get();
        // Disconnect from session so that the updates on updatedApprovalRule are not directly saved in db
        em.detach(updatedApprovalRule);
        updatedApprovalRule
            .programId(UPDATED_PROGRAM_ID)
            .type(UPDATED_TYPE)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT);

        restApprovalRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedApprovalRule.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedApprovalRule))
            )
            .andExpect(status().isOk());

        // Validate the ApprovalRule in the database
        List<ApprovalRule> approvalRuleList = approvalRuleRepository.findAll();
        assertThat(approvalRuleList).hasSize(databaseSizeBeforeUpdate);
        ApprovalRule testApprovalRule = approvalRuleList.get(approvalRuleList.size() - 1);
        assertThat(testApprovalRule.getProgramId()).isEqualTo(UPDATED_PROGRAM_ID);
        assertThat(testApprovalRule.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testApprovalRule.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testApprovalRule.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testApprovalRule.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testApprovalRule.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingApprovalRule() throws Exception {
        int databaseSizeBeforeUpdate = approvalRuleRepository.findAll().size();
        approvalRule.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApprovalRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, approvalRule.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(approvalRule))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApprovalRule in the database
        List<ApprovalRule> approvalRuleList = approvalRuleRepository.findAll();
        assertThat(approvalRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchApprovalRule() throws Exception {
        int databaseSizeBeforeUpdate = approvalRuleRepository.findAll().size();
        approvalRule.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApprovalRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(approvalRule))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApprovalRule in the database
        List<ApprovalRule> approvalRuleList = approvalRuleRepository.findAll();
        assertThat(approvalRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApprovalRule() throws Exception {
        int databaseSizeBeforeUpdate = approvalRuleRepository.findAll().size();
        approvalRule.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApprovalRuleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(approvalRule)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApprovalRule in the database
        List<ApprovalRule> approvalRuleList = approvalRuleRepository.findAll();
        assertThat(approvalRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateApprovalRuleWithPatch() throws Exception {
        // Initialize the database
        approvalRuleRepository.saveAndFlush(approvalRule);

        int databaseSizeBeforeUpdate = approvalRuleRepository.findAll().size();

        // Update the approvalRule using partial update
        ApprovalRule partialUpdatedApprovalRule = new ApprovalRule();
        partialUpdatedApprovalRule.setId(approvalRule.getId());

        partialUpdatedApprovalRule.programId(UPDATED_PROGRAM_ID).type(UPDATED_TYPE).createdBy(UPDATED_CREATED_BY);

        restApprovalRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApprovalRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApprovalRule))
            )
            .andExpect(status().isOk());

        // Validate the ApprovalRule in the database
        List<ApprovalRule> approvalRuleList = approvalRuleRepository.findAll();
        assertThat(approvalRuleList).hasSize(databaseSizeBeforeUpdate);
        ApprovalRule testApprovalRule = approvalRuleList.get(approvalRuleList.size() - 1);
        assertThat(testApprovalRule.getProgramId()).isEqualTo(UPDATED_PROGRAM_ID);
        assertThat(testApprovalRule.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testApprovalRule.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testApprovalRule.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testApprovalRule.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testApprovalRule.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateApprovalRuleWithPatch() throws Exception {
        // Initialize the database
        approvalRuleRepository.saveAndFlush(approvalRule);

        int databaseSizeBeforeUpdate = approvalRuleRepository.findAll().size();

        // Update the approvalRule using partial update
        ApprovalRule partialUpdatedApprovalRule = new ApprovalRule();
        partialUpdatedApprovalRule.setId(approvalRule.getId());

        partialUpdatedApprovalRule
            .programId(UPDATED_PROGRAM_ID)
            .type(UPDATED_TYPE)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT);

        restApprovalRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApprovalRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApprovalRule))
            )
            .andExpect(status().isOk());

        // Validate the ApprovalRule in the database
        List<ApprovalRule> approvalRuleList = approvalRuleRepository.findAll();
        assertThat(approvalRuleList).hasSize(databaseSizeBeforeUpdate);
        ApprovalRule testApprovalRule = approvalRuleList.get(approvalRuleList.size() - 1);
        assertThat(testApprovalRule.getProgramId()).isEqualTo(UPDATED_PROGRAM_ID);
        assertThat(testApprovalRule.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testApprovalRule.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testApprovalRule.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testApprovalRule.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testApprovalRule.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingApprovalRule() throws Exception {
        int databaseSizeBeforeUpdate = approvalRuleRepository.findAll().size();
        approvalRule.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApprovalRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, approvalRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(approvalRule))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApprovalRule in the database
        List<ApprovalRule> approvalRuleList = approvalRuleRepository.findAll();
        assertThat(approvalRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApprovalRule() throws Exception {
        int databaseSizeBeforeUpdate = approvalRuleRepository.findAll().size();
        approvalRule.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApprovalRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(approvalRule))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApprovalRule in the database
        List<ApprovalRule> approvalRuleList = approvalRuleRepository.findAll();
        assertThat(approvalRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApprovalRule() throws Exception {
        int databaseSizeBeforeUpdate = approvalRuleRepository.findAll().size();
        approvalRule.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApprovalRuleMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(approvalRule))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApprovalRule in the database
        List<ApprovalRule> approvalRuleList = approvalRuleRepository.findAll();
        assertThat(approvalRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteApprovalRule() throws Exception {
        // Initialize the database
        approvalRuleRepository.saveAndFlush(approvalRule);

        int databaseSizeBeforeDelete = approvalRuleRepository.findAll().size();

        // Delete the approvalRule
        restApprovalRuleMockMvc
            .perform(delete(ENTITY_API_URL_ID, approvalRule.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ApprovalRule> approvalRuleList = approvalRuleRepository.findAll();
        assertThat(approvalRuleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
