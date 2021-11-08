package org.soaplab.repository.microstream;

import java.util.Map;
import java.util.UUID;

import org.soaplab.domain.Fragrance;
import org.soaplab.repository.FragranceRepository;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FragranceRepositoryMSImpl extends IngredientRepositoryMSImpl<Fragrance> implements FragranceRepository {

	public FragranceRepositoryMSImpl(MicrostreamRepository repository) {
		super(repository);
	}

	@Override
	protected Map<UUID, Fragrance> getIdToEntityMapping() {
		return repository.getRoot().getAllFragrances();
	}
}
