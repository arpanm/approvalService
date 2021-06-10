package com.simplify.approval.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplify.approval.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ApprovalRequestItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApprovalRequestItem.class);
        ApprovalRequestItem approvalRequestItem1 = new ApprovalRequestItem();
        approvalRequestItem1.setId(1L);
        ApprovalRequestItem approvalRequestItem2 = new ApprovalRequestItem();
        approvalRequestItem2.setId(approvalRequestItem1.getId());
        assertThat(approvalRequestItem1).isEqualTo(approvalRequestItem2);
        approvalRequestItem2.setId(2L);
        assertThat(approvalRequestItem1).isNotEqualTo(approvalRequestItem2);
        approvalRequestItem1.setId(null);
        assertThat(approvalRequestItem1).isNotEqualTo(approvalRequestItem2);
    }
}
