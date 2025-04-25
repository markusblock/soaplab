package org.soaplab.repository.microstream;

import java.util.Set;

import org.eclipse.store.storage.embedded.types.EmbeddedStorageManager;
import org.soaplab.SoaplabProperties;
import org.soaplab.domain.Fragrance;
import org.soaplab.domain.exception.EntityDeletionFailedException;
import org.soaplab.domain.exception.EntityDeletionFailedException.REASON;
import org.soaplab.repository.FragranceRepository;
import org.springframework.stereotype.Component;

@Component
public class FragranceRepositoryMSImpl extends IngredientRepositoryMSImpl<Fragrance> implements FragranceRepository {

	private static final long serialVersionUID = 1L;

	public FragranceRepositoryMSImpl(DataRoot dataRoot, SoaplabProperties properties,
			EmbeddedStorageManager repository) {
		super(dataRoot, properties, repository);
	}

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
