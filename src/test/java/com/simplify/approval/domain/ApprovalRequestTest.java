package com.simplify.approval.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplify.approval.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ApprovalRequestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApprovalRequest.class);
        ApprovalRequest approvalRequest1 = new ApprovalRequest();
        approvalRequest1.setId(1L);
        ApprovalRequest approvalRequest2 = new ApprovalRequest();
        approvalRequest2.setId(approvalRequest1.getId());
        assertThat(approvalRequest1).isEqualTo(approvalRequest2);
        approvalRequest2.setId(2L);
        assertThat(approvalRequest1).isNotEqualTo(approvalRequest2);
        approvalRequest1.setId(null);
        assertThat(approvalRequest1).isNotEqualTo(approvalRequest2);
    }
}
