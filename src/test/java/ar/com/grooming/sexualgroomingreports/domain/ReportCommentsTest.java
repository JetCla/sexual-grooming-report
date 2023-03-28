package ar.com.grooming.sexualgroomingreports.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ar.com.grooming.sexualgroomingreports.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportCommentsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportComments.class);
        ReportComments reportComments1 = new ReportComments();
        reportComments1.setId(1L);
        ReportComments reportComments2 = new ReportComments();
        reportComments2.setId(reportComments1.getId());
        assertThat(reportComments1).isEqualTo(reportComments2);
        reportComments2.setId(2L);
        assertThat(reportComments1).isNotEqualTo(reportComments2);
        reportComments1.setId(null);
        assertThat(reportComments1).isNotEqualTo(reportComments2);
    }
}
