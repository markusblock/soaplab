package org.soaplab.repository.microstream;

import java.util.Map;
import java.util.UUID;

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
	protected Map<UUID, Acid> getIdToEntityMapping() {
		return repository.getRoot().getAllAcids();
	}
}
