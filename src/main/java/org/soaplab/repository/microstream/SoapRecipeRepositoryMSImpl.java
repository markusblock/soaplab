package org.soaplab.repository.microstream;

import java.util.Map;
import java.util.UUID;

import org.soaplab.domain.SoapRecipe;
import org.soaplab.repository.SoapRecipeRepository;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SoapRecipeRepositoryMSImpl extends EntityRepositoryMSImpl<SoapRecipe> implements SoapRecipeRepository {

	public SoapRecipeRepositoryMSImpl(MicrostreamRepository repository) {
		super(repository);
	}

	@Override
	protected Map<UUID, SoapRecipe> getIdToEntityMapping() {
		return repository.getRoot().getAllSoapReceipts();
	}

	@Override
	protected void storeCompositeEntitiesInRepository(SoapRecipe soapRecipe) {
		repository.getStorage().storeAll(soapRecipe.getAcids().values());
		repository.getStorage().storeAll(soapRecipe.getFats().values());
		repository.getStorage().storeAll(soapRecipe.getFragrances().values());
		repository.getStorage().storeAll(soapRecipe.getLiquids().values());
	}
}
