package org.mysoap.repository.microstream;

import java.util.Map;

import org.mysoap.domain.SoapReceipt;
import org.mysoap.repository.SoapReceiptRepository;
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
