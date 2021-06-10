package com.simplify.approval.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplify.approval.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ApprovalRuleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApprovalRule.class);
        ApprovalRule approvalRule1 = new ApprovalRule();
        approvalRule1.setId(1L);
        ApprovalRule approvalRule2 = new ApprovalRule();
        approvalRule2.setId(approvalRule1.getId());
        assertThat(approvalRule1).isEqualTo(approvalRule2);
        approvalRule2.setId(2L);
        assertThat(approvalRule1).isNotEqualTo(approvalRule2);
        approvalRule1.setId(null);
        assertThat(approvalRule1).isNotEqualTo(approvalRule2);
    }
}
