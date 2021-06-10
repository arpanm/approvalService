package com.simplify.approval.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplify.approval.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubRuleInListItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubRuleInListItem.class);
        SubRuleInListItem subRuleInListItem1 = new SubRuleInListItem();
        subRuleInListItem1.setId(1L);
        SubRuleInListItem subRuleInListItem2 = new SubRuleInListItem();
        subRuleInListItem2.setId(subRuleInListItem1.getId());
        assertThat(subRuleInListItem1).isEqualTo(subRuleInListItem2);
        subRuleInListItem2.setId(2L);
        assertThat(subRuleInListItem1).isNotEqualTo(subRuleInListItem2);
        subRuleInListItem1.setId(null);
        assertThat(subRuleInListItem1).isNotEqualTo(subRuleInListItem2);
    }
}
