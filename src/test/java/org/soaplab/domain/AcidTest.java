package org.soaplab.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.soaplab.web.rest.TestUtil;

class AcidTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Acid.class);
        Acid acid1 = new Acid();
        acid1.setId(1L);
        Acid acid2 = new Acid();
        acid2.setId(acid1.getId());
        assertThat(acid1).isEqualTo(acid2);
        acid2.setId(2L);
        assertThat(acid1).isNotEqualTo(acid2);
        acid1.setId(null);
        assertThat(acid1).isNotEqualTo(acid2);
    }
}
