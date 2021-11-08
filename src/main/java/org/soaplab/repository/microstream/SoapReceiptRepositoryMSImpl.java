package org.soaplab.repository.microstream;

import java.util.Map;
import java.util.UUID;

import org.soaplab.domain.SoapReceipt;
import org.soaplab.repository.SoapRecipeRepository;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SoapReceiptRepositoryMSImpl extends EntityRepositoryMSImpl<SoapReceipt> implements SoapRecipeRepository {

	public SoapReceiptRepositoryMSImpl(MicrostreamRepository repository) {
		super(repository);
	}

	@Override
	protected Map<UUID, SoapReceipt> getIdToEntityMapping() {
		return repository.getRoot().getAllSoapReceipts();
	}

}
