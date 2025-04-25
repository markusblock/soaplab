package org.soaplab.repository.microstream;

import java.util.Set;

import org.eclipse.store.storage.embedded.types.EmbeddedStorageManager;
import org.soaplab.SoaplabProperties;
import org.soaplab.domain.Fat;
import org.soaplab.domain.exception.EntityDeletionFailedException;
import org.soaplab.domain.exception.EntityDeletionFailedException.REASON;
import org.soaplab.repository.FatRepository;
import org.springframework.stereotype.Component;

@Component
public class FatRepositoryMSImpl extends IngredientRepositoryMSImpl<Fat> implements FatRepository {

	private static final long serialVersionUID = 1L;

	public FatRepositoryMSImpl(DataRoot dataRoot, SoaplabProperties properties, EmbeddedStorageManager repository) {
		super(dataRoot, properties, repository);
	}

	@Override
	protected Set<Fat> getEntitiesInternal() {
		return getDataRoot().getAllFats();
	}

	@Override
	protected void assertEntityIsNotReferencedByOtherEntities(Fat entity) {
		if (getDataRoot().getAllSoapRecipes().stream().anyMatch(soapRecipe -> {
			return soapRecipe.getFats().stream()
					.anyMatch(recipeEntry -> recipeEntry.getIngredient().getId().equals(entity.getId()));
		})) {
			throw new EntityDeletionFailedException(entity, REASON.ENTITY_STILL_REFERENCED);
		}
	}
}
