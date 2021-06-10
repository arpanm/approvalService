package com.simplify.approval.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplify.approval.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubRuleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubRule.class);
        SubRule subRule1 = new SubRule();
        subRule1.setId(1L);
        SubRule subRule2 = new SubRule();
        subRule2.setId(subRule1.getId());
        assertThat(subRule1).isEqualTo(subRule2);
        subRule2.setId(2L);
        assertThat(subRule1).isNotEqualTo(subRule2);
        subRule1.setId(null);
        assertThat(subRule1).isNotEqualTo(subRule2);
    }
}
