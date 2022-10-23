package org.soaplab.repository.microstream;

import java.util.Set;

import org.soaplab.domain.Acid;
import org.soaplab.repository.AcidRepository;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AcidRepositoryMSImpl extends IngredientRepositoryMSImpl<Acid> implements AcidRepository {

	public AcidRepositoryMSImpl(MicrostreamRepository repository) {
		super(repository);
	}

	@Override
	protected Set<Acid> getEntitiesInternal() {
		return repository.getRoot().getAllAcids();
	}
}
