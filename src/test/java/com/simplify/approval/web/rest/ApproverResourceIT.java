package com.simplify.approval.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.simplify.approval.IntegrationTest;
import com.simplify.approval.domain.Approver;
import com.simplify.approval.repository.ApproverRepository;
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
 * Integration tests for the {@link ApproverResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ApproverResourceIT {

    private static final String DEFAULT_PROGRAM_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_PROGRAM_USER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Integer DEFAULT_LEVEL = 1;
    private static final Integer UPDATED_LEVEL = 2;

    private static final String ENTITY_API_URL = "/api/approvers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ApproverRepository approverRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApproverMockMvc;

    private Approver approver;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Approver createEntity(EntityManager em) {
        Approver approver = new Approver().programUserId(DEFAULT_PROGRAM_USER_ID).email(DEFAULT_EMAIL).level(DEFAULT_LEVEL);
        return approver;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Approver createUpdatedEntity(EntityManager em) {
        Approver approver = new Approver().programUserId(UPDATED_PROGRAM_USER_ID).email(UPDATED_EMAIL).level(UPDATED_LEVEL);
        return approver;
    }

    @BeforeEach
    public void initTest() {
        approver = createEntity(em);
    }

    @Test
    @Transactional
    void createApprover() throws Exception {
        int databaseSizeBeforeCreate = approverRepository.findAll().size();
        // Create the Approver
        restApproverMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(approver)))
            .andExpect(status().isCreated());

        // Validate the Approver in the database
        List<Approver> approverList = approverRepository.findAll();
        assertThat(approverList).hasSize(databaseSizeBeforeCreate + 1);
        Approver testApprover = approverList.get(approverList.size() - 1);
        assertThat(testApprover.getProgramUserId()).isEqualTo(DEFAULT_PROGRAM_USER_ID);
        assertThat(testApprover.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testApprover.getLevel()).isEqualTo(DEFAULT_LEVEL);
    }

    @Test
    @Transactional
    void createApproverWithExistingId() throws Exception {
        // Create the Approver with an existing ID
        approver.setId(1L);

        int databaseSizeBeforeCreate = approverRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApproverMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(approver)))
            .andExpect(status().isBadRequest());

        // Validate the Approver in the database
        List<Approver> approverList = approverRepository.findAll();
        assertThat(approverList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkProgramUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = approverRepository.findAll().size();
        // set the field null
        approver.setProgramUserId(null);

        // Create the Approver, which fails.

        restApproverMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(approver)))
            .andExpect(status().isBadRequest());

        List<Approver> approverList = approverRepository.findAll();
        assertThat(approverList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = approverRepository.findAll().size();
        // set the field null
        approver.setEmail(null);

        // Create the Approver, which fails.

        restApproverMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(approver)))
            .andExpect(status().isBadRequest());

        List<Approver> approverList = approverRepository.findAll();
        assertThat(approverList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = approverRepository.findAll().size();
        // set the field null
        approver.setLevel(null);

        // Create the Approver, which fails.

        restApproverMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(approver)))
            .andExpect(status().isBadRequest());

        List<Approver> approverList = approverRepository.findAll();
        assertThat(approverList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllApprovers() throws Exception {
        // Initialize the database
        approverRepository.saveAndFlush(approver);

        // Get all the approverList
        restApproverMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(approver.getId().intValue())))
            .andExpect(jsonPath("$.[*].programUserId").value(hasItem(DEFAULT_PROGRAM_USER_ID)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)));
    }

    @Test
    @Transactional
    void getApprover() throws Exception {
        // Initialize the database
        approverRepository.saveAndFlush(approver);

        // Get the approver
        restApproverMockMvc
            .perform(get(ENTITY_API_URL_ID, approver.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(approver.getId().intValue()))
            .andExpect(jsonPath("$.programUserId").value(DEFAULT_PROGRAM_USER_ID))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL));
    }

    @Test
    @Transactional
    void getNonExistingApprover() throws Exception {
        // Get the approver
        restApproverMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewApprover() throws Exception {
        // Initialize the database
        approverRepository.saveAndFlush(approver);

        int databaseSizeBeforeUpdate = approverRepository.findAll().size();

        // Update the approver
        Approver updatedApprover = approverRepository.findById(approver.getId()).get();
        // Disconnect from session so that the updates on updatedApprover are not directly saved in db
        em.detach(updatedApprover);
        updatedApprover.programUserId(UPDATED_PROGRAM_USER_ID).email(UPDATED_EMAIL).level(UPDATED_LEVEL);

        restApproverMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedApprover.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedApprover))
            )
            .andExpect(status().isOk());

        // Validate the Approver in the database
        List<Approver> approverList = approverRepository.findAll();
        assertThat(approverList).hasSize(databaseSizeBeforeUpdate);
        Approver testApprover = approverList.get(approverList.size() - 1);
        assertThat(testApprover.getProgramUserId()).isEqualTo(UPDATED_PROGRAM_USER_ID);
        assertThat(testApprover.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testApprover.getLevel()).isEqualTo(UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void putNonExistingApprover() throws Exception {
        int databaseSizeBeforeUpdate = approverRepository.findAll().size();
        approver.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApproverMockMvc
            .perform(
                put(ENTITY_API_URL_ID, approver.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(approver))
            )
            .andExpect(status().isBadRequest());

        // Validate the Approver in the database
        List<Approver> approverList = approverRepository.findAll();
        assertThat(approverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchApprover() throws Exception {
        int databaseSizeBeforeUpdate = approverRepository.findAll().size();
        approver.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApproverMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(approver))
            )
            .andExpect(status().isBadRequest());

        // Validate the Approver in the database
        List<Approver> approverList = approverRepository.findAll();
        assertThat(approverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApprover() throws Exception {
        int databaseSizeBeforeUpdate = approverRepository.findAll().size();
        approver.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApproverMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(approver)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Approver in the database
        List<Approver> approverList = approverRepository.findAll();
        assertThat(approverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateApproverWithPatch() throws Exception {
        // Initialize the database
        approverRepository.saveAndFlush(approver);

        int databaseSizeBeforeUpdate = approverRepository.findAll().size();

        // Update the approver using partial update
        Approver partialUpdatedApprover = new Approver();
        partialUpdatedApprover.setId(approver.getId());

        partialUpdatedApprover.programUserId(UPDATED_PROGRAM_USER_ID).email(UPDATED_EMAIL);

        restApproverMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApprover.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApprover))
            )
            .andExpect(status().isOk());

        // Validate the Approver in the database
        List<Approver> approverList = approverRepository.findAll();
        assertThat(approverList).hasSize(databaseSizeBeforeUpdate);
        Approver testApprover = approverList.get(approverList.size() - 1);
        assertThat(testApprover.getProgramUserId()).isEqualTo(UPDATED_PROGRAM_USER_ID);
        assertThat(testApprover.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testApprover.getLevel()).isEqualTo(DEFAULT_LEVEL);
    }

    @Test
    @Transactional
    void fullUpdateApproverWithPatch() throws Exception {
        // Initialize the database
        approverRepository.saveAndFlush(approver);

        int databaseSizeBeforeUpdate = approverRepository.findAll().size();

        // Update the approver using partial update
        Approver partialUpdatedApprover = new Approver();
        partialUpdatedApprover.setId(approver.getId());

        partialUpdatedApprover.programUserId(UPDATED_PROGRAM_USER_ID).email(UPDATED_EMAIL).level(UPDATED_LEVEL);

        restApproverMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApprover.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApprover))
            )
            .andExpect(status().isOk());

        // Validate the Approver in the database
        List<Approver> approverList = approverRepository.findAll();
        assertThat(approverList).hasSize(databaseSizeBeforeUpdate);
        Approver testApprover = approverList.get(approverList.size() - 1);
        assertThat(testApprover.getProgramUserId()).isEqualTo(UPDATED_PROGRAM_USER_ID);
        assertThat(testApprover.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testApprover.getLevel()).isEqualTo(UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void patchNonExistingApprover() throws Exception {
        int databaseSizeBeforeUpdate = approverRepository.findAll().size();
        approver.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApproverMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, approver.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(approver))
            )
            .andExpect(status().isBadRequest());

        // Validate the Approver in the database
        List<Approver> approverList = approverRepository.findAll();
        assertThat(approverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApprover() throws Exception {
        int databaseSizeBeforeUpdate = approverRepository.findAll().size();
        approver.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApproverMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(approver))
            )
            .andExpect(status().isBadRequest());

        // Validate the Approver in the database
        List<Approver> approverList = approverRepository.findAll();
        assertThat(approverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApprover() throws Exception {
        int databaseSizeBeforeUpdate = approverRepository.findAll().size();
        approver.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApproverMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(approver)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Approver in the database
        List<Approver> approverList = approverRepository.findAll();
        assertThat(approverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteApprover() throws Exception {
        // Initialize the database
        approverRepository.saveAndFlush(approver);

        int databaseSizeBeforeDelete = approverRepository.findAll().size();

        // Delete the approver
        restApproverMockMvc
            .perform(delete(ENTITY_API_URL_ID, approver.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Approver> approverList = approverRepository.findAll();
        assertThat(approverList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
