package ar.com.grooming.sexualgroomingreports.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ar.com.grooming.sexualgroomingreports.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VictimTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Victim.class);
        Victim victim1 = new Victim();
        victim1.setId(1L);
        Victim victim2 = new Victim();
        victim2.setId(victim1.getId());
        assertThat(victim1).isEqualTo(victim2);
        victim2.setId(2L);
        assertThat(victim1).isNotEqualTo(victim2);
        victim1.setId(null);
        assertThat(victim1).isNotEqualTo(victim2);
    }
}
