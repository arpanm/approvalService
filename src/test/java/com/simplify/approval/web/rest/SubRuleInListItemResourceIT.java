package com.simplify.approval.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.simplify.approval.IntegrationTest;
import com.simplify.approval.domain.SubRuleInListItem;
import com.simplify.approval.repository.SubRuleInListItemRepository;
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
 * Integration tests for the {@link SubRuleInListItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SubRuleInListItemResourceIT {

    private static final String DEFAULT_STR_ITEM = "AAAAAAAAAA";
    private static final String UPDATED_STR_ITEM = "BBBBBBBBBB";

    private static final Float DEFAULT_DEC_ITEM = 1F;
    private static final Float UPDATED_DEC_ITEM = 2F;

    private static final String ENTITY_API_URL = "/api/sub-rule-in-list-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SubRuleInListItemRepository subRuleInListItemRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubRuleInListItemMockMvc;

    private SubRuleInListItem subRuleInListItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubRuleInListItem createEntity(EntityManager em) {
        SubRuleInListItem subRuleInListItem = new SubRuleInListItem().strItem(DEFAULT_STR_ITEM).decItem(DEFAULT_DEC_ITEM);
        return subRuleInListItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubRuleInListItem createUpdatedEntity(EntityManager em) {
        SubRuleInListItem subRuleInListItem = new SubRuleInListItem().strItem(UPDATED_STR_ITEM).decItem(UPDATED_DEC_ITEM);
        return subRuleInListItem;
    }

    @BeforeEach
    public void initTest() {
        subRuleInListItem = createEntity(em);
    }

    @Test
    @Transactional
    void createSubRuleInListItem() throws Exception {
        int databaseSizeBeforeCreate = subRuleInListItemRepository.findAll().size();
        // Create the SubRuleInListItem
        restSubRuleInListItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subRuleInListItem))
            )
            .andExpect(status().isCreated());

        // Validate the SubRuleInListItem in the database
        List<SubRuleInListItem> subRuleInListItemList = subRuleInListItemRepository.findAll();
        assertThat(subRuleInListItemList).hasSize(databaseSizeBeforeCreate + 1);
        SubRuleInListItem testSubRuleInListItem = subRuleInListItemList.get(subRuleInListItemList.size() - 1);
        assertThat(testSubRuleInListItem.getStrItem()).isEqualTo(DEFAULT_STR_ITEM);
        assertThat(testSubRuleInListItem.getDecItem()).isEqualTo(DEFAULT_DEC_ITEM);
    }

    @Test
    @Transactional
    void createSubRuleInListItemWithExistingId() throws Exception {
        // Create the SubRuleInListItem with an existing ID
        subRuleInListItem.setId(1L);

        int databaseSizeBeforeCreate = subRuleInListItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubRuleInListItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subRuleInListItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubRuleInListItem in the database
        List<SubRuleInListItem> subRuleInListItemList = subRuleInListItemRepository.findAll();
        assertThat(subRuleInListItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSubRuleInListItems() throws Exception {
        // Initialize the database
        subRuleInListItemRepository.saveAndFlush(subRuleInListItem);

        // Get all the subRuleInListItemList
        restSubRuleInListItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subRuleInListItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].strItem").value(hasItem(DEFAULT_STR_ITEM)))
            .andExpect(jsonPath("$.[*].decItem").value(hasItem(DEFAULT_DEC_ITEM.doubleValue())));
    }

    @Test
    @Transactional
    void getSubRuleInListItem() throws Exception {
        // Initialize the database
        subRuleInListItemRepository.saveAndFlush(subRuleInListItem);

        // Get the subRuleInListItem
        restSubRuleInListItemMockMvc
            .perform(get(ENTITY_API_URL_ID, subRuleInListItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subRuleInListItem.getId().intValue()))
            .andExpect(jsonPath("$.strItem").value(DEFAULT_STR_ITEM))
            .andExpect(jsonPath("$.decItem").value(DEFAULT_DEC_ITEM.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingSubRuleInListItem() throws Exception {
        // Get the subRuleInListItem
        restSubRuleInListItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSubRuleInListItem() throws Exception {
        // Initialize the database
        subRuleInListItemRepository.saveAndFlush(subRuleInListItem);

        int databaseSizeBeforeUpdate = subRuleInListItemRepository.findAll().size();

        // Update the subRuleInListItem
        SubRuleInListItem updatedSubRuleInListItem = subRuleInListItemRepository.findById(subRuleInListItem.getId()).get();
        // Disconnect from session so that the updates on updatedSubRuleInListItem are not directly saved in db
        em.detach(updatedSubRuleInListItem);
        updatedSubRuleInListItem.strItem(UPDATED_STR_ITEM).decItem(UPDATED_DEC_ITEM);

        restSubRuleInListItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSubRuleInListItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSubRuleInListItem))
            )
            .andExpect(status().isOk());

        // Validate the SubRuleInListItem in the database
        List<SubRuleInListItem> subRuleInListItemList = subRuleInListItemRepository.findAll();
        assertThat(subRuleInListItemList).hasSize(databaseSizeBeforeUpdate);
        SubRuleInListItem testSubRuleInListItem = subRuleInListItemList.get(subRuleInListItemList.size() - 1);
        assertThat(testSubRuleInListItem.getStrItem()).isEqualTo(UPDATED_STR_ITEM);
        assertThat(testSubRuleInListItem.getDecItem()).isEqualTo(UPDATED_DEC_ITEM);
    }

    @Test
    @Transactional
    void putNonExistingSubRuleInListItem() throws Exception {
        int databaseSizeBeforeUpdate = subRuleInListItemRepository.findAll().size();
        subRuleInListItem.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubRuleInListItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subRuleInListItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subRuleInListItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubRuleInListItem in the database
        List<SubRuleInListItem> subRuleInListItemList = subRuleInListItemRepository.findAll();
        assertThat(subRuleInListItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSubRuleInListItem() throws Exception {
        int databaseSizeBeforeUpdate = subRuleInListItemRepository.findAll().size();
        subRuleInListItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubRuleInListItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subRuleInListItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubRuleInListItem in the database
        List<SubRuleInListItem> subRuleInListItemList = subRuleInListItemRepository.findAll();
        assertThat(subRuleInListItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSubRuleInListItem() throws Exception {
        int databaseSizeBeforeUpdate = subRuleInListItemRepository.findAll().size();
        subRuleInListItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubRuleInListItemMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subRuleInListItem))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubRuleInListItem in the database
        List<SubRuleInListItem> subRuleInListItemList = subRuleInListItemRepository.findAll();
        assertThat(subRuleInListItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSubRuleInListItemWithPatch() throws Exception {
        // Initialize the database
        subRuleInListItemRepository.saveAndFlush(subRuleInListItem);

        int databaseSizeBeforeUpdate = subRuleInListItemRepository.findAll().size();

        // Update the subRuleInListItem using partial update
        SubRuleInListItem partialUpdatedSubRuleInListItem = new SubRuleInListItem();
        partialUpdatedSubRuleInListItem.setId(subRuleInListItem.getId());

        partialUpdatedSubRuleInListItem.strItem(UPDATED_STR_ITEM);

        restSubRuleInListItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubRuleInListItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubRuleInListItem))
            )
            .andExpect(status().isOk());

        // Validate the SubRuleInListItem in the database
        List<SubRuleInListItem> subRuleInListItemList = subRuleInListItemRepository.findAll();
        assertThat(subRuleInListItemList).hasSize(databaseSizeBeforeUpdate);
        SubRuleInListItem testSubRuleInListItem = subRuleInListItemList.get(subRuleInListItemList.size() - 1);
        assertThat(testSubRuleInListItem.getStrItem()).isEqualTo(UPDATED_STR_ITEM);
        assertThat(testSubRuleInListItem.getDecItem()).isEqualTo(DEFAULT_DEC_ITEM);
    }

    @Test
    @Transactional
    void fullUpdateSubRuleInListItemWithPatch() throws Exception {
        // Initialize the database
        subRuleInListItemRepository.saveAndFlush(subRuleInListItem);

        int databaseSizeBeforeUpdate = subRuleInListItemRepository.findAll().size();

        // Update the subRuleInListItem using partial update
        SubRuleInListItem partialUpdatedSubRuleInListItem = new SubRuleInListItem();
        partialUpdatedSubRuleInListItem.setId(subRuleInListItem.getId());

        partialUpdatedSubRuleInListItem.strItem(UPDATED_STR_ITEM).decItem(UPDATED_DEC_ITEM);

        restSubRuleInListItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubRuleInListItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubRuleInListItem))
            )
            .andExpect(status().isOk());

        // Validate the SubRuleInListItem in the database
        List<SubRuleInListItem> subRuleInListItemList = subRuleInListItemRepository.findAll();
        assertThat(subRuleInListItemList).hasSize(databaseSizeBeforeUpdate);
        SubRuleInListItem testSubRuleInListItem = subRuleInListItemList.get(subRuleInListItemList.size() - 1);
        assertThat(testSubRuleInListItem.getStrItem()).isEqualTo(UPDATED_STR_ITEM);
        assertThat(testSubRuleInListItem.getDecItem()).isEqualTo(UPDATED_DEC_ITEM);
    }

    @Test
    @Transactional
    void patchNonExistingSubRuleInListItem() throws Exception {
        int databaseSizeBeforeUpdate = subRuleInListItemRepository.findAll().size();
        subRuleInListItem.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubRuleInListItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, subRuleInListItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subRuleInListItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubRuleInListItem in the database
        List<SubRuleInListItem> subRuleInListItemList = subRuleInListItemRepository.findAll();
        assertThat(subRuleInListItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSubRuleInListItem() throws Exception {
        int databaseSizeBeforeUpdate = subRuleInListItemRepository.findAll().size();
        subRuleInListItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubRuleInListItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subRuleInListItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubRuleInListItem in the database
        List<SubRuleInListItem> subRuleInListItemList = subRuleInListItemRepository.findAll();
        assertThat(subRuleInListItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSubRuleInListItem() throws Exception {
        int databaseSizeBeforeUpdate = subRuleInListItemRepository.findAll().size();
        subRuleInListItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubRuleInListItemMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subRuleInListItem))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubRuleInListItem in the database
        List<SubRuleInListItem> subRuleInListItemList = subRuleInListItemRepository.findAll();
        assertThat(subRuleInListItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSubRuleInListItem() throws Exception {
        // Initialize the database
        subRuleInListItemRepository.saveAndFlush(subRuleInListItem);

        int databaseSizeBeforeDelete = subRuleInListItemRepository.findAll().size();

        // Delete the subRuleInListItem
        restSubRuleInListItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, subRuleInListItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SubRuleInListItem> subRuleInListItemList = subRuleInListItemRepository.findAll();
        assertThat(subRuleInListItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
