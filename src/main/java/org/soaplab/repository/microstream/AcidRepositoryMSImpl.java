package org.soaplab.repository.microstream;

import java.util.Set;

import org.eclipse.store.storage.embedded.types.EmbeddedStorageManager;
import org.soaplab.SoaplabProperties;
import org.soaplab.domain.Acid;
import org.soaplab.domain.exception.EntityDeletionFailedException;
import org.soaplab.domain.exception.EntityDeletionFailedException.REASON;
import org.soaplab.repository.AcidRepository;
import org.springframework.stereotype.Component;

@Component
public class AcidRepositoryMSImpl extends IngredientRepositoryMSImpl<Acid> implements AcidRepository {

	private static final long serialVersionUID = 1L;

	public AcidRepositoryMSImpl(DataRoot dataRoot, SoaplabProperties properties,
			EmbeddedStorageManager storageManager) {
		super(dataRoot, properties, storageManager);
	}

	@Override
	protected Set<Acid> getEntitiesInternal() {
		return getDataRoot().getAllAcids();
	}

	@Override
	protected void assertEntityIsNotReferencedByOtherEntities(Acid entity) {
		if (getDataRoot().getAllLyeRecipes().stream().anyMatch(lyeRecipe -> {
			return lyeRecipe.getAcids().stream()
					.anyMatch(recipeEntry -> recipeEntry.getIngredient().getId().equals(entity.getId()));
		})) {
			throw new EntityDeletionFailedException(entity, REASON.ENTITY_STILL_REFERENCED);
		}
	}
}
