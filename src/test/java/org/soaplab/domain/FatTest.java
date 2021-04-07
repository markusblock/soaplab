package org.soaplab.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.soaplab.web.rest.TestUtil;

class FatTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Fat.class);
        Fat fat1 = new Fat();
        fat1.setId(1L);
        Fat fat2 = new Fat();
        fat2.setId(fat1.getId());
        assertThat(fat1).isEqualTo(fat2);
        fat2.setId(2L);
        assertThat(fat1).isNotEqualTo(fat2);
        fat1.setId(null);
        assertThat(fat1).isNotEqualTo(fat2);
    }
}
