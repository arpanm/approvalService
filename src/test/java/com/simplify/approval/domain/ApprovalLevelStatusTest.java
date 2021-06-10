package com.simplify.approval.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplify.approval.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ApprovalLevelStatusTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApprovalLevelStatus.class);
        ApprovalLevelStatus approvalLevelStatus1 = new ApprovalLevelStatus();
        approvalLevelStatus1.setId(1L);
        ApprovalLevelStatus approvalLevelStatus2 = new ApprovalLevelStatus();
        approvalLevelStatus2.setId(approvalLevelStatus1.getId());
        assertThat(approvalLevelStatus1).isEqualTo(approvalLevelStatus2);
        approvalLevelStatus2.setId(2L);
        assertThat(approvalLevelStatus1).isNotEqualTo(approvalLevelStatus2);
        approvalLevelStatus1.setId(null);
        assertThat(approvalLevelStatus1).isNotEqualTo(approvalLevelStatus2);
    }
}
