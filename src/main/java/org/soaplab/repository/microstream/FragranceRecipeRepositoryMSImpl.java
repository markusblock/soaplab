package org.soaplab.repository.microstream;

import java.util.Set;

import lombok.RequiredArgsConstructor;
import org.soaplab.domain.*;
import org.soaplab.domain.exception.EntityDeletionFailedException;
import org.soaplab.repository.*;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class FragranceRecipeRepositoryMSImpl extends EntityRepositoryMSImpl<FragranceRecipe> implements FragranceRecipeRepository {

	private static final long serialVersionUID = 1L;

	private final FragranceRepository fragranceRepository;

	@Override
	protected void assertEntityIsNotReferencedByOtherEntities(FragranceRecipe entity) {
		if (getDataRoot().getAllSoapRecipes().stream().anyMatch(soapRecipe -> {
			return soapRecipe.getFragranceRecipe().getId().equals(entity.getId());
		})) {
			throw new EntityDeletionFailedException(entity, EntityDeletionFailedException.REASON.ENTITY_STILL_REFERENCED);
		}
	}

	@Override
	protected void getAndReplaceCompositeEntitiesFromRepository(FragranceRecipe entity) {
		// reload composite entities to reflect potential changes on them
		entity.getFragrances().forEach(entry -> {
			final Fragrance fragrance = fragranceRepository.get(entry.getIngredient().getId());
			entry.setIngredient(fragrance);
		});
	}

	@Override
	protected Set<FragranceRecipe> getEntitiesInternal() {
		return getDataRoot().getAllFragranceRecipes();
	}
}
