package com.simplify.approval.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplify.approval.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IndividualApprovalStatusTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IndividualApprovalStatus.class);
        IndividualApprovalStatus individualApprovalStatus1 = new IndividualApprovalStatus();
        individualApprovalStatus1.setId(1L);
        IndividualApprovalStatus individualApprovalStatus2 = new IndividualApprovalStatus();
        individualApprovalStatus2.setId(individualApprovalStatus1.getId());
        assertThat(individualApprovalStatus1).isEqualTo(individualApprovalStatus2);
        individualApprovalStatus2.setId(2L);
        assertThat(individualApprovalStatus1).isNotEqualTo(individualApprovalStatus2);
        individualApprovalStatus1.setId(null);
        assertThat(individualApprovalStatus1).isNotEqualTo(individualApprovalStatus2);
    }
}
