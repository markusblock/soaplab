package org.soaplab.repository.microstream;

import java.util.List;
import java.util.stream.Collectors;

import org.soaplab.domain.Ingredient;
import org.soaplab.repository.IngredientRepository;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public abstract class IngredientRepositoryMSImpl<T extends Ingredient> extends EntityRepositoryMSImpl<T>
		implements IngredientRepository<T> {

	public IngredientRepositoryMSImpl(MicrostreamRepository repository) {
		super(repository);
	}

	@Override
	public List<T> findByInci(String inci) {
		return getEntitiesInternal().stream()
				.filter(ingredient -> ingredient.getInci().toLowerCase().contains(inci.toLowerCase()))
				.collect(Collectors.toList());
	}

	@Override
	public List<T> findByNameOrInci(String nameOrInci) {
		return getEntitiesInternal().stream()
				.filter(ingredient -> ingredient.getName().toLowerCase().contains(nameOrInci.toLowerCase())
						|| ingredient.getInci().toLowerCase().contains(nameOrInci.toLowerCase()))
				.collect(Collectors.toList());
	}
}
