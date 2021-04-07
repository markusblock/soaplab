package org.soaplab.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.soaplab.web.rest.TestUtil;

class FragranceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Fragrance.class);
        Fragrance fragrance1 = new Fragrance();
        fragrance1.setId(1L);
        Fragrance fragrance2 = new Fragrance();
        fragrance2.setId(fragrance1.getId());
        assertThat(fragrance1).isEqualTo(fragrance2);
        fragrance2.setId(2L);
        assertThat(fragrance1).isNotEqualTo(fragrance2);
        fragrance1.setId(null);
        assertThat(fragrance1).isNotEqualTo(fragrance2);
    }
}
