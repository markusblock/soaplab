package org.soaplab.repository.microstream;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.soaplab.domain.Ingredient;
import org.soaplab.repository.IngredientsRepository;
import org.springframework.stereotype.Component;

@Component
public class IngredientsRepositoryMSImpl extends EntityRepositoryMSImpl<Ingredient> implements IngredientsRepository {

	private static final long serialVersionUID = 1L;

	@Override
	public List<Ingredient> findByInci(String inci) {
		return getEntitiesInternal().stream()
				.filter(ingredient -> ingredient.getInci().toLowerCase().contains(inci.toLowerCase()))
				.collect(Collectors.toList());
	}

	@Override
	public List<Ingredient> findByNameOrInci(String nameOrInci) {
		return getEntitiesInternal().stream()
				.filter(ingredient -> ingredient.getName().toLowerCase().contains(nameOrInci.toLowerCase())
						|| ingredient.getInci().toLowerCase().contains(nameOrInci.toLowerCase()))
				.collect(Collectors.toList());
	}

	@Override
	protected Set<Ingredient> getEntitiesInternal() {
		final Set<Ingredient> ingredients = new HashSet<>();
		ingredients.addAll(getDataRoot().getAllAcids());
		ingredients.addAll(getDataRoot().getAllAdditives());
		ingredients.addAll(getDataRoot().getAllFats());
		ingredients.addAll(getDataRoot().getAllFragrances());
		ingredients.addAll(getDataRoot().getAllKOH());
		ingredients.addAll(getDataRoot().getAllLiquids());
		ingredients.addAll(getDataRoot().getAllNaOH());
		return ingredients;
	}
}
