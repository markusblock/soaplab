package org.soaplab.repository.microstream;

import java.util.Set;

import org.soaplab.domain.Liquid;
import org.soaplab.repository.LiquidRepository;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LiquidRepositoryMSImpl extends IngredientRepositoryMSImpl<Liquid> implements LiquidRepository {

	public LiquidRepositoryMSImpl(MicrostreamRepository repository) {
		super(repository);
	}

	@Override
	protected Set<Liquid> getEntitiesInternal() {
		return repository.getRoot().getAllLiquids();
	}
}
