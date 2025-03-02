package org.soaplab.repository.microstream;

import java.util.Set;

import org.soaplab.domain.Additive;
import org.soaplab.domain.exception.EntityDeletionFailedException;
import org.soaplab.domain.exception.EntityDeletionFailedException.REASON;
import org.soaplab.repository.AdditiveRepository;
import org.springframework.stereotype.Component;

@Component
public class AdditiveRepositoryMSImpl extends IngredientRepositoryMSImpl<Additive> implements AdditiveRepository {

	private static final long serialVersionUID = 1L;

	@Override
	protected Set<Additive> getEntitiesInternal() {
		return getDataRoot().getAllAdditives();
	}

	@Override
	protected void assertEntityIsNotReferencedByOtherEntities(Additive entity) {
		if (isEntityReferencedBySoapRecipe(entity) || isEntityReferencedByLyeRecipe(entity)) {
			throw new EntityDeletionFailedException(entity, REASON.ENTITY_STILL_REFERENCED);
		}
	}

	private boolean isEntityReferencedByLyeRecipe(Additive entity) {
		return getDataRoot().getAllLyeRecipes().stream().anyMatch(lyeRecipe -> {
			return lyeRecipe.getAdditives().stream()
					.anyMatch(recipeEntry -> recipeEntry.getIngredient().getId().equals(entity.getId()));
		});
	}

	private boolean isEntityReferencedBySoapRecipe(Additive entity) {
		return getDataRoot().getAllSoapRecipes().stream().anyMatch(soapRecipe -> {
			return soapRecipe.getAdditives().stream()
					.anyMatch(recipeEntry -> recipeEntry.getIngredient().getId().equals(entity.getId()));
		});
	}
}
