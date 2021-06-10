package com.simplify.approval.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.simplify.approval.IntegrationTest;
import com.simplify.approval.domain.SubRule;
import com.simplify.approval.domain.enumeration.AppendType;
import com.simplify.approval.domain.enumeration.Condition;
import com.simplify.approval.domain.enumeration.DataType;
import com.simplify.approval.repository.SubRuleRepository;
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
 * Integration tests for the {@link SubRuleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SubRuleResourceIT {

    private static final String DEFAULT_FIELD_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_NAME = "BBBBBBBBBB";

    private static final DataType DEFAULT_DATA_TYPE = DataType.STR;
    private static final DataType UPDATED_DATA_TYPE = DataType.DEC;

    private static final Condition DEFAULT_CONDITION = Condition.EQUAL;
    private static final Condition UPDATED_CONDITION = Condition.IN;

    private static final Float DEFAULT_RANGE_MIN_VALUE = 1F;
    private static final Float UPDATED_RANGE_MIN_VALUE = 2F;

    private static final Float DEFAULT_RANGE_MAX_VALUE = 1F;
    private static final Float UPDATED_RANGE_MAX_VALUE = 2F;

    private static final String DEFAULT_EQUAL_STR_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_EQUAL_STR_VALUE = "BBBBBBBBBB";

    private static final Float DEFAULT_EQUAL_DEC_VALUE = 1F;
    private static final Float UPDATED_EQUAL_DEC_VALUE = 2F;

    private static final AppendType DEFAULT_APPEND_TYPE = AppendType.START;
    private static final AppendType UPDATED_APPEND_TYPE = AppendType.AND;

    private static final String ENTITY_API_URL = "/api/sub-rules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SubRuleRepository subRuleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubRuleMockMvc;

    private SubRule subRule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubRule createEntity(EntityManager em) {
        SubRule subRule = new SubRule()
            .fieldName(DEFAULT_FIELD_NAME)
            .dataType(DEFAULT_DATA_TYPE)
            .condition(DEFAULT_CONDITION)
            .rangeMinValue(DEFAULT_RANGE_MIN_VALUE)
            .rangeMaxValue(DEFAULT_RANGE_MAX_VALUE)
            .equalStrValue(DEFAULT_EQUAL_STR_VALUE)
            .equalDecValue(DEFAULT_EQUAL_DEC_VALUE)
            .appendType(DEFAULT_APPEND_TYPE);
        return subRule;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubRule createUpdatedEntity(EntityManager em) {
        SubRule subRule = new SubRule()
            .fieldName(UPDATED_FIELD_NAME)
            .dataType(UPDATED_DATA_TYPE)
            .condition(UPDATED_CONDITION)
            .rangeMinValue(UPDATED_RANGE_MIN_VALUE)
            .rangeMaxValue(UPDATED_RANGE_MAX_VALUE)
            .equalStrValue(UPDATED_EQUAL_STR_VALUE)
            .equalDecValue(UPDATED_EQUAL_DEC_VALUE)
            .appendType(UPDATED_APPEND_TYPE);
        return subRule;
    }

    @BeforeEach
    public void initTest() {
        subRule = createEntity(em);
    }

    @Test
    @Transactional
    void createSubRule() throws Exception {
        int databaseSizeBeforeCreate = subRuleRepository.findAll().size();
        // Create the SubRule
        restSubRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subRule)))
            .andExpect(status().isCreated());

        // Validate the SubRule in the database
        List<SubRule> subRuleList = subRuleRepository.findAll();
        assertThat(subRuleList).hasSize(databaseSizeBeforeCreate + 1);
        SubRule testSubRule = subRuleList.get(subRuleList.size() - 1);
        assertThat(testSubRule.getFieldName()).isEqualTo(DEFAULT_FIELD_NAME);
        assertThat(testSubRule.getDataType()).isEqualTo(DEFAULT_DATA_TYPE);
        assertThat(testSubRule.getCondition()).isEqualTo(DEFAULT_CONDITION);
        assertThat(testSubRule.getRangeMinValue()).isEqualTo(DEFAULT_RANGE_MIN_VALUE);
        assertThat(testSubRule.getRangeMaxValue()).isEqualTo(DEFAULT_RANGE_MAX_VALUE);
        assertThat(testSubRule.getEqualStrValue()).isEqualTo(DEFAULT_EQUAL_STR_VALUE);
        assertThat(testSubRule.getEqualDecValue()).isEqualTo(DEFAULT_EQUAL_DEC_VALUE);
        assertThat(testSubRule.getAppendType()).isEqualTo(DEFAULT_APPEND_TYPE);
    }

    @Test
    @Transactional
    void createSubRuleWithExistingId() throws Exception {
        // Create the SubRule with an existing ID
        subRule.setId(1L);

        int databaseSizeBeforeCreate = subRuleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subRule)))
            .andExpect(status().isBadRequest());

        // Validate the SubRule in the database
        List<SubRule> subRuleList = subRuleRepository.findAll();
        assertThat(subRuleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFieldNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = subRuleRepository.findAll().size();
        // set the field null
        subRule.setFieldName(null);

        // Create the SubRule, which fails.

        restSubRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subRule)))
            .andExpect(status().isBadRequest());

        List<SubRule> subRuleList = subRuleRepository.findAll();
        assertThat(subRuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDataTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = subRuleRepository.findAll().size();
        // set the field null
        subRule.setDataType(null);

        // Create the SubRule, which fails.

        restSubRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subRule)))
            .andExpect(status().isBadRequest());

        List<SubRule> subRuleList = subRuleRepository.findAll();
        assertThat(subRuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkConditionIsRequired() throws Exception {
        int databaseSizeBeforeTest = subRuleRepository.findAll().size();
        // set the field null
        subRule.setCondition(null);

        // Create the SubRule, which fails.

        restSubRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subRule)))
            .andExpect(status().isBadRequest());

        List<SubRule> subRuleList = subRuleRepository.findAll();
        assertThat(subRuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAppendTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = subRuleRepository.findAll().size();
        // set the field null
        subRule.setAppendType(null);

        // Create the SubRule, which fails.

        restSubRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subRule)))
            .andExpect(status().isBadRequest());

        List<SubRule> subRuleList = subRuleRepository.findAll();
        assertThat(subRuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSubRules() throws Exception {
        // Initialize the database
        subRuleRepository.saveAndFlush(subRule);

        // Get all the subRuleList
        restSubRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].fieldName").value(hasItem(DEFAULT_FIELD_NAME)))
            .andExpect(jsonPath("$.[*].dataType").value(hasItem(DEFAULT_DATA_TYPE.toString())))
            .andExpect(jsonPath("$.[*].condition").value(hasItem(DEFAULT_CONDITION.toString())))
            .andExpect(jsonPath("$.[*].rangeMinValue").value(hasItem(DEFAULT_RANGE_MIN_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].rangeMaxValue").value(hasItem(DEFAULT_RANGE_MAX_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].equalStrValue").value(hasItem(DEFAULT_EQUAL_STR_VALUE)))
            .andExpect(jsonPath("$.[*].equalDecValue").value(hasItem(DEFAULT_EQUAL_DEC_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].appendType").value(hasItem(DEFAULT_APPEND_TYPE.toString())));
    }

    @Test
    @Transactional
    void getSubRule() throws Exception {
        // Initialize the database
        subRuleRepository.saveAndFlush(subRule);

        // Get the subRule
        restSubRuleMockMvc
            .perform(get(ENTITY_API_URL_ID, subRule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subRule.getId().intValue()))
            .andExpect(jsonPath("$.fieldName").value(DEFAULT_FIELD_NAME))
            .andExpect(jsonPath("$.dataType").value(DEFAULT_DATA_TYPE.toString()))
            .andExpect(jsonPath("$.condition").value(DEFAULT_CONDITION.toString()))
            .andExpect(jsonPath("$.rangeMinValue").value(DEFAULT_RANGE_MIN_VALUE.doubleValue()))
            .andExpect(jsonPath("$.rangeMaxValue").value(DEFAULT_RANGE_MAX_VALUE.doubleValue()))
            .andExpect(jsonPath("$.equalStrValue").value(DEFAULT_EQUAL_STR_VALUE))
            .andExpect(jsonPath("$.equalDecValue").value(DEFAULT_EQUAL_DEC_VALUE.doubleValue()))
            .andExpect(jsonPath("$.appendType").value(DEFAULT_APPEND_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSubRule() throws Exception {
        // Get the subRule
        restSubRuleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSubRule() throws Exception {
        // Initialize the database
        subRuleRepository.saveAndFlush(subRule);

        int databaseSizeBeforeUpdate = subRuleRepository.findAll().size();

        // Update the subRule
        SubRule updatedSubRule = subRuleRepository.findById(subRule.getId()).get();
        // Disconnect from session so that the updates on updatedSubRule are not directly saved in db
        em.detach(updatedSubRule);
        updatedSubRule
            .fieldName(UPDATED_FIELD_NAME)
            .dataType(UPDATED_DATA_TYPE)
            .condition(UPDATED_CONDITION)
            .rangeMinValue(UPDATED_RANGE_MIN_VALUE)
            .rangeMaxValue(UPDATED_RANGE_MAX_VALUE)
            .equalStrValue(UPDATED_EQUAL_STR_VALUE)
            .equalDecValue(UPDATED_EQUAL_DEC_VALUE)
            .appendType(UPDATED_APPEND_TYPE);

        restSubRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSubRule.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSubRule))
            )
            .andExpect(status().isOk());

        // Validate the SubRule in the database
        List<SubRule> subRuleList = subRuleRepository.findAll();
        assertThat(subRuleList).hasSize(databaseSizeBeforeUpdate);
        SubRule testSubRule = subRuleList.get(subRuleList.size() - 1);
        assertThat(testSubRule.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);
        assertThat(testSubRule.getDataType()).isEqualTo(UPDATED_DATA_TYPE);
        assertThat(testSubRule.getCondition()).isEqualTo(UPDATED_CONDITION);
        assertThat(testSubRule.getRangeMinValue()).isEqualTo(UPDATED_RANGE_MIN_VALUE);
        assertThat(testSubRule.getRangeMaxValue()).isEqualTo(UPDATED_RANGE_MAX_VALUE);
        assertThat(testSubRule.getEqualStrValue()).isEqualTo(UPDATED_EQUAL_STR_VALUE);
        assertThat(testSubRule.getEqualDecValue()).isEqualTo(UPDATED_EQUAL_DEC_VALUE);
        assertThat(testSubRule.getAppendType()).isEqualTo(UPDATED_APPEND_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingSubRule() throws Exception {
        int databaseSizeBeforeUpdate = subRuleRepository.findAll().size();
        subRule.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subRule.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subRule))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubRule in the database
        List<SubRule> subRuleList = subRuleRepository.findAll();
        assertThat(subRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSubRule() throws Exception {
        int databaseSizeBeforeUpdate = subRuleRepository.findAll().size();
        subRule.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subRule))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubRule in the database
        List<SubRule> subRuleList = subRuleRepository.findAll();
        assertThat(subRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSubRule() throws Exception {
        int databaseSizeBeforeUpdate = subRuleRepository.findAll().size();
        subRule.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubRuleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subRule)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubRule in the database
        List<SubRule> subRuleList = subRuleRepository.findAll();
        assertThat(subRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSubRuleWithPatch() throws Exception {
        // Initialize the database
        subRuleRepository.saveAndFlush(subRule);

        int databaseSizeBeforeUpdate = subRuleRepository.findAll().size();

        // Update the subRule using partial update
        SubRule partialUpdatedSubRule = new SubRule();
        partialUpdatedSubRule.setId(subRule.getId());

        partialUpdatedSubRule
            .fieldName(UPDATED_FIELD_NAME)
            .condition(UPDATED_CONDITION)
            .equalStrValue(UPDATED_EQUAL_STR_VALUE)
            .equalDecValue(UPDATED_EQUAL_DEC_VALUE);

        restSubRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubRule))
            )
            .andExpect(status().isOk());

        // Validate the SubRule in the database
        List<SubRule> subRuleList = subRuleRepository.findAll();
        assertThat(subRuleList).hasSize(databaseSizeBeforeUpdate);
        SubRule testSubRule = subRuleList.get(subRuleList.size() - 1);
        assertThat(testSubRule.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);
        assertThat(testSubRule.getDataType()).isEqualTo(DEFAULT_DATA_TYPE);
        assertThat(testSubRule.getCondition()).isEqualTo(UPDATED_CONDITION);
        assertThat(testSubRule.getRangeMinValue()).isEqualTo(DEFAULT_RANGE_MIN_VALUE);
        assertThat(testSubRule.getRangeMaxValue()).isEqualTo(DEFAULT_RANGE_MAX_VALUE);
        assertThat(testSubRule.getEqualStrValue()).isEqualTo(UPDATED_EQUAL_STR_VALUE);
        assertThat(testSubRule.getEqualDecValue()).isEqualTo(UPDATED_EQUAL_DEC_VALUE);
        assertThat(testSubRule.getAppendType()).isEqualTo(DEFAULT_APPEND_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateSubRuleWithPatch() throws Exception {
        // Initialize the database
        subRuleRepository.saveAndFlush(subRule);

        int databaseSizeBeforeUpdate = subRuleRepository.findAll().size();

        // Update the subRule using partial update
        SubRule partialUpdatedSubRule = new SubRule();
        partialUpdatedSubRule.setId(subRule.getId());

        partialUpdatedSubRule
            .fieldName(UPDATED_FIELD_NAME)
            .dataType(UPDATED_DATA_TYPE)
            .condition(UPDATED_CONDITION)
            .rangeMinValue(UPDATED_RANGE_MIN_VALUE)
            .rangeMaxValue(UPDATED_RANGE_MAX_VALUE)
            .equalStrValue(UPDATED_EQUAL_STR_VALUE)
            .equalDecValue(UPDATED_EQUAL_DEC_VALUE)
            .appendType(UPDATED_APPEND_TYPE);

        restSubRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubRule))
            )
            .andExpect(status().isOk());

        // Validate the SubRule in the database
        List<SubRule> subRuleList = subRuleRepository.findAll();
        assertThat(subRuleList).hasSize(databaseSizeBeforeUpdate);
        SubRule testSubRule = subRuleList.get(subRuleList.size() - 1);
        assertThat(testSubRule.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);
        assertThat(testSubRule.getDataType()).isEqualTo(UPDATED_DATA_TYPE);
        assertThat(testSubRule.getCondition()).isEqualTo(UPDATED_CONDITION);
        assertThat(testSubRule.getRangeMinValue()).isEqualTo(UPDATED_RANGE_MIN_VALUE);
        assertThat(testSubRule.getRangeMaxValue()).isEqualTo(UPDATED_RANGE_MAX_VALUE);
        assertThat(testSubRule.getEqualStrValue()).isEqualTo(UPDATED_EQUAL_STR_VALUE);
        assertThat(testSubRule.getEqualDecValue()).isEqualTo(UPDATED_EQUAL_DEC_VALUE);
        assertThat(testSubRule.getAppendType()).isEqualTo(UPDATED_APPEND_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingSubRule() throws Exception {
        int databaseSizeBeforeUpdate = subRuleRepository.findAll().size();
        subRule.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, subRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subRule))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubRule in the database
        List<SubRule> subRuleList = subRuleRepository.findAll();
        assertThat(subRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSubRule() throws Exception {
        int databaseSizeBeforeUpdate = subRuleRepository.findAll().size();
        subRule.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subRule))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubRule in the database
        List<SubRule> subRuleList = subRuleRepository.findAll();
        assertThat(subRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSubRule() throws Exception {
        int databaseSizeBeforeUpdate = subRuleRepository.findAll().size();
        subRule.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubRuleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(subRule)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubRule in the database
        List<SubRule> subRuleList = subRuleRepository.findAll();
        assertThat(subRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSubRule() throws Exception {
        // Initialize the database
        subRuleRepository.saveAndFlush(subRule);

        int databaseSizeBeforeDelete = subRuleRepository.findAll().size();

        // Delete the subRule
        restSubRuleMockMvc
            .perform(delete(ENTITY_API_URL_ID, subRule.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SubRule> subRuleList = subRuleRepository.findAll();
        assertThat(subRuleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
