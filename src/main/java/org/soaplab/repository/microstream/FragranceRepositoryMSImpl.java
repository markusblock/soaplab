package org.soaplab.repository.microstream;

import java.util.Set;

import org.soaplab.domain.Fragrance;
import org.soaplab.domain.exception.EntityDeletionFailedException;
import org.soaplab.domain.exception.EntityDeletionFailedException.REASON;
import org.soaplab.repository.FragranceRepository;
import org.springframework.stereotype.Component;

@Component
public class FragranceRepositoryMSImpl extends IngredientRepositoryMSImpl<Fragrance> implements FragranceRepository {

	private static final long serialVersionUID = 1L;

	@Override
	protected Set<Fragrance> getEntitiesInternal() {
		return getDataRoot().getAllFragrances();
	}

	@Override
	protected void assertEntityIsNotReferencedByOtherEntities(Fragrance entity) {
		if (getDataRoot().getAllFragranceRecipes().stream().anyMatch(recipe -> {
			return recipe.getFragrances().stream()
					.anyMatch(recipeEntry -> recipeEntry.getIngredient().getId().equals(entity.getId()));
		})) {
			throw new EntityDeletionFailedException(entity, REASON.ENTITY_STILL_REFERENCED);
		}
	}
}
