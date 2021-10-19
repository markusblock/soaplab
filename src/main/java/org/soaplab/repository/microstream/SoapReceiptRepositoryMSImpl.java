package org.soaplab.repository.microstream;

import java.util.Map;

import org.soaplab.domain.SoapReceipt;
import org.soaplab.repository.SoapReceiptRepository;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SoapReceiptRepositoryMSImpl extends EntityRepositoryMSImpl<SoapReceipt> implements SoapReceiptRepository {

	public SoapReceiptRepositoryMSImpl(MicrostreamRepository repository) {
		super(repository);
	}

	@Override
	protected Map<Long, SoapReceipt> getIdToEntityMapping() {
		return repository.getRoot().getAllSoapReceipts();
	}

}
