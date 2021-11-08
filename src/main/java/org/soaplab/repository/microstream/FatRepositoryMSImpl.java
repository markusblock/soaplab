package org.soaplab.repository.microstream;

import java.util.Map;
import java.util.UUID;

import org.soaplab.domain.Fat;
import org.soaplab.repository.FatRepository;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FatRepositoryMSImpl extends IngredientRepositoryMSImpl<Fat> implements FatRepository {

	public FatRepositoryMSImpl(MicrostreamRepository repository) {
		super(repository);
	}

	@Override
	protected Map<UUID, Fat> getIdToEntityMapping() {
		return repository.getRoot().getAllFats();
	}
}
