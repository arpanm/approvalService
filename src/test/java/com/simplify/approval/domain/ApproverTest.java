package com.simplify.approval.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplify.approval.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ApproverTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Approver.class);
        Approver approver1 = new Approver();
        approver1.setId(1L);
        Approver approver2 = new Approver();
        approver2.setId(approver1.getId());
        assertThat(approver1).isEqualTo(approver2);
        approver2.setId(2L);
        assertThat(approver1).isNotEqualTo(approver2);
        approver1.setId(null);
        assertThat(approver1).isNotEqualTo(approver2);
    }
}
