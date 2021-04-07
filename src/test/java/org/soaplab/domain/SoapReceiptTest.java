package org.soaplab.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.soaplab.web.rest.TestUtil;

class SoapReceiptTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SoapReceipt.class);
        SoapReceipt soapReceipt1 = new SoapReceipt();
        soapReceipt1.setId(1L);
        SoapReceipt soapReceipt2 = new SoapReceipt();
        soapReceipt2.setId(soapReceipt1.getId());
        assertThat(soapReceipt1).isEqualTo(soapReceipt2);
        soapReceipt2.setId(2L);
        assertThat(soapReceipt1).isNotEqualTo(soapReceipt2);
        soapReceipt1.setId(null);
        assertThat(soapReceipt1).isNotEqualTo(soapReceipt2);
    }
}
