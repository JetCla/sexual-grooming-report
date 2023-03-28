package ar.com.grooming.sexualgroomingreports.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ar.com.grooming.sexualgroomingreports.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportLogTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportLog.class);
        ReportLog reportLog1 = new ReportLog();
        reportLog1.setId(1L);
        ReportLog reportLog2 = new ReportLog();
        reportLog2.setId(reportLog1.getId());
        assertThat(reportLog1).isEqualTo(reportLog2);
        reportLog2.setId(2L);
        assertThat(reportLog1).isNotEqualTo(reportLog2);
        reportLog1.setId(null);
        assertThat(reportLog1).isNotEqualTo(reportLog2);
    }
}
